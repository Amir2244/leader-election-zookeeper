package org.dev.zookeeper.lab;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles leader election using Apache Curator's LeaderSelector.
 * This component allows distributed services to elect a leader among themselves.
 */
@Component
public class LeaderElection extends LeaderSelectorListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(LeaderElection.class);
    private volatile boolean isLeader = false;
    private final LeaderSelector leaderSelector;

    /**
     * Initializes the leader election process.
     * @param client The CuratorFramework client used for ZooKeeper operations
     */
    public LeaderElection(CuratorFramework client) {
        this.leaderSelector = new LeaderSelector(client, "/leader-election", this);
        this.leaderSelector.autoRequeue();
        this.leaderSelector.start();
        log.info("Leader election initialized");
    }

    /**
     * Called when this instance is elected as the leader.
     * Runs continuously until leadership is relinquished.
     * @param client The CuratorFramework client
     * @throws Exception if any error occurs during leadership
     */
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        isLeader = true;
        onLeadershipAcquired();
        try {
            while (isLeader) {
                Thread.sleep(1000);
            }
        } finally {
            isLeader = false;
            onLeadershipRelinquished();
        }
    }

    /**
     * Called when leadership is acquired.
     * Can be overridden to implement specific leadership behavior.
     */
    protected void onLeadershipAcquired() {
        log.info("Leadership acquired");
    }

    /**
     * Called when leadership is relinquished.
     * Can be overridden to implement specific cleanup behavior.
     */
    protected void onLeadershipRelinquished() {
        log.info("Leadership relinquished");
    }

    /**
     * Checks if this instance is currently the leader.
     * @return true if this instance is the leader, false otherwise
     */
    public boolean isLeader() {
        return isLeader;
    }

    /**
     * Stops the leader election process and releases leadership if held.
     */
    public void stop() {
        isLeader = false;
        leaderSelector.close();
        log.info("Leader election stopped");
    }
}
//
//@Component
//public class LeaderElection implements Watcher {
//    /** Logger for this class */
//    private static final Logger log = LoggerFactory.getLogger(LeaderElection.class);
//    /** ZooKeeper path for election coordination */
//    private static final String ELECTION_PATH = "/election";
//    /** Flag indicating if this node is currently the leader */
//    private volatile boolean isLeader = false;
//    /** ZooKeeper client instance */
//    private final ZooKeeper zooKeeper;
//    /** Path of this node's election znode */
//    private String nodePath;
//
//    /**
//      * Constructs a new LeaderElection instance.
//      * @param zooKeeper The ZooKeeper client instance
//      */
//    public LeaderElection(ZooKeeper zooKeeper) {
//        this.zooKeeper = zooKeeper;
//        initialize();
//    }
//
//    /**
//      * Initializes the leader election process by creating necessary ZooKeeper nodes.
//      * Creates the election parent node if it doesn't exist and creates an ephemeral
//      * sequential node for this instance.
//      */
//    private void initialize() {
//        try {
//            try {
//                zooKeeper.create(ELECTION_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            } catch (KeeperException.NodeExistsException e) {
//                log.debug("Election path already exists");
//            }
//
//            nodePath = zooKeeper.create(ELECTION_PATH + "/node-", new byte[0],
//                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//            checkLeadership();
//        } catch (Exception e) {
//            log.error("Error in leader election", e);
//        }
//    }
//
//
//    /**
//      * Checks if this node should be the leader by comparing znode paths.
//      * The node with the lowest sequential number becomes the leader.
//      * @throws KeeperException if a ZooKeeper operation fails
//      * @throws InterruptedException if the operation is interrupted
//      */
//    private void checkLeadership() throws KeeperException, InterruptedException {
//        java.util.List<String> children = zooKeeper.getChildren(ELECTION_PATH, this);
//        children.sort(String::compareTo);
//
//        String firstNode = ELECTION_PATH + "/" + children.get(0);
//        isLeader = nodePath.equals(firstNode);
//
//        if (isLeader) {
//            log.info("This node is now the leader");
//        } else {
//            log.info("This node is a follower");
//        }
//    }
//
//    /**
//      * Processes ZooKeeper events.
//      * Handles NodeChildrenChanged events to trigger leadership checks when the election state changes.
//      * @param event The ZooKeeper event to process
//      */
//    @Override
//    public void process(WatchedEvent event) {
//        if (event.getType() == Event.EventType.NodeChildrenChanged) {
//            try {
//                checkLeadership();
//            } catch (Exception e) {
//                log.error("Error checking leadership", e);
//            }
//        }
//    }
//
//    /**
//      * Checks if this node is currently the leader.
//      * @return true if this node is the leader, false otherwise
//      */
//    public boolean isLeader() {
//        return isLeader;
//    }
//}
