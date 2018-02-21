package wjc.zookeeper.$5_3_2;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 * ZooKeeper API创建节点，使用异步(async)接口。
 * </pre>
 * Author: 王俊超
 * Date: 2018-02-21 17:34
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class ZooKeeper_Create_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{

        ZooKeeper zookeeper = new ZooKeeper("localhost:2181",
                5000, //
                new ZooKeeper_Create_API_ASync_Usage());
        connectedSemaphore.await();

        zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new StringCallbackImpl(), "I am context.");

        zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new StringCallbackImpl(), "I am context.");

        zookeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                new StringCallbackImpl(), "I am context.");
        Thread.sleep( 60000);
        zookeeper.close();
    }

    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
class StringCallbackImpl implements AsyncCallback.StringCallback{
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("Create path result: [" + rc + ", " + path + ", "
                + ctx + ", real path name: " + name);
    }
}