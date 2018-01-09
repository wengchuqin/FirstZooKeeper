package top.chuqin.curator.master.select;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by wengchuqin on 2018/1/9.
 */
public class RecipesMasterSelect {
    private static String masterPath = "/curator_recipes_master_path";
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.215.130:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws Exception {
        client.start();
        LeaderSelector selector = new LeaderSelector(client,
                masterPath,
                new LeaderSelectorListenerAdapter() {
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        System.out.println("成为Master角色:" +  client.getData());
                        Thread.sleep(3000);
                        System.out.println("完成Master操作，释放Master权利");
                    }
                });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

}
