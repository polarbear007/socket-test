package lesson11_������04_TCPЭ��9_�����ϴ�һ���༶�ļ���֮���̰߳�1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverDemo {
	public static void main(String[] args) throws IOException {
		// �����������˵�socket����
		ServerSocket ss = new ServerSocket(50000);
		
		// ʹ��һ��whileѭ����ÿ����һ���ͻ������ӵ�ʱ�򣬾�����һ����Ӧ��socket���󣬲��Ҵ���һ��ר�ŵ��߳�����������ͻ��˵���Ϣ
		// �����ӣ��Ϳ���ͬʱ���ն���ͻ����ϴ����ļ��ˡ�����Ҫ�Ŷӣ�һ��һ������
		while(true) {
			// �����ͻ������ӣ���������Ӧ��socket����
			Socket s = ss.accept();
			new Thread(new UserThread(s)).start();
		}
//		ss.close();

	}

	// ���Ƿ���˽����ļ����ļ��еķ�������Ϊ����ʹ�ö��̣߳�����������ʵ����Ҫ�ˣ�
//	public static void receive(Socket s, File folder) throws IOException {
//		// ��Ϊ��������Ȳ���֪���ͻ�����Ҫ�������ļ������ļ��У���������Ū��һ����ѭ������ͣ�ؽ��ա�
//		// �ȿͻ����Ǳ�ȫ���ϴ���ϣ�ʹ�� shutdownOutput() �������ر��������������˳�ѭ����
//		while (true) {
//			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//			String name = br.readLine();
//			 System.out.println("�յ�����" + name);
//			if (name == null) {
//				// ���ͻ����ϴ�����Ժ󣬻�ʹ��shutdownOutput()�� ��ʱ�����ǵ� br.readLine() ������û�취�����κζ����ģ�
//				// ������ʱ��name��ֵΪnull,���ʱ�����Ǿ�֪���ļ����Ѿ���������ˣ������˳�ѭ����
//				break;
//				// �ļ��л��ļ��������ǲ��ܰ���  �� | " < > / \ ? *  ���ַ��ģ������ڴ����ļ�������ʱ�����Ƕ�������ǰ��ƴ��һ�� "Folder:"�����߷�������
//				//  ����������һ���ļ��У��ļ��е�������	Folder: ������ַ�����Ҫʹ�õ�ʱ���ʹ��subString()������һ��
//				// �������õ�������ļ��������Ժ󣬾ͻ���ָ�����ļ������潨��һ��ͬ���Ŀ��ļ���
//			} else if (name.contains("Folder:")) {
//				name = name.substring("Folder:".length());
//				File file = new File(folder, name);
//				file.mkdir();
//				// �������ļ����Ժ󣬿���̨���һ����"Sever: ready to receive folder!"   ������ԣ�����ʹ�õ�ʱ�򣬿���ע�͵���
//				System.out.println("Sever: ready to receive folder!");
//				//  ֮�� ���ǾͿ���ʹ������ͨ���ͻ��ˣ��ҿ��ļ��н����ˣ�����Դ�����ļ�������Ķ����ˡ�
//				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//				bw.write("Sever: ready to receive folder!");
//				bw.newLine();
//				bw.flush();
//				
////				receive(s, folder);
//
//			} else {
//				// ��Ϊ�����ڴ��ļ�����ʱ�򣬻��������ļ������·���ͳ��ȣ�������ǰ��ƴ����һ��  File  �ַ�������Ϊ���������յ�������������Щ��������������ļ��ģ���Щ��������һ���ļ��ģ�
//				// ��һ����֪�����������ڴ��ļ�����ʱ�����Ҫ�ȸ��߷�������Щ��Ϣ��
//				String[] nameArray = name.split("\\|\\|");
//				// �鿴�ַ�������û������ȷ�ָ�
//				// for (int x = 0; x < nameArray.length; x++) {
//				// System.out.println("nameArray[" + x + "] = " + nameArray[x]);
//				// }
//				
//				// �������� nameArray[1] ����Ǹ��ļ������·����  Ϊʲô����ȡ�ļ����أ�   ��Ϊ�����ȡ�ļ�������ô�����Ҫȥ����ʹ�õݹ飬�����ں��ң��벻������Ժ�����ɣ�
//				// �õ�����Ժ󣬽�Ϸ���һ��ʼ������ļ��У����Ǿ�֪����ʲô�ط�����һ���յ�ͬ���ļ�
//				File file = new File(folder, nameArray[1]);
//				file.createNewFile();
//				// ������յ�������ļ��Ĵ�С�����ڸ��߷�������������ļ�д��������ݾ�Ӧ��ֹͣ��
//				int length = Integer.parseInt(nameArray[2]);
//				// �����ǰ���Щ���ݶ���ȡ���ˣ����ļ�Ҳ�������ˣ��Ϳ��Ը��߿ͻ��˿��Կ�ʼ��������
//				System.out.println("Sever: ready to receive file!");
//
//				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//				bw.write("Sever: ready to receive file!");
//				bw.newLine();
//				bw.flush();
//
//				// �ͻ��˽��յ����ǵ���Ϣ�Ժ󣬾�֪�� ���Կ�ʼ���ļ��������ˣ��������ʹ�������
//				BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
//				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//				byte[] bys = new byte[1024];
//				int len = 0;
//				while ((len = bis.read(bys)) != -1) {
//					bos.write(bys, 0, len);
//					bos.flush();
//
//					// ����ܹؼ��� ��ȡ�ļ��ĳ��Ȳ�����bis.read(bys) ���棬��Ϊÿ��ʹ��read���� ��������Ҫ��ȡ�����ݡ���ôֻҪ�����ݾͻᱻ���ҵ���ȫ���������׵ġ�
//					//   ��Ȼ�������Ҫ   while(file.length()!= length && (len = bis.read(bys) != -1))  Ӧ����û������
//					if (file.length() == length) {
//						System.out.println(file.getName() + "---> ������ϣ�");
//						break;
//					}
//				}
//				// ����ԭ���Ĵ��룬������ɺ�û�и��ͻ��˷���һ�½������������������໥�ȴ����⡣
//				bos.close();
//			}
//
//		}
//	}
}
