package lesson11_网络编程03_UDP协议A;

// 开辟两个线程，一个用于发送一个用于接收信息。
// 注意 ：  发送端不需要指定本身的端口，但是需要指定目的地（也就是接收端的端口），所以这个接收端的端口号应该发给两个线程

public class Test1 {
	public static void main(String[] args) {
		// 这个客户端的发送端口为10086，接收端口为10000
		int receivePort = 12345;
		SendMessage sm = new SendMessage(receivePort);
		ReceiveMessage rm = new ReceiveMessage(receivePort);
		
		Thread t1 = new Thread(sm);
		Thread t2 = new Thread(rm);
		
		t1.start();
		t2.start();
	}
}
