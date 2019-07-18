package lesson11_网络编程04_TCP协议4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

// 把    Java.pdf 文件上传到服务器（非文本哦），并返回反馈信息

public class ClientDemo {
	public static void main(String[] args) throws IOException {
		// 创建客户端socket对象
		Socket s = new Socket(InetAddress.getLocalHost(), 12306);

		// 读取文件数据，并把这些数据写入输出流里
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream("Java.pdf"));
		BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
			bos.flush();
		}
		
		// 关闭输出流，通知服务器，这里已经全部上传完毕
		s.shutdownOutput();
		
		// 等待服务器反馈信息
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		System.out.println(br.readLine());
		
		br.close();
		bis.close();
		bos.close();
		s.close();
		
		
	}
}
