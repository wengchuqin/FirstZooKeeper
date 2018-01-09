package top.chuqin.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wengchuqin on 2018/1/8.
 */
public class ConnectTest implements Watcher {
    private static CountDownLatch connectedSemephore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.215.130:2181",
                5000,
                new ConnectTest());
        System.out.println("state:" + zooKeeper.getState());

        try {
            connectedSemephore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Zookeeper session established");
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println("Recervi watched even: " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemephore.countDown();
        }
    }
}
