# 线程中断

------

> Java没有提供任何机制来安全地终止线程。但它提供了中断（Interruption）,这是一种协作机制，能使一个线程终止另一个线程的当前工作。

    public class Thread{
        public void interrupt();//将线程的interrupted状态设置为ture
        public boolean isInterrupted();//返回线程的interrupted状态
        public static boolean interrupted();//清除interrupted状态，并返回上一个状态
    }
    
## 可中断的阻塞函数 ##

 - Thread.sleep()
 - BlockingQueue.put()

## Future取消超时任务 ##

    public static void timeRun(Runnable r, long timeout, TimeUnit unit)throws InterruptedException{
        Future<?> task = taskExec.submit(r);
        try{
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            //任务超时
        } catch (ExecutionException e) {
            //任务抛出异常的话，会重新抛出异常
            throw launderThrowable(e.getCause());
        } finally {
            //如果任务已经结束，执行取消操作也不会带来任何影响
            task.cancel(true);
        }
    }
    
    
## 不可中断阻塞的处理 ##

 - Java.io包中的Socket I/O。在服务器的应用程序中，最常见的阻塞I/O形式就是对套接字进行读取和写入。虽然InputStream和OutputStream中的read和write等方法都不会响应中断，但通过关闭底层的套接字，可以使得由于执行read或write等方法而被阻塞的线程抛出一个SocketException。
 - Java.io包中的同步I/O。当中断一个正在InterruptibleChannel上等待的线程时，将抛出ClosedByInterruptException并关闭链路（这还会使得其他在这条链路上阻塞的线程同样抛出ClosedByInterruptException）。当关闭一个InterruptibleChannel时，将导致所有在链路操作上阻塞的线程都抛出AsynchronousCloseException。大多数标准的Channel都实现了InterruptibleChannel。
 - Selector的异步I/O。如果一个线程在调用Selector.select方法（在java.nio.channels中）时阻塞了，那么调用close或wakeup方法会使线程抛出ClosedSelectorException并提前返回。
 - 获取锁。如果一个线程由于等待锁而阻塞，那么将无法响应中断，因为线程认为它肯定会获得锁，所以将不会理会中断请求。但是，在Lock类中提供了lockInterruptibly方法，该方法允许在等待一个锁的同事仍能响应中断

        //重写interrupt()方法实现非标准取消操作
        public class ReaderThread extends Thread {
            private final Socket socket;
            private final InputStream in;
            
            public ReaderThread(Socket socket) throw IOException {
                this.socket = socket;
                this.in = socket.getInputStream();
            }
            
            public void interrupt(){
                try {
                    socket.close();
                } catch (IOException ignored) {
                    
                } finally {
                    super.interrupt();
                }
            }
            
            public void run(){
                try {
                    byte[] buf = new byte[BUFSZ];
                    while(true) {
                        int count = in.read(buf);
                        if (count < 0) {
                            break;
                        } else if (count > 0) {
                            processBuffer(buf, count);
                        }
                    }
                } catch(IOException e){/*线程退出*/}
            }
        }
        

        //重写newTaskFor实现非标准取消操作,使得Callable可以返回自定义的Future
        public interface CallableTask<T> extends Callable<T> {
            void cancel();
            RunnableFuture<T> newTask();
        }
        
        @ThreadSafe
        public class CancellingExecutor extends ThreadPoolExecutor {
            ...
            protected<T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
                if (callable instanceof CancellableTask){
                    return ((CancellableTask<T>) callable).newTask();
                } else {
                    return super.newTaskFor(callable);
                }
            }
        }
        
        publci abstract class SocketUsingTask<T> implements CancellableTask<T> {
            @GuardedBy("this") private Socket socket;
            
            protected synchronized void setSocket(Socket s) {
                socket = s;
            }
            
            public synchronized void cancel() {
                try {
                    if (socket != null){
                        socket.close();
                    }
                } catch (IOException ignored) {}
            }
            
            public RunnableFuture<T> newTask(){
                return new FutureTask<T>(this) {
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        try {
                            SocketUsingTask.this.cancel();
                        } finally {
                            return super.cancel(mayInteruptIfRunning);
                        }
                    }
                }
            }
        }
        
        
