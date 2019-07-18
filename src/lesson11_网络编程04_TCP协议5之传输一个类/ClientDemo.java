package lesson11_网络编程04_TCP协议5之传输一个类;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDemo {
	public static void main(String[] args) throws IOException {
		// 创建客户端socket对象
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 12345);

		// 获取输出流，因为我们这里想要传的是一个对象，所以使用 ObjectOutputStream 包装
		ObjectOutputStream obs = new ObjectOutputStream(s.getOutputStream());
		obs.writeObject(new User("haha", "123456"));
		obs.flush();
		
		// 关闭输出流，通知服务器端，这里已经全部输出完毕
		s.shutdownOutput();
		
		// 获取输入流，读取服务器返回的信息
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println(result);
		
		br.close();
		obs.close();
		s.close();
		
	}
}
