package lesson11_������03_UDPЭ��B;

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
			
				// �������Ͷ�socket ����
				DatagramSocket ds = null;
				try {
					ds = new DatagramSocket();
				} catch (SocketException e) {
					e.printStackTrace();
				}

				while(true) {
				// ����¼�����ݣ����������
				Scanner sc = new Scanner(System.in);
				System.out.print("Ҫ���͵�����:");
				String message = sc.nextLine();
				byte[] bys = message.getBytes();
				DatagramPacket p = new DatagramPacket(bys, bys.length);
				// ����Ŀ�ĵ�IP
				try {
					p.setAddress(InetAddress.getLocalHost());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				// ����Ŀ�ĵض˿�
				p.setPort(receivePort);

				if (ds != null) {
					// ��������
					try {
						ds.send(p);
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Ϊ���ܹ�һֱ������Ϣ������Ͳ��ͷ���Դ��
					// �ͷ���Դ
//					ds.close();
//					sc.close();
				}
				
				// ÿ�η������ݺ���Ϣһ��
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			
	}

}
