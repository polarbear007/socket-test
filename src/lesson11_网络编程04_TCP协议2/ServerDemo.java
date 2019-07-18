package lesson11_网络编程04_TCP协议2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo {
	public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket(12306);
		
		Socket s = ss.accept();
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String line = null;
		while((line= br.readLine()) != null) {
			System.out.println(line);
		}
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("信息接收完毕！");
		bw.newLine();
		bw.flush();
		
		bw.close();
		br.close();
		s.close();
		ss.close();
		
	}
}
