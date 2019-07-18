package lesson11_网络编程03_UDP协议B;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveMessage implements Runnable {
	private int receivePort;
	public ReceiveMessage (int receivePort) {
		this.receivePort = receivePort;
	}
	@Override
	public void run() {
		

			// 创建socket对象
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(receivePort);
			} catch (SocketException e) {
				e.printStackTrace();
			}

			while(true) {
			// 创建一个足够大的数据包，用来接收数据
			byte[] bys = new byte[1024 * 76];
			DatagramPacket p = new DatagramPacket(bys, bys.length);

			if(ds != null) {
				// 接收数据
				try {
					ds.receive(p);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 解析数据
				bys = p.getData();
				String message = new String(bys, 0, p.getLength());
				System.out.println("接收到的内容:" + message);

				// 为了能够一直接收消息，这里就不释放资源了
				// 释放资源
//				ds.close();
			}
		}
	}

}
