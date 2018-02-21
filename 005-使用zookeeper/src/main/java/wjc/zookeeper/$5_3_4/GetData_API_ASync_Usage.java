package wjc.zookeeper.$5_3_4;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 *
 * </pre>
 * Author: 王俊超
 * Date: 2018-02-21 18:51
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class GetData_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {

        String path = "/zk-book-" + System.currentTimeMillis();
        zk = new ZooKeeper("localhost:2181",
                5000, //
                new GetData_API_ASync_Usage());
        connectedSemaphore.await();

        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zk.getData(path, true, new DataCallbackImpl(), null);

        zk.setData(path, "123".getBytes(), -1);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                try {
                    zk.getData(event.getPath(), true, new DataCallbackImpl(), null);
                } catch (Exception e) {
                }
            }
        }
    }
}

class DataCallbackImpl implements AsyncCallback.DataCallback {
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("-->" + rc + ", " + path + ", " + new String(data));
        System.out.println("-->" + stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
    }
}
