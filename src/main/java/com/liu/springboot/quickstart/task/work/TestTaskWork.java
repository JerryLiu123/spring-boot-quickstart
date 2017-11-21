package com.liu.springboot.quickstart.task.work;

import com.liu.springboot.quickstart.task.IQueueTask;

public class TestTaskWork extends IQueueTask {

	private int value;
	
	public TestTaskWork(int value) {
		super();
		// TODO Auto-generated constructor stub
		this.value = value;
	}

	
	public TestTaskWork() {
		super();
	}

	@Override
	protected boolean needExecuteImmediate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("--------------------------------"+this.value);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("执行结束--------------------------------"+this.value);
	}

}
