package lesson11_网络编程04_TCP协议6_上传一个文件包括文件名;

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
		// 先以客户端的IP地址作为名字，建一个文件夹，再把传过来的文件放在这个文件夹里面
		File folder = new File(s.getInetAddress().getHostAddress());
		folder.mkdir();
		
		String name = br.readLine();
		// 拿到了文件名，就在指定的文件夹里面生成一个空的文件
		File file = new File(folder, name);
		file.createNewFile();

		// 给客户端反馈，说已经建好了空文件，可以把数据传过来了
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("ok");
		bw.newLine();
		bw.flush();

		// 因为不知道传的文件会是什么，所以我们这里使用字节流来传输数据
		BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
		}
		//  文件上传成功
		System.out.println("文件接收完毕 ！");
		bis.close();
		bos.close();
		s.close();
		ss.close();

	}
}
