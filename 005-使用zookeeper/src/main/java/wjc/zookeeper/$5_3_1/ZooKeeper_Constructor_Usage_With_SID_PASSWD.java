package wjc.zookeeper.$5_3_1;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 *Java API -> 创建连接 -> 创建一个最基本的ZooKeeper对象实例，复用sessionId和sesion passwd
 * </pre>
 * Author: 王俊超
 * Date: 2018-02-21 17:27
 * Blog: http://blog.csdn.net/derrantcm
 * Github: https://github.com/wang-jun-chao
 * All Rights Reserved !!!
 */
public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static void main(String[] args) throws Exception{
        ZooKeeper zookeeper = new ZooKeeper("localhost:2181",
                5000, //
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD());
        connectedSemaphore.await();
        long sessionId = zookeeper.getSessionId();
        byte[] passwd  = zookeeper.getSessionPasswd();

        System.out.println("sessionId: " + sessionId);
        System.out.println("passwd: " + new String(passwd, "utf-8"));

        //Use illegal sessionId and sessionPassWd
        zookeeper = new ZooKeeper("localhost:2181",
                5000, //
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),//
                1L,//
                "test".getBytes());
        //Use correct sessionId and sessionPassWd
        zookeeper = new ZooKeeper("localhost:2181",
                5000, //
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),//
                sessionId,//
                passwd);
        Thread.sleep( Integer.MAX_VALUE );
    }
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event：" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
