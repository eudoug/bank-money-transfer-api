package com.doug.moneytransferapi.service.customer

import com.doug.moneytransferapi.model.Customer
import com.doug.moneytransferapi.service.ApplicationServiceTest
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.junit.Test
import java.io.IOException
import java.net.URISyntaxException
import kotlin.test.assertEquals


/**
 * Integration testing for RestAPI
 * Test data are initialised from src/test/resources/database.sql
 * INSERT INTO Customer (CustomerName, EmailAddress) VALUES ('Ajani','ajanigoldmane@gmail.com');  --ID=1
 * INSERT INTO Customer (CustomerName, EmailAddress) VALUES ('Sorin','sorinmarkov@gmail.com');  --ID=2
 */
class CustomerApplicationBadRequestScenarios : ApplicationServiceTest()  {

    /*
        TestCase D1 Negative Category = CustomerService
        Scenario: When trying to create customer already existed using JSON this test should return 400 BAD REQUEST
        Return Code: 400 BAD REQUEST
    */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun iShouldThrowBadRequestWhenTryingToCreateExistingCustomer() {
        val uri = builder.setPath("/customer/create").build()
        val customer = Customer("Ajani Goldmane", "ajanigoldmane@gmail.com")
        val jsonInString = mapper.writeValueAsString(customer)
        val entity = StringEntity(jsonInString)
        val request = HttpPost(uri)
        request.setHeader("Content-type", "application/json")
        request.entity = entity
        val response = client.execute(request)
        val statusCode = response.statusLine.statusCode
        assertEquals(statusCode, 400)
    }
}