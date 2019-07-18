package lesson11_网络编程04_TCP协议6_上传一个文件包括文件名;

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
		
		// 先把文件名传输过去
		File file = new File("b.txt");
		String name = file.getName();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write(name);
		bw.newLine();
		bw.flush();
		
		// 等待服务器返回信息
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		// 如果服务器返回  ok,  说明服务器已经准备好了，要接收数据了
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
			System.out.println("文件上传完毕！");
		}else {
			System.out.println("服务器出错了！");
		}
		
		s.close();
	}
}
