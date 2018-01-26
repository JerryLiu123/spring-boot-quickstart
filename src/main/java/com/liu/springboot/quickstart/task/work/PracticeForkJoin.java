package com.liu.springboot.quickstart.task.work;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * RecursiveAction,没有返回值得fork/join
 * RecursiveTask<>，有返回值得fork/join
 * @author xiaoming
 *
 */
public class PracticeForkJoin extends RecursiveTask<Integer> {

    private List<String> values;//执行的数据
    private int sThreshold;//最小执行的大小
    
    public PracticeForkJoin(List<String> values, int sThreshold) {
        super();
        this.values = values;
        this.sThreshold = sThreshold;
    }

    @Override
    protected Integer compute() {
        // TODO Auto-generated method stub
        int mLength = values.size();
        if(mLength < sThreshold) {
            int ag = computeDirectly();
            return ag;
        }else {
            //切分任务
            int split = mLength / 2;
//            invokeAll(new PracticeForkJoin(values.subList(0, split), sThreshold), 
//                    new PracticeForkJoin(values.subList(split, values.size()), sThreshold));
            PracticeForkJoin leftWork = new PracticeForkJoin(values.subList(0, split), sThreshold);
            PracticeForkJoin righWork = new PracticeForkJoin(values.subList(split, values.size()), sThreshold);
            leftWork.fork();//执行任务
            righWork.fork();
            return leftWork.join() + righWork.join();
        }
    }
    
    private Integer computeDirectly() {
        int ag = 0;
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
            System.err.println("当前任务大小为-----"+this.values.size());
            for(String a : values) {
                ag ++;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ag;
    }
    
    public static void main(String[] args) {
        List<String> vas = Arrays.asList("1,2,3,4,5,6,7,8,9,1,2,4,5,6,7,7,6,6,6,6,6,66".split(","));
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        System.out.println(vas.size()+"----------"+vas.size()/2);
        long bg = System.currentTimeMillis();
        Future<Integer> result = forkJoinPool.submit(new PracticeForkJoin(vas, 5));
        long ed = System.currentTimeMillis();
        try {
            System.out.println("执行时间为:"+(ed-bg)+"----返回结果为:"+result.get());
            TimeUnit.MILLISECONDS.sleep(20000);
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
