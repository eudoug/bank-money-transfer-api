
## Welcome to my bank example!

This is a REST API that supports money transactions between customer accounts.
In this application we have three main characters: the customer, the account and the transaction.
The data consistency and authentication will not be treated on the API, so this application 
considering that all data provided was treated by another layer on the system architecture.
For this project I'm using lightweight frameworks and libraries.


## Summary

This README file is dedicated to those who want to find out which technologies
have been used and want to test this API.


### Technologies

API Design: OpenAPI (Is a specification for machine-readable interface files for describing, 
producing, consuming, and visualizing RESTful web services)

Program Language: Kotlin and Java 11

Development Process: TDD (A software development process that relies on the repetition 
of a very short development cycle, when tests are created before the feature, granting 100% coverage)

Container: Jetty (Jetty is a HTTP server and Container Servlet 100% written in Java)

DataBase: H2 (H2 is a relational database management system written in Java)

Logger: Log4j (Apache Log4j is a Java-based logging utility)

Code Coverage: SonarLint Plugin - SonarQube

HTTP: Apache HTTP Client (Is a compliant HTTP agent implementation based on HttpCore)



## Testing

As this project was created based on TDD process, all the tests were designed using the test-first approach. 

Integration tests was created based on standards most used on mature projects and following the BDD process.

You can also test the application using the Postman Collection provided on the project. You just need to 
import the JSON following this tutorial: [a relative link](./document/Postman.md)


## API Endpoints

To design the API was used the OpenAPI specification originally known as the Swagger Specification.

The OpenAPI Specification is language-agnostic and is used right now on projects for great companies like Google and Microsoft. 

With an OpenAPI spec, an API can be easily managed with some API management tools like Google Apigee.

The project spec (OpenApi conception) can be found on the link [a link](https://app.swaggerhub.com/apis/eudoug/DougMoneyTransfer/1.0.0) or inside project [a relative link](./document/OpenAPI.yaml). 

To understand the API endpoints the swagger editor shows the following information: 

**Figure 1** - Customer Endpoints

![](./document/image%201.png)


**Figure 2** - Account Endpoints

![](./document/image%202.png)


**Figure 3** - Transaction Endpoint

![](./document/image%203.png)


 
## Working on the code

Clone the repository locally and open the project with your preferred IDE.


## API Installation

For this API you should generate a package to run the application.
You can follow this steps to make this application run:

1. Open your preferred terminal 
2. Go to the project root folder
3. On the terminal type: mvn package
4. Wait until package is created
5. On the terminal type: java -jar money-transfer-api-1.0-SNAPSHOT.jar

To access the API you can use: http://localhost:8081
