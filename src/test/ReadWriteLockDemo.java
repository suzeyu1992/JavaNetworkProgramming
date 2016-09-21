package test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author :  suzeyu
 * Time   :  2016-09-21  下午2:23
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */
public class ReadWriteLockDemo {

    /**
     * 控制读写操作并发的锁
     */
    private final ReentrantReadWriteLock mLock ;
    private String mName;

    public ReadWriteLockDemo(){
        mLock = new ReentrantReadWriteLock();
    }

    public void setPersonData(String name){
        // 获取写操作的锁
        ReentrantReadWriteLock.WriteLock writeLock = mLock.writeLock();

        try {
            //  获取 写锁使用权, 其余线程需要操作只有在写锁被关闭才可以
            writeLock.lock();
            mName = name;
        }finally {
            //  释放 写锁使用权
            writeLock.unlock();
        }
    }

    public String getName(){
        ReentrantReadWriteLock.ReadLock readLock = mLock.readLock();

        try {
            readLock.lock();
            return mName;
        }finally {
            readLock.unlock();
        }
    }

}
