package top.chuqin.curator.test.server;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.EnsurePath;

import java.util.Scanner;

/**
 * Created by wengchuqin on 2018/1/9.
 * 目标：
 * 实现一个ZooKeeper配置服务器，让客户端订阅配置信息
 *
 * 步骤：
 * （1）开启ZooKeeper测试服务器。
 * （2）创建节点/config
 * （3）在/config发布配置信息
 *
 */
public class TestingServerSample {
    static public String configPath = "/config";
    static public String serverConnectString = "127.0.0.1:2181";

    public static void main(String[] args) throws Exception {

        //开启测试服务器
        TestingServer server = new TestingServer(2181);
        System.out.println("TestingServer started");

        //创建一个客户端，用来发布配置信息
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(serverConnectString)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        System.out.println("publish client started");

        //创建/config节点
        EnsurePath ensurePath = new EnsurePath(configPath);
        ensurePath.ensure(client.getZookeeperClient());

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("input key and value");
            String key = scanner.next();
            String value = scanner.next();
            System.out.printf("key is %s, value is %s\n", key, value);

            EnsurePath configEnsurePath = new EnsurePath(configPath + "/" + key);
            configEnsurePath.ensure(client.getZookeeperClient());
            client.setData().forPath(configEnsurePath.getPath(), value.getBytes());
            System.out.println("publish success");
        }
    }
}
