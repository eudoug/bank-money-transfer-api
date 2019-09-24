package com.doug.moneytransferapi.service.transaction

import com.doug.moneytransferapi.model.CustomerTransaction
import com.doug.moneytransferapi.service.ApplicationServiceTest
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.junit.Test
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URISyntaxException
import kotlin.test.assertEquals

/*
* Integration testing for RestAPI
* Test data are initialised from src/test/resources/demo.sql
*   INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Ajani Goldmane',300.0000,'USD'), id 1;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Liliana Vess',500.0000,'USD'), id 2;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Sorin Markov',500.0000,'USD'), id 3;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Ajani Goldmane',700.0000,'EUR'), id 4;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Liliana Vess',700.0000,'EUR'), id 5;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Sorin Markov',500.0000,'EUR'), id 6;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Ajani Goldmane',700.0000,'GBP'), id 7;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Liliana Vess',700.0000,'GBP'), id 8;
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Sorin Markov',500.0000,'GBP'), id 9;
 */
class TransactionApplicationBadRequestScenarios : ApplicationServiceTest() {

    /*
   TestCase D1 Negative Category = TransactionService
   Scenario: When try to transfer between one account to another
             with accounts with different currency code this test
             should return bad request
   Return Code: 400 OK
*/
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnInternalServerErrorWhenTransactionDifferentCurrencyCode() {
        val uri = builder.setPath("/transaction").build()
        val amount = BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN)
        val transaction = CustomerTransaction("EUR", amount, 1L, 2L)

        val jsonInString = mapper.writeValueAsString(transaction)
        val entity = StringEntity(jsonInString)
        val request = HttpPost(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode,400)
    }

    /*
     TestCase C2 Negative Category = TransactionService
     Scenario: When try to transfer money from one account to another
               with source account without sufficient fund this test should
               return bad request
     Return Code: 400 OK
 */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnInternalServerErrorWhenTransferNotEnoughFund() {
        val uri = builder.setPath("/transaction").build()
        val amount = BigDecimal(100000).setScale(4, RoundingMode.HALF_EVEN)
        val transaction = CustomerTransaction("EUR", amount, 1L, 4L)

        val jsonInString = mapper.writeValueAsString(transaction)
        val entity = StringEntity(jsonInString)
        val request = HttpPost(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode,400)
    }
}