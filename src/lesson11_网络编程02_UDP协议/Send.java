package lesson11_网络编程02_UDP协议;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send {
	public static void main(String[] args) throws IOException{
		// 创建发送端的 Socket 对象
		DatagramSocket ds = new DatagramSocket();
		
		// 打包数据,因为数据的传输是通过IO流来传输的，所以数据是存放在一个字节数组里面的。
		//  这里打包的数据包括： 字节数组， 字节数组的长度， 接收端的IP对象， 还有接收端程序所使用的端口
		//   因为我们这里的接收端还是本机的另一个程序，所以IP对象依然是本机的IP对象，端口随便写一个 10000以上的数字就好了。 端口的范围是：  [0-256*256]
		byte[] bys = "hello world".getBytes();
		int length = bys.length;
		DatagramPacket p = new DatagramPacket(bys, length, InetAddress.getLocalHost(), 10086);
		
		// 通过 Socket对象的 send() 方法，  发送数据
		ds.send(p);
		
		//释放资源
		ds.close();
	}
}
