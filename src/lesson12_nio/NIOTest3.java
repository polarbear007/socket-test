package lesson12_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

// 使用非阻塞式的方式来传输一句很短的字符串
public class NIOTest3 {
	@Test
	public void client() throws IOException {
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 10000));
		// 获取channel 以后，直接设置成 非阻塞式的
		socketChannel.configureBlocking(false);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Scanner sc = new Scanner(System.in);
		while(true) {
			String line = sc.nextLine();
			if(line.equals("exit")) {
				break;
			}
			buffer.put(line.getBytes());
			buffer.flip();
			socketChannel.write(buffer);
			buffer.clear();
		}
		
		// socketChannel.shutdownOutput();
		
		sc.close();
		socketChannel.close();
	}
	
	@Test
	public void server() throws IOException {
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		// 先设置成非阻塞
		ssChannel.configureBlocking(false);
		// 绑定端口号
		ssChannel.bind(new InetSocketAddress(10000));
		// 获取 selector 
		Selector selector = Selector.open();
		
		// 然后把这个  ssChannel 注册到selector
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		// 不断地轮询。   
		// select() 方法会阻塞，直到selector 内部监听的事件触发了， select() 方法会返回一个大于0的值
		while(selector.select() > 0) {
			// 获取所有的 keys ，一个 keys 代表 selector 监听的一个事件触发了
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while(it.hasNext()) {
				SelectionKey key = it.next();
				
				if(key.isAcceptable()){
					// 如果key是acceptable，说明有客户端请求进来，那么我们就直接执行 accept
					SocketChannel sChannel = ssChannel.accept();
					
					// 拿到这个 sChannel 第一件事就是把模式设置成非阻塞
					sChannel.configureBlocking(false);
					
					// 然后再把这个 sChannel 注册到 selector
					sChannel.register(selector, SelectionKey.OP_READ);
					
				}else if(key.isReadable()) {
					// 如果 key 是 readAble 的话，那么说明客户端已经有上传数据过来了
					// 我们通过 key 拿到这个 SocketChannel 对象
					SocketChannel sChannel = (SocketChannel) key.channel();
					
					// 读取数据还是需要 byteBuffer 
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					int len = 0;
					int size = 0;
					while(true){
						len = sChannel.read(buffer);
						//【注意】 len 的值是有特殊的含义的
						// 如果 len 的值大于 0 ,说明确实从 sChannel 里面读取到数据了，可能还没有读取完，所以不要退出循环
						// 如果 len 的值等于0, 说明本次sChannel 里面的所有数据都读取完了，但是客户端还没有关闭 ,后面可能还会有新的数据进来
						// 如果len 的值等于-1， 说明客户端的 socketchannel 已经关闭，所以我们服务端也要跟着关闭
						//  【一定要注意判断 -1 的情况，如果没有关闭，会一直触发 readable 事件，但是又读取不到任何数据，死循环】
						if(len > 0) {
							size += len;
							buffer.flip();
							System.out.println(new String(buffer.array(), 0, len));;
							buffer.clear();
						}else if(len == -1) {
							sChannel.close();
							break;
						}else if(len == 0) {
							break;
						}
					}
					System.out.println("本次实际接收：" + size + "字节数据");
				}
				// 取消选择键 SelectionKey
				it.remove();
			}
			
			System.out.println("本次所有事件处理完毕");
		}
	}
}



