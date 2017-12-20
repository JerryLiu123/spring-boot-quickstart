package com.liu.springboot.quickstart.util;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.liu.springboot.quickstart.config.ConstantsConfig;


public class ZookeeperOperator implements Watcher {
	
	private static Logger logger = Logger.getLogger(ZookeeperOperator.class);
	private ZooKeeper zooKeeper;
	AtomicInteger seq = new AtomicInteger();
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	private String hosts;
	
	public ZookeeperOperator(String hosts){
	    this.hosts = hosts;
	}
	
	public void init() {
        try {
            zooKeeper = new ZooKeeper(this.hosts, Integer.parseInt(ConstantsConfig.sessionTimeOut), this);
            countDownLatch.await(4000, TimeUnit.MILLISECONDS);//设置等待超时时间
            //初始化本应用的节点
            if(this.checkChild(ConstantsConfig.ZKMAINZONE, false) == null) {
                this.createPerNode(ConstantsConfig.ZKMAINZONE, "应用主节点".getBytes());
            }
            if(this.checkChild(ConstantsConfig.ZKSERNODE, false) == null) {
                this.createPerNode(ConstantsConfig.ZKSERNODE, "每一个ser注册的节点名目录".getBytes());
            }
            if(this.checkChild(ConstantsConfig.ZKSERNODE+"/0", false) == null) {
                this.createPerNode(ConstantsConfig.ZKSERNODE+"/0", "测试节点".getBytes());
            }
            if(this.checkChild(ConstantsConfig.ZKWORKNODE, false) == null) {
                this.createPerNode(ConstantsConfig.ZKWORKNODE, "任务节点".getBytes());
            }
            if(this.checkChild(ConstantsConfig.ZKWORKALLNODE, false) == null) {
                this.createPerNode(ConstantsConfig.ZKWORKALLNODE, "所有任务".getBytes());
            }
            zooKeeper.getChildren(ConstantsConfig.ZKSERNODE, this);
            zooKeeper.getData(ConstantsConfig.ZKWORKNODE, this, null);
            zooKeeper.getData(ConstantsConfig.ZKWORKALLNODE, this, null);
            logger.info("-----初始化ZookeeperOperator服务成功-----");
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("-----初始化ZookeeperOperator服务失败！！！-----", e);
        } 
	}
	public ZookeeperOperator() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * 监控所有的zook变化
	 */
	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
        if (event == null) {
            return;
        }
        KeeperState keeperState = event.getState(); // 连接状态
        EventType eventType = event.getType(); // 事件类型
        String path = event.getPath(); // 受影响的path
        
        try {
	        if (KeeperState.SyncConnected == keeperState) {
	            if (EventType.None == eventType) {
	                // 成功连接上ZK服务器
	            	logger.info("-----成功连接zookeeper服务器-----");
	                countDownLatch.countDown();
	            } else if (EventType.NodeCreated == eventType) {
	                // 创建节点
	                logger.info("-----创建节点:"+path+"-----");
	            } else if (EventType.NodeDataChanged == eventType) {
	                // 更新节点内容
	                if(path.startsWith(ConstantsConfig.ZKWORKNODE)) {
	                    if(path.equals(ConstantsConfig.ZKWORKALLNODE)) {
	                        logger.info("-----需要执行的所有任务发生变化-----");
	                    }else if(path.equals(ConstantsConfig.ZKWORKNODE+"/"+ConstantsConfig.serID)) {
	                        logger.info("-----本节点执行的任务数量变化 ( "+path+")-----");
	                    }
	                    
	                    logger.info("-----节点："+path+"  修改后内容为："+new String(zooKeeper.getData(path, this, null), "utf-8")+"-----");
	                }
	            } else if (EventType.NodeChildrenChanged == eventType) {
	                // 更新子节点
	                if(path.equals(ConstantsConfig.ZKSERNODE)){
	                	logger.info("-----有server("+path+")加入或停止-----");
	                	//DistributeTask.run();
	                	zooKeeper.getChildren(path, this);
	                	//logger.info("-----有server加入或停止   结束-----");
	                }
	            } else if (EventType.NodeDeleted == eventType) {
	                // 删除节点
	            }
	             
	        } else if (KeeperState.Disconnected == keeperState) {
	        	logger.warn("-----有zk ser断开-----");
	        } else if (KeeperState.AuthFailed == keeperState) {
	            logger.warn("-----permisson 检测失败-----");
	        } else if (KeeperState.Expired == keeperState) {
	            logger.error("-----session 失效-----");
	            this.close();
            	logger.error("session 尝试连接");
            	zooKeeper = new ZooKeeper(ConstantsConfig.zkHosts, Integer.parseInt(ConstantsConfig.sessionTimeOut), this);
	            logger.error("zookeeper 重新连接成功");
	        }
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("-----zookeeper 监控失败 -----", e);
		}
	}
	
	/**
	 * 关闭zookeeper连接
	 * @throws InterruptedException
	 */
	public void close() throws InterruptedException{
		this.zooKeeper.close();
		this.zooKeeper = null;
	}
	
	/**
	 * 创建永久节点
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void createPerNode(String path, byte[] data) throws KeeperException, InterruptedException{
		this.createNode(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	/**
	 * 创建有序临时节点
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void createSeqNode(String path, byte[] data) throws KeeperException, InterruptedException{
		this.createNode(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
	
    /**
     * 创建节点
     * 
     * @param path 节点路径
     * @param data 数据内容
     * @param acl 访问控制列表
     * @param createMode znode创建类型
     * @return
     * @throws InterruptedException 
     * @throws KeeperException 
     */
	public void createNode(String path, byte[] data, List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException {
            //设置监控(由于zookeeper的监控都是一次性的，所以每次必须设置监控)
    	if(this.checkChild(path, false) == null){
    		this.zooKeeper.create(path, data, acl, createMode);
    	}
    }
	
	/**
	 * 判断节点是否存在
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public Stat checkChild(String path, boolean needWatch) throws KeeperException, InterruptedException{
		return this.zooKeeper.exists(path, needWatch);
	}
	
	/**
	 * 获得节点内容
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public byte[] getNodeData(String path, boolean needWatc) throws KeeperException, InterruptedException{
		return this.zooKeeper.getData(path, needWatc, null);
	}
	
	/**
	 * 修改节点内容
	 * @param path
	 * @param data
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public Stat setData(String path, byte[] data) throws KeeperException, InterruptedException{
		return this.zooKeeper.setData(path, data, -1);
	}
    /**
     * 获取子节点
     * 
     * @param path 节点路径
     * @param needWatch  是否监控这个目录节点，这里的 watcher是在创建ZooKeeper实例时指定的watcher
     * @return
     * @throws InterruptedException 
     * @throws KeeperException 
     */
    public List<String> getChildren(String path, boolean needWatch) throws KeeperException, InterruptedException {
    	return this.zooKeeper.getChildren(path, needWatch);

    }
	/**
	 * 删除目录
	 * @param path
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 */
	public void delFile(String path) throws InterruptedException, KeeperException{
		this.zooKeeper.delete(path, -1);
	}
    
    public void delNode(String path) throws KeeperException, InterruptedException{
    	if(this.zooKeeper.exists(path, true) != null){
    		this.zooKeeper.delete(path, -1);
    	}
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }
    
}
