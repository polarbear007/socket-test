package lesson11_网络编程04_TCP协议9_真正上传一个多级文件夹之多线程版1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException {
		// 创建服务器端的socket对象
		ServerSocket ss = new ServerSocket(50000);
		
		// 使用一个while循环，每当有一个客户端连接的时候，就生成一个相应的socket对象，并且创建一个专门的线程来接收这个客户端的信息
		// 这样子，就可以同时接收多个客户端上传的文件了。不需要排队，一个一个来。
		while(true) {
			// 监听客户端连接，并生成相应的socket对象
			Socket s = ss.accept();
			new Thread(new UserThread(s)).start();
		}
//		ss.close();

	}

	// 这是服务端接收文件或文件夹的方法（因为我们使用多线程，所以这里其实不需要了）
//	public static void receive(Socket s, File folder) throws IOException {
//		// 因为服务端事先并不知道客户端需要传多少文件或者文件夹，所以这里弄了一个死循环，不停地接收。
//		// 等客户端那边全部上传完毕，使用 shutdownOutput() 方法，关闭了流，我们再退出循环！
//		while (true) {
//			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//			String name = br.readLine();
//			 System.out.println("收到名字" + name);
//			if (name == null) {
//				// 当客户端上传完毕以后，会使用shutdownOutput()， 这时候我们的 br.readLine() 方法是没办法读到任何东西的，
//				// 所以这时候name的值为null,这个时候我们就知道文件都已经接收完毕了，可以退出循环了
//				break;
//				// 文件夹或文件名里面是不能包含  ： | " < > / \ ? *  等字符的，所以在传递文件夹名的时候，我们都在名字前面拼接一个 "Folder:"，告诉服务器，
//				//  传过来的是一个文件夹，文件夹的名字是	Folder: 后面的字符串，要使用的时候就使用subString()方法剪一下
//				// 服务器得到了这个文件夹名称以后，就会在指定的文件夹里面建立一个同名的空文件夹
//			} else if (name.contains("Folder:")) {
//				name = name.substring("Folder:".length());
//				File file = new File(folder, name);
//				file.mkdir();
//				// 建好了文件夹以后，控制台输出一个："Sever: ready to receive folder!"   方便调试（真正使用的时候，可以注释掉）
//				System.out.println("Sever: ready to receive folder!");
//				//  之后 我们就可以使用流，通过客户端，我空文件夹建好了，你可以传这个文件夹里面的东西了。
//				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//				bw.write("Sever: ready to receive folder!");
//				bw.newLine();
//				bw.flush();
//				
////				receive(s, folder);
//
//			} else {
//				// 因为我们在传文件名的时候，还附加了文件的相对路径和长度，还在最前面拼接了一个  File  字符串，因为服务器接收的是流，具体哪些部分数据是这个文件的，哪些数据是另一个文件的，
//				// 他一无所知。所以我们在传文件名的时候就需要先告诉服务器这些信息。
//				String[] nameArray = name.split("\\|\\|");
//				// 查看字符数组有没法有正确分割
//				// for (int x = 0; x < nameArray.length; x++) {
//				// System.out.println("nameArray[" + x + "] = " + nameArray[x]);
//				// }
//				
//				// 这里我们 nameArray[1] 存的是该文件的相对路径。  为什么不获取文件名呢？   因为如果获取文件名，那么你就需要去考虑使用递归，我现在很乱，想不清楚，以后再想吧！
//				// 得到这个以后，结合服务一开始建议的文件夹，我们就知道在什么地方建议一个空的同名文件
//				File file = new File(folder, nameArray[1]);
//				file.createNewFile();
//				// 这里接收的是这个文件的大小，用于告诉服务器，往这个文件写入多少数据就应该停止了
//				int length = Integer.parseInt(nameArray[2]);
//				// 当我们把这些数据都获取好了，空文件也建立好了，就可以告诉客户端可以开始传数据了
//				System.out.println("Sever: ready to receive file!");
//
//				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//				bw.write("Sever: ready to receive file!");
//				bw.newLine();
//				bw.flush();
//
//				// 客户端接收到我们的信息以后，就知道 可以开始传文件的数据了，于是流就传过来了
//				BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
//				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//				byte[] bys = new byte[1024];
//				int len = 0;
//				while ((len = bis.read(bys)) != -1) {
//					bos.write(bys, 0, len);
//					bos.flush();
//
//					// 这个很关键， 读取文件的长度不能在bis.read(bys) 后面，因为每次使用read（） 方法，都要读取流数据。那么只要流数据就会被打乱掉。全部都会乱套的。
//					//   当然，如果你要   while(file.length()!= length && (len = bis.read(bys) != -1))  应该是没有问题
//					if (file.length() == length) {
//						System.out.println(file.getName() + "---> 接收完毕！");
//						break;
//					}
//				}
//				// 这是原来的代码，接收完成后，没有给客户端反馈一下接收情况，很容易造成相互等待问题。
//				bos.close();
//			}
//
//		}
//	}
}
