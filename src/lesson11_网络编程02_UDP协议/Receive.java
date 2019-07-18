package lesson11_������02_UDPЭ��;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receive {
	public static void main(String[] args) throws IOException{
		// �������ն�socket���� �˿���Ҫ�����Ͷ��Ǳ�ȷ�ϵĶ˿�һ������Ȼ���ղ�����   ���Ͷ������Ƿ���10086�˿ڵģ�����������ն˾�ʹ�� 10086
		DatagramSocket ds = new DatagramSocket(10086);
		
		// ����һ���㹻������ݰ���������������
		byte[] bys = new byte[1024*64];
		int len = bys.length;
		DatagramPacket p = new DatagramPacket(bys, len);
		
		// ��������
		ds.receive(p);
		
		// ��������
		bys = p.getData();
		String message = new String(bys, 0 , p.getLength());
		System.out.println(message);
		
		// �ͷ���Դ
		ds.close();
	}
}
