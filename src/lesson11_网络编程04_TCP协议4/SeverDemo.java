package lesson11_网络编程04_TCP协议4;

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
		// 创建服务器端socket对象
		ServerSocket ss = new ServerSocket(12306);

		// 监听客户端连接
		Socket s = ss.accept();

		// 读取客户端传来的数据,获取输入流，因为我们知道传来的数据可能不是文本数据，所以我们就直接使用高效字节流包装
		// 我们要把接收的数据写入 Copy.pdf 文件中，所以再建一个字节高效输出流
		BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("Copy.pdf"));
		byte[] bys = new byte[1024];
		int len = 0;
		while ((len = bis.read(bys)) != -1) {
			bos.write(bys, 0, len);
		}
		
		// 给客户端反馈信息
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("文件上传成功！");
		bw.flush();
		
		// 释放资源 
		bw.close();
		bis.close();
		bos.close();
		s.close();
		ss.close();

	}
}
