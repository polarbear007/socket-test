package lesson11_网络编程03_UDP协议B;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SendMessage implements Runnable {
	private int receivePort;
	public SendMessage(int receivePort) {
		this.receivePort = receivePort;
	}

	@Override
	public void run() {
			
				// 创建发送端socket 对象
				DatagramSocket ds = null;
				try {
					ds = new DatagramSocket();
				} catch (SocketException e) {
					e.printStackTrace();
				}

				while(true) {
				// 键盘录入数据，并打包数据
				Scanner sc = new Scanner(System.in);
				System.out.print("要发送的内容:");
				String message = sc.nextLine();
				byte[] bys = message.getBytes();
				DatagramPacket p = new DatagramPacket(bys, bys.length);
				// 设置目的地IP
				try {
					p.setAddress(InetAddress.getLocalHost());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				// 设置目的地端口
				p.setPort(receivePort);

				if (ds != null) {
					// 发送数据
					try {
						ds.send(p);
					} catch (IOException e) {
						e.printStackTrace();
					}

					// 为了能够一直发送消息，这里就不释放资源了
					// 释放资源
//					ds.close();
//					sc.close();
				}
				
				// 每次发完数据后，休息一会
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			
	}

}
