package org.dev.zookeeper.lab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private final LeaderElection leaderElection;

    public RoleController(LeaderElection leaderElection) {
        this.leaderElection = leaderElection;
    }

    @GetMapping("/role")
    public String getRole() {
        return leaderElection.isLeader() ? "Leader" : "Follower";
    }
}
