package com.doug.moneytransferapi.service.customer

import com.doug.moneytransferapi.model.Customer
import com.doug.moneytransferapi.service.ApplicationServiceTest
import junit.framework.Assert.assertTrue
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.junit.Test
import java.io.IOException
import java.net.URISyntaxException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


/**
 * Integration testing for RestAPI
 * Test data are initialised from src/test/resources/demo.sql
 * INSERT INTO Customer (CustomerName, EmailAddress) VALUES ('Ajani','ajanigoldmane@gmail.com');  --ID=1
 * INSERT INTO Customer (CustomerName, EmailAddress) VALUES ('Sorin','sorinmarkov@gmail.com');  --ID=2
 */
class CustomerApplicationSuccessScenarios : ApplicationServiceTest() {

   /*
       TestCase D1 Positive Category = CustomerService
       Scenario: When trying to get a customer by given customer
                 name this test should return success code
       Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenGetCustomer() {
        val uri = builder.setPath("/customer/Sorin Markov").build()
        val request = HttpGet(uri)
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode

        assertEquals(statusCode, 200)
        //check the content
        val jsonString = EntityUtils.toString(response.entity)
        val customer = mapper.readValue(jsonString, Customer::class.java)
        assertEquals(customer.customerName, "Sorin Markov")
        assertEquals(customer.emailAddress,"sorinmarkov@gmail.com")
    }

    /*
        TestCase D2 Positive Category = CustomerService
        Scenario: When trying to create customer
                  this test should return a success code
        Return Code: 200 OK
     */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenCreateOneCustomer() {
        val uri = builder.setPath("/customer/create").build()
        val customer = Customer("Nissa Revane", "nissarevane@gmail.com")
        val jsonInString = mapper.writeValueAsString(customer)
        val entity = StringEntity(jsonInString)
        val request = HttpPost(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)
        val jsonString = EntityUtils.toString(response.entity)
        val customerAfterCreation = mapper.readValue(jsonString, Customer::class.java)
        assertEquals(customerAfterCreation.customerName, "Nissa Revane")
        assertEquals(customerAfterCreation.emailAddress, "nissarevane@gmail.com")

    }

    /*
        TestCase D3 Positive Category = CustomerService
        Scenario: When trying to request all customer this test should return a success code
        Return Code: 200 OK
     */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenGetAllCustomersRegistered() {
        val uri = builder.setPath("/customer/all").build()
        val request = HttpGet(uri)
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)
        val jsonString = EntityUtils.toString(response.entity)
        val customers = mapper.readValue(jsonString, Array<Customer>::class.java)
        assertNotNull(customers)
    }

    /*
        TestCase D4 Positive Category = CustomerService
        Scenario: When trying to update a existing Customer using JSON provided
                  from client, this test should return a success code
        Return Code: 200 OK
     */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenUpdateExistingCustomer() {
        val uri = builder.setPath("/customer/2").build()
        val customer = Customer(2L, "Ajani", "ajani@gmail.com")
        val jsonInString = mapper.writeValueAsString(customer)
        val entity = StringEntity(jsonInString)
        val request = HttpPut(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)
    }

    /*
        TestCase D5 Positive Category = CustomerService
        Scenario: When trying to delete a customer this test should return a success code
        Return Code: 200 OK
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldReturnSuccessWhenDeleteExistedCustomer() {
        val uri = builder.setPath("/customer/3").build()
        val request = HttpDelete(uri)
        request.setHeader("Content-type", "application/json")
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 200)
    }


}