package com.doug.moneytransferapi.service.transaction

import com.doug.moneytransferapi.model.CustomerTransaction
import com.doug.moneytransferapi.service.ApplicationServiceTest
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.junit.Test
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URISyntaxException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
class TransactionApplicationInternalServerErrorScenarios : ApplicationServiceTest() {

    /*
         TestCase C1 Negative Category = TransactionService
         Scenario: When trying to withdraw money from insufficient fund
                   this test should return internal server error
         Return Code: 500 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnInternalServerErrorWhenWithdrawNonSufficientFund() {
        val uri = builder.setPath("/account/8/withdraw/7695.2145").build()
        val request = HttpPut(uri)
        request.setHeader("Content-type", "application/json")
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        val responseBody = EntityUtils.toString(response.entity)
        assertEquals(statusCode,500)
    }
}