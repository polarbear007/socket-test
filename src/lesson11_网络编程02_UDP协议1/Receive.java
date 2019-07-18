package lesson11_网络编程02_UDP协议1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receive {
	public static void main(String[] args) throws IOException {
		// 创建接收端socket对象
		DatagramSocket ds = new DatagramSocket(10086);

		// 创建接收的数据包，要足够大
		byte[] bys = new byte[1024 * 76];
		DatagramPacket p = new DatagramPacket(bys, bys.length);

		// 接收数据包
		ds.receive(p);

		// 解析数据包内容,并输出
		bys = p.getData();
		int len = p.getLength();
		String message = new String(bys, 0, len);
		System.out.println(p.getPort() + ":" + message);
		
		// 释放资源
		ds.close();
		
	}
}
