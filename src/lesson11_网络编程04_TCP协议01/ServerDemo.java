package lesson11_������04_TCPЭ��01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo {
	public static void main(String[] args) throws IOException{
		//�����������˵�socket���� 
		ServerSocket ss = new ServerSocket(12345);
		
		// �����ͻ������ӣ�����У�������һ����Ӧ��socket���� 
		Socket s = ss.accept();
		
		// ͨ���õ���socket���� ����ȡ����������ȡ����(��Ϊ����֪����ȡ�����ı����ݣ����԰������������װ���ַ���)
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		String line = null;
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
		
		br.close();
		s.close();
		// ��������һ���ǲ��رյ�
		//ss.close();
		
	}
}
