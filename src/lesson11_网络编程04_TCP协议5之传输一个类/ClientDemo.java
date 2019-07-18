package lesson11_������04_TCPЭ��5֮����һ����;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDemo {
	public static void main(String[] args) throws IOException {
		// �����ͻ���socket����
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 12345);

		// ��ȡ���������Ϊ����������Ҫ������һ����������ʹ�� ObjectOutputStream ��װ
		ObjectOutputStream obs = new ObjectOutputStream(s.getOutputStream());
		obs.writeObject(new User("haha", "123456"));
		obs.flush();
		
		// �ر��������֪ͨ�������ˣ������Ѿ�ȫ��������
		s.shutdownOutput();
		
		// ��ȡ����������ȡ���������ص���Ϣ
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println(result);
		
		br.close();
		obs.close();
		s.close();
		
	}
}
