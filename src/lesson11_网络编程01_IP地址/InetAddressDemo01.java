package lesson11_������01_IP��ַ;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*	InetAddress �����û�й��췽��������ʹ�����ṩ�ľ�̬������������
 * 
 * 	public static InetAddress getByName(String host)  ����Ĳ���������һ��IP��ַ��Ҳ������������������������Է�������������������IP��ַ�� IP���� 
 * 
 * 	public static InetAddress getLocalHost()      ֱ�ӷ��ر�����IP��ַ���� 
 * 
 * 	public String getHostName()			ͨ�������ȡ������
 * 	public String getHostAddress()      ͨ�������ȡ����IP��ַ���ַ���������ʽ
 * 
 * 
 */

public class InetAddressDemo01 {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress i = InetAddress.getByName("www.baidu.com");
		System.out.println(i.getHostName() + ":" + i.getHostAddress());
		System.out.println("-----------------------------------------");
		
		System.out.println(InetAddress.getLocalHost().getHostName() + ":" + InetAddress.getLocalHost().getHostAddress());
		System.out.println("-----------------------------------------");
		
		InetAddress[] arr = InetAddress.getAllByName("www.baidu.com");
		for (InetAddress inetAddress : arr) {
			System.out.println(inetAddress.getHostAddress());
		}
	}
}
