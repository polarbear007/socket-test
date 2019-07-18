package lesson11_网络编程04_TCP协议91_上传多级文件夹之多线程_断点续传版;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDemo02 {
	public static void main(String[] args) throws IOException {
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 50000);

		File folder = new File("ccc");
		String parentPath = folder.getAbsoluteFile().getParent();
		System.out.println(parentPath);
		if (folder.isDirectory()) {
			sendFolder(s, folder, parentPath);
			System.out.println("所有文件已经上传完毕！");
			s.shutdownOutput();
		} else if (folder.isFile()) {
			sendFile(s, folder, parentPath);
		} else {
			System.out.println("非法路径！");
		}

		s.close();

	}

	// 客户端把发送文件和发送文件夹写成两个方法，因为发送端是可以知道要发送的是文件还是文件夹的
	// 这是发送文件夹的方法

	public static void sendFolder(Socket s, File folder, String parentPath) throws IOException {
		// 先把文件夹名称传过去
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		System.out.println("发送--->" + "Folder:" + folder.getAbsolutePath().substring(parentPath.length()));
		bw.write("Folder:" + folder.getAbsolutePath().substring(parentPath.length()));
		bw.newLine();
		bw.flush();

		// 等待服务端根据接收信息，在合适的位置建立空文件夹后反馈
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println("接收到--->" + result);
		// 等服务器那边传来消息说准备好了，那么我们就可以开始遍历这个文件夹里面的内容了
		if (result.equals("ready")) {
			File[] fileArray = folder.listFiles();
			if (fileArray != null) {
				for (File f : fileArray) {
					// 如果是文件夹里面的文件夹，那么就使用递归，再次把文件名发过去，然后再遍历这个文件夹
					if (f.isDirectory()) {
						sendFolder(s, f, parentPath);
						// 如果是文件的话，那么我们就调用发送文件的方法
					} else if (f.isFile()) {
						sendFile(s, f, parentPath);
					}
				}
			}
		}

	}

	public static void sendFile(Socket s, File file, String parentPath) throws IOException {
		// 先把文件名和文件长度发送过去
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		System.out.println(
				"发送--->" + "File||" + file.getAbsolutePath().substring(parentPath.length()) + "||" + file.length());
		bw.write("File||" + file.getAbsolutePath().substring(parentPath.length()) + "||" + file.length());
		bw.newLine();
		bw.flush();

		// 等待服务端根据接收信息，在合适的位置建立空文件后反馈
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println("接收到--->" + result);
		if (result.equals("ready")) {
			// 读取文件内容，写入通信的流里面
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());

			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
			System.out.println(file.getPath() + "---> 上传完毕！");
			
		}else {
			
			long pos = Long.parseLong(result);
			if(pos < file.length()) {
				System.out.println("断点续传！！");
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
				
				raf.seek(pos);
				byte[] bys = new byte[1024];
				int len = 0;
				while ((len = raf.read(bys)) != -1) {
					bos.write(bys, 0, len);
					bos.flush();
				}
				System.out.println(file.getPath() + "---> 断点续传完毕！");
			}else {
				System.out.println("服务器已经有这个文件了，跳过");
			}
		}
		
		

		// 上传完毕后，不要马上进行一下步操作，特别是又开始传下一个文件的名字和长度。
		// 因为上传和接收不可能同步，所以可能这边上传完很久了，连下一个文件名和长度都传过去了，
		// 服务端那边都还没有接收完成。那么，结果就是服务器的上一个文件可能会把我们传过去的文件名和长度这些数据也当成是上一个文件的数据，写进上一个文件。结果就是，客户端
		// 在等服务器反馈，服务器在等客户端传下一个文件的名字和长度（其实已经传过去了）
		result = br.readLine();
		if(!result.equals("Finish!")) {
			System.out.println("出错了！！");
		}
	}

}
