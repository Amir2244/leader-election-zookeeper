package org.dev.zookeeper.lab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling role-related endpoints.
 */
@RestController
public class RoleController {

    private final LeaderElection leaderElection;

    /**
     * Constructs a RoleController with the required LeaderElection dependency.
     * @param leaderElection the leader election service
     */
    public RoleController(LeaderElection leaderElection) {
        this.leaderElection = leaderElection;
    }

    /**
     * Returns the current role of this node in the cluster.
     * @return "Leader" if this node is the leader, "Follower" otherwise
     */
    @GetMapping("/role")
    public String getRole() {
        return leaderElection.isLeader() ? "Leader" : "Follower";
    }
}