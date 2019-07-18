package lesson11_������04_TCPЭ��6_�ϴ�һ���ļ������ļ���;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(10086);

		Socket s = ss.accept();

		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		// ���Կͻ��˵�IP��ַ��Ϊ���֣���һ���ļ��У��ٰѴ��������ļ���������ļ�������
		File folder = new File(s.getInetAddress().getHostAddress());
		folder.mkdir();
		
		String name = br.readLine();
		// �õ����ļ���������ָ�����ļ�����������һ���յ��ļ�
		File file = new File(folder, name);
		file.createNewFile();

		// ���ͻ��˷�����˵�Ѿ������˿��ļ������԰����ݴ�������
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("ok");
		bw.newLine();
		bw.flush();

		// ��Ϊ��֪�������ļ�����ʲô��������������ʹ���ֽ�������������
		BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
		}
		//  �ļ��ϴ��ɹ�
		System.out.println("�ļ�������� ��");
		bis.close();
		bos.close();
		s.close();
		ss.close();

	}
}
