package lesson11_������04_TCPЭ��2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

//  ����¼���ַ����������͸��������ˡ�  Ȼ����������ؽ�����Ϣ��ʵ�ֽ�����

public class ClientDemo {
	public static void main(String[] args) throws IOException {
		// �����ͻ���socket����
		Socket s = new Socket(InetAddress.getLocalHost(), 12306);
		// ����¼������
		Scanner sc = new Scanner(System.in);
		while (true) {
			String line = sc.nextLine();
			if (line.equalsIgnoreCase("over")) {
				break;
			}
			// ��ȡ�����
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		// �ر�����׽��ֵ��������֪ͨ�������ˣ���Ϣ�Ѿ�ȫ���������
		// �����û����仰���������ڿͻ������� ��over��,
		// ���￪ʼ�ȴ��������˵ķ�����Ϣ�����Ƿ������˲�֪�����Ѿ�ȫ����������ˣ�whileѭ����ԶҲ�����������ڵȴ��ͻ��˴������ݹ�ȥ���໥�ȴ���������
		s.shutdownOutput();
		// ��÷���
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result = br.readLine();
		System.out.println(result);

		br.close();
		sc.close();
		s.close();

	}
}
