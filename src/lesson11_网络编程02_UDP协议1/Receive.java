package lesson11_������02_UDPЭ��1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receive {
	public static void main(String[] args) throws IOException {
		// �������ն�socket����
		DatagramSocket ds = new DatagramSocket(10086);

		// �������յ����ݰ���Ҫ�㹻��
		byte[] bys = new byte[1024 * 76];
		DatagramPacket p = new DatagramPacket(bys, bys.length);

		// �������ݰ�
		ds.receive(p);

		// �������ݰ�����,�����
		bys = p.getData();
		int len = p.getLength();
		String message = new String(bys, 0, len);
		System.out.println(p.getPort() + ":" + message);
		
		// �ͷ���Դ
		ds.close();
		
	}
}
