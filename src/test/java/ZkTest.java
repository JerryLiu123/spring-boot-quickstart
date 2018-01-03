import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.liu.springboot.quickstart.config.ConstantsConfig;
import com.liu.springboot.quickstart.util.ZookeeperOperator;

public class ZkTest extends BaseTest {

    @Autowired
    private ZookeeperOperator zookeeperOperator;
    @Test
    public void test() {
        String zkMySerNode = ConstantsConfig.ZKSERNODE + "/" + ConstantsConfig.serID;
        String zkMyWorkNode = ConstantsConfig.ZKWORKNODE + "/" + ConstantsConfig.serID;
        
        try {
//            zookeeperOperator.createSeqNode(zkMySerNode, "创建有序临时节点".getBytes());
//            zookeeperOperator.setData(zkMyWorkNode, "1111111111".getBytes());
//            zookeeperOperator.setData(ConstantsConfig.ZKWORKALLNODE, "5555555".getBytes());
//            Thread.sleep(4000);
//            System.err.println("---------------");
            zookeeperOperator.createSeqNode(ConstantsConfig.ZKSERNODE + "/2", "创建有序临时节点".getBytes());
//            zookeeperOperator.setData(ConstantsConfig.ZKWORKALLNODE, "4444444".getBytes());
//            zookeeperOperator.setData(zkMyWorkNode, "333333".getBytes());
//            zookeeperOperator.delNode(ConstantsConfig.ZKSERNODE + "/2");
//            Thread.sleep(12000);
        } catch (KeeperException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
