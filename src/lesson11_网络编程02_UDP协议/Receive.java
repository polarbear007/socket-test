package lesson11_网络编程02_UDP协议;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receive {
	public static void main(String[] args) throws IOException{
		// 创建接收端socket对象， 端口需要跟发送端那边确认的端口一样，不然接收不到。   发送端那里是发往10086端口的，所以这里接收端就使用 10086
		DatagramSocket ds = new DatagramSocket(10086);
		
		// 创建一个足够大的数据包，用来接收数据
		byte[] bys = new byte[1024*64];
		int len = bys.length;
		DatagramPacket p = new DatagramPacket(bys, len);
		
		// 接收数据
		ds.receive(p);
		
		// 解析数据
		bys = p.getData();
		String message = new String(bys, 0 , p.getLength());
		System.out.println(message);
		
		// 释放资源
		ds.close();
	}
}
