package lesson11_网络编程04_TCP协议3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

// 把 a.txt 文件上传到 服务器，，接收完成后，服务器返回"上传成功"信息给客户端

public class ClientDemo {
	public static void main(String[] args) throws IOException{
		// 创建客户端socket对象 
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 10086);
		
		// 读取文本文件数据；   并把读取到的数据写入输出流中
		BufferedReader br = new BufferedReader(new FileReader("a.txt"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		String line = null;
		while((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		
		// 关闭输出流，通知服务器端，数据已经全部上传
		s.shutdownOutput();
		
		// 等待服务器端反馈
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println(result);
		
		br.close();
		bw.close();
		s.close();
	}
}
