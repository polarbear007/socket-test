package lesson11_������02_UDPЭ��1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// socket �������׽��֣�����ip ��  �˿������֡�  �����̣���ʵ����ָ  socket��̡�
//	socketͨ�ŵ�ԭ���ǣ� ͨ�ŵ����˶���socket����Ȼ�����������ഫ����Ϣ��  ���˵�socket������Ϣ���������ض���Э����ͨ��IO��ʵ�ֵġ�

//   ��ͬ�Ĵ���Э����Ҫ������ͬ��  socket���� UDPЭ����Ҫ����   datagramSocket ����
//    DatagramSocket ��Ĺ��췽�����õ�������
//				public DatagramSocket()  		ʹ�õ��Ǳ�����IP��ַ��һ������˿ڣ�UDPЭ�鷢�Ͷ�ֻ�ܷ��ͣ����ܽ��գ����Զ˿ڿ��Բ�ָ����
//				public DatagramSocket(int port) ʹ�õ��Ǳ�����IP��ַ��һ��ָ���Ķ˿ڣ����Ͷ˵�����Ѿ�ȷ��Ҫ�͵��ĸ�IP�Ͷ˿ڣ���ô���ն�һ��Ҫ������˿ڣ���Ȼ���ղ�����
//	
//  ���õķ�����
//				public void send(DatagramPacket p)		�����������ݰ�
//				public void receive(DatagramPacket p)	�����������ݰ�,���յ����ݰ������p����

//				public void connect(InetAddress address,int port)	����ָ�����socket�������ĸ�������ַ�Ͷ˿ڷ���������
//																	���ȷ�Ϲ����ˣ����͵����ݰ��Ͳ���Ҫָ��IP�Ͷ˿��ˣ����ǿ����ڴ������ݰ���ʱ���ָ����ip�Ͷ˿ڣ�
//																	������ݰ�û��ָ������ô���Ǿ�Ҫͨ�����������ָ�����ݷ��͵�ip �� �˿ڡ�
//																	
//
//				public void disconnect()				�Ͽ��׽��ֹ���


public class Send {
	public static void main(String[] args) throws IOException{
		// �������Ͷ�socket����
		DatagramSocket ds = new DatagramSocket();
//		ds.connect(InetAddress.getLocalHost(), 10086);
//		ds.disconnect();
		
		//  �������
		byte[] bys = "�ҳԺ��ˣ����أ�".getBytes();
		int len = bys.length;
		DatagramPacket p = new DatagramPacket(bys, len);
		p.setPort(10086);
		p.setAddress(InetAddress.getLocalHost());
		
		
		// �������ݰ�
		ds.send(p);
		
		// �ͷ���Դ
		ds.close();
		
		
	}
}
