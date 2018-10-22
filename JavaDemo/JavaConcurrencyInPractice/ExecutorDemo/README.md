# Executor框架

------

## 常用的线程池 ##

> Executors的静态工厂方法提供了几种常用的线程池

 - newFixedThreadPool-固定长度线程池，创建一个固定长度的线程池，每当提交一个任务时就创建一个线程，直到达到线程池的最大数量，线程池的规模将不会再变化（如果某个线程由于发生了未处理的Exception而结束，那么线程池会补充一个新的线程）。

    
> Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue.  At any point, at most {@code nThreads} threads will be active processing tasks. If additional tasks are submitted when all threads are active, they will wait in the queue until a thread is available. If any thread terminates due to a failure during execution prior to shutdown, a new one will take its place if needed to execute subsequent tasks.  The threads in the pool will exist until it is explicitly {@link ExecutorService#shutdown shutdown}.

  
        /**
        *@param nThreads 线程池长度
        */
        Executors.newFixedThreadPool(int nThreads){
            return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
        }
        /**
        *@param nThreads 线程池长度
        *@param threadFactory 线程创建工厂
        */
        Executors.newFixedThreadPool(int nThreads, ThreadFactory threadFactory){
            return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory);
        }

 - newCacheThreadPool-可缓存的线程池，如果线程池规模超过了处理需求时，将回收空闲的线程，当需求增加时，可以添加新的线程，线程规模不再受限制。

 

> Creates a thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available.  These pools will typically improve the performance of programs that execute many short-lived asynchronous tasks. Calls to {@code execute} will reuse previously constructed threads if available. If no existing thread is available, a new thread will be created and added to the pool. Threads that have not been used for sixty seconds are terminated and removed from the cache. Thus, a pool that remains idle for long enough will not consume any resources. Note that pools with similar properties but different details (for example, timeout parameters) may be created using {@link ThreadPoolExecutor} constructors.

         
        Executors.newCacheThreadPool(){
            return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
        }
        /**
        *@param threadFactory 线程创建工厂
        */
        Executors.newCacheThreadPool(ThreadFactory threadFactory){
            return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>(),
                                      threadFactory);
        }

  - newSingleThreadExecutor-单线程线程池，创建单个工作线程来执行任务，如果线程异常结束，会创建另一个线程来替代。用于保证任务队列串行执行。

>  Creates an Executor that uses a single worker thread operating off an unbounded queue. (Note however that if this single thread terminates due to a failure during execution prior to shutdown, a new one will take its place if needed to execute subsequent tasks.)  Tasks are guaranteed to execute  sequentially, and no more than one task will be active at any given time. Unlike the otherwise equivalent {@code newFixedThreadPool(1)} the returned executor is guaranteed not to be reconfigurable to use additional threads.

    
            Executors.newCacheThreadPool(){
                return new FinalizableDelegatedExecutorService
                        (new ThreadPoolExecutor(1, 1,
                                            0L, TimeUnit.MILLISECONDS,
                                            new LinkedBlockingQueue<Runnable>()));
            }
            /**
            *@param threadFactory 线程创建工厂
            */
            Executors.newCacheThreadPool(ThreadFactory threadFactory){
                return new FinalizableDelegatedExecutorService
                            (new ThreadPoolExecutor(1, 1,
                                                    0L, TimeUnit.MILLISECONDS,
                                                    new LinkedBlockingQueue<Runnable>(),
                                                    threadFactory));
            }
            //FinalizableDelegatedExecutorService继承DelegatedExecutorService包装类，只暴露部分方法。重写了finalize方法，调用shutdown()来结束线程池

> /**
     * DelegatedExecutorService
     * A wrapper class that exposes only the ExecutorService methods of an ExecutorService   implementation.
     */


   - newScheduledThreadPool-延时/定时线程池，创建一个固定长度的线程池，以延迟或定时的方式来执行任务。
