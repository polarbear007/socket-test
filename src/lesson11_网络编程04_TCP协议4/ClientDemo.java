package lesson11_������04_TCPЭ��4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

// ��    Java.pdf �ļ��ϴ��������������ı�Ŷ���������ط�����Ϣ

public class ClientDemo {
	public static void main(String[] args) throws IOException {
		// �����ͻ���socket����
		Socket s = new Socket(InetAddress.getLocalHost(), 12306);

		// ��ȡ�ļ����ݣ�������Щ����д���������
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream("Java.pdf"));
		BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
			bos.flush();
		}
		
		// �ر��������֪ͨ�������������Ѿ�ȫ���ϴ����
		s.shutdownOutput();
		
		// �ȴ�������������Ϣ
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		System.out.println(br.readLine());
		
		br.close();
		bis.close();
		bos.close();
		s.close();
		
		
	}
}
