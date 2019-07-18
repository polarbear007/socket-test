package lesson11_������05_TCPЭ��01_�ϴ��༶�ļ���֮��װ�ļ���Ϣ�����;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

// �ϴ�һ���༶�ļ��е�����������

public class Client {
	public static void main(String[] args) throws Exception {
		 Socket s = new Socket(InetAddress.getLocalHost(), 10086);
		 
		 // ���ȣ�Ҫ�����ϴ����ļ�����
		 File file = new File("ccc\\bbb");
		 String rootPath = file.getAbsoluteFile().getParent();
		 
		 if(file.exists()) {
			 if(file.isFile()) {
				 sendFile(s, file, rootPath);
			 }else if(file.isDirectory()) {
				 sendFolder(s, file, rootPath);
			 }
			 
			 // ��������Ժ����ǿ��Դ���  null �����󣩸�����ˣ����߷���ˣ����е��ļ����Ѿ��ϴ������
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(null);
			oos.flush();
		 }else {
			 System.out.println("�ļ������ڣ�");
		 }
	}

	public static void sendFolder(Socket s, File file, String rootPath) throws Exception {
		// �ļ��е�˼·���ļ���࣬�Ȱ��ļ��е���Ϣ��װ��һ�����󣬴�����������
		String foldername = file.getName();
		String path = file.getAbsoluteFile().getParent().replace(rootPath, "");
		FileMessage fm = new FileMessage("folder", foldername, 0, path);
		
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(fm);
		oos.flush();
		
		// �����Ժ󣬿ͻ��˾������ȴ�������������Ϣ
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String message =  br.readLine();
		//System.out.println(message);
		
		// ������������� ready, ˵���������Ǳ��Ѿ����ÿ��ļ����ˣ���ʼ׼�������ļ��������������
		if(message.equals("ready")) {
			File[] files = file.listFiles();
			if(files != null) {
				for (File f : files) {
					if(f.isFile()) {
						sendFile(s, f, rootPath);
					}else if(f.isDirectory()) {
						sendFolder(s, f, rootPath);
					}
				}
			}else {
				System.out.println(file.getName() + "---------> ���ļ���Ϊ�ջ����޷�����");
			}
		}
	}

	public static void sendFile(Socket s, File file, String rootPath) throws Exception {
		// �ȴ� �ļ���+�ļ�����+�ļ�����+�ļ���·��    ��ȥ(�����һ������)
		String filename = file.getName();
		long length = file.length();
		String path = file.getAbsoluteFile().getParent().replace(rootPath, "");
		FileMessage fm = new FileMessage("file", filename, length, path);
		
		
		
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(fm);
		oos.flush();
		
		// �����Ժ󣬿ͻ��˾������ȴ�������������Ϣ
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String message =  br.readLine();
		System.out.println(message);
		
		// ������������� ready, ˵���������Ǳ��Ѿ����ÿ��ļ��ˣ���ʼ׼�������ļ�����
		if(message.equals("ready")) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
			byte[] bys = new byte[1024];
			int len = 0;
			while((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
			bis.close();
			
			// �ϴ������ļ������Ժ󣬿ͻ��˻���Ҫ�ȴ�������Ǳ߰��ļ����ݶ��������Ժ󣬷�����Ϣ���ܿ�ʼһ�²��Ĳ���
			// readLine() �����������һ������ʽ���������û�ж�ȡ����Ӧ����Ϣ���ͻ�һֱ�ȴ�
			// �����ȡ����ĳ���ַ�������������ַ������� finish �Ļ�����ôҲ�ǳ����ˣ�Ӧ���������쳣
			message =  br.readLine();
			if(!message.equals("finish")) {
				throw new RuntimeException("�������쳣");
			}else {
				System.out.println(filename + "------>�ļ��ϴ��ɹ�");
			}
		}
	}
	
	
}
