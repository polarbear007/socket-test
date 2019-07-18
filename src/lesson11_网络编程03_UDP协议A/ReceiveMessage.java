package lesson11_������03_UDPЭ��A;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveMessage implements Runnable {
	private int receivePort;
	public ReceiveMessage(int receivePort) {
		this.receivePort = receivePort;
	}


	@Override
	public void run() {
		

			// ����socket����
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(receivePort);
			} catch (SocketException e) {
				e.printStackTrace();
			}

			while(true) {
			// ����һ���㹻������ݰ���������������
			byte[] bys = new byte[1024 * 76];
			int len = bys.length;
			DatagramPacket p = new DatagramPacket(bys, len);

			if(ds != null) {
				// ��������
				try {
					ds.receive(p);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// ��������
				bys = p.getData();
				String message = new String(bys, 0 , p.getLength());
				System.out.println("���յ�������Ϊ:" + message);

				// Ϊ��һֱ���գ��Ͳ��ͷ���Դ��
				// �ͷ���Դ
//				ds.close();
			}
		}
	}

}
