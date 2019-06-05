# MoneyTransfer test task 

RESTFull API for money transfers between accounts

## Libraries used
- **sparkjava**  2.7.2 - micro framework for creating web applications with minimal effort
- **projectlombok** 1.18.6 -  
- **jackson** 2.9.8 - work with JSON
- **gson** 2.8.5 -  work with JSON 
- **junit** 4.11 - unit testing

I used tutorial from http://sparkjava.com/tutorials/ to create simple WebServer

## Features
- Create user.
- Create account
- Deposit money to or withdraw money from account.
- Transfer money between accounts.
- Get all users account
- Get total balance all user accounts
- In-memory database to store all transactions and accounts.
- Thread-safe

## Starting the application
1. Clone the repo and run the following command: `mvn clean package`
2. This will create a single fat jar which can be executed with `java -jar target/moneytransfer-1.0-SNAPSHOT-jar-with-dependencies.jar`
3. All functionality checked by junit tests.

## Docs
### User
#### Create User

## Using the REST API
### Accounts
#### Creating user  
To create user send POST request: 
```
curl -XPOST http://localhost:4567/user/create -d '
  {
   "firstName" : "FirstName",
   "secondName" : "LastName" 
   }'
```
The server will respond with information about the created user in a JSON format:
```
{
  "firstName" : "FirstName",
  "secondName" : "LastName",
  "id" : 285246008
}
```
#### Creating account  
To create an account it's a simple POST request: 
```
curl -XPOST http://localhost:4567/account/create -d '
{
    "currency": "EUR",
    "user": {
        "firstName": "FirstName",
        "secondName": "LastName",
        "id": 285246008
    }
}'
```
The server will respond with information about the created account in a JSON format:
```
{
  "id" : "f2bcd4d4-ba47-4770-b5a8-3f88bbe55d43",
  "number" : "40702EUR1300200814",
  "balance" : 0,
  "user" : {
    "firstName" : "FirstName",
    "secondName" : "LastName",
    "id" : 285246008
  },
  "currency" : "EUR"
}
```
#### Get all account
To get information about all account you can submit a GET request : 
```
curl -XGET http://localhost:4567/account/getall -d'
{
    "firstName" : "FirstName",
    "secondName" : "LastName",
    "id" : 285246008
}'
```
The server will respond with information about the all user accounts in a JSON format.
```
[ {
  "id" : "f2bcd4d4-ba47-4770-b5a8-3f88bbe55d43",
  "number" : "40702EUR1300200814",
  "balance" : 0,
  "user" : {
    "firstName" : "FirstName",
    "secondName" : "LastName",
    "id" : 285246008
  },
  "currency" : "EUR"
} ]
```
 
#### Get getTotalBalance for user accounts
To get information about total balance on all user accounts, you can submit a GET request : 
```
curl -XGET http://localhost:4567/account/totalbalance -d'
{
    "firstName" : "FirstName",
    "secondName" : "LastName",
    "id" : 285246008
}'
```
The server will response with information about total balance on all user accounts.

### Deposit money on account
To deposit money on user account, send POST request
```
curl -XPOST http://localhost:4567/payment/deposit -d'
{
  "userId" : 285246008,
  "dstAccount" : "40702EUR174996961",
  "amount" : 10
}'
```
The server will response with information about payment transaction, looks like
```
{
  "id" : 1193250765,
  "accountFrom" : null,
  "accountTo" : {
    "id" : "9d6e749b-fec5-42c3-987a-bf2041f7447c",
    "number" : "40702EUR174996961",
    "balance" : 50,
    "user" : {
      "firstName" : "FirstName",
      "secondName" : "LastName",
      "id" : 285246008
    },
    "currency" : "EUR"
  },
  "dt" : 1554444028489,
  "amount" : 10
}
```
### Withdraw money from account
To withdraw money from user account, send POST request
```
curl -XPOST http://localhost:4567/payment/withdraw -d'
{
  "userId" : 285246008,
  "srcAccount" : "40702EUR174996961",
  "amount" : 10
}'
```
Server will response with information about payment transaction, looks like
```
{
  "id" : 1620679540,
  "accountFrom" : {
    "id" : "9d6e749b-fec5-42c3-987a-bf2041f7447c",
    "number" : "40702EUR174996961",
    "balance" : 40,
    "user" : {
      "firstName" : "FirstName",
      "secondName" : "LastName",
      "id" : 285246008
    },
    "currency" : "EUR"
  },
  "accountTo" : null,
  "dt" : 1554444199431,
  "amount" : 10
}
```
### Transfer money from one account to another
To transfer money between two account, send POST request
```
curl -XPOST http://localhost:4567/payment/transfer -d'
{
  "userId" : 285246008,
  "srcAccount" : "40702EUR174996961",
  "dstAccount" : "40702EUR1689053154",
  "amount" : 10
}'
```
Server will response with information about payment transaction, looks like
```
{
  "id" : 238893229,
  "accountFrom" : {
    "id" : "9d6e749b-fec5-42c3-987a-bf2041f7447c",
    "number" : "40702EUR174996961",
    "balance" : 20,
    "user" : {
      "firstName" : "FirstName",
      "secondName" : "LastName",
      "id" : 285246008
    },
    "currency" : "EUR"
  },
  "accountTo" : {
    "id" : "bdcf69b5-3071-4dde-bc9b-446c332508dc",
    "number" : "40702EUR1689053154",
    "balance" : 10,
    "user" : {
      "firstName" : "FirstName",
      "secondName" : "LastName",
      "id" : 285246008
    },
    "currency" : "EUR"
  },
  "dt" : 1554444414728,
  "amount" : 10
}
```
###TODO list
- fix feedback bug: 
hardly readable code, lots of commented or not used lines, typos, small code reuse poor REST: endpoint for getting the list of entities is called getAll, expects GET requests with body REST API is not tested except of one method
- Support for different currencies
- Money exchange 
- Separate transaction creation and processing with thread-safe queues(ConcurrentLinkedQueue or another)
- Payment transaction status