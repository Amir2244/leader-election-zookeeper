package org.dev.zookeeper.lab;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    /**
     * Creates and starts a CuratorFramework client to connect to ZooKeeper.
     *
     * @return A configured and started CuratorFramework client instance
     * connected to localhost:2181 with exponential backoff retry policy
     */
    @Bean
    public CuratorFramework curatorFramework() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "localhost:2181",
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        return client;
    }
}