> A {@link ThreadPoolExecutor} that can additionally schedule commands to run after a given delay, or to execute periodically.This class is preferable to {@link java.util.Timer} when multiple worker threads are needed, or when the additional flexibility or capabilities of {@link ThreadPoolExecutor} (which this class extends) are required.

        /**
         * Creates a thread pool that can schedule commands to run after a
         * given delay, or to execute periodically.
         * @param corePoolSize the number of threads to keep in the pool,
         * even if they are idle
         * @return a newly created scheduled thread pool
         * @throws IllegalArgumentException if {@code corePoolSize < 0}
         */
        Executors.newSingleThreadExecutor(int corePoolSize){
            return new ScheduledThreadPoolExecutor(corePoolSize);
        }
        /**
         * Creates a thread pool that can schedule commands to run after a
         * given delay, or to execute periodically.
         * @param corePoolSize the number of threads to keep in the pool,
         * even if they are idle
         * @param threadFactory the factory to use when the executor
         * creates a new thread
         * @return a newly created scheduled thread pool
         * @throws IllegalArgumentException if {@code corePoolSize < 0}
         * @throws NullPointerException if threadFactory is null
         */
        Executors.newSingleThreadExecutor(int corePoolSize, ThreadFactory threadFactory){
            return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
        }
        
    ScheduledExecutorService方法
    

        /**
         * Creates and executes a one-shot action that becomes enabled
         * after the given delay.
         *
         * @param command the task to execute
         * @param delay the time from now to delay execution
         * @param unit the time unit of the delay parameter
         * @return a ScheduledFuture representing pending completion of
         *         the task and whose {@code get()} method will return
         *         {@code null} upon completion
         * @throws RejectedExecutionException if the task cannot be
         *         scheduled for execution
         * @throws NullPointerException if command is null
         */
          
        public ScheduledFuture<?> schedule(Runnable command,
                                           long delay, TimeUnit unit);


        /**
         * Creates and executes a ScheduledFuture that becomes enabled after the
         * given delay.
         *
         * @param callable the function to execute
         * @param delay the time from now to delay execution
         * @param unit the time unit of the delay parameter
         * @param <V> the type of the callable's result
         * @return a ScheduledFuture that can be used to extract result or cancel
         * @throws RejectedExecutionException if the task cannot be
         *         scheduled for execution
         * @throws NullPointerException if callable is null
         */
        public <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                               long delay, TimeUnit unit);
                                               

        /**
         * Creates and executes a periodic action that becomes enabled first
         * after the given initial delay, and subsequently with the given
         * period; that is, executions will commence after
         * {@code initialDelay}, then {@code initialDelay + max(runTime, period)}, then
         * {@code initialDelay + 2 * max(runTime, period)}, and so on.
         *
         * <p>The sequence of task executions continues indefinitely until
         * one of the following exceptional completions occur:
         * <ul>
         * <li>The task is {@linkplain Future#cancel explicitly cancelled}
         * via the returned future.
         * <li>The executor terminates, also resulting in task cancellation.
         * <li>An execution of the task throws an exception.  In this case
         * calling {@link Future#get() get} on the returned future will
         * throw {@link ExecutionException}.
         * </ul>
         * Subsequent executions are suppressed.  Subsequent calls to
         * {@link Future#isDone isDone()} on the returned future will
         * return {@code true}.
         *
         * <p>If any execution of this task takes longer than its period, then
         * subsequent executions may start late, but will not concurrently
         * execute.
         *
         * @param command the task to execute
         * @param initialDelay the time to delay first execution
         * @param period the period between successive executions
         * @param unit the time unit of the initialDelay and period parameters
         * @return a ScheduledFuture representing pending completion of
         *         the series of repeated tasks.  The future's {@link
         *         Future#get() get()} method will never return normally,
         *         and will throw an exception upon task cancellation or
         *         abnormal termination of a task execution.
         * @throws RejectedExecutionException if the task cannot be
         *         scheduled for execution
         * @throws NullPointerException if command is null
         * @throws IllegalArgumentException if period less than or equal to zero
         */
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                      long initialDelay,
                                                      long period,
                                                      TimeUnit unit);
                                                      

        /**
         * Creates and executes a periodic action that becomes enabled first
         * after the given initial delay, and subsequently with the
         * given delay between the termination of one execution and the
         * commencement of the next. executions will commence after
         * {@code initialDelay}, then {@code initialDelay + runTime + period}, then
         * {@code initialDelay + 2 * (runTime + period)}, and so on.
         *
         * <p>The sequence of task executions continues indefinitely until
         * one of the following exceptional completions occur:
         * <ul>
         * <li>The task is {@linkplain Future#cancel explicitly cancelled}
         * via the returned future.
         * <li>The executor terminates, also resulting in task cancellation.
         * <li>An execution of the task throws an exception.  In this case
         * calling {@link Future#get() get} on the returned future will
         * throw {@link ExecutionException}.
         * </ul>
         * Subsequent executions are suppressed.  Subsequent calls to
         * {@link Future#isDone isDone()} on the returned future will
         * return {@code true}.
         *
         * @param command the task to execute
         * @param initialDelay the time to delay first execution
         * @param delay the delay between the termination of one
         * execution and the commencement of the next
         * @param unit the time unit of the initialDelay and delay parameters
         * @return a ScheduledFuture representing pending completion of
         *         the series of repeated tasks.  The future's {@link
         *         Future#get() get()} method will never return normally,
         *         and will throw an exception upon task cancellation or
         *         abnormal termination of a task execution.
         * @throws RejectedExecutionException if the task cannot be
         *         scheduled for execution
         * @throws NullPointerException if command is null
         * @throws IllegalArgumentException if delay less than or equal to zero
         */
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                         long initialDelay,
                                                         long delay,
                                                         TimeUnit unit);

## 任务的表达形式 ##

 - 无返回结果

