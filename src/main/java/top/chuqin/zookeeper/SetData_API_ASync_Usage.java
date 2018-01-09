package top.chuqin.zookeeper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

// ZooKeeper API 更新节点数据内容，使用异步(async)接口。
public class SetData_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        String path = "/zk-book5";
        zk = new ZooKeeper("192.168.215.130:2181",
                5000, //
                new SetData_API_ASync_Usage());
        connectedSemaphore.await();

        zk.create( path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );
        zk.setData( path, "456".getBytes(), -1, new IStatCallback(), null );
        zk.setData( path, "789".getBytes(), -1, new IStatCallback(), null );

        Thread.sleep( Integer.MAX_VALUE );
    }
    @Override
    public void process(WatchedEvent event) {
        if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) {
                System.out.println("connectedSemaphore.countDown();");
                connectedSemaphore.countDown();
            }
        }
    }
}
class IStatCallback implements AsyncCallback.StatCallback{
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == 0) {
            System.out.println("SUCCESS");
        }
    }
}
