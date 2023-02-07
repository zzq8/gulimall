package com.zzq.gulimall.search.thread;

import java.util.concurrent.*;

public class ThreadTest {

    /**
     * 必须还是自己写一下，不然很陌生。发现自己写不出！
     * 都是靠着 Thread 的 start() 启动，而new的这个 Thread 都是直接或者间接实现了 Runnable 接口
     * 1）Thread 方式启动
     *         new thread01().start();
     *
     * 2）Runnable
     *         new Thread(new runnable01()).start();
     *
     * 3）Callable+FutureTask 【陌生】
     *         FutureTask<Integer> futureTask = new FutureTask<>(new callable01());  //FutureTask 间接实现了 Runnable
     *         new Thread(futureTask).start();
     *         //阻塞等待整个线程执行完成,获取返回结果    你可以把这个get放在逻辑最后面,不影响其他逻辑的执行
     *         System.out.println(futureTask.get());  //get()获取 run()的返回值
     *
     * 4）线程池【ExecutorService】：我们以后再业务代码里面,以上三种启动线程的方式都不用.【将所有的多线程异步任务都交给线程池执行】 以上三种都不能控制资源，只有线程池能控制 性能稳定
     *          方式一：工具类
     *              //注意这个是属性，但是最好还是在方法中先写会有提示！然后再剪切到属性。不然光在属性中写点方法没提示不说左边也不给自动生成，光只有右半边会报错就又要去源码copy
     *              public static ExecutorService pool = Executors.newFixedThreadPool(10);//注意带了 s 是Executors工具类
     *              pool.execute(new thread01()); //给线程池直接提交任务  注意submit() 有返回值，execute()没
     *          方式二：直接 new ---> 参数是面试重点  ---> 线程池建议用原生是为了规避资源耗尽的风险，也就是 OOM
     *              new ThreadPoolExecutor(线程池七大参数)
     *
     *              corePoolSize – the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set
     *              maximumPoolSize – the maximum number of threads to allow in the pool
     *
     *              keepAliveTime - 解雇临时工，针对核心线程数外的线程超出这个时间还没有被使用就解雇
     *              unit – the time unit for the keepAliveTime argument
     *
     *              workQueue – the queue to use for holding tasks before they are executed. This queue will hold only the Runnable tasks submitted by the execute method.
     *              阻塞队列.如果任务有很多,就会将目前多的任务放在队列里面.只要有线程空闲,就会去队列里面取出新的任务继续执行.
     *              threadFactory – the factory to use when the executor creates a new thread  【一般默认的，除非想自定义比如想给线程名字一个约束】
     *              RejectedExecutionHandler handler – the handler to use when execution is blocked because the thread bounds and queue capacities are reached【如果队列和maximumPoolSize要满了,按照我们指定的拒绝策略拒绝执行任务】
     *
     *              new ThreadPoolExecutor(5,
     *                 200,
     *                 10,
     *                 TimeUnit.SECONDS,
     *                 new LinkedBlockingQueue<>(1000), //这里注意要填 capacity 不填默认 this(Integer.MAX_VALUE); 要出问题
     *                 Executors.defaultThreadFactory(),
     *                 //抛出异常，并且丢弃掉任务
     *                 new ThreadPoolExecutor.AbortPolicy());
     *
     *                 CompletableFuture.runAsync(()->{
     *             System.out.println(Thread.currentThread().getName() + " Begin.");
     *             int c = 10 / 2;
     *             System.out.println(Thread.currentThread().getName() + " End.");
     *         },pool);
     */
    public static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Begin.");

        /**
         * whenComplete(res,exception) 方法完成后的感知，看下面例子
         * handle(res,exception) 方法完成后的处理，相对上面可以 return
         */
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " Begin.");
            int c = 10 / 0;
            System.out.println(Thread.currentThread().getName() + " End.");
            return c;
        }, pool).whenComplete((res,exception)->{
            //虽然能得到异常信息,但是没法修改返回数据.
            System.out.println("异步任务成功完成了...结果是:"+res+";异常是:"+exception);
        }).exceptionally(throwable -> {
            //可以感知异常,同时返回默认值
            return 10;
        });
        Integer integer = future.get();

        System.out.println(Thread.currentThread().getName() + " End."+integer);
    }

    static class callable01 implements Callable<Integer> {


        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " Begin.");
            int c = 10 / 2;
            System.out.println(Thread.currentThread().getName() + " End.");
            return c;
        }
    }

    static class runnable01 implements Runnable{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " begin..");
            int c = 10 / 2;
            System.out.println(Thread.currentThread().getName() + " end.."+c);
        }
    }

    static class thread01 extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " begin..");
            int c = 10 / 2;
            System.out.println(Thread.currentThread().getName() + " end..");
        }
    }




}
