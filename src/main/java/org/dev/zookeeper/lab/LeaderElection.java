package org.dev.zookeeper.lab;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class LeaderElection extends LeaderSelectorListenerAdapter {

    private boolean isLeader = false;

    public LeaderElection(CuratorFramework client) {
        LeaderSelector leaderSelector = new LeaderSelector(client, "/leader-election", this);
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        isLeader = true;
        try {
            while (isLeader) {
                Thread.sleep(1000);
            }
        } finally {
            isLeader = false;
        }
    }

    public boolean isLeader() {
        return isLeader;
    }
}
