package lesson11_网络编程05_TCP协议01_上传多级文件夹之封装文件信息对象版;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

// 上传一个多级文件夹到服务器里面

public class Client {
	public static void main(String[] args) throws Exception {
		 Socket s = new Socket(InetAddress.getLocalHost(), 10086);
		 
		 // 首先，要创建上传的文件对象
		 File file = new File("ccc\\bbb");
		 String rootPath = file.getAbsoluteFile().getParent();
		 
		 if(file.exists()) {
			 if(file.isFile()) {
				 sendFile(s, file, rootPath);
			 }else if(file.isDirectory()) {
				 sendFolder(s, file, rootPath);
			 }
			 
			 // 最后传完了以后，我们可以传个  null （对象）给服务端，告诉服务端，所有的文件都已经上传完毕了
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(null);
			oos.flush();
		 }else {
			 System.out.println("文件不存在！");
		 }
	}

	public static void sendFolder(Socket s, File file, String rootPath) throws Exception {
		// 文件夹的思路跟文件差不多，先把文件夹的信息封装成一个对象，传到服务器端
		String foldername = file.getName();
		String path = file.getAbsoluteFile().getParent().replace(rootPath, "");
		FileMessage fm = new FileMessage("folder", foldername, 0, path);
		
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(fm);
		oos.flush();
		
		// 传完以后，客户端就阻塞等待服务器返回信息
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String message =  br.readLine();
		//System.out.println(message);
		
		// 如果服务器返回 ready, 说明服务器那边已经建好空文件夹了，则开始准备传输文件夹里面的内容了
		if(message.equals("ready")) {
			File[] files = file.listFiles();
			if(files != null) {
				for (File f : files) {
					if(f.isFile()) {
						sendFile(s, f, rootPath);
					}else if(f.isDirectory()) {
						sendFolder(s, f, rootPath);
					}
				}
			}else {
				System.out.println(file.getName() + "---------> 该文件夹为空或者无法访问");
			}
		}
	}

	public static void sendFile(Socket s, File file, String rootPath) throws Exception {
		// 先传 文件名+文件类型+文件长度+文件的路径    过去(打包成一个对象)
		String filename = file.getName();
		long length = file.length();
		String path = file.getAbsoluteFile().getParent().replace(rootPath, "");
		FileMessage fm = new FileMessage("file", filename, length, path);
		
		
		
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(fm);
		oos.flush();
		
		// 传完以后，客户端就阻塞等待服务器返回信息
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String message =  br.readLine();
		System.out.println(message);
		
		// 如果服务器返回 ready, 说明服务器那边已经建好空文件了，则开始准备传输文件内容
		if(message.equals("ready")) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
			byte[] bys = new byte[1024];
			int len = 0;
			while((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
			bis.close();
			
			// 上传完了文件内容以后，客户端还需要等待服务端那边把文件内容都接收完以后，返回信息才能开始一下步的操作
			// readLine() 方法本身就是一个阻塞式方法，如果没有读取到相应的信息，就会一直等待
			// 如果读取到了某个字符串，但是这个字符串不是 finish 的话，那么也是出错了，应该主动抛异常
			message =  br.readLine();
			if(!message.equals("finish")) {
				throw new RuntimeException("服务器异常");
			}else {
				System.out.println(filename + "------>文件上传成功");
			}
		}
	}
	
	
}
