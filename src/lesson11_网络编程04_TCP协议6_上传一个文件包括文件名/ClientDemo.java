package lesson11_������04_TCPЭ��6_�ϴ�һ���ļ������ļ���;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDemo {
	public static void main(String[] args) throws IOException{
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 10086);
		
		// �Ȱ��ļ��������ȥ
		File file = new File("b.txt");
		String name = file.getName();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write(name);
		bw.newLine();
		bw.flush();
		
		// �ȴ�������������Ϣ
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		// �������������  ok,  ˵���������Ѿ�׼�����ˣ�Ҫ����������
		if(result.equals("ok")) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
			}
			
			bis.close();
			bos.close();
			System.out.println("�ļ��ϴ���ϣ�");
		}else {
			System.out.println("�����������ˣ�");
		}
		
		s.close();
	}
}
