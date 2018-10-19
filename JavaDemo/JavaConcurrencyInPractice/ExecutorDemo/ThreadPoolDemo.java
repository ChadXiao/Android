package chad.xiao.executordemo;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import chad.xiao.executordemo.Task.MyCallable;
import chad.xiao.executordemo.Task.MyCallbackWIthException;
import chad.xiao.executordemo.Task.MyRunnable;
import chad.xiao.executordemo.Task.MyRunnableWithException;

/**
 * Created by Chad.xiao on 2018/10/17 0017 12:20.
 * newFixedThreadPool开辟固定长度的线程池,如果原来线程池抛异常，会开辟新的线程池
 * newCachedThreadPool创建一个可缓存的线程池，如果线程池当前规模超过了处理需求时，会回收空闲的线程，当需求增加时，则可以添加新的线程，线程池的规模不存在任何限制
 * newSingleThreadExecutor创建一个单线程的线程池来执行任务，如果线程异常结束，会创建另一个线程来替代。确保任务在队列中的顺序来串行执行（例如FIFO、LIFO、优先级）
 * Executor扩展了ExecutorService接口，用于管理生命周期
 *      void shutdown();//平缓的关闭已添加的线程，不在接收新的任务，同时等等已经提交的任务执行完成
 *      List<Runnable> shutdownNow();//尝试取消所有运行中的任务，并且不在启动任务队列中尚未开始执行的任务，返回未执行的任务列表
 *      boolean isShutdown();//返回线程池是否处于关闭状态。
 *      boolean isTerminated();//用于轮询线程池是否已经终止。
 *      boolean awaitTermination(long timeout, TimeUnit unit)throws InterruptedException;//等待线程池达到终止状态,需配合shutdown()或者shutdownNow()使用
 */
public class ThreadPoolDemo {

    public static void main(String[] args){
//        runnableTest(Executors.newFixedThreadPool(2));
//        callableTest(Executors.newCachedThreadPool());
//        runnableTest(Executors.newCachedThreadPool());
//        runnableTest(Executors.newSingleThreadExecutor());
//        awaitTerminationTest(Executors.newCachedThreadPool());
//        shutdownTest(Executors.newFixedThreadPool(5));
        testScheduledThreadPool();
    }

    private static void runnableTest(ExecutorService executorService){
        for (int i = 0; i < 10; i++){
            executorService.execute(new MyRunnable("t" + i));
        }
    }

    public static void callableTest(ExecutorService executorService){
        try {
            for (int i = 0; i < 10; i++) {
                Future<String> result = executorService.submit(new MyCallable("t" + i));
                System.out.println(" result: " + result.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void runningThrowExceptionTest(ExecutorService executorService){
        for (int i = 0; i < 20; i++){
            executorService.execute(new MyRunnableWithException("t" + i));
        }
    }

    private static void callableThrowExceptionTest(ExecutorService executorService){
        try {
            for (int i = 0; i < 20; i++) {
                Future<String> result = executorService.submit(new MyCallbackWIthException("t" + i));
                System.out.println(i + " result: " + result.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void shutdownTest(ExecutorService executorService){
        for (int i = 0; i < 20; i++){
            executorService.execute(new MyRunnable("t" + i));
        }
        System.out.println("before shutdown is shutdown : " + executorService.isShutdown());
        executorService.shutdown();
        System.out.println("after shutdown is shutdown : " + executorService.isShutdown());
        for (int i = 20; i < 30; i++){
            executorService.execute(new MyRunnable("t" + i));
        }
    }

    private static void shutdownNowTest(ExecutorService executorService){
        for (int i = 0; i < 20; i++){
            executorService.execute(new MyRunnable("t" + i));
        }
        System.out.println("before shutdownNow is shutdown : " + executorService.isShutdown());
        executorService.shutdownNow();
        System.out.println("after shutdownNow is shutdown : " + executorService.isShutdown());
        for (int i = 20; i < 30; i++){
            executorService.execute(new MyRunnable("t" + i));
        }
    }

    /**
     * @param executorService
     */
    public static void awaitTerminationTest(ExecutorService executorService){
        try {
            for (int i = 0; i < 20; i++) {
                executorService.execute(new MyRunnable("t" + i));
            }
            System.out.println("before awaitTermination is termination : " + executorService.isTerminated());
            executorService.shutdown();
            boolean isTerminated = executorService.awaitTermination(4, TimeUnit.SECONDS);
            System.out.println("after awaitTermination is termination : " + isTerminated);
//            for (int i = 20; i < 30; i++) {
//                executorService.execute(new MyRunnable("t" + i));
//            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void testScheduledThreadPool(){
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(5);
        System.out.println("start time " + new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis()));
        exec.scheduleWithFixedDelay(
                new Runnable() {

                    int count = 0;

                    @Override
                    public void run() {
                        System.out.println("thread " + Thread.currentThread().getName() + " run " + count++  + " at " + new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis()));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ,3
                ,1
                ,TimeUnit.SECONDS
        );
    }


}


