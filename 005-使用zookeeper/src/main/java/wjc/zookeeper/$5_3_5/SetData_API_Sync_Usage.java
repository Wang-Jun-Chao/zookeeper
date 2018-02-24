package wjc.zookeeper.$5_3_5;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 * ZooKeeper API 更新节点数据内容，使用同步(sync)接口。
 * </pre>
 * Author: 王俊超
 * Date: 2018-02-24 08:25
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class SetData_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {

        String path = "/zk-book";
        zk = new ZooKeeper("localhost:2181",
                5000, //
                new SetData_API_Sync_Usage());
        connectedSemaphore.await();

        zk.create( path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );
        zk.getData( path, true, null );

        Stat stat = zk.setData( path, "456".getBytes(), -1 );
        System.out.println(stat.getCzxid()+","+
                stat.getMzxid()+","+
                stat.getVersion());
        Stat stat2 = zk.setData( path, "456".getBytes(), stat.getVersion() );
        System.out.println(stat2.getCzxid()+","+
                stat2.getMzxid()+","+
                stat2.getVersion());
        try {
            zk.setData( path, "456".getBytes(), stat.getVersion() );
        } catch ( KeeperException e ) {
            System.out.println("Error: " + e.code() + "," + e.getMessage());
        }
//        Thread.sleep( Integer.MAX_VALUE );
        zk.close();
    }

    @Override
    public void process(WatchedEvent event) {
        if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
            if (Watcher.Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
