import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.liu.springboot.quickstart.Application;
import com.liu.springboot.quickstart.service.VideoService;

/**
 *@author lgh
 *@date:2016年5月4日上午11:21:58
 *@version:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
@WebAppConfiguration // 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
public class BaseTest {

//	@Autowired
//	private BiZoneInfoMapper biZoneInfoMapper;
	@Autowired
	private VideoService videoService;
	
	@Test
	public void test(){
		
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try {
					videoService.testException2();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				try {
					videoService.testException();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	@Test
	public void testName() throws Exception {
		BiZoneInfo biZoneInfo = new BiZoneInfo();
		biZoneInfo.setvFileid(11111);
		biZoneInfo.setzAvailable(11111);
		biZoneInfo.setzFile("testFile1");
		biZoneInfo.setzHdfsfile("testHdfsFile2");
		biZoneInfo.setzIsdel(11111);
		//biZoneInfoMapper.insert(biZoneInfo);
		System.out.println("------------");
		
	}*/
}
