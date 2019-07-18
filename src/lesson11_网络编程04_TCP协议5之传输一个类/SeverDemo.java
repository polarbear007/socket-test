package lesson11_网络编程04_TCP协议5之传输一个类;

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
		//创建服务器端socket对象 
		ServerSocket ss = new ServerSocket(12345);
		
		// 监听客户端连接
		Socket s = ss.accept();
		
		// 获取输入流，读取客户端发过来的数据。因为我们知道发过来的是一个类，所以我们使用ObjectInputStream 类来包装
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		User u = (User)ois.readObject();
		System.out.println("u :" +  u);
		
		// 把拿到的对象跟文本文件里面存的数据进行对比，如果找到一样的，就返回true
		boolean flag = false;
		ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("account.txt"));
		
		try {
			while(true) {
				User user = (User)ois2.readObject();
				System.out.println("user :　" + user);
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
			bw.write("验证成功！");
		}else {
			bw.write("验证失败！");
		}
		
		
		bw.close();
		ois.close();
		s.close();
		ss.close();
		
	}
}
