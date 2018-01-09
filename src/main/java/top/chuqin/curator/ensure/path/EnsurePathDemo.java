package top.chuqin.curator.ensure.path;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;

/**
 * Created by wengchuqin on 2018/1/9.
 * EnsurePath采用静默的节点创建方式，
 * 如果节点存在，那么不进行任何操作，也不抛出异常，
 * 如果节点不存在，则正常创建节点。
 *
 */
public class EnsurePathDemo {
    static String path = "/zk-book/c1";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.215.130:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    public static void main(String[] args) throws Exception {

        client.start();
        client.usingNamespace( "zk-book" );

        EnsurePath ensurePath = new EnsurePath(path);
        ensurePath.ensure(client.getZookeeperClient());

        EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/c1");
        ensurePath2.ensure(client.getZookeeperClient());
    }
}
