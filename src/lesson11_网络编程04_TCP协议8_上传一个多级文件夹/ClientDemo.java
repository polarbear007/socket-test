package lesson11_������04_TCPЭ��8_�ϴ�һ���༶�ļ���;

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
	public static void main(String[] args) throws IOException {
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 12306);

		File folder = new File("ccc");
		if(folder.isDirectory()) {
			sendFolder(s, folder);
			System.out.println("�����ļ��Ѿ��ϴ���ϣ�");
			s.shutdownOutput();
		}else if(folder.isFile()) {
			sendFile(s, folder);
		}else {
			System.out.println("�Ƿ�·����");
		}
		
		s.close();

	}

	// �ͻ��˰ѷ����ļ��ͷ����ļ���д��������������Ϊ���Ͷ��ǿ���֪��Ҫ���͵����ļ������ļ��е�
	//  ���Ƿ����ļ��еķ���
	
	public static void sendFolder(Socket s, File folder) throws IOException {
		// �Ȱ��ļ������ƴ���ȥ
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		System.out.println("����--->" + "Folder:" + folder.getPath());
		bw.write("Folder:" + folder.getPath());
		bw.newLine();
		bw.flush();

		// �ȴ�����˸��ݽ�����Ϣ���ں��ʵ�λ�ý������ļ��к���
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println("���յ�--->" + result);
		// �ȷ������Ǳߴ�����Ϣ˵׼�����ˣ���ô���ǾͿ��Կ�ʼ��������ļ��������������
		if (result.equals("Sever: ready to receive folder!")) {
			File[] fileArray = folder.listFiles();
			if(fileArray != null) {
				for(File f : fileArray) {
					// ������ļ���������ļ��У���ô��ʹ�õݹ飬�ٴΰ��ļ�������ȥ��Ȼ���ٱ�������ļ���
					if(f.isDirectory()) {
						sendFolder(s, f);
						// ������ļ��Ļ�����ô���Ǿ͵��÷����ļ��ķ���
					}else if(f.isFile()) {
						sendFile(s, f);
					}
				}
			}
		}

	}

	public static void sendFile(Socket s, File file) throws IOException {
		// �Ȱ��ļ������ļ����ȷ��͹�ȥ
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		System.out.println("����--->" + "File||" + file.getPath() + "||" + file.length());
		bw.write("File||" + file.getPath() + "||" + file.length());
		bw.newLine();
		bw.flush();

		// �ȴ�����˸��ݽ�����Ϣ���ں��ʵ�λ�ý������ļ�����
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println("���յ�--->" + result);
		if (result.equals("Sever: ready to receive file!")) {
			// ��ȡ�ļ����ݣ�д��ͨ�ŵ�������
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());

			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
			bis.close();
			System.out.println(file.getPath() + "---> �ϴ���ϣ�");
		}
	}

}
