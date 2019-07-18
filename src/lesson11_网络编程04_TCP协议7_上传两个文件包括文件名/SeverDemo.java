package lesson11_网络编程04_TCP协议7_上传两个文件包括文件名;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(10086);

		Socket s = ss.accept();
		File folder = new File(s.getInetAddress().getHostAddress() + "x");
		folder.mkdirs();
		receiveFile(s, folder);
		System.out.println("Server: Start to receive the second file!");
		receiveFile(s, folder);
	}

	private static void receiveFile(Socket s, File folder) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String name = br.readLine();
		System.out.println("Server: receive the file name---->" +name);
		int length = Integer.parseInt(br.readLine());
		File file = new File(folder,name);
		System.out.println("Server: receive the length of the file---->"+length);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("ok");
		bw.newLine();
		bw.flush();
		System.out.println("Server: Ok! Ready to receive a File!");
		
		
		BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
			bos.flush();
			// 这个退出条件必须一相等，就退出。  不能在while 条件里面判断，因为如果使用 while(file.length() < length) 的话，会把其他文件的一部分数据读取进来。
			if(file.length() == length) {
				break;
			}
		}
		bos.close();
		System.out.println("Server: One file received!");

	}

//	private static File receiveFolderName(Socket s, File folder) throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//		String name = br.readLine();
//		File copyFolder = new File(folder, name);
//		copyFolder.mkdir();
//		return copyFolder;
//	}
}
