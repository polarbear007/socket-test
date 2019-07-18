package lesson11_网络编程01_IP地址;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*	InetAddress 这个类没有构造方法，可以使用其提供的静态方法创建对象。
 * 
 * 	public static InetAddress getByName(String host)  里面的参数可以是一个IP地址，也可以是主机名。这个方法可以返回任意主机或者任意IP地址的 IP对象 
 * 
 * 	public static InetAddress getLocalHost()      直接返回本机的IP地址对象 
 * 
 * 	public String getHostName()			通过对象获取主机名
 * 	public String getHostAddress()      通过对象获取主机IP地址的字符串表现形式
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
