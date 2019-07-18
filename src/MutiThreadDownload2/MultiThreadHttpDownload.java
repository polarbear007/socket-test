package MutiThreadDownload2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiThreadHttpDownload {
	private String url;
	private File destFolder;
	private File destFile;
	private String filename;
	private long totalBytes;
	private long downloadedBytes;
	private int threadCount;
	private boolean isAvailable;
	// 如果我们没有指定，那么默认下载到桌面
	private String destFolderPath = "C:\\Users\\Administrator\\Desktop\\download";

	{
		// 这里要设置一下，因为java 默认设置重定向为true, 也就是说会自动跳转。这样我们几乎不可能拿到状态码 302， 不利我们去分析，所以这里先设置为
		// false
		HttpURLConnection.setFollowRedirects(false);
	}

	public MultiThreadHttpDownload(String url) throws IOException {
		init(url, this.destFolderPath);
	}

	public MultiThreadHttpDownload(String url, String destFolderPath) throws IOException {
		init(url, destFolderPath);
		this.destFolderPath = destFolderPath;
	}

	public MultiThreadHttpDownload(String url, int threadCount) throws IOException {
		init(url, destFolderPath);
		this.threadCount = threadCount;
	}

	public MultiThreadHttpDownload(String url, String filename, String destFolderPath) throws IOException {
		init(url, destFolderPath);
		this.destFolderPath = destFolderPath;
		this.filename = filename;
	}

	public MultiThreadHttpDownload(String url, String filename, int threadCount, String destFolderPath)
			throws IOException {
		init(url, destFolderPath);
		this.destFolderPath = destFolderPath;
		this.filename = filename;
		this.threadCount = threadCount;
	}

	public void download() {
		if (isAvailable) {
			long start, end;
			for (int i = 0; i < threadCount; i++) {
				if (i == threadCount - 1) {
					start = i * (totalBytes / threadCount);
					end = totalBytes - 1;
				} else {
					start = i * (totalBytes / threadCount);
					end = (i + 1) * (totalBytes / threadCount) - 1;
				}

				new Thread(new downloadThread(start, end)).start();

			}

		} else {
			throw new RuntimeException("该链接无法下载");
		}
	}

	private void init(String url, String destFolderPath) throws IOException {
		// 首先，我们需要确认这个 url 是否是有效的
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();

		// 第一次打开以后，其实只是为了获取connection 对象，然后我们需要使用这个对象设置请求头。
		// 设置好了以后，我们会再次 使用 connect() 方法连接一次
		conn.setConnectTimeout(15 * 1000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept",
				"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setRequestProperty("Referer", url);
		conn.setRequestProperty("Charset", "UTF-8");
		// 要假装一下我们是通过浏览器访问的
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.connect();

		// 第一次连接返回的状态码应该是 200， 如果不是的话，那么就是链接有问题，我们可以手动抛一个异常
		if (conn.getResponseCode() == 200) {
			// 如果连接正常的话，那么我们就要去或取文件的 大小了
			this.totalBytes = conn.getContentLength();

			// 判断一下文件的大小，如果小于0的话，那么就再抛异常，说明这个链接指向的并不是一个文件
			if (totalBytes <= 0 || conn.getContentType().equals("text/html")) {
				throw new RuntimeException("不是有效的文件链接");
			}

			String field = conn.getHeaderField("Content-Disposition");
			if (field != null && field.contains("filename")) {
				String regex = "filename=\"([\\w\\W]+)\"$";
				Matcher m = Pattern.compile(regex).matcher(field);
				if (m.find()) {
					this.filename = m.group(1);
				}
			} else {
				String regex = "(?<=/)[^/]+\\.[^/]+$";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(url);
				if (m.find()) {
					if (m.group().contains("?")) {
						this.filename = m.group().replaceAll("\\?[^/]+$", "");
					} else {
						this.filename = m.group();
					}
				}
			}

			// 如果前面都没有啥问题，那么我们就认为此链接是可以下载文件的
			this.isAvailable = true;

			// 获取本机的cpu 核数，设置默认的并发数量
			this.threadCount = Runtime.getRuntime().availableProcessors();

			// 最后，都没有啥问题，我们再去保存的文件夹和空文件
			destFolder = new File(destFolderPath);
			destFolder.mkdirs();
			destFile = new File(destFolder, filename);
			destFile.createNewFile();

			// 如果确认这个 url 没有问题的话，那么我们就把这个 url 当成是最终有效的 url ， 并赋值给成员变量 url
			this.url = url;

			// 如果状态码是 302的话，那么我们就修改 url 为要跳转的那个地址，然后再次调用init() 方法进行初始化
		} else if (conn.getResponseCode() == 302) {
			url = conn.getHeaderField("location");
			init(url, destFolderPath);
		} else {
			throw new RuntimeException("链接异常");
		}
	}

	// 用来查看url 是否可用
	public boolean isAvailable() {
		return isAvailable;
	}

	// 只是用来查询当前类的成员变量值是否正常，可以删除
	@Override
	public String toString() {
		return "MultiThreadHttpDownload [url=" + url + ", destFolder=" + destFolder + ", destFile=" + destFile
				+ ", filename=" + filename + ", totalBytes=" + totalBytes + ", downloadedBytes=" + downloadedBytes
				+ ", threadCount=" + threadCount + ", isAvailable=" + isAvailable + "]";
	}

	// 创建一个成员内部类，用来开辟多线程下载
	private class downloadThread implements Runnable {
		private long start;
		private long end;
		private long writtenBytes;
		private long totalLength;

		public downloadThread(long start, long end) {
			super();
			this.start = start;
			this.end = end;
			this.totalLength = end - start + 1;
		}

		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + "开始下载!" + "	需要下载字节：" + totalLength/(1024*1024) + "M" );

			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

				conn.setConnectTimeout(15 * 1000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept",
						"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
				conn.setRequestProperty("Accept-Language", "zh-CN");
				conn.setRequestProperty("Referer", url.toString());
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Range", "bytes=" + start + "-" + end);// 设置获取实体数据的范围

				conn.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.connect();

				//System.out.println(Thread.currentThread().getName() + ": " + conn.getResponseCode());

				// 如果服务器支持 Range 请求的话，那么会返回 206
				if (conn.getResponseCode() == 206) {
					BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
					int len = 0;
					byte[] buf = new byte[1024 * 16];

					RandomAccessFile raf = new RandomAccessFile(destFile, "rwd");
					raf.seek(start);
					while ((len = bis.read(buf)) != -1) {
						raf.write(buf, 0, len);
						writtenBytes += len;
						//System.out.println(Thread.currentThread().getName() + ": " + (writtenBytes/(1024*1024)) + "M");
					}
					raf.close();
					bis.close();
					System.out.println(Thread.currentThread().getName() + "完成下载  ： " + start + " -- " + end);

				} else if (conn.getResponseCode() == 200) {
					BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
					int len = 0;
					byte[] buf = new byte[1024 * 16];

					RandomAccessFile raf = new RandomAccessFile(destFile, "rwd");
					bis.skip(start);
					raf.seek(start);
					while (true) {
						len = bis.read(buf);
						raf.write(buf, 0, len);
						writtenBytes += len;
						
						// 因为如果服务器不支持Range 请求的话，那么我们请求的实际是整个文件。
						// 只有最后一部门才会 len = -1, 所以如果以此为退出循环的条件，就会有问题
						// 当然，这样子的话，可能有部分文件被重复写入，但是没关系，因为写入的内容都是一样的，也不用担心线程安全问题
						if (writtenBytes >= totalLength) {
							break;
						}
					}
					raf.close();
					bis.close();
					System.out.println(Thread.currentThread().getName() + "完成下载  ： " + start + " -- " + end);
				}

			} catch (Exception e) {
				e.printStackTrace();
				if(writtenBytes < totalLength) {
					System.out.println(Thread.currentThread().getName() + "连接断了，断线重连中...");
					// 断线重连
					new Thread(new downloadThread(writtenBytes, end)).start();
				}
			}
		}

	}

}
