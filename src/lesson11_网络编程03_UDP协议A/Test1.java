package lesson11_������03_UDPЭ��A;

// ���������̣߳�һ�����ڷ���һ�����ڽ�����Ϣ��
// ע�� ��  ���Ͷ˲���Ҫָ������Ķ˿ڣ�������Ҫָ��Ŀ�ĵأ�Ҳ���ǽ��ն˵Ķ˿ڣ�������������ն˵Ķ˿ں�Ӧ�÷��������߳�

public class Test1 {
	public static void main(String[] args) {
		// ����ͻ��˵ķ��Ͷ˿�Ϊ10086�����ն˿�Ϊ10000
		int receivePort = 12345;
		SendMessage sm = new SendMessage(receivePort);
		ReceiveMessage rm = new ReceiveMessage(receivePort);
		
		Thread t1 = new Thread(sm);
		Thread t2 = new Thread(rm);
		
		t1.start();
		t2.start();
	}
}
