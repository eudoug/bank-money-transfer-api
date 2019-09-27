package com.doug.moneytransferapi.service.transaction

import com.doug.moneytransferapi.model.Account
import com.doug.moneytransferapi.model.CustomerTransaction
import com.doug.moneytransferapi.service.ApplicationServiceTest
import junit.framework.Assert.assertTrue
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
class TransactionApplicationSuccessScenarios : ApplicationServiceTest() {

    /*
         TestCase C1 Positive Category = TransactionService
         Scenario: When trying to deposit money this test should return success code
         Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenDepositMoney() {
        val uri = builder.setPath("/account/1/deposit/100").build()
        val request = HttpPut(uri)
        request.setHeader("Content-type", "application/json")
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode,200)
        val jsonString = EntityUtils.toString(response.entity)
        val afterDeposit = mapper.readValue(jsonString, Account::class.java)
        // I should have my balance increased from 300 to 400
        assertTrue(afterDeposit.balance?.equals(BigDecimal(400).setScale(4, RoundingMode.HALF_EVEN))!!)
    }

    /*
         TestCase C2 Positive Category = TransactionService
         Scenario: When trying to withdraw money from account with
                   sufficient fund this test should return success code
         Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessCodeWhenDrawWithSufficientFund() {
        val uri = builder.setPath("/account/2/withdraw/100").build()
        val request = HttpPut(uri)
        request.setHeader("Content-type", "application/json")
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
       assertEquals(statusCode,200)
        val jsonString = EntityUtils.toString(response.entity)
        val afterDeposit = mapper.readValue(jsonString, Account::class.java)
        // I should have my balance increased from 500 to 400
        assertTrue(afterDeposit.balance?.equals(BigDecimal(400).setScale(4, RoundingMode.HALF_EVEN))!!)
    }

    /*
         TestCase C3 Positive Category = TransactionService
         Scenario: When trying to transfer from one account to another with
                   sufficient fund this test should return success code
         Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessCodeWhenTransactionEnoughFund() {
        val uri = builder.setPath("/transfer/money").build()
        val amount = BigDecimal(63.65).setScale(4, RoundingMode.HALF_EVEN)
        val transaction = CustomerTransaction("GBP", amount, 7L, 8L)

        val jsonInString = mapper.writeValueAsString(transaction)
        val entity = StringEntity(jsonInString)
        val request = HttpPost(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode,200)
    }

}