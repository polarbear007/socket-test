package lesson11_������05_TCPЭ��01_�ϴ��༶�ļ���֮��װ�ļ���Ϣ�����;

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
		// ���ȣ�������ô˵����ȡ���������Ժ��ȸ��� socket �����ip+�˿ںŴ���һ����Ŀ¼
		String rootPath = "C:\\Users\\Administrator\\Desktop\\hehe\\" + s.getInetAddress() + "_" + s.getPort();
		File rootFolder = new File(rootPath);
		rootFolder.mkdirs();
		
		// ������Ǵ���дһ����ѭ������ͣ�ؽ��տͻ��˴��������ļ������ļ��У�ֱ���ļ��ϴ����
		while(true) {
			// ���ܽ����ļ������ļ��У����Ƕ�Ҫ�Ƚ��� FileMessage ����
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
			
			// �����˶�Ӧ�Ŀ��ļ��Ժ����ǾͿ��Ը��ͻ��˷�����Ϣ��
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bw.write("ready");
			bw.newLine();
			bw.flush();
			
			
			// �ȿͻ����Ǳ߽��յ�����Ϣ�󣬾ͻῪʼ�����ļ��� ��������ҲҪ׼���ý����ļ�����
			BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			byte[] bys = new byte[1024];
			int len = 0;
			while((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
				
				// ʵ���� (len = bis.read(bys)) != -1  ������ʽ����������Զ�����ģ���Ϊͨ�������Ƕ���ر�ͨ������Ȼ��Զ�����ܷ��� -1
				// ����������Ҫÿ��д�������Ժ󣬶��Ƚ�һ�� 
				if(file.length() == fm.getLength()) {
					break;
				}
			}
			
			bos.close();
			
			// ������ѭ����˵�����������Ѿ�������ϣ���ʱ�ͻ������ڵȴ�����˵���Ϣ
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
			
			// �������ļ���,���ǾͿ���֪ͨ�ͻ��˿�ʼ���ļ���������ļ���
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bw.write("ready");
			bw.newLine();
			bw.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
