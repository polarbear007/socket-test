package lesson11_������04_TCPЭ��7_�ϴ������ļ������ļ���;

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

// ��ΪTCPЭ���ǰ��ļ�ת���������䣬��ô���Ҫ������ļ���������ǲ������֣���Щ����������ļ�����Щ�������Ǹ��ļ��ġ����ֻ��һ���ļ�����ô���ǿ���ͨ��shutdownoutput() ����������
//	���������ļ��Ѿ��ϴ���ϡ����������Ҫ������ļ�����ô����Ҫ�������ķָ  ��ˣ��ڴ���֮ǰ������Ҫ���ļ�������Ҫ���ļ��ĳ��ȡ�ֻҪ���������յ��ļ����ȹ��ˣ���ֹͣ���գ��ĳɽ�����һ��

public class ClientDemo {
	public static void main(String[] args) throws IOException {
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 10086);

		sendFile(s, new File("a.txt"));
		System.out.println("Client:  Start to send the second file!");
		sendFile(s, new File("b.txt"));
	}

	private static void sendFile(Socket s, File f) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write(f.getName());
		System.out.println("Client: Send file name---->" + f.getName());
		bw.newLine();
		bw.flush();
		bw.write(String.valueOf(f.length()));
		System.out.println("Client: Send the length of the file---->" + String.valueOf(f.length()));
		bw.newLine();
		bw.flush();

		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		if (br.readLine().equals("ok")) {
			System.out.println("Client: Start to upload!");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
			bis.close();
		}
	}
}
