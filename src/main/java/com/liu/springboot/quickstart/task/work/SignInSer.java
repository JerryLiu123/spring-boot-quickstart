package com.liu.springboot.quickstart.task.work;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.config.ConstantsConfig;
import com.liu.springboot.quickstart.util.ZookeeperOperator;


@Component
public class SignInSer {

    private static Logger logger = Logger.getLogger(SignInSer.class);
    @Autowired
    private ZookeeperOperator zookeeperOperator;
    
    @Async("taskPool")
    public void run() {
        // TODO Auto-generated method stub
        String zkMySerNode = ConstantsConfig.ZKSERNODE + "/" + ConstantsConfig.serID;
        logger.info("-----开启节点注册:"+zkMySerNode+"-----");
        while(true) {
            try {
                if(zookeeperOperator.checkChild(zkMySerNode, true) == null) {
                    zookeeperOperator.createSeqNode(zkMySerNode, String.valueOf(ConstantsConfig.serID).getBytes());
                    logger.info("-----创建节点成功！！！-----");
                }else {
                    zookeeperOperator.getChildren(zkMySerNode, true);
                    logger.info("-----"+zkMySerNode+"  节点正常-----");
                }
                Thread.sleep(5000);
            } catch (KeeperException e) {
                // TODO Auto-generated catch block
                logger.error("注册节点失败!!!!--KeeperException：", e);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                logger.error("注册节点失败!!!!--InterruptedException：", e);
            }
        }
    }

}
