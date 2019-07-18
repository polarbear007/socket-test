package lesson11_������04_TCPЭ��91_�ϴ��༶�ļ���֮���߳�_�ϵ�������;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDemo02 {
	public static void main(String[] args) throws IOException {
		Socket s = new Socket(InetAddress.getLocalHost().getHostName(), 50000);

		File folder = new File("ccc");
		String parentPath = folder.getAbsoluteFile().getParent();
		System.out.println(parentPath);
		if (folder.isDirectory()) {
			sendFolder(s, folder, parentPath);
			System.out.println("�����ļ��Ѿ��ϴ���ϣ�");
			s.shutdownOutput();
		} else if (folder.isFile()) {
			sendFile(s, folder, parentPath);
		} else {
			System.out.println("�Ƿ�·����");
		}

		s.close();

	}

	// �ͻ��˰ѷ����ļ��ͷ����ļ���д��������������Ϊ���Ͷ��ǿ���֪��Ҫ���͵����ļ������ļ��е�
	// ���Ƿ����ļ��еķ���

	public static void sendFolder(Socket s, File folder, String parentPath) throws IOException {
		// �Ȱ��ļ������ƴ���ȥ
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		System.out.println("����--->" + "Folder:" + folder.getAbsolutePath().substring(parentPath.length()));
		bw.write("Folder:" + folder.getAbsolutePath().substring(parentPath.length()));
		bw.newLine();
		bw.flush();

		// �ȴ�����˸��ݽ�����Ϣ���ں��ʵ�λ�ý������ļ��к���
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println("���յ�--->" + result);
		// �ȷ������Ǳߴ�����Ϣ˵׼�����ˣ���ô���ǾͿ��Կ�ʼ��������ļ��������������
		if (result.equals("ready")) {
			File[] fileArray = folder.listFiles();
			if (fileArray != null) {
				for (File f : fileArray) {
					// ������ļ���������ļ��У���ô��ʹ�õݹ飬�ٴΰ��ļ�������ȥ��Ȼ���ٱ�������ļ���
					if (f.isDirectory()) {
						sendFolder(s, f, parentPath);
						// ������ļ��Ļ�����ô���Ǿ͵��÷����ļ��ķ���
					} else if (f.isFile()) {
						sendFile(s, f, parentPath);
					}
				}
			}
		}

	}

	public static void sendFile(Socket s, File file, String parentPath) throws IOException {
		// �Ȱ��ļ������ļ����ȷ��͹�ȥ
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		System.out.println(
				"����--->" + "File||" + file.getAbsolutePath().substring(parentPath.length()) + "||" + file.length());
		bw.write("File||" + file.getAbsolutePath().substring(parentPath.length()) + "||" + file.length());
		bw.newLine();
		bw.flush();

		// �ȴ�����˸��ݽ�����Ϣ���ں��ʵ�λ�ý������ļ�����
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println("���յ�--->" + result);
		if (result.equals("ready")) {
			// ��ȡ�ļ����ݣ�д��ͨ�ŵ�������
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());

			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
			System.out.println(file.getPath() + "---> �ϴ���ϣ�");
			
		}else {
			
			long pos = Long.parseLong(result);
			if(pos < file.length()) {
				System.out.println("�ϵ���������");
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
				
				raf.seek(pos);
				byte[] bys = new byte[1024];
				int len = 0;
				while ((len = raf.read(bys)) != -1) {
					bos.write(bys, 0, len);
					bos.flush();
				}
				System.out.println(file.getPath() + "---> �ϵ�������ϣ�");
			}else {
				System.out.println("�������Ѿ�������ļ��ˣ�����");
			}
		}
		
		

		// �ϴ���Ϻ󣬲�Ҫ���Ͻ���һ�²��������ر����ֿ�ʼ����һ���ļ������ֺͳ��ȡ�
		// ��Ϊ�ϴ��ͽ��ղ�����ͬ�������Կ�������ϴ���ܾ��ˣ�����һ���ļ����ͳ��ȶ�����ȥ�ˣ�
		// ������Ǳ߶���û�н�����ɡ���ô��������Ƿ���������һ���ļ����ܻ�����Ǵ���ȥ���ļ����ͳ�����Щ����Ҳ��������һ���ļ������ݣ�д����һ���ļ���������ǣ��ͻ���
		// �ڵȷ������������������ڵȿͻ��˴���һ���ļ������ֺͳ��ȣ���ʵ�Ѿ�����ȥ�ˣ�
		result = br.readLine();
		if(!result.equals("Finish!")) {
			System.out.println("�����ˣ���");
		}
	}

}
