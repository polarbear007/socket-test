package lesson11_������05_TCPЭ��01_�ϴ��༶�ļ���֮��װ�ļ���Ϣ�����;

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
