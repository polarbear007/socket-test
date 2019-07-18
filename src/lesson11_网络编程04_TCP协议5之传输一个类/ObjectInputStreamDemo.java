package lesson11_网络编程04_TCP协议5之传输一个类;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//  为 accout.txt 添加一些原始的数据，并测试  ObjectInputStream 如何遍布文件的内容 

public class ObjectInputStreamDemo {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("account.txt"));
//		oos.writeObject(new User("admin", "admin"));
//		oos.writeObject(new User("haha", "123456"));
		
		
		FileInputStream fis = new FileInputStream("account.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
	
		
		try {
			while (true) {
				Object o = ois.readObject();
				User u = (User) o;
				System.out.println(u.getUserName() + "---" + u.getPassword());
			}
		}catch(EOFException e) {
			//e.printStackTrace();
		}finally {
			ois.close();
		}
	}
}
