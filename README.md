# Zookeeper Leader Election

A Spring Boot application demonstrating distributed leader election using Apache Zookeeper and Curator Framework.

## Overview

This project implements a simple leader election mechanism where multiple instances of the application compete for leadership using Zookeeper's coordination capabilities.

## Components

- **LabApplication**: Main Spring Boot application entry point
- **LeaderElection**: Handles the leader election logic using Curator's LeaderSelector
- **RoleController**: REST endpoint to check if the current instance is a leader
- **ZookeeperConfig**: Configuration for Curator Framework connection

## Prerequisites

- Java 17 or higher
- Apache Zookeeper running on localhost:2181
- Maven

## Technical Stack

- Spring Boot
- Apache Curator Framework
- Apache Zookeeper
- Spring Web

## API Endpoints

- `GET /role` - Returns the current role of the instance (Leader/Follower)

## Running the Application

1. Start Zookeeper server:
    
    zkserver start
    

2. Build the project:
    
    mvn clean install
    

3. Run the application:
    
    mvn spring-boot:run
    

## How it Works

1. Each application instance connects to Zookeeper using Curator Framework
2. The LeaderElection component participates in leader election using Curator's LeaderSelector
3. Only one instance becomes the leader while others remain followers
4. The role can be checked via the REST endpoint
5. If the leader dies, another instance automatically takes leadership

## Testing

Run multiple instances of the application and use the `/role` endpoint to observe leader election in action. Only one instance will be the leader at any given time.

## Features

- Automatic leader election
- Leadership requeue on failure
- REST endpoint for role checking
- Exponential backoff retry strategy for Zookeeper connection

## Configuration

The Zookeeper connection is configured to connect to `localhost:2181` with exponential backoff retry (1000ms initial delay, 3 retries).
