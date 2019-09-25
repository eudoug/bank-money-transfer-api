package com.doug.moneytransferapi.data

import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Customer
import junit.framework.TestCase
import org.apache.log4j.Logger
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerBehaviorTest {

    private val log = Logger.getLogger(CustomerBehaviorTest::class.java)

    private val dataObjectFactory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2)

    @Before
    fun setup() {
        // prepare test database and test data by executing sql script database.sql
        log.debug("setting up test database and sample data....")
        dataObjectFactory.generateApplicationData()
    }

    @After
    fun tearDown() {
        // TearDown
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldGetAllCustomers() {
        val allCustomers = dataObjectFactory.customerDataObject.getAllCustomer
        TestCase.assertTrue(allCustomers.size > 1)
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldGetCustomerById() {
        val customer = dataObjectFactory.customerDataObject.getCustomerById(2L)
        assertEquals(customer.customerName,"Liliana Vess")
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldntGetNonExistingCustomerById() {
        val customer = dataObjectFactory.customerDataObject.getCustomerById(500L)
        assertTrue(customer == null)
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldntGetNonExistingCustomerByName() {
        val customer = dataObjectFactory.customerDataObject.getCustomerByName("Jace Beleren")
        assertNull(customer)
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldCreateCustomer() {
        val customer = Customer("Chandra Nalaar", "chandranalaar@gmail.com")
        val id = dataObjectFactory.customerDataObject.insertCustomer(customer)
        val customerAfterInsert = dataObjectFactory.customerDataObject.getCustomerById(id)
        assertEquals(customer.emailAddress,"chandranalaar@gmail.com")
        assertEquals(customerAfterInsert.customerName,"Chandra Nalaar")

    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldUpdateCustomer() {
        val customer = Customer(1L.toString(), "ajani@gmail.com")
        val rowCount = dataObjectFactory.customerDataObject.updateCustomer(1L, customer)
        // assert one row(customer) updated
        assertEquals(rowCount, 1)
        assertEquals(dataObjectFactory.customerDataObject.getCustomerById(1L).emailAddress,"ajani@gmail.com")
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldntUpdateNonExistingCustomer() {
        val customer = Customer(500L.toString(), "Jace Beleren")
        val rowCount = dataObjectFactory.customerDataObject.updateCustomer(500L, customer)
        // assert one row(customer) updated
        assertEquals(rowCount, 0)
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldDeleteCustomer() {
        val rowCount = dataObjectFactory.customerDataObject.deleteCustomer(1L)
        // assert one row(customer) deleted
        assertEquals(rowCount, 1)
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldntDeleteNonExistingCustomer() {
        val rowCount = dataObjectFactory.customerDataObject.deleteCustomer(500L)
        // assert no row(customer) deleted
        assertEquals(rowCount, 0)
    }
}