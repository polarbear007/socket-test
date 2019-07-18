package lesson11_网络编程04_TCP协议01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo {
	public static void main(String[] args) throws IOException{
		//创建服务器端的socket对象 
		ServerSocket ss = new ServerSocket(12345);
		
		// 监听客户端连接，如果有，就生成一个相应的socket对象 
		Socket s = ss.accept();
		
		// 通过得到的socket对象 ，获取输入流，读取数据(因为我们知道读取的是文本数据，所以把这个输入流包装成字符流)
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		String line = null;
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
		
		br.close();
		s.close();
		// 服务器端一般是不关闭的
		//ss.close();
		
	}
}
