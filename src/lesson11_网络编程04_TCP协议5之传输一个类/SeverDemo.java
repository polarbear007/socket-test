package lesson11_������04_TCPЭ��5֮����һ����;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		//������������socket���� 
		ServerSocket ss = new ServerSocket(12345);
		
		// �����ͻ�������
		Socket s = ss.accept();
		
		// ��ȡ����������ȡ�ͻ��˷����������ݡ���Ϊ����֪������������һ���࣬��������ʹ��ObjectInputStream ������װ
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		User u = (User)ois.readObject();
		System.out.println("u :" +  u);
		
		// ���õ��Ķ�����ı��ļ����������ݽ��жԱȣ�����ҵ�һ���ģ��ͷ���true
		boolean flag = false;
		ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("account.txt"));
		
		try {
			while(true) {
				User user = (User)ois2.readObject();
				System.out.println("user :��" + user);
				if(user.equals(u)) {
					flag = true;
					break;
				}
			}
		}catch(EOFException e) {
			e.printStackTrace();
		}finally {
			ois2.close();
		}
		
		
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		if(flag) {
			bw.write("��֤�ɹ���");
		}else {
			bw.write("��֤ʧ�ܣ�");
		}
		
		
		bw.close();
		ois.close();
		s.close();
		ss.close();
		
	}
}
