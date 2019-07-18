package lesson12_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

// 使用阻塞式的 nio 网络传输一个文件
// 虽然这里使用的 nio ，但是仍然是使用阻塞式的 方式来传输数据的
public class NIOTest2 {
	@Test
	public void client() throws IOException {
		// 先获取客户端的连接通道 
		// 客户端需要指定 ip + 端口号
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 10000));
		
		// 因为我们需要读取本地文件，上传给服务器，所以还需要一个fileChannel
		FileChannel fileChannel = FileChannel.open(Paths.get("vue的简单入门.doc"), StandardOpenOption.READ);
		
		// 指定一个缓冲区用来存取数据
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		// 读写数据
		while(fileChannel.read(byteBuffer) != -1) {
			byteBuffer.flip();
			sChannel.write(byteBuffer);
			byteBuffer.clear();
		}
		
		// 因为只是传一个文件，所以我们这里没有通知服务端这里已经发送完了
		// 如果是传多个文件，一定要通知服务器，这里已经发送完了
		sChannel.shutdownOutput();
		
		// 最后释放资源 
		fileChannel.close();
		sChannel.close();
	}
	
	@Test
	public void server() throws IOException {
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		// ServerSocketChannel 只需要指定端口号，不需要指定 ip 地址
		ssChannel.bind(new InetSocketAddress(10000));
		
		// 阻塞等待客户端连接, 如果有客户连接了，就会生成一个对应的 socketChannel
		SocketChannel sChannel = ssChannel.accept();
		
		// 要把客户端上传的文件保存起来，我们同样需要一个fileChannel
		FileChannel fileChannel = FileChannel.open(Paths.get("copy.doc"), 
				                                    StandardOpenOption.WRITE, 
				                                    StandardOpenOption.CREATE);
		
		// 同样需要一个 byteBuffer 来存取数据
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		while(sChannel.read(byteBuffer) != -1) {
			byteBuffer.flip();
			fileChannel.write(byteBuffer);
			byteBuffer.clear();
		}
		
		// 通知一下客户端，我们这里接收完成
		sChannel.shutdownInput();
		
		// 因为只是传一个文件，所以没有通知客户端这里已经接收完了
		fileChannel.close();
		sChannel.close();
		// 一般服务端的通道是不需要关闭的，但是这里只是演示
		ssChannel.close();
	}
}
