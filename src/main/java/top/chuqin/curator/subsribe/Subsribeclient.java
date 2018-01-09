package top.chuqin.curator.subsribe;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import top.chuqin.curator.test.server.TestingServerSample;

import java.util.concurrent.TimeUnit;

/**
 * Created by wengchuqin on 2018/1/9.
 * 目的：接收服务器发布的信息
 */
public class SubsribeClient {
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(TestingServerSample.serverConnectString)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        System.out.println("subsribe client started");

        final PathChildrenCache cache = new PathChildrenCache(client, TestingServerSample.configPath, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED," + event.getData().getPath() + new String(event.getData().getData(), "UTF-8"));
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED," + event.getData().getPath() + new String(event.getData().getData(), "UTF-8"));
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED," + event.getData().getPath() + new String(event.getData().getData(), "UTF-8"));
                        break;
                    default:
                        break;
                }
            }
        });

        TimeUnit.SECONDS.sleep(1000);
    }

}
