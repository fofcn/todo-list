# Java Lock ReentrantLock
ReentrantLock分为公平锁和非公平锁。
构造函数有一个boolean fair参数来指定公平锁还是非公平锁，
true为公平锁false为非公平锁，默认为非公平锁，因为非公平锁可能可以提高线程的平均周转时间。

# 公平锁实现
```java
static final class FairSync extends Sync {
    private static final long serialVersionUID = -3000897897090466540L;

    final void lock() {
        acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        // c==0表示当前可能有线程持有该锁变量
        // 这里会进行再次检查因为可能在下一个周期所有线程都已经释放了锁变量
        if (c == 0) {
            // c==0表示当前线程需要排队，hasQueuedPredecessors()检查队列有没有前驱，
            // 如果没有前驱的话就尝试获取锁变量（compareAndSetState(0, acquires)）
            // 成功就将持有锁变量的线程设置为当前线程
            if (!hasQueuedPredecessors() &&
                compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        // ReentrantLock可重入性支持
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            // 小于0代表nextc有符号整型溢出，超过Integer.MAX_VALUE，ReentrantLock最大只能是Integer.MAX_VALUE
            // 这表示ReentrantLock的可冲入次数为Integer.MAX_VALUE-1次
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
```
# 非公平锁实现
```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;

    final void lock() {
        // 非公平锁在lock时会尝试获取锁状态变量，如果获取成功就设置锁变量的持有者是当前线程
        // 这算是插队机制但不算抢占
        // 否则当前线程排队等待锁变量
        if (compareAndSetState(0, 1))
            setExclusiveOwnerThread(Thread.currentThread());
        else
            acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }

    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            // 即使现在锁变量为0也要尝试获取一下锁变量
            if (compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        // ReentrantLock可重入性支持
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            // 小于0代表nextc有符号整型溢出，超过Integer.MAX_VALUE，ReentrantLock最大只能是Integer.MAX_VALUE
            // 这表示ReentrantLock的可冲入次数为Integer.MAX_VALUE-1次
            if (nextc < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
```
