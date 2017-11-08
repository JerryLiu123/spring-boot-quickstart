import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.liu.springboot.quickstart.service.RedisService;
import com.liu.springboot.quickstart.util.ArrayUtils;
import com.liu.springboot.quickstart.util.task.RedisThreadPool;
import com.liu.springboot.quickstart.util.task.work.TestTaskWork;

public class RedisTest extends BaseTest {

	@Autowired
	private RedisThreadPool redisThreadPool;
	@Autowired
	private RedisService redisService;
	
	@Test
	public void takTest() {
		try {
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("456", "456");
			map.put("789", "789");
			redisService.set("123", map);
			System.out.println("放入队列开始");
			for(int i=0; i<5;i++) {
				redisThreadPool.pushFromTail(new TestTaskWork(i));
			}
			System.out.println("放入队列结束");
			System.err.println(redisService.exists("123"));
			System.err.println(redisService.get("123"));
			System.err.println(redisService.delForValue(new ArrayList<String>() {{add("123");}}));
			System.err.println(redisService.exists("123"));
			Thread.sleep(15000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
