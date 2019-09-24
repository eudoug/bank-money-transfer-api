
## Welcome to my bank example!

This is a REST API that supports money transactions between customer accounts.
In this application we have three main characters: the customer, the account and the transaction.
First of all, for the sake of performance I'm using lightweight frameworks and libraries.


## Summary

This README file is dedicated to those that want to figure out which tecnologies was used and also want to 
test and understand this API.


### Technologies

API Design: OpenAPI (is a specification for machine-readable interface files for describing, producing, consuming, and visualizing RESTful web services)

Program Language: Kotlin and Java 11

Develomment Process: TDD (Is a software development process that relies on the repetition of a very short development cycle)

Deployment: Docker (Docker is a set of platform-as-a-service (PaaS) products that use OS-level virtualization to deliver software in packages called containers.

Container: Jetty (Jetty  is a HTTP server and Container Servlet 100% written in Java)

DataBase: H2 (H2 is a relational database management system written in Java)

Logger: Log4j (Apache Log4j is a Java-based logging utility)

HTTP: Apache HTTP Client(Is an compliant HTTP agent implementation based on HttpCore)

 
## Working on the code

Clone the repository locally and open the project with your preferred IDE.


## Testing

As this project was created based on TDD process all the tests were concepted using the test-first approach. 

Integration tests was created based on standards most used on mature projects and following the BDD process.

## API Endpoins

To design the API was used the OpenAPI specification originally known as the Swagger Specification.

The OpenAPI Specification is language-agnostic and is used right now on projects for great companies like Google and Microsoft. With an OpenAPI spec an API can be easily managed with some API management tools like Google Apigee.

The project spec (OpenApi conception) can be found on the link:  https://app.swaggerhub.com/apis/eudoug/DougMoneyTransfer/1.0.0

To understand the API endpoints the swagger editor shows the following information:



![](image%201.png)
**Figure 1** - Customer Endpoints

![](image%202.png)
**Figure 2** - Account Endpoints

![](image%203.png)
**Figure 3** - Transaction Endpoint
