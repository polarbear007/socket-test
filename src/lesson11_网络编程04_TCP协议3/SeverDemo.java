package lesson11_网络编程04_TCP协议3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException{
		//创建服务器端的socket对象 
		ServerSocket ss = new ServerSocket(10086);
		
		// 监听客户端连接
		Socket s = ss.accept();
		
		// 读取客户端传过来的数据，获取输入流（因为我们知道是文本数据，所以直接使用高效字符流包装 InputStream）
		// 由于我们需要把读取的数据写入到一个文件中去，因为不知道文件名，所以我们就自己建一个文件  b.txt
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));
		String line = null;
		while((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		
		// 给客户端反馈信息
		bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("文件上传成功！");
		bw.newLine();
		bw.flush();
		
		bw.close();
		br.close();
		s.close();
		ss.close();
		
		
	}
}
