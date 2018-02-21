package wjc.zookeeper.$5_3_3;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 * ZooKeeper API 删除节点，使用同步(sync)接口。
 * </pre>
 * Author: 王俊超
 * Date: 2018-02-21 17:42
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class Delete_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {

        String path = "/zk-book";
        zk = new ZooKeeper("domain1.book.zookeeper:2181",
                5000, //
                new Delete_API_Sync_Usage());
        connectedSemaphore.await();

        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.delete(path, -1);

        Thread.sleep(60000);
        zk.close();
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
