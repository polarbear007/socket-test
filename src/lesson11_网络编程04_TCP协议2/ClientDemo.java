package lesson11_网络编程04_TCP协议2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

//  键盘录入字符串，并发送给服务器端。  然后服务器返回接收信息，实现交互！

public class ClientDemo {
	public static void main(String[] args) throws IOException {
		// 创建客户端socket对象
		Socket s = new Socket(InetAddress.getLocalHost(), 12306);
		// 键盘录入数据
		Scanner sc = new Scanner(System.in);
		while (true) {
			String line = sc.nextLine();
			if (line.equalsIgnoreCase("over")) {
				break;
			}
			// 获取输出流
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		// 关闭这个套接字的输出流，通知服务器端，信息已经全部传输完毕
		// （如果没有这句话，当我们在客户端输入 “over”,
		// 这里开始等待服务器端的反馈信息。但是服务器端不知道你已经全部输入结束了，while循环永远也出不来，还在等待客户端传输数据过去。相互等待，死锁）
		s.shutdownOutput();
		// 获得反馈
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println(result);

		br.close();
		sc.close();
		s.close();

	}
}
