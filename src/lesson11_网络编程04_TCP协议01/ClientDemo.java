package lesson11_������04_TCPЭ��01;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

// TCPЭ��� UDPЭ�����Ĳ�ͬ���ǣ� TCPͨ������Ҫ˫���Ƚ������ӣ�Ȼ����˫������Ϣ���ļ��� ����ˣ��������ϣ�����յ���Ϣ���ٸ�������Ϣ���Ǳ߷�����Ϣ����ô��Ӧ��ʹ��TCPЭ�飩
//	��UDPֻ�з��Ͷ��ܷ�����Ϣ�����Ҳ��ܽ��ն���û���յ����������ǰ���Ϣ����ȥ�����ˡ�

public class ClientDemo {
	public static void main(String[] args) throws IOException{
		//  �����ͻ���socket���� 
		// public Socket(String host, int port) 
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 12345);
		
		// ��ȡ�������д����(��Ϊ�������ﴫ������ı����ݣ����Կ���ʹ��ת�������������ת�����ַ���)
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		bw.write("���ѽ��");
		bw.newLine();
		bw.flush();
		
		// �ͷ���Դ
		bw.close();
		s.close();
	}
}
