package com.doug.moneytransferapi.service.account

import com.doug.moneytransferapi.service.ApplicationServiceTest
import junit.framework.Assert.assertTrue
import org.apache.http.client.methods.HttpDelete
import org.junit.Test
import java.io.IOException
import java.net.URISyntaxException
import kotlin.test.assertEquals

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
class AccountApplicationBadRequestScenarios : ApplicationServiceTest() {

    /*
         TestCase A1 Negative Category = AccountService
         Scenario: When trying to delete non-existent account this test should return 404 NOT FOUND
         Return Code: 404 NOT FOUND
     */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnNotFoundWhenDeleteNonExistingAccount() {
        val uri = builder.setPath("/account/300").build()
        val request = HttpDelete(uri)
        request.setHeader("Content-type", "application/json")
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 404)
    }
}