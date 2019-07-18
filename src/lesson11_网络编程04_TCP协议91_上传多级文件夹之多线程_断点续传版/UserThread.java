package lesson11_������04_TCPЭ��91_�ϴ��༶�ļ���֮���߳�_�ϵ�������;

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
		// ����socket���󣬵õ��ͻ��˵�IP��ַ���������IP��ַ����һ���ļ��У����ڴ������ͻ����ϴ��������ļ�
		// ������������ֱ�Ӵ��ڹ���Ŀ¼�£����ϣ�����ڵ��Ե�ĳ���ط�����ô����Ҫȡһ���ַ�����ƴ�ӣ�
		File folder = new File("C:\\Users\\Administrator\\Desktop");
		folder.mkdir();
		receive(s, folder);
		System.out.println(Thread.currentThread().getName() + "�����ļ�������ϣ�");	
	}

	// ���Ƿ���˽����ļ����ļ��еķ���
	public static void receive(Socket s, File folder) {
		try {

			// ��Ϊ��������Ȳ���֪���ͻ�����Ҫ�������ļ������ļ��У���������Ū��һ����ѭ������ͣ�ؽ��ա�
			// �ȿͻ����Ǳ�ȫ���ϴ���ϣ�ʹ�� shutdownOutput() �������ر��������������˳�ѭ����
			while (true) {
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String name = br.readLine();
				System.out.println("�յ�����" + name);
				if (name == null) {
					// ���ͻ����ϴ�����Ժ󣬻�ʹ��shutdownOutput()�� ��ʱ�����ǵ� br.readLine() ������û�취�����κζ����ģ�
					// ������ʱ��name��ֵΪnull,���ʱ�����Ǿ�֪���ļ����Ѿ���������ˣ������˳�ѭ����
					break;
					// �ļ��л��ļ��������ǲ��ܰ��� �� | " < > / \ ? * ���ַ��ģ������ڴ����ļ�������ʱ�����Ƕ�������ǰ��ƴ��һ��
					// "Folder:"�����߷�������
					// ����������һ���ļ��У��ļ��е������� Folder: ������ַ�����Ҫʹ�õ�ʱ���ʹ��subString()������һ��
					// �������õ�������ļ��������Ժ󣬾ͻ���ָ�����ļ������潨��һ��ͬ���Ŀ��ļ���
				} else if (name.startsWith("Folder:")) {
					name = name.substring("Folder:".length());
					File file = new File(folder, name);
					file.mkdir();
					// �������ļ����Ժ󣬿���̨���һ����"Sever: ready to receive folder!" ������ԣ�����ʹ�õ�ʱ�򣬿���ע�͵���
					System.out.println("Sever: ready to receive folder!");
					// ֮�� ���ǾͿ���ʹ������ͨ���ͻ��ˣ��ҿ��ļ��н����ˣ�����Դ�����ļ�������Ķ����ˡ�
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					bw.write("ready");
					bw.newLine();
					bw.flush();

					// receive(s, folder);

				} else {
					// ��Ϊ�����ڴ��ļ�����ʱ�򣬻��������ļ������·���ͳ��ȣ�������ǰ��ƴ����һ�� File
					// �ַ�������Ϊ���������յ�������������Щ��������������ļ��ģ���Щ��������һ���ļ��ģ�
					// ��һ����֪�����������ڴ��ļ�����ʱ�����Ҫ�ȸ��߷�������Щ��Ϣ��
					String[] nameArray = name.split("\\|\\|");
					// �鿴�ַ�������û������ȷ�ָ�
					// for (int x = 0; x < nameArray.length; x++) {
					// System.out.println("nameArray[" + x + "] = " + nameArray[x]);
					// }

					// �������� nameArray[1] ����Ǹ��ļ������·���� Ϊʲô����ȡ�ļ����أ�
					// ��Ϊ�����ȡ�ļ�������ô�����Ҫȥ����ʹ�õݹ飬�����ں��ң��벻������Ժ�����ɣ�
					// �õ�����Ժ󣬽�Ϸ���һ��ʼ������ļ��У����Ǿ�֪����ʲô�ط�����һ���յ�ͬ���ļ�
					File file = new File(folder, nameArray[1]);
					boolean flag = file.createNewFile();
					// ������յ�������ļ��Ĵ�С�����ڸ��߷�������������ļ�д��������ݾ�Ӧ��ֹͣ��
					long length = Integer.parseInt(nameArray[2]);
					// �����ǰ���Щ���ݶ���ȡ���ˣ����ļ�Ҳ�������ˣ��Ϳ��Ը��߿ͻ��˿��Կ�ʼ��������
					System.out.println("Sever: ready to receive file!");

					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					if(flag) {
						bw.write("ready");
					}else {
						bw.write(String.valueOf(file.length()));
						
					}
					bw.newLine();
					bw.flush();
					
					// ����Ѿ����ڵ��ļ�����С�ڿͻ��˴������ĳ������ݣ���ô˵������ļ��ǲ������ģ���Ҫ����
					// ��Ϊ�ͻ����Ǳߣ�һ�����ַ���˵��ļ��������ľͻ�������ֱ�ӵȴ��������߷���finish, ����������û�и��������Ļ���
					//  ���ֻ��໥�ȴ�������
					if(file.length() < length) {
						// �ͻ��˽��յ����ǵ���Ϣ�Ժ󣬾�֪�� ���Կ�ʼ���ļ��������ˣ��������ʹ�������
						BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true));
						byte[] bys = new byte[1024];
						int len = 0;
						while ((len = bis.read(bys)) != -1) {
							bos.write(bys, 0, len);
							bos.flush();

							// ����ܹؼ��� ��ȡ�ļ��ĳ��Ȳ�����bis.read(bys) ���棬��Ϊÿ��ʹ��read����
							// ��������Ҫ��ȡ�����ݡ���ôֻҪ�����ݾͻᱻ���ҵ���ȫ���������׵ġ�
							// ��Ȼ�������Ҫ while(file.length()!= length && (len = bis.read(bys) != -1)) Ӧ����û������
							if (file.length() == length) {
								System.out.println("ԭ���ļ���С��" + length);
								System.out.println("ʵ�ʽ��մ�С��" + file.length());
								System.out.println(file.getName() + "---> ������ϣ�");
								break;
							}
						}
						bos.close();
					}else {
						System.out.println("�ļ��Ѿ����ڣ�������");
					}
					
					// Ϊ���ÿͻ����ϴ���һ���ļ��󣬵ȴ�һ�£����ﷵ��һ����Ϣ�����߿ͻ��������Ѿ�������ϣ����Խ�����һ��������
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
