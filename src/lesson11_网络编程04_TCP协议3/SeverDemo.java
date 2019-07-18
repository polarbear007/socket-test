package lesson11_������04_TCPЭ��3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException{
		//�����������˵�socket���� 
		ServerSocket ss = new ServerSocket(10086);
		
		// �����ͻ�������
		Socket s = ss.accept();
		
		// ��ȡ�ͻ��˴����������ݣ���ȡ����������Ϊ����֪�����ı����ݣ�����ֱ��ʹ�ø�Ч�ַ�����װ InputStream��
		// ����������Ҫ�Ѷ�ȡ������д�뵽һ���ļ���ȥ����Ϊ��֪���ļ������������Ǿ��Լ���һ���ļ�  b.txt
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));
		String line = null;
		while((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		
		// ���ͻ��˷�����Ϣ
		bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("�ļ��ϴ��ɹ���");
		bw.newLine();
		bw.flush();
		
		bw.close();
		br.close();
		s.close();
		ss.close();
		
		
	}
}
