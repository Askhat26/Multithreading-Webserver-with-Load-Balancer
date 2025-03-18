# Multithreading Webserver with Load Balancer

## Overview
This project implements a multithreading web server with a load balancer to efficiently distribute incoming client requests among multiple backend servers. It supports concurrent request handling using a thread pool and ensures balanced load distribution using a round-robin algorithm.

## Features
- **Multithreading Web Server**: Handles multiple client connections concurrently.
- **Load Balancer**: Distributes requests among multiple backend servers.
- **Thread Pool**: Optimizes resource usage and improves scalability.
- **Logging**: Tracks request distribution and server handling.

## Components
1. **Server**: A multi-threaded web server that processes client requests.
2. **Client**: A client that connects to the server via the load balancer.
3. **Load Balancer**: A component that distributes incoming requests across multiple backend servers.

## Setup & Usage
### Prerequisites
- Java Development Kit (JDK) installed

### Running the Load Balancer
```sh
javac LoadBalancer.java
java LoadBalancer
```

### Running Multiple Servers
Run multiple instances of the server on different ports (e.g., 8010, 8011, 8012):
```sh
javac Server.java
java Server 8010
```
Repeat the above command for other ports.

### Running the Client
```sh
javac Client.java
java Client
```

## Logging
Each request handled by the load balancer is logged to track which server processed it.

## Future Enhancements
- Implement dynamic server discovery.
- Add health checks for backend servers.
- Support for different load balancing strategies (e.g., Least Connections).