##停止基于线程的服务##

 - 管理ExecuterServer的服务

    public class LogService {
        private final ExecutorService exec = new SingleThreadExecutor();
        ...
        
        public void start(){}
        
        public void stop() throws Interrupted Exception {
        //服务停止时关闭线程池
            try {
                exec.shutdown();
                exec.awaitTermination(TIMEOUT, UNIT);
            } finally {
                writer.close();
            }
        }
        
        public void log(String msg){
            try{
                exec.execute(new WriteTask(msg));
            } catch (RejectedExecutionException ignored){}
        }
        
    }
    
    

 - “毒丸（Poison Pill）”对象

> 用于关闭生产者-消费者服务，指在一个放在队列上的对象，当得到这个对象时，立即停止。在FIFO（先进先出）队列中，"毒丸"对象将确保消费者在关闭之前首先完成队列中的所有工作，在提交“毒丸”对象之前的所有任务都会被处理，而生产者在提交了“毒丸”对象后，将不会再提交任何工作。

    package chad.xiao.executordemo;
    
    /**
     * Created by Chad.xiao on 2018/10/22 0022 11:37.
     */
    
    import java.io.File;
    import java.util.concurrent.BlockingQueue;
    import java.util.concurrent.LinkedBlockingQueue;
    import java.util.concurrent.TimeUnit;
    
    /**
     * IndexingService
     * <p/>
     * Shutdown with poison pill
     *
     * @author Brian Goetz and Tim Peierls
     */
    public class IndexingService {
        private static final int CAPACITY = 1000;
        private static final File POISON = new File("");
        private final IndexerThread consumer = new IndexerThread();
        private final CrawlerThread producer = new CrawlerThread();
        private final BlockingQueue<File> queue;
    
        public IndexingService() {
            this.queue = new LinkedBlockingQueue<File>(CAPACITY);
        }
    
        private boolean alreadyIndexed(File f) {
            return false;
        }
    
        /**
         * 生产者线程
         */
        class CrawlerThread extends Thread {
            public void run() {
                try {
                    crawl();
                } catch (InterruptedException e) { /* fall through */
                } finally {
                    while (true) {
                        try {
                            System.out.println("生产者放入毒丸对象");
                            queue.put(POISON);
                            break;
                        } catch (InterruptedException e1) { /* retry */
                        }
                    }
                }
            }
    
            private void crawl() throws InterruptedException {
                while (true){
                    System.out.println("生产这生产对象111");
                    queue.put(new File("111"));
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
    
        /**
         * 消费者线程
         */
        class IndexerThread extends Thread {
            public void run() {
                try {
                    while (true) {
                        File file = queue.take();
                        if (file == POISON) {
                            System.out.println("遇到毒丸对象");
                            break;
                        } else {
                            indexFile(file);
                        }
                    }
                } catch (InterruptedException consumed) {
                }
            }
    
            public void indexFile(File file) {
                System.out.println("消费" + file.getAbsolutePath());
            };
        }
    
        public void start() {
            producer.start();
            consumer.start();
        }
    
        public void stop() {
            producer.interrupt();
        }
    
        public void awaitTermination() throws InterruptedException {
            consumer.join();
        }
    
        public static void main(String[] args){
            IndexingService indexingService = new IndexingService();
            indexingService.start();
            try {
                TimeUnit.SECONDS.sleep(2);
                indexingService.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


> 只有在生产者和消费者的数量都已知的情况下，才可以使用“毒丸”对象。在IndexingServer中采用的解决方案可以扩展到多个生产者：只需要每个生产者都向队列中放入一个“毒丸”对象，并且消费者仅当在接收到N（Producers）个“毒丸”对象时才停止。这种方法也可以扩展到多个消费者的情况，只需要生产者将N（Consumers）个“毒丸”对象放入队列。<I>然而,当生产者和消费者数量较大时，这种方法将变得难以使用。只有在无界队列中，“毒丸”对象才能可靠地工作。</I>

 
    
    

 
        
    

 
 


