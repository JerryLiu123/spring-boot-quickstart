import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.liu.springboot.quickstart.service.VideoService;

public class JtaTest extends BaseTest {

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
}
