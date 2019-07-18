package lesson11_网络编程05_TCP协议01_上传多级文件夹之封装文件信息对象版;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread implements Runnable{
	private Socket s;
	public ServerThread(Socket s) {
		this.s = s;
	}
	
	@Override
	public void run() {
		// 首先，不管怎么说，获取到了连接以后，先根据 socket 对象的ip+端口号创建一个根目录
		String rootPath = "C:\\Users\\Administrator\\Desktop\\hehe\\" + s.getInetAddress() + "_" + s.getPort();
		File rootFolder = new File(rootPath);
		rootFolder.mkdirs();
		
		// 这里，我们打算写一个死循环，不停地接收客户端传过来的文件或者文件夹，直到文件上传完毕
		while(true) {
			// 不管接收文件还是文件夹，我们都要先接收 FileMessage 对象
			FileMessage fm = null;
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				fm = (FileMessage)ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if(fm != null) {
				if(fm.getType().equals("file")) {
					receiveFile(fm, rootFolder);
				}else if(fm.getType().equals("folder")) {
					receiveFolder(fm, rootFolder);
				}
			}else {
				break;
			}
			
			
				
		}
	}
	
	
	public void receiveFile(FileMessage fm, File rootFolder) {
		String filename =  fm.getName();
		String path = rootFolder.getAbsolutePath() + fm.getParentPath();
		//System.out.println(path);
		File file = new File(path, filename);
		try {
			file.createNewFile();
			
			// 建好了对应的空文件以后，我们就可以给客户端返回信息了
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bw.write("ready");
			bw.newLine();
			bw.flush();
			
			
			// 等客户端那边接收到了信息后，就会开始传输文件。 我们这里也要准备好接收文件内容
			BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			byte[] bys = new byte[1024];
			int len = 0;
			while((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
				
				// 实际上 (len = bis.read(bys)) != -1  这个表达式在这里是永远成立的，因为通道流除非对面关闭通道，不然永远不可能返回 -1
				// 所以我们需要每次写入数据以后，都比较一下 
				if(file.length() == fm.getLength()) {
					break;
				}
			}
			
			bos.close();
			
			// 跳出了循环后，说明服务器端已经接收完毕，此时客户端正在等待服务端的信息
			bw.write("finish");
			bw.newLine();
			bw.flush();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public void receiveFolder(FileMessage fm, File rootFolder) {
		String filename =  fm.getName();
		String path = rootFolder.getAbsolutePath() + fm.getParentPath();
		//System.out.println(path);
		File file = new File(path, filename);
		try {
			file.mkdir();
			
			// 建好了文件夹,我们就可以通知客户端开始传文件夹里面的文件了
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bw.write("ready");
			bw.newLine();
			bw.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
