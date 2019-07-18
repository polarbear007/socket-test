package lesson11_网络编程05_TCP协议01_上传多级文件夹之封装文件信息对象版;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(10086);
		
		try {
			while(true) {
				Socket s = ss.accept();
				new Thread(new ServerThread(s)).start();
			}
		}finally {
			ss.close();
		}
		
		
	}
}
