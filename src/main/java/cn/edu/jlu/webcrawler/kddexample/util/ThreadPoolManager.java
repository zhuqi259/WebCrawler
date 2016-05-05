package cn.edu.jlu.webcrawler.kddexample.util;

import cn.edu.jlu.webcrawler.kddexample.task.Download;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhuqi259 on 2015/10/30.
 */
public class ThreadPoolManager {
    private static ThreadPoolManager instance = null;
    private List<Download> taskQueue = Collections
            .synchronizedList(new LinkedList<Download>());// 任务队列
    private WorkThread[] workQueue; // 工作线程（真正执行任务的线程）
    private static int worker_num = 100; // 工作线程数量（默认工作线程数量是100）
    private int worker_sum = 0;

    private int completedCount = 0;
    private Object completeLock = new Object(); // 同步锁辅助对象

    // private static int worker_count = 0;

    private ThreadPoolManager() {
        this(5);
    }

    private ThreadPoolManager(int num) {
        worker_num = num;
        workQueue = new WorkThread[worker_num];
        for (int i = 0; i < worker_num; i++) {
            workQueue[i] = new WorkThread(i);
        }
    }

    public static synchronized ThreadPoolManager getInstance() {
        if (instance == null)
            instance = new ThreadPoolManager();
        return instance;
    }

    // public void addTask(Upload task) {
    // worker_sum++;
    // // 对任务队列的操作要上锁
    // synchronized (taskQueue) {
    // if (task != null) {
    // taskQueue.add(task);
    // taskQueue.notifyAll();
    // System.out.println("task id " + task.getInfo() + " submit!");
    // }
    // }
    // }

    public void BatchAddTask(Download[] tasks) {
        // 此时只有一个线程操作completedCount
        completedCount = 0;
        worker_sum = tasks.length;
        // 对任务队列的修改操作要上锁
        synchronized (taskQueue) {
            for (Download e : tasks) {
                if (e != null) {
                    taskQueue.add(e);
                    taskQueue.notifyAll();
                    System.out.println("task id " + e.getInfo() + " submit!");
                }
            }
        }
    }

    // 先保证所有任务已经执行完毕
    public void completed() {
        synchronized (completeLock) {
            while (completedCount < worker_sum) { // 保证线程都执行完了
                try {
                    completeLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void destory() {
        System.out.println("pool begins to destory ...");

        for (int i = 0; i < worker_num; i++) {
            workQueue[i].stopThread();
            workQueue[i] = null;
        }
        // 对任务队列的操作要上锁
        synchronized (taskQueue) {
            taskQueue.clear();
        }
        instance = null;
        System.out.println("pool ends to destory ...");
    }

    private class WorkThread extends Thread {
        private int taksId;
        private boolean isRuning = true;
        private boolean isWaiting = false;

        public WorkThread(int taskId) {
            this.taksId = taskId;
            this.start();
        }

        public boolean isWaiting() {
            return isWaiting;
        }

        // 如果任务进行中时，不能立刻终止线程，需要等待任务完成之后检测到isRuning为false的时候，退出run()方法
        public void stopThread() {
            isRuning = false;
        }

        @Override
        public void run() {
            while (isRuning) {
                Download temp = null;
                // 对任务队列的操作要上锁
                synchronized (taskQueue) {
                    // 任务队列为空，等待新的任务加入
                    while (isRuning && taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait(20);
                        } catch (InterruptedException e) {
                            System.out
                                    .println("InterruptedException occured...");
                            e.printStackTrace();
                        }
                    }
                    if (isRuning)
                        temp = taskQueue.remove(0);
                }
                // 当等待新任务加入时候，终止线程(调用stopThread函数)造成 temp ＝ null
                if (temp != null) {
                    System.out.println("task info: " + temp.getInfo()
                            + " is begining");
                    isWaiting = false;
                    temp.downloadFile();
                    synchronized (completeLock) {
                        completedCount++;
                        completeLock.notifyAll();
                    }
                    isWaiting = true;
                    System.out.println("task info: " + temp.getInfo()
                            + " is finished");
                }
            }
        }
    }
}
