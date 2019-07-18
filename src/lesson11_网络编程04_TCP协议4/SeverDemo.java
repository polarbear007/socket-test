package lesson11_������04_TCPЭ��4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException {
		// ������������socket����
		ServerSocket ss = new ServerSocket(12306);

		// �����ͻ�������
		Socket s = ss.accept();

		// ��ȡ�ͻ��˴���������,��ȡ����������Ϊ����֪�����������ݿ��ܲ����ı����ݣ��������Ǿ�ֱ��ʹ�ø�Ч�ֽ�����װ
		// ����Ҫ�ѽ��յ�����д�� Copy.pdf �ļ��У������ٽ�һ���ֽڸ�Ч�����
		BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("Copy.pdf"));
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
		}
		
		// ���ͻ��˷�����Ϣ
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("�ļ��ϴ��ɹ���");
		bw.flush();
		
		// �ͷ���Դ 
		bw.close();
		bis.close();
		bos.close();
		s.close();
		ss.close();

	}
}
