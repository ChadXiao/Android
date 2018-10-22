# �߳��ж�

------

> Javaû���ṩ�κλ�������ȫ����ֹ�̡߳������ṩ���жϣ�Interruption��,����һ��Э�����ƣ���ʹһ���߳���ֹ��һ���̵߳ĵ�ǰ������

    public class Thread{
        public void interrupt();//���̵߳�interrupted״̬����Ϊture
        public boolean isInterrupted();//�����̵߳�interrupted״̬
        public static boolean interrupted();//���interrupted״̬����������һ��״̬
    }
    
## ���жϵ��������� ##

 - Thread.sleep()
 - BlockingQueue.put()

## Futureȡ����ʱ���� ##

    public static void timeRun(Runnable r, long timeout, TimeUnit unit)throws InterruptedException{
        Future<?> task = taskExec.submit(r);
        try{
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            //����ʱ
        } catch (ExecutionException e) {
            //�����׳��쳣�Ļ����������׳��쳣
            throw launderThrowable(e.getCause());
        } finally {
            //��������Ѿ�������ִ��ȡ������Ҳ��������κ�Ӱ��
            task.cancel(true);
        }
    }
    
    
## �����ж������Ĵ��� ##

 - Java.io���е�Socket I/O���ڷ�������Ӧ�ó����У����������I/O��ʽ���Ƕ��׽��ֽ��ж�ȡ��д�롣��ȻInputStream��OutputStream�е�read��write�ȷ�����������Ӧ�жϣ���ͨ���رյײ���׽��֣�����ʹ������ִ��read��write�ȷ��������������߳��׳�һ��SocketException��
 - Java.io���е�ͬ��I/O�����ж�һ������InterruptibleChannel�ϵȴ����߳�ʱ�����׳�ClosedByInterruptException���ر���·���⻹��ʹ��������������·���������߳�ͬ���׳�ClosedByInterruptException�������ر�һ��InterruptibleChannelʱ����������������·�������������̶߳��׳�AsynchronousCloseException���������׼��Channel��ʵ����InterruptibleChannel��
 - Selector���첽I/O�����һ���߳��ڵ���Selector.select��������java.nio.channels�У�ʱ�����ˣ���ô����close��wakeup������ʹ�߳��׳�ClosedSelectorException����ǰ���ء�
 - ��ȡ�������һ���߳����ڵȴ�������������ô���޷���Ӧ�жϣ���Ϊ�߳���Ϊ���϶������������Խ���������ж����󡣵��ǣ���Lock�����ṩ��lockInterruptibly�������÷��������ڵȴ�һ������ͬ��������Ӧ�ж�

        //��дinterrupt()����ʵ�ַǱ�׼ȡ������
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
                } catch(IOException e){/*�߳��˳�*/}
            }
        }
        

        //��дnewTaskForʵ�ַǱ�׼ȡ������,ʹ��Callable���Է����Զ����Future
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
        
        
##ֹͣ�����̵߳ķ���##

 - ����ExecuterServer�ķ���

    public class LogService {
        private final ExecutorService exec = new SingleThreadExecutor();
        ...
        
        public void start(){}
        
        public void stop() throws Interrupted Exception {
        //����ֹͣʱ�ر��̳߳�
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
    
    

 - �����裨Poison Pill��������

> ���ڹر�������-�����߷���ָ��һ�����ڶ����ϵĶ��󣬵��õ��������ʱ������ֹͣ����FIFO���Ƚ��ȳ��������У�"����"����ȷ���������ڹر�֮ǰ������ɶ����е����й��������ύ�����衱����֮ǰ���������񶼻ᱻ���������������ύ�ˡ����衱����󣬽��������ύ�κι�����

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
         * �������߳�
         */
        class CrawlerThread extends Thread {
            public void run() {
                try {
                    crawl();
                } catch (InterruptedException e) { /* fall through */
                } finally {
                    while (true) {
                        try {
                            System.out.println("�����߷��붾�����");
                            queue.put(POISON);
                            break;
                        } catch (InterruptedException e1) { /* retry */
                        }
                    }
                }
            }
    
            private void crawl() throws InterruptedException {
                while (true){
                    System.out.println("��������������111");
                    queue.put(new File("111"));
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
    
        /**
         * �������߳�
         */
        class IndexerThread extends Thread {
            public void run() {
                try {
                    while (true) {
                        File file = queue.take();
                        if (file == POISON) {
                            System.out.println("�����������");
                            break;
                        } else {
                            indexFile(file);
                        }
                    }
                } catch (InterruptedException consumed) {
                }
            }
    
            public void indexFile(File file) {
                System.out.println("����" + file.getAbsolutePath());
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


> ֻ���������ߺ������ߵ���������֪������£��ſ���ʹ�á����衱������IndexingServer�в��õĽ������������չ����������ߣ�ֻ��Ҫÿ�������߶�������з���һ�������衱���󣬲��������߽����ڽ��յ�N��Producers���������衱����ʱ��ֹͣ�����ַ���Ҳ������չ����������ߵ������ֻ��Ҫ�����߽�N��Consumers���������衱���������С�<I>Ȼ��,�������ߺ������������ϴ�ʱ�����ַ������������ʹ�á�ֻ�����޽�����У������衱������ܿɿ��ع�����</I>

 
    
    

 
        
    

 
 


