package MutiThreadDownload2;

import java.io.IOException;

public class MultiThreadHttpDownloadTest {
	public static void main(String[] args) throws IOException {
		
		//MultiThreadHttpDownload m = new MultiThreadHttpDownload("https://www.baidu.com/img/bd_logo1.png", "C:\\Users\\Administrator\\Desktop\\download");
		MultiThreadHttpDownload m = new MultiThreadHttpDownload("https://ip72057795.ahcdn.com/key=ZWaASeMx9nO4XRV0FDwBUA,s=,end=1532010127,limit=2/state=eCK9/buffer=5000000:5710896,7067.0/speed=70036/reftag=073313961/30/102/5/73151525/000/000/482/482.mp4");
		//MultiThreadHttpDownload m = new MultiThreadHttpDownload("http://download.qianqian.com/channel/1/web_daohang");
		
		if(m.isAvailable()) {
			m.download();
		}
	}
}
