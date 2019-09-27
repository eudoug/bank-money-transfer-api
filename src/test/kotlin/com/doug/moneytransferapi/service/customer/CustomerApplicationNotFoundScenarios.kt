package com.doug.moneytransferapi.service.customer

import com.doug.moneytransferapi.model.Customer
import com.doug.moneytransferapi.service.ApplicationServiceTest
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.junit.Test
import java.io.IOException
import java.net.URISyntaxException
import kotlin.test.assertEquals

/**
 * Integration testing for RestAPI
 * Test data are initialised from src/test/resources/database.sql
 * INSERT INTO Customer (CustomerName, EmailAddress) VALUES ('Ajani','ajanigoldmane@gmail.com'), id 1
 * INSERT INTO Customer (CustomerName, EmailAddress) VALUES ('Sorin','sorinmarkov@gmail.com'), id 2
 */
class CustomerApplicationNotFoundScenarios : ApplicationServiceTest() {

    /*
        TestCase D1 Negative Category = CustomerService
        Scenario: When trying to update non existed Customer using JSON provided
                  from client this test should return 404 NOT FOUND
        Return Code: 404 NOT FOUND
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnNotFoundWhenUpdateNonExistingCustomer() {
        val uri = builder.setPath("/customer/100").build()
        val customer = Customer(2L, "Ajani", "ajani@gmail.com")
        val jsonInString = mapper.writeValueAsString(customer)
        val entity = StringEntity(jsonInString)
        val request = HttpPut(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 404)
    }

    /*
        TestCase D2 Negative Category = CustomerService
        Scenario: When trying to delete non-existed customer this test should return 404 NOT FOUND
        Return Code: 404 NOT FOUND
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnNotFoundWhenTryToDeleteNonExistingCustomer() {
        val uri = builder.setPath("/customer/300").build()
        val request = HttpDelete(uri)
        request.setHeader("Content-type", "application/json")
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 404)
    }

}