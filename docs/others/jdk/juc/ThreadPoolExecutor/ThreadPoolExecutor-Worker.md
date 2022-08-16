# ThreadPoolExecutor Worker
Worker继承了AQS(AbstractQueuedSynchronizer)并实现了Runnable。

# 构造函数
```java
 Worker(Runnable firstTask) {
    // 设置AQS的state为-1
    setState(-1); 
    this.firstTask = firstTask;
    // 通过ThreadPoolExecutor设置的ThreadFactory创建线程并设置Runnable为自己
    this.thread = getThreadFactory().newThread(this);
}
```

# 实现Runnable接口的run
```java
public void run() {
    runWorker(this);
}
```

```java
final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    // 创建work时传入的Runnable任务
    Runnable task = w.firstTask;
    w.firstTask = null;
    w.unlock(); // allow interrupts
    boolean completedAbruptly = true;
    try {
        // task 为空则从工作队列中获取任务
        while (task != null || (task = getTask()) != null) {
            w.lock();
            // 线程池状态为TERMINATED 或 TIDYING 或 STOP时中断worker线程
            // 
            // If pool is stopping, ensure thread is interrupted;
            // if not, ensure thread is not interrupted.  This
            // requires a recheck in second case to deal with
            // shutdownNow race while clearing interrupt
            if ((runStateAtLeast(ctl.get(), STOP) ||
                 (Thread.interrupted() &&
                  runStateAtLeast(ctl.get(), STOP))) &&
                !wt.isInterrupted())
                wt.interrupt();
            try {
                // 执行任务前回调-->执行任务-->执行任务后回调
                beforeExecute(wt, task);
                Throwable thrown = null;
                try {
                    task.run();
                } catch (RuntimeException x) {
                    thrown = x; throw x;
                } catch (Error x) {
                    thrown = x; throw x;
                } catch (Throwable x) {
                    thrown = x; throw new Error(x);
                } finally {
                    afterExecute(task, thrown);
                }
            } finally {
                task = null;
                w.completedTasks++;
                w.unlock();
            }
        }
        completedAbruptly = false;
    } finally {
        processWorkerExit(w, completedAbruptly);
    }
}
```