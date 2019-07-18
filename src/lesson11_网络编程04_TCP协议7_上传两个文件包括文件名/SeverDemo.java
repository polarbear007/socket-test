package lesson11_������04_TCPЭ��7_�ϴ������ļ������ļ���;

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
		File folder = new File(s.getInetAddress().getHostAddress() + "x");
		folder.mkdirs();
		receiveFile(s, folder);
		System.out.println("Server: Start to receive the second file!");
		receiveFile(s, folder);
	}

	private static void receiveFile(Socket s, File folder) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String name = br.readLine();
		System.out.println("Server: receive the file name---->" +name);
		int length = Integer.parseInt(br.readLine());
		File file = new File(folder,name);
		System.out.println("Server: receive the length of the file---->"+length);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("ok");
		bw.newLine();
		bw.flush();
		System.out.println("Server: Ok! Ready to receive a File!");
		
		
		BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
			bos.flush();
			// ����˳���������һ��ȣ����˳���  ������while ���������жϣ���Ϊ���ʹ�� while(file.length() < length) �Ļ�����������ļ���һ�������ݶ�ȡ������
			if(file.length() == length) {
				break;
			}
		}
		bos.close();
		System.out.println("Server: One file received!");

	}

//	private static File receiveFolderName(Socket s, File folder) throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//		String name = br.readLine();
//		File copyFolder = new File(folder, name);
//		copyFolder.mkdir();
//		return copyFolder;
//	}
}