Runnable作为基本任务的表示形式，是一中有很大局限的抽象，没有返回值

    ExecutorService executorService = Executors.newFixedThreadPool(5)
    for (int i = 0; i < 10; i++){
        executorService.execute(new MyRunnable("t" + i));
    }

    public class MyRunnable implements Runnable {
    
        private String name;
    
        public MyRunnable(String name){
            this.name = name == null ? "" : name;
        }
    
        @Override
        public void run() {
            for (int i = 0; i < 2; i++){
                System.out.println(name + " === " + Thread.currentThread().getName() + " :　" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
*运行结果*

    t0 === pool-1-thread-1 :　0
    t1 === pool-1-thread-2 :　0 
    t0 === pool-1-thread-1 :　1 
    t1 === pool-1-thread-2 :　1 
    t2 === pool-1-thread-2 :　0 
    t3 === pool-1-thread-1 :　0 
    t3 === pool-1-thread-1 :　1 
    t2 === pool-1-thread-2 :　1 
    t4 === pool-1-thread-1 :　0 
    t5 === pool-1-thread-2 :　0 
    t4 === pool-1-thread-1 :　1 
    t5 === pool-1-thread-2 :　1 
    t6 === pool-1-thread-1 :　0 
    t7 === pool-1-thread-2 :　0 
    t6 === pool-1-thread-1 :　1 
    t7 === pool-1-thread-2 :　1 
    t8 === pool-1-thread-2 :　0 
    t9 === pool-1-thread-1 :　0 
    t9 === pool-1-thread-1 :　1 
    t8 === pool-1-thread-2 :　1

    
 - 有返回结果

        try {
                    for (int i = 0; i < 20; i++) {
                        Future<String> result = executorService.submit(new MyCallable("t" + i));
                        System.out.println(" result: " + result.get());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
        import java.util.concurrent.Callable;
        public class MyCallable implements Callable<String> {
        
            private String name;
        
            public MyCallable(String name){
                this.name = name == null ? "" : name;
            }
        
            @Override
            public String call() throws Exception {
                return name + " === " + Thread.currentThread().getName() + " running ";
            }
        }

运行结果：

     result: t0 === pool-1-thread-1 running 
     result: t1 === pool-1-thread-2 running 
     result: t2 === pool-1-thread-1 running 
     result: t3 === pool-1-thread-2 running 
     result: t4 === pool-1-thread-1 running 
     result: t5 === pool-1-thread-1 running 
     result: t6 === pool-1-thread-1 running 
     result: t7 === pool-1-thread-2 running 
     result: t8 === pool-1-thread-1 running 
     result: t9 === pool-1-thread-2 running
    
 
   
## 线程池的生命周期 ##

> 为了解决执行服务的生命周期问题，Exector扩展了ExectorService接口，添加一些用于管理生命周期的方法（同事还有一些用于任务提交的遍历方法）。

    void shutdown();//平缓的关闭已添加的线程，不在接收新的任务，同时等等已经提交的任务执行完成
    
    ist<Runnable> shutdownNow();//尝试取消所有运行中的任务，并且不在启动任务队列中尚未开始执行的任务，返回未执行的任务列表
    
    boolean isShutdown();//返回线程池是否处于关闭状态。
    
    boolean isTerminated();//用于轮询线程池是否已经终止。
    
    boolean awaitTermination(long timeout, TimeUnit unit)throws InterruptedException;//等待线程池达到终止状态,需配合shutdown()或者shutdownNow()使用
 */

 

CompletionService 监听任务的完成
-------

>  A {@link CompletionService} that uses a supplied {@link Executor}  to execute tasks.  This class arranges that submitted tasks are, upon completion, placed on a queue accessible using {@code take}. The class is lightweight enough to be suitable for transient use  when processing groups of tasks.  <p> <b>Usage Examples.</b>  Suppose you have a set of solvers for a  certain problem, each returning a value of some type {@code  Result}, and would like to  run them concurrently, processing the  results of each of them that  return a non-null value, in some  method {@code use(Result r)}. You  could write this as:

     
      
      void solve(Executor e, Collection<Callable<Result>> solvers) throws InterruptedException, ExecutionException {
        CompletionService<Result> ecs = new ExecutorCompletionService<Result>(e);
        for (Callable<Result> s : solvers)
          ecs.submit(s);
        int n = solvers.size();
        for (int i = 0; i < n; ++i) {
          Result r = ecs.take().get();
          if (r != null)
            use(r);
        }
      }}
     
      

> Suppose instead that you would like to use the first non-null result of  the set of tasks, ignoring any that encounter exceptions,and cancelling all other tasks when the first one is ready:

     

      void solve(Executor e, Collection<Callable<Result>> solvers)throws InterruptedException {
        CompletionService<Result> ecs = new ExecutorCompletionService<Result>(e);
        int n = solvers.size();
        List<Future<Result>> futures = new ArrayList<>(n);
        Result result = null;
        try {
          for (Callable<Result> s : solvers)
            futures.add(ecs.submit(s));
          for (int i = 0; i < n; ++i) {
            try {
              Result r = ecs.take().get();
              if (r != null) {
                result = r;
                break;
              }
            } catch (ExecutionException ignore) {}
          }
        }
        finally {
          for (Future<Result> f : futures)
            f.cancel(true);
        }
     
        if (result != null)
          use(result);
      }}
      
## 线程中断 ##
[线程中断][https://github.com/ChadXiao/Android/tree/master/JavaDemo/JavaConcurrencyInPractice/ExecutorDemo/InterruptDemo]