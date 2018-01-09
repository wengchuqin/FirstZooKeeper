package top.chuqin.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wengchuqin on 2018/1/9.
 */
public class RecipesLock {
    static String lock_path = "/curator_recipes_lock_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.215.130:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    /**
     *  利用分布式锁（InterProcessMutex）实现订单号不重复
     */
    public static void main(String[] args) throws Exception {
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client,lock_path);
        final CountDownLatch down = new CountDownLatch(1);
        for(int i = 0; i < 30; i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        down.await();
                        lock.acquire();
                    } catch ( Exception e ) {}
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.out.println("生成的订单号是 : "+orderNo);
                    try {
                        lock.release();
                    } catch ( Exception e ) {}
                }
            }).start();
        }
        down.countDown();
    }
}
