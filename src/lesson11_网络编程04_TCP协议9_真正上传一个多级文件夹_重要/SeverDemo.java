package lesson11_������04_TCPЭ��9_�����ϴ�һ���༶�ļ���_��Ҫ;

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
		// �����������˵�socket����
		ServerSocket ss = new ServerSocket(50000);

		while (true) {
			// �����ͻ������ӣ���������Ӧ��socket����
			Socket s = ss.accept();

			// ����socket���󣬵õ��ͻ��˵�IP��ַ���������IP��ַ����һ���ļ��У����ڴ������ͻ����ϴ��������ļ�
			// ������������ֱ�Ӵ��ڹ���Ŀ¼�£����ϣ�����ڵ��Ե�ĳ���ط�����ô����Ҫȡһ���ַ�����ƴ�ӣ�
			File folder = new File("C:\\Users\\Administrator\\Desktop\\" + System.currentTimeMillis());
			folder.mkdir();

			// ��������ʹ��receive() ���������տͻ��˴��������ļ������ļ��С�
			// ��ʹ�÷�����ʵ����Ҫ��Ϊ�˴��ݶ༶�ļ��е�ʱ�����ʹ�õݹ顣��
			// ��Ϊʲô���ѽ����ļ��кͽ����ļ��ֳ����������أ� ��Ҫ���������ڻ����壬�������Ҳ���Գ����ģ�
			receive(s, folder);
			System.out.println("�����ļ�������ϣ�");

			s.close();
		}
		// ss.close();

	}

	// ���Ƿ���˽����ļ����ļ��еķ���
	public static void receive(Socket s, File folder) throws IOException {
		// ��Ϊ��������Ȳ���֪���ͻ�����Ҫ�������ļ������ļ��У���������Ū��һ����ѭ������ͣ�ؽ��ա�
		// �ȿͻ����Ǳ�ȫ���ϴ���ϣ�ʹ�� shutdownOutput() �������ر��������������˳�ѭ����
		while (true) {
			System.out.println("�ȴ��ͻ��˴����ֹ���");
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
			} else if (name.startsWith("Folder||")) {
				name = name.substring("Folder||".length());
				File file = new File(folder, name);
				file.mkdir();
				// �������ļ����Ժ󣬿���̨���һ����"Sever: ready to receive folder!" ������ԣ�����ʹ�õ�ʱ�򣬿���ע�͵���
				System.out.println("Sever: ready to receive folder!");
				// ֮�� ���ǾͿ���ʹ������ͨ���ͻ��ˣ��ҿ��ļ��н����ˣ�����Դ�����ļ�������Ķ����ˡ�
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write("Sever: ready to receive folder!");
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
				file.createNewFile();
				// ������յ�������ļ��Ĵ�С�����ڸ��߷�������������ļ�д��������ݾ�Ӧ��ֹͣ��
				long length = Long.parseLong(nameArray[2]);
				// �����ǰ���Щ���ݶ���ȡ���ˣ����ļ�Ҳ�������ˣ��Ϳ��Ը��߿ͻ��˿��Կ�ʼ��������
				System.out.println("Sever: ready to receive file!");

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write("Sever: ready to receive file!");
				bw.newLine();
				bw.flush();

				// �ͻ��˽��յ����ǵ���Ϣ�Ժ󣬾�֪�� ���Կ�ʼ���ļ��������ˣ��������ʹ�������
				BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				byte[] bys = new byte[1024];
				int len = 0;
				while ((len = bis.read(bys)) != -1) {
					bos.write(bys, 0, len);
					bos.flush();
					if (file.length() >= length) {
						System.out.println("ԭʼ��С��" + length);
						System.out.println("���մ�С��" + file.length());
						break;
					}
				}
				bos.close();
				// ������һ���ļ��󣬷���һ����Ϣ��˵�Ѿ���������ˣ�����׼����������������
				// ��һ�����Ҫ����Ϊ���û�з�����Ϣ�Ļ�����ô�ڴ�����ļ���ʱ�򣬿ͻ��ˣ���һ���ļ������ˣ��������ִ�������һ���ļ������֣��ͳ��ȡ�Ȼ��ʼ�ȷ�����������
				// ������һ��ʱ������⣬�����ͻ��˴����˵���˲�䣬����������ʵ��û�н�����ɡ���ô��������ˣ�ʵ��������ͨ������������������ļ����ݣ�����������һ���ļ�
				// �����ļ��е���Ϣ������������£���Ȼ������ while ������˸� file.length() >= length ���жϣ���������û���õġ���Ϊ����һ�ζ�ȡ��д��һ���ֽ����顣
				// ������һ���ֽ�һ���ֽڵ�ȥд������ʹ���˻���������������һ���ֽ�һ���ֽڵض�д����ʵҲ��һ�ζ�дһ���ֽ����顣 ���Ҫ���Ǹ��жϺ�ʹ�Ļ������Ǳ��벻��ʹ�û�������
				// ����ֻ��һ���ֽ�һ���ֽڵض�д���ݲ�����Ч������������Ч�ʾ�̫���ˡ��ܶ���֮�����ǵ��Ǹ��жϲ�����������Ч�ظ��߷�������ʲô�ط�����д����һ���ļ���
				
				// ���Ǿͳ��������������⣺ 
				// ����������һ���ļ�����Ϣд����һ���ļ���βȥ�ˣ�Ȼ��������˿�ʼ�ڵ���һ���ļ������ֺͳ�����Ϣ��ʱ����ʵ�ͻ�����ʹ����ˣ������ڵȴ��������˷����� 
				// ��������Ϊ������໥�ȴ��������������⡣
				
				//	��ˣ�����İ취���ڿͻ��˴����Ժ󣬸��������������ȷ���������һ���ļ�������д���Ժ󣬸��ͻ���һ�����������߿ͻ��ˣ����Է���һ���ļ������ļ��ļ��е���Ϣ�ˡ�
				//  ���⣬�ǲ���˵��������ķ������Ͳ���Ҫ�ٴ��ļ��ĳ�����Ϣ���أ�  ��ʵҲ���ǵģ���Ϊ����֮ǰ˵����ʹ��while ȥ�������ݵĻ���(len = bis.read(bys)) != -1
				//  ����ԶҲ�����Լ�����ѭ���ģ���Ϊ�Ǹ�ͨ�����Ƕ�̬�ģ�û�б߽�ģ� len �����ܷ���-1�� ֻ�пͻ�����߹ر�socket ���󣬻���shutdownOutput() , �Ǹ�
				//  while ѭ��������������   �������Ǻ�����ܻ��кܶ��ļ���Ҫ������Ȼ������ô���������ڿͻ����ϴ���һ���ļ��Ժ������ȴ�������Ϣ��ǰ���£����ļ��ĳ�����Ϣ��������
				//	��Ϊ���������жϵ�����������Ч�ġ�
				
				bw.write("Finish!");
				bw.newLine();
				bw.flush();

			}

		}
	}
}
