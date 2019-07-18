package lesson11_������04_TCPЭ��91_�ϴ��༶�ļ���֮���߳�_�ϵ�������;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

// ��test.txt �ĵ�����벿��ɾ����Ȼ��������������򣬻ᷢ�֣���ʧ�Ĳ��ֲ�ȫ�ˣ�����
// ���ǲ�֪����io�������ǲ��Ƕ��Ǵ�ͷ��ʼ����
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
