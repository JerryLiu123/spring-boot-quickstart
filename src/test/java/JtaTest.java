import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.liu.springboot.quickstart.service.IMonitorService;
import com.liu.springboot.quickstart.service.IVideoService;

import jodd.datetime.TimeUtil;

public class JtaTest extends BaseTest {

	@Autowired
	private IVideoService videoService;
	@Test 
	public void test(){
		//将videoService转为iMonitorService
//	    System.err.println("------------------");
//	    try {
//	        IMonitorService a = (IMonitorService) videoService;
//	        a.getMonitorInfoBean();
//	        Thread.sleep(10000);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
	    
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
