package com.doug.moneytransferapi.service.account

import com.doug.moneytransferapi.model.Account
import com.doug.moneytransferapi.service.ApplicationServiceTest
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.junit.Test
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URISyntaxException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


/**
 * Integration testing for RestAPI
 * Test data are initialised from src/test/resources/database.sql
 *
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Ajani Goldmane',300.0000,'USD'); --ID =1
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Liliana Vess',500.0000,'USD'); --ID =2
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Sorin Markov',500.0000,'USD'); --ID =3
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Ajani Goldmane',700.0000,'EUR'); --ID =4
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Liliana Vess',700.0000,'EUR'); --ID =5
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Sorin Markov',500.0000,'EUR'); --ID =6
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Ajani Goldmane',700.0000,'GBP'); --ID =7
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Liliana Vess',700.0000,'GBP'); --ID =8
    INSERT INTO Account (CustomerName,Balance,CurrencyCode) VALUES ('Sorin Markov',500.0000,'GBP'); --ID =9
 */
class AccountApplicationSuccessScenarios : ApplicationServiceTest() {

    /*
        TestCase A1 Positive Category = AccountService
        Scenario: When trying to get customer account by customer name this test should return success code
        Return Code: 200 OK
     */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenGetAccountByCustomerName() {
        val uri = builder.setPath("/account/1").build()
        val request = HttpGet(uri)
        val response = client.execute(request as HttpUriRequest?)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)

        //assert the content
        val jsonString = EntityUtils.toString(response.entity)
        val account = mapper.readValue(jsonString, Account::class.java)
        assertEquals(account.customerName, "Ajani Goldmane")
    }

    /*
        TestCase A2 Positive Category = AccountService
        Scenario: When trying to get account balance by given account ID this test should return success code
        Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenGetAccountBalance() {
        val uri = builder.setPath("/account/1/balance").build()
        val request = HttpGet(uri)
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode,200)

        //assert the content,
        val balance = EntityUtils.toString(response.entity)
        val res = BigDecimal(balance).setScale(4, RoundingMode.HALF_EVEN)
        val db = BigDecimal(300).setScale(4, RoundingMode.HALF_EVEN)
        assertEquals(res, db)
    }

    /*
        TestCase A3 Positive Category = AccountService
        Scenario: When trying to get all customer accounts this test should return success code
        Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenGetAllAccounts() {
        val uri = builder.setPath("/account/all").build()
        val request = HttpGet(uri)
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)

        //assert the content
        val jsonString = EntityUtils.toString(response.entity)
        val accounts = mapper.readValue(jsonString, Array<Account>::class.java)
        assertNotNull(accounts)
    }

    /*
        TestCase A4 Positive Category = AccountService
        Scenario: When trying to create new customer account this test should return success code
        Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenCreateAccount() {
        val uri = builder.setPath("/account/create").build()
        val balance = BigDecimal(1000).setScale(4, RoundingMode.HALF_EVEN)
        val acc = Account("Chandra Naalar", balance, "USD")
        val jsonInString = mapper.writeValueAsString(acc)
        val entity = StringEntity(jsonInString)
        val request = HttpPut(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)
        val jsonString = EntityUtils.toString(response.entity)
        val accountAfterCreation = mapper.readValue(jsonString, Account::class.java)
        assertEquals(accountAfterCreation.customerName, "Chandra Naalar")
        assertEquals(accountAfterCreation.currencyCode, "USD")
    }

    /*
    TestCase A5 Positive Category = AccountService
    Scenario: When trying to delete valid customer account this test should return success code
    Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenDeleteAccount() {
        val uri = builder.setPath("/account/3").build()
        val request = HttpDelete(uri)
        request.setHeader("Content-type", "application/json")
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)
    }

}