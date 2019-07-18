package lesson11_网络编程02_UDP协议1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// socket 是网络套接字，包含ip 和  端口两部分。  网络编程，其实就是指  socket编程。
//	socket通信的原理是： 通信的两端都有socket对象，然后两个对象互相传递信息。  两端的socket对象信息传输是在特定的协议下通过IO流实现的。

//   不同的传输协议需要创建不同的  socket对象， UDP协议需要创建   datagramSocket 对象
//    DatagramSocket 类的构造方法常用的有两个
//				public DatagramSocket()  		使用的是本机的IP地址和一个随机端口（UDP协议发送端只管发送，不管接收，所以端口可以不指定）
//				public DatagramSocket(int port) 使用的是本机的IP地址和一个指定的端口（发送端的如果已经确认要送到哪个IP和端口，那么接收端一定要用这个端口，不然接收不到）
//	
//  常用的方法：
//				public void send(DatagramPacket p)		用来发送数据包
//				public void receive(DatagramPacket p)	用来接收数据包,接收的数据包存放在p里面

//				public void connect(InetAddress address,int port)	可以指定这个socket对象与哪个主机地址和端口发生关联。
//																	如果确认关联了，发送的数据包就不需要指定IP和端口了（我们可以在创建数据包的时候就指定好ip和端口）
//																	如果数据包没有指定，那么我们就要通过这个方法来指定数据发送的ip 和 端口。
//																	
//
//				public void disconnect()				断开套接字关联


public class Send {
	public static void main(String[] args) throws IOException{
		// 创建发送端socket对象
		DatagramSocket ds = new DatagramSocket();
//		ds.connect(InetAddress.getLocalHost(), 10086);
//		ds.disconnect();
		
		//  打包数据
		byte[] bys = "我吃好了，你呢？".getBytes();
		int len = bys.length;
		DatagramPacket p = new DatagramPacket(bys, len);
		p.setPort(10086);
		p.setAddress(InetAddress.getLocalHost());
		
		
		// 发送数据包
		ds.send(p);
		
		// 释放资源
		ds.close();
		
		
	}
}
