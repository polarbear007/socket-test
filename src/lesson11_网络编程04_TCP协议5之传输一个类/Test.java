package lesson11_������04_TCPЭ��5֮����һ����;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Test {
	public static void main(String[] args) throws Exception {
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("account.txt"));
		
//		while(fis.available() > 0 ) {
//			User user = (User)ois.readObject();
//			System.out.println(user);
//		}
		try {
			while(true) {
				User user = (User)ois.readObject();
				System.out.println(user);
			}
		}catch(EOFException e){
			e.printStackTrace();
		}finally {
			ois.close();
		}
		
		
	}
}
