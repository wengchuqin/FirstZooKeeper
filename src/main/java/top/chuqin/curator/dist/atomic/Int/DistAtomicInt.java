package top.chuqin.curator.dist.atomic.Int;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
/**
 * Created by wengchuqin on 2018/1/9.
 */
public class DistAtomicInt {
    static String distatomicint_path = "/curator_recipes_distatomicint_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.215.130:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    /**
     * 使用分布式计数器（DistributedAtomicInteger）
     */
    public static void main( String[] args ) throws Exception {
        client.start();
        DistributedAtomicInteger atomicInteger =
                new DistributedAtomicInteger( client, distatomicint_path,
                        new RetryNTimes( 3, 1000 ) );
        AtomicValue<Integer> rc = atomicInteger.add( 8 );
        System.out.println( "Result: " + rc.succeeded() );
    }
}
