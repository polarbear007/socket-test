package lesson11_������02_UDPЭ��;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send {
	public static void main(String[] args) throws IOException{
		// �������Ͷ˵� Socket ����
		DatagramSocket ds = new DatagramSocket();
		
		// �������,��Ϊ���ݵĴ�����ͨ��IO��������ģ����������Ǵ����һ���ֽ���������ġ�
		//  �����������ݰ����� �ֽ����飬 �ֽ�����ĳ��ȣ� ���ն˵�IP���� ���н��ն˳�����ʹ�õĶ˿�
		//   ��Ϊ��������Ľ��ն˻��Ǳ�������һ����������IP������Ȼ�Ǳ�����IP���󣬶˿����дһ�� 10000���ϵ����־ͺ��ˡ� �˿ڵķ�Χ�ǣ�  [0-256*256]
		byte[] bys = "hello world".getBytes();
		int length = bys.length;
		DatagramPacket p = new DatagramPacket(bys, length, InetAddress.getLocalHost(), 10086);
		
		// ͨ�� Socket����� send() ������  ��������
		ds.send(p);
		
		//�ͷ���Դ
		ds.close();
	}
}
