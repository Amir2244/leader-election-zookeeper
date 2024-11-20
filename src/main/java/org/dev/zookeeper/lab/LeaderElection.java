package org.dev.zookeeper.lab;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
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
