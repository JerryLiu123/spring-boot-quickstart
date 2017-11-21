package com.liu.springboot.quickstart.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;

/**
* 线程池
* 所有任务基类
* 其他任务必须继承此抽象类
* 
* @author lgh
*/
public abstract class IQueueTask implements Runnable, Serializable {
    // private static Logger logger = Logger.getLogger(Task.class);
    /* 产生时间 */
    private Date generateTime = null;
    /* 提交执行时间 */
    private Date submitTime = null;
    /* 开始执行时间 */
    private Date beginExceuteTime = null;
    /* 执行完成时间 */
    private Date finishTime = null;

    private long taskId;

    public IQueueTask() {
        this.generateTime = new Date();
    }

    /**
    * 任务执行入口
    */
    public abstract void run();

    /**
    * 是否需要立即执行
    * 
    * @return
    */
    protected abstract boolean needExecuteImmediate();

    /**
    * 任务信息
    * 
    * @return String
    */
    public abstract String info();

    public Date getGenerateTime() {
        return generateTime;
    }

    public Date getBeginExceuteTime() {
        return beginExceuteTime;
    }

    public void setBeginExceuteTime(Date beginExceuteTime) {
        this.beginExceuteTime = beginExceuteTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

}

