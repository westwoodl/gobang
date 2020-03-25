package com.xrc.gb.manager.util;

import com.xrc.gb.repository.cache.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 核心：使用cas解决并发安全问题，使用aqs 实现线程竞争
 *
 * @author xu rongchao
 * @date 2020/3/24 18:49
 * @see java.util.concurrent.locks.ReentrantLock
 * Sync 抽象类的两个子类：1.FairSync 2.NoFairSync
 */
public class DistributedReentrantLock implements DistributedLock, Serializable {

    @Resource
    private RedisCache redisCache;

    private Object key;

    public DistributedReentrantLock setKey(Object key) {
        return this;
    }


    @Override
    public void lock() {
        for (; ; ) {
            if (acquire(1))
                return;
        }
    }


    static class Sync extends AbstractQueuedSynchronizer {



    }

    private boolean compareAndSetCache(Object expect, int update) {
        if (redisCache.get(key.toString()) == expect) {
            synchronized (DistributedReentrantLock.class) {
                if (redisCache.get(key.toString()) == expect) {
                    redisCache.set(key.toString(), update);
                    return true;
                }
            }
        }
        return false;
    }


    private boolean acquire(int arg) {
        return compareAndSetCache(null, arg);
    }

    /**
     * 获取锁，逻辑和 lock() 方法一样，但这个方法在获取锁过程中能响应中断。
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * 从关键字字面理解，这是在尝试获取锁，获取成功返回：true，获取失败返回：false,
     * 这个方法不会等待，有以下三种情况：
     */
    @Override
    public boolean tryLock() {
        return acquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long deadLine = System.nanoTime() + unit.toNanos(time);
        for (; ; ) {
            if (Thread.interrupted())
                throw new InterruptedException();
            if (acquire(1))
                return true;
            if (deadLine - System.nanoTime() < 0L)
                return false;
        }
    }

    @Override
    public void unlock() {
        if (key == null)
            throw new NullPointerException();
        redisCache.remove(key.toString());
        key = null;
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("newCondition is not support on DistributedReentrantLock. Expected future release");
    }
}
