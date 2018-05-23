# Simple Finance API

This project is a simple finance API which -aims- to show some best practices while building REST APIs on Spring.

## Components
- account: This API manages account resource. Account is related some customer with customerId. Also an account has multiple transactions which is managed by transaction service.
- transaction: This API manages transaction resource.
- gateway: Reverse proxy with zuul (https://github.com/Netflix/zuul)
- eureka-server: Service discovery. Each account and transaction service is registered to eureka-server and load balancing is handled with ribbon on client-side (i.e the caller service).

## Installation

1- Build component images
```
mvn clean package
```

2- Run all components
```
docker-compose up -d
```

3- Hit `http://localhost:8761/` from browser to see ACCOUNT, TRANSACTION and GATEWAY components are running and registered to eureka-server.


