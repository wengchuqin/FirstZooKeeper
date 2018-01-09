package top.chuqin.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

/**
 * Created by wengchuqin on 2018/1/8.
 */
public class CRUDTest {
    CuratorFramework client = null;

    @Before
    public void init(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.215.130:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @Test
    public void create() throws Exception {
        client.create().forPath("/path" + Instant.now().getEpochSecond(), "value".getBytes());
        System.out.println("create done");
    }

    @Test
    public void getDataTest() throws Exception {
        String path = "/path" + Instant.now().getEpochSecond();
        client.create().forPath(path, "value".getBytes());
        System.out.println("create done");

        System.out.println("get : " + new String(client.getData().forPath(path), "UTF-8"));
    }

    @Test
    public void CVSTest() throws Exception {
        //添加节点
        String path = "/path" + Instant.now().getEpochSecond();
        client.create().forPath(path, "value".getBytes());
        System.out.println("create done");

        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        //CVS成功，设置新值
        System.out.println("Success set node for : " + path + ", new version: "
                + client.setData().withVersion(stat.getVersion()).forPath(path).getVersion());

        //CVS失败，因为stat.getVersion()不是最新版本
        try {
            client.setData().withVersion(stat.getVersion()).forPath(path);
        } catch (Exception e) {
            System.out.println("Fail set node due to " + e.getMessage());
        }
    }
}
