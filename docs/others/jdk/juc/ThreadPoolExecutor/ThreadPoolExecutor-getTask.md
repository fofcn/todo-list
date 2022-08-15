# ThreadPoolExecutor-getTask()
```java
private Runnable getTask() {
    // 超时默认设置为false
    boolean timedOut = false; // Did the last poll() time out?

    for (;;) {
        // 获取线程池运行状态
        int c = ctl.get();
        int rs = runStateOf(c);

        // 线程池状态值关系： TERMINATED > TIDYING > STOP > SHUTDOWN > RUNNING
        // 这里rs >= SHUTDOWN则状态为： TERMINATED > TIDYING > STOP > SHUTDOWN这四种状态
        // rs >= SHUTDOWN同时也意味着用户调用了shutdown()函数
        // rs >= STOP对应线程池状态为： TERMINATED > TIDYING > STOP这三种
        // rs >= STOP意味着用户调用了shutdownNow()函数
        // workQueue.isEmpty()检查表示线程池状态处于SHUTDOWN及后续状态是任务队列为空
        // 减少线程池worker数量
        if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
            decrementWorkerCount();
            return null;
        }

        int wc = workerCountOf(c);

        // Are workers subject to culling?
        boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

        // worker数量大于设置的最大值 或者 超时回收机制启用
        // 并且 worker数量大于1 或者 任务队列为空
        // 线程池工作队列数量减一
        if ((wc > maximumPoolSize || (timed && timedOut))
            && (wc > 1 || workQueue.isEmpty())) {
            if (compareAndDecrementWorkerCount(c))
                return null;
            continue;
        }

        try {
            // 这里通过队列获取任务来判定是否超时
            // workQueue.poll()函数在keepAliveTime时间内如果没有获取到任务会返回null值，
            // workQueue.take()如果没有任务会阻塞等待直到有任务返回
            // 如果Runnable r为空则代表执行workQueue.poll否则执行了workQueue.take()
            Runnable r = timed ?
                workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                workQueue.take();
            if (r != null)
                return r;
            timedOut = true;
        } catch (InterruptedException retry) {
            timedOut = false;
        }
    }
}
```