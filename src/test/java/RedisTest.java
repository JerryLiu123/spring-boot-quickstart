import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.liu.springboot.quickstart.service.IRedisService;
import com.liu.springboot.quickstart.task.IQueueTask;
import com.liu.springboot.quickstart.task.RedisQueuePool;
import com.liu.springboot.quickstart.task.work.TestTaskWork;
import com.liu.springboot.quickstart.util.ArrayUtils;

public class RedisTest extends BaseTest {

	@Autowired
	private RedisQueuePool redisQueuePool;
	@Autowired
	private IRedisService redisService;
	@Autowired
	private RedissonClient redissonClient;
	
	@Test
	public void takTest() {
		try {
		    Thread.sleep(2000);
			System.out.println("放入队列开始");
			for(int i=0; i<5;i++) {
				redisQueuePool.pushFromTail(new TestTaskWork(i));
			}
			System.out.println("放入队列结束");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("456", "456");
			map.put("789", "789");
			System.err.println("放入redis信息>>>");
			redisService.set("123", map);
			System.err.println("判断redis信息>>>"+redisService.exists("123"));
			System.err.println("获得redis信息>>>"+redisService.get("123"));
			System.err.println("删除redis信息>>>");
			redisService.delForValue("123");
			System.err.println("判断redis信息>>>"+redisService.exists("123"));
			Thread.sleep(15000);
			RQueue<IQueueTask> rQueue = redissonClient.getQueue("key");//获得队列
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
