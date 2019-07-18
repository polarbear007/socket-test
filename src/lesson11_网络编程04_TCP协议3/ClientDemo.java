package lesson11_������04_TCPЭ��3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

// �� a.txt �ļ��ϴ��� ����������������ɺ󣬷���������"�ϴ��ɹ�"��Ϣ���ͻ���

public class ClientDemo {
	public static void main(String[] args) throws IOException{
		// �����ͻ���socket���� 
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 10086);
		
		// ��ȡ�ı��ļ����ݣ�   ���Ѷ�ȡ��������д���������
		BufferedReader br = new BufferedReader(new FileReader("a.txt"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		String line = null;
		while((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		
		// �ر��������֪ͨ�������ˣ������Ѿ�ȫ���ϴ�
		s.shutdownOutput();
		
		// �ȴ��������˷���
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println(result);
		
		br.close();
		bw.close();
		s.close();
	}
}
