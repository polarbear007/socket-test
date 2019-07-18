package lesson12_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import org.junit.Test;

// 使用非阻塞式的方式来传输一个文件
public class NIOTest4 {
	
	@Test
	public void test() throws IOException {
		for (int i = 0; i < 5; i++) {
			client();
		}
	}
	
	@Test
	public void client() throws IOException {
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 10000));
		// 获取channel 以后，直接设置成 非阻塞式的
		//socketChannel.configureBlocking(false);
		
		// 获取 fileChannel 对象
		FileChannel fileChannel = FileChannel.open(Paths.get("1.avi"), StandardOpenOption.READ);
		
		// 获取一个 bytebuffer 用于存取数据
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		
		while(fileChannel.read(buffer) != -1) {
			buffer.flip();
			socketChannel.write(buffer);
			buffer.clear();
		}
		
		// 上传完以后，使用这个通知服务器自己已经上传完了
		socketChannel.shutdownOutput();
		
		// 上传完了以后，不要马上关闭 socketChannel ，先等服务器接收完以后再关闭
		int len = socketChannel.read(buffer);
		buffer.flip();
		String message = new String(buffer.array(), 0, len);
		buffer.clear();
		if("ok".equals(message)) {
			socketChannel.close();
		}
		
		//socketChannel.close();
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
					FileChannel fileChannel = (FileChannel) key.attachment();
					if(fileChannel == null) {
						long timestamp = System.currentTimeMillis();
						// 保存客户端上传的文件，需要fileChannel 
						fileChannel = FileChannel.open(Paths.get(timestamp + ".avi"), StandardOpenOption.APPEND, 
																		StandardOpenOption.WRITE, 
																		StandardOpenOption.CREATE);
						key.attach(fileChannel);
					}
					System.out.println(fileChannel);
					// 读取数据还是需要 byteBuffer 
					ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
					
					
					int len = 0;
					int size = 0;
					while(true){
						len = sChannel.read(buffer);
						//【注意】 len 的值是有特殊的含义的
						// 如果 len 的值大于 0 ,说明确实从 sChannel 里面读取到数据了，可能还没有读取完，所以不要退出循环
						// 如果 len 的值等于0, 说明本次sChannel 里面的所有数据都读取完了，但是客户端还没有关闭 ,后面可能还会有新的数据进来
						//   【如果是阻塞io的话，不会有等于0的情况，有数据就是大于0，没有数据就是-1； 断开连接也是-1】
						// 如果len 的值等于-1， 说明客户端的 socketchannel 已经关闭，所以我们服务端也要跟着关闭
						//  【一定要注意判断 -1 的情况，如果没有关闭，会一直触发 readable 事件，但是又读取不到任何数据，死循环】
						if(len > 0) {
							size += len;
							buffer.flip();
							fileChannel.write(buffer);
							buffer.clear();
						}else if(len == -1) {
							// 通知客户端服务端已经接收完毕，可以发送下一个，或者关闭客户端了
							sChannel.write(buffer.put("ok".getBytes()));
							// 最后我们一定要记得 close ，不然很可能会因为没有close 而写入失败
							// 这个 close 应该有 flush 的功能
							fileChannel.close();
							
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



