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

## Features
- [x] unit tests for service layer with service mocks
- [x] dockerized services with spotify-maven-docker library
- [x] docker-compose to run all containers
- [x] spring-eureka for service discovery
- [x] zuul reverse proxy ( gateway ) to access multiple services seamlessly
- [x] 


## TODO

1- Add more tests
 - test a
 - test b
 
2- Add pagination to `transactions-service/api/transactions` endpoint

3- Currently account-service fetchs transactions for an account from transactions-service. Instead, account-service should get max N transactions from transactions-service to keep account-service safe. If client want to fetch all transactions it can use `transactions-service/api/transactions` endpoint.

4- Currently balance is calculated on account-service on each account get request iterating over transactions. There may be better solutions ( we can decide such solutions according to api usage frequencies and patterns, i.e how transactions are created, how balance is changed )

 - Calculate balance on transaction-service and return it as response header
 - Or inform account on each new transaction and keep calculated balance on account. 

5- ..
 
