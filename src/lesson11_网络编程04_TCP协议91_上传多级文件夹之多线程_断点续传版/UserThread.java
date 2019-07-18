package lesson11_网络编程04_TCP协议91_上传多级文件夹之多线程_断点续传版;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class UserThread implements Runnable {
	private Socket s;

	public UserThread(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		// 根据socket对象，得到客户端的IP地址，并以这个IP地址建立一个文件夹，用于存放这个客户端上传的所有文件
		// （这里我们是直接存在工程目录下，如果希望存在电脑的某个地方，那么就需要取一个字符串来拼接）
		File folder = new File("C:\\Users\\Administrator\\Desktop");
		folder.mkdir();
		receive(s, folder);
		System.out.println(Thread.currentThread().getName() + "所有文件接收完毕！");	
	}

	// 这是服务端接收文件或文件夹的方法
	public static void receive(Socket s, File folder) {
		try {

			// 因为服务端事先并不知道客户端需要传多少文件或者文件夹，所以这里弄了一个死循环，不停地接收。
			// 等客户端那边全部上传完毕，使用 shutdownOutput() 方法，关闭了流，我们再退出循环！
			while (true) {
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
				} else if (name.startsWith("Folder:")) {
					name = name.substring("Folder:".length());
					File file = new File(folder, name);
					file.mkdir();
					// 建好了文件夹以后，控制台输出一个："Sever: ready to receive folder!" 方便调试（真正使用的时候，可以注释掉）
					System.out.println("Sever: ready to receive folder!");
					// 之后 我们就可以使用流，通过客户端，我空文件夹建好了，你可以传这个文件夹里面的东西了。
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					bw.write("ready");
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
					boolean flag = file.createNewFile();
					// 这里接收的是这个文件的大小，用于告诉服务器，往这个文件写入多少数据就应该停止了
					long length = Integer.parseInt(nameArray[2]);
					// 当我们把这些数据都获取好了，空文件也建立好了，就可以告诉客户端可以开始传数据了
					System.out.println("Sever: ready to receive file!");

					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					if(flag) {
						bw.write("ready");
					}else {
						bw.write(String.valueOf(file.length()));
						
					}
					bw.newLine();
					bw.flush();
					
					// 如果已经存在的文件长度小于客户端传过来的长度数据，那么说明这个文件是不完整的，需要续传
					// 因为客户端那边，一旦发现服务端的文件是完整的就会跳过，直接等待服务端这边反馈finish, 如果我们这边没有跟着跳过的话，
					//  就又会相互等待！！！
					if(file.length() < length) {
						// 客户端接收到我们的信息以后，就知道 可以开始传文件的数据了，于是流就传过来了
						BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true));
						byte[] bys = new byte[1024];
						int len = 0;
						while ((len = bis.read(bys)) != -1) {
							bos.write(bys, 0, len);
							bos.flush();

							// 这个很关键， 读取文件的长度不能在bis.read(bys) 后面，因为每次使用read（）
							// 方法，都要读取流数据。那么只要流数据就会被打乱掉。全部都会乱套的。
							// 当然，如果你要 while(file.length()!= length && (len = bis.read(bys) != -1)) 应该是没有问题
							if (file.length() == length) {
								System.out.println("原来文件大小：" + length);
								System.out.println("实际接收大小：" + file.length());
								System.out.println(file.getName() + "---> 接收完毕！");
								break;
							}
						}
						bos.close();
					}else {
						System.out.println("文件已经存在，跳过！");
					}
					
					// 为了让客户端上传完一个文件后，等待一下，这里返回一下信息，告诉客户端这里已经接收完毕，可以进行下一步操作了
					bw.write("Finish!");
					bw.newLine();
					bw.flush();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
