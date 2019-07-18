package lesson11_网络编程04_TCP协议91_上传多级文件夹之多线程_断点续传版;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileDemo01 {
	public static void main(String[] args) throws IOException{
		RandomAccessFile raf = new RandomAccessFile(new File("test.txt"), "rw");
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("a.txt")));
		
		byte[] bys = new byte[5];
		int len = 0;
		while((len = bis.read(bys)) != -1) {
			raf.write(bys, 0, len);
		}
		
		bis.close();
		raf.close();
	}
}
