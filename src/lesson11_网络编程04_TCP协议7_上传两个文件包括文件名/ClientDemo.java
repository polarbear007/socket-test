package lesson11_网络编程04_TCP协议7_上传两个文件包括文件名;

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

// 因为TCP协议是把文件转换成流传输，那么如果要传多个文件，服务端是不能区分，哪些数据是这个文件，哪些数据是那个文件的。如果只有一个文件，那么我们可以通过shutdownoutput() 方法，告诉
//	服务器，文件已经上传完毕。但是如果需要传多个文件，那么就需要考虑流的分割。  因此，在传输之前，除了要传文件名，还要传文件的长度。只要服务器接收的文件长度够了，就停止接收，改成接收下一个

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
