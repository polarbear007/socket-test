package lesson11_������03_UDPЭ��B;

public class Test2 {
	public static void main(String[] args) {
		int receivePort = 10000;
		SendMessage sm = new SendMessage(receivePort);
		ReceiveMessage rm = new ReceiveMessage(receivePort);
		
		Thread t1 = new Thread(sm);
		Thread t2 = new Thread(rm);
		
		t1.start();
		t2.start();
	}
}
