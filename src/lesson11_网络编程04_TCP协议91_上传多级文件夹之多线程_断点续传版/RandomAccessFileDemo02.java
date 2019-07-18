package lesson11_网络编程04_TCP协议91_上传多级文件夹之多线程_断点续传版;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

// 把test.txt 文档，后半部分删掉，然后再运行这个程序，会发现，丢失的部分补全了！！！
// 但是不知道，io流传输是不是都是从头开始传的
public class RandomAccessFileDemo02 {
	public static void main(String[] args) throws IOException{
		File file = new File("test.txt");
		RandomAccessFile raf = new RandomAccessFile(new File("a.txt"), "rw");
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file , true));
		
		long pos = file.length();
		raf.seek(pos);
		
		byte[] bys = new byte[5];
		int len = 0;
		while((len = raf.read(bys)) != -1) {
			bos.write(bys, 0, len);
			bos.flush();
		}
		
		bos.close();
		raf.close();
	}
}
