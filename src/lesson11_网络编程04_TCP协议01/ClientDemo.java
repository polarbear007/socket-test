package lesson11_网络编程04_TCP协议01;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

// TCP协议跟 UDP协议最大的不同就是， TCP通信是需要双方先建立连接，然后再双向传输信息或文件。 （因此，如果我们希望接收到信息后，再给发送信息的那边反馈信息，那么就应该使用TCP协议）
//	而UDP只有发送端能发送消息，并且不管接收端有没有收到，反正就是把信息发出去就行了。

public class ClientDemo {
	public static void main(String[] args) throws IOException{
		//  创建客户端socket对象 
		// public Socket(String host, int port) 
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 12345);
		
		// 获取输出流，写数据(因为我们这里传输的是文本内容，所以可以使用转换流，把输出流转换成字符流)
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("你好呀！");
		bw.newLine();
		bw.flush();
		
		// 释放资源
		bw.close();
		s.close();
	}
}
