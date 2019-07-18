package lesson11_网络编程04_TCP协议9_真正上传一个多级文件夹_重要;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException {
		// 创建服务器端的socket对象
		ServerSocket ss = new ServerSocket(50000);

		while (true) {
			// 监听客户端连接，并生成相应的socket对象
			Socket s = ss.accept();

			// 根据socket对象，得到客户端的IP地址，并以这个IP地址建立一个文件夹，用于存放这个客户端上传的所有文件
			// （这里我们是直接存在工程目录下，如果希望存在电脑的某个地方，那么就需要取一个字符串来拼接）
			File folder = new File("C:\\Users\\Administrator\\Desktop\\" + System.currentTimeMillis());
			folder.mkdir();

			// 这里我们使用receive() 方法来接收客户端传过来的文件或者文件夹。
			// （使用方法来实现主要是为了传递多级文件夹的时候可以使用递归。）
			// （为什么不把接收文件夹和接收文件分成两个方法呢？ 主要还是我现在还理不清，这个代码也是试出来的）
			receive(s, folder);
			System.out.println("所有文件接收完毕！");

			s.close();
		}
		// ss.close();

	}

	// 这是服务端接收文件或文件夹的方法
	public static void receive(Socket s, File folder) throws IOException {
		// 因为服务端事先并不知道客户端需要传多少文件或者文件夹，所以这里弄了一个死循环，不停地接收。
		// 等客户端那边全部上传完毕，使用 shutdownOutput() 方法，关闭了流，我们再退出循环！
		while (true) {
			System.out.println("等待客户端传名字过来");
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String name = br.readLine();
			System.out.println("收到名字" + name);
			if (name == null) {
				// 当客户端上传完毕以后，会使用shutdownOutput()， 这时候我们的 br.readLine() 方法是没办法读到任何东西的，
				// 所以这时候name的值为null,这个时候我们就知道文件都已经接收完毕了，可以退出循环了
				break;
				// 文件夹或文件名里面是不能包含 ： | " < > / \ ? * 等字符的，所以在传递文件夹名的时候，我们都在名字前面拼接一个
				// "Folder:"，告诉服务器，
				// 传过来的是一个文件夹，文件夹的名字是 Folder: 后面的字符串，要使用的时候就使用subString()方法剪一下
				// 服务器得到了这个文件夹名称以后，就会在指定的文件夹里面建立一个同名的空文件夹
			} else if (name.startsWith("Folder||")) {
				name = name.substring("Folder||".length());
				File file = new File(folder, name);
				file.mkdir();
				// 建好了文件夹以后，控制台输出一个："Sever: ready to receive folder!" 方便调试（真正使用的时候，可以注释掉）
				System.out.println("Sever: ready to receive folder!");
				// 之后 我们就可以使用流，通过客户端，我空文件夹建好了，你可以传这个文件夹里面的东西了。
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write("Sever: ready to receive folder!");
				bw.newLine();
				bw.flush();

				// receive(s, folder);

			} else {
				// 因为我们在传文件名的时候，还附加了文件的相对路径和长度，还在最前面拼接了一个 File
				// 字符串，因为服务器接收的是流，具体哪些部分数据是这个文件的，哪些数据是另一个文件的，
				// 他一无所知。所以我们在传文件名的时候就需要先告诉服务器这些信息。
				String[] nameArray = name.split("\\|\\|");
				// 查看字符数组有没法有正确分割
				// for (int x = 0; x < nameArray.length; x++) {
				// System.out.println("nameArray[" + x + "] = " + nameArray[x]);
				// }

				// 这里我们 nameArray[1] 存的是该文件的相对路径。 为什么不获取文件名呢？
				// 因为如果获取文件名，那么你就需要去考虑使用递归，我现在很乱，想不清楚，以后再想吧！
				// 得到这个以后，结合服务一开始建议的文件夹，我们就知道在什么地方建议一个空的同名文件
				File file = new File(folder, nameArray[1]);
				file.createNewFile();
				// 这里接收的是这个文件的大小，用于告诉服务器，往这个文件写入多少数据就应该停止了
				long length = Long.parseLong(nameArray[2]);
				// 当我们把这些数据都获取好了，空文件也建立好了，就可以告诉客户端可以开始传数据了
				System.out.println("Sever: ready to receive file!");

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write("Sever: ready to receive file!");
				bw.newLine();
				bw.flush();

				// 客户端接收到我们的信息以后，就知道 可以开始传文件的数据了，于是流就传过来了
				BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				byte[] bys = new byte[1024];
				int len = 0;
				while ((len = bis.read(bys)) != -1) {
					bos.write(bys, 0, len);
					bos.flush();
					if (file.length() >= length) {
						System.out.println("原始大小：" + length);
						System.out.println("接收大小：" + file.length());
						break;
					}
				}
				bos.close();
				// 接收完一个文件后，返回一条信息，说已经接收完成了，可以准备接下来的事情了
				// 这一点很重要，因为如果没有返回信息的话，那么在传多个文件的时候，客户端，第一个文件传完了，就马上又传过来下一个文件的名字，和长度。然后开始等服务器反馈。
				// 这里有一个时间差问题，即，客户端传完了的那瞬间，服务器端其实还没有接收完成。那么问题就来了，实际上留在通道里面的流不仅包含文件内容，还包含了下一个文件
				// 或者文件夹的信息。在这种情况下，虽然我们在 while 里面搞了个 file.length() >= length 的判断，但是那是没有用的。因为我们一次读取并写入一个字节数组。
				// 而不是一个字节一个字节地去写。而且使用了缓存流，就算我们一个字节一个字节地读写，其实也是一次读写一个字节数组。 如果要让那个判断好使的话，我们必须不能使用缓冲流，
				// 而且只能一个字节一个字节地读写数据才能有效，但是那样子效率就太低了。总而言之，我们的那个判断并不能真正有效地告诉服务器在什么地方结束写入上一个文件。
				
				// 于是就出现了这样的问题： 
				// 服务器把下一个文件的信息写到上一个文件结尾去了，然后服务器端开始在等下一个文件的名字和长度信息的时候，其实客户端早就传过了，而且在等待服务器端反馈， 
				// 这样就人为造成了相互等待，类似死锁问题。
				
				//	因此，解决的办法是在客户端传完以后，给个阻塞方法，等服务器把上一个文件的流读写完以后，给客户端一个反馈，告诉客户端，可以发下一个文件或者文件文件夹的信息了。
				//  另外，是不是说有了上面的方法，就不需要再传文件的长度信息了呢？  其实也不是的，因为我们之前说过，使用while 去接收数据的话，(len = bis.read(bys)) != -1
				//  是永远也不会自己跳出循环的，因为那个通道流是动态的，没有边界的， len 不可能返回-1， 只有客户端这边关闭socket 对象，或者shutdownOutput() , 那个
				//  while 循环才能跳出来。   但是我们后面可能还有很多文件需要传，当然不能那么做。所以在客户端上传完一个文件以后阻塞等待反馈信息的前提下，把文件的长度信息传过来，
				//	做为服务器端判断的条件还是有效的。
				
				bw.write("Finish!");
				bw.newLine();
				bw.flush();

			}

		}
	}
}
