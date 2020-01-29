# money-transfer
Money Transfer application is a stand alone multi-threaded Java application to transfer amount from one account to another

The main focus of this application is to handle the concurrency without corrupted the account's state during fund transfer.

The main feature of this application are:

1. <b> Optimistic locking implementaion, i.e no locking or waiting of thread visible, but still handles the race condition efficiently to transfer amount </b>
2. <b> No chance of Deadlock </b>
3. <b> No thread suspension </b>
4. <b> Reduced thread latency </b>
5. Implemented using core java and other libraries, no heavy weight framework.
6. Only need Java to run the application

Main Class : MoneyTransferApplication.java

Application, when you start using main class, performs following operation in order
 1. Initialize google guice injector to create dependency graph as per the guice module @MoneyTransferModule
 2. Initialize DB table and sample data
 3. Expose REST APIs routes (or endpoints) to 
 		3.1 Transfer amount between two accounts, and 
 		3.2 Get account data (id and amount)
 4. Sample data contains two accounts with ids 1 and 2 and their inital amount is 10000 each.
 
 Technologies & References
 1) Web server - Embedded Jetty Server URL http://localhost:4567/
 		SparkJava is used to create quick server and exposing REST API end points 
 2) Core java, Google guice, maven
 3) Database : H2 in memory database (URL jdbc:h2:~/testDB)
 4) Testing using JUnit
 
 
 Rest End Point:
 
 1) Get Account data : GET@http//localhost:4567/account/1
 
 2) Transfer amount 100  from account 1 to 2 : POST@http://localhost:4567/money-transfer
  Request Header: ContentType application/json
  Request Body:
                 {
                 "fromAccountId": 1,
                 "toAccountId": 2,
                 amount: 100
                 }
