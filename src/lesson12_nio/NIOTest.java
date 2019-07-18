package lesson12_nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

// fileChannel 有两种获取方式  ：
//		1、 从流对象获取
//      2、 直接从静态方式获取   FileChannel.open()

// 缓冲区的类型：   直接和非直接
//     非直接的缓冲区：       ByteBuffer.allocate()
//     直接缓冲区：              ByteBuffer.allocateDirect()

//     MappedByteBuffer:  也相当于直接缓冲区
//     MappedByteBuffer buffer = fileChannel.map()

//     fileChannel.transferTo() ===>  也相当于使用直接缓冲区
public class NIOTest {
	// 阻塞式的 nio 
	@SuppressWarnings("resource")
	@Test
	public void test() throws Exception {
		// 先获取两个通道
		FileChannel fromChannel = new FileInputStream("vue的简单入门.doc").getChannel();
		FileChannel toChannel = new FileOutputStream("copy.doc").getChannel();
		
		// 然后设置共同的 byteBuffer 缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024*16);
		
		// 再然后使用fromChannel 读取数据，写入 byteBuffer
		// 切换模式，再使用 toChannel 从 byteBuffer 读取数据，写入 toChannel
		while(fromChannel.read(byteBuffer) != -1) {
			// 切换成读取模式
			byteBuffer.flip();
			toChannel.write(byteBuffer);
			// 清空缓冲区
			byteBuffer.clear();
		}
		
		// 释放资源
		fromChannel.close();
		toChannel.close();
	}
	
	// 使用内存映射文件的方式传递文件
	@Test
	public void test2() throws IOException {
		// 上面的获取通道的方式是必须先创建io流对象
		// 但是 nio 的关注点并不是 Io 流对象，所以我们创建 io 流的对象其实没有太大用处
		// 所以 nio 提供了另一种获取通道的方式：  直接 open()
		
		// open() 方法要指定一个Path 对象，我们可以使用 Paths 工具类获取
		// 还需要指定打开通道的方式： 我们可以根据需要传一个或者多个 StandardOpenOption 枚举类对象
		// 对于要读取的文件，我们一般就一个 read 就够了
		// 对于要写入的文件，我们首先需要一个write, 如果文件不存在，还要有创建的权限 create 
		// (因为后面的mapmode 是read_write, 所以这里我们还需要再添加一个read 权限) 
		FileChannel fromChannel = FileChannel.open(Paths.get("vue的简单入门.doc"), StandardOpenOption.READ);
		FileChannel toChannel = FileChannel.open(Paths.get("copy.doc"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
	
		// 拿到了filechannel 以后，我们可以直接使用 map 方法获取缓冲区
		// 这种方式拿到的缓冲区相当于   allocateDirect()
		MappedByteBuffer fromBuffer = fromChannel.map(MapMode.READ_ONLY, 0, fromChannel.size());
		MappedByteBuffer toBuffer = toChannel.map(MapMode.READ_WRITE, 0, fromChannel.size());
		
		// MappedByteBuffer 的大小直接就是文件的大小
		// 所以我们直接就用个   put() 完事儿，不需要while 
		toBuffer.put(fromBuffer);
		
		// 最后，同样要释放资源
		fromChannel.close();
		toChannel.close();
	}
	
	
	// 我们使用transferTo() 或者 transferFrom() 方法这种方式来复制文件，相当于第二种内存映射
	@SuppressWarnings("resource")
	@Test
	public void test3() throws Exception {
		FileChannel fromChannel = new FileInputStream("vue的简单入门.doc").getChannel();
		FileChannel toChannel = new FileOutputStream("copy.doc").getChannel();
		// 使用下面的其中一个方法即可
		// toChannel.transferFrom(fromChannel, 0, fromChannel.size());
		fromChannel.transferTo(0, fromChannel.size(), toChannel);
	}
	
	// 分散读取和聚集写入
	// 分散读取是指： 把一个通道对应的文件，分散到多个缓冲区中（按顺序写入地缓冲区）
	// 聚集写入是指： 把多个缓冲区中的数据，集中写到一个通道中去（也是按顺序写入）
	@Test
	public void test4() throws IOException {
		FileChannel channel = FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);
		
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);
		
		ByteBuffer[] bufs = {buf1, buf2};
		// 分散读取
		channel.read(bufs);
		
		FileChannel channel2 = FileChannel.open(Paths.get("2.txt"), StandardOpenOption.READ, 
				                             StandardOpenOption.WRITE, 
				                             StandardOpenOption.CREATE);
		// 先把所有的缓冲区改变一下读写模式
		for (ByteBuffer byteBuffer : bufs) {
			byteBuffer.flip();
		}
		
		// 聚集写入
		channel2.write(bufs);
		
		channel.close();
		channel2.close();
	}
}
