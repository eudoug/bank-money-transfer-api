package com.doug.moneytransferapi.service

import com.doug.moneytransferapi.data.DataObjectFactory
import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Customer
import org.apache.log4j.Logger
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
class CustomerService {

    private val factory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2)

    /**
     * Find all Customers
     * @return
     * @throws ExceptionHandler
     */
    val allCustomers: List<Customer>
        @GET
        @Path("/all")
        @Throws(ExceptionHandler::class)
        get() = factory.customerDataObject.allCustomer

    /**
     * Find by customerName
     * @param customerName
     * @return
     * @throws ExceptionHandler
     */
    @GET
    @Path("/{customerName}")
    @Throws(ExceptionHandler::class)
    fun getCustomerByName(@PathParam("customerName") customerName: String): Customer {
        if (log.isDebugEnabled)
            log.debug("Request Received for get Customer by Name $customerName")
        return factory.customerDataObject.getCustomerByName(customerName) ?: throw WebApplicationException(
            "Customer Not Found",
            Response.Status.NOT_FOUND
        )
    }

    /**
     * Create Customer
     * @param customer
     * @return
     * @throws ExceptionHandler
     */
    @POST
    @Path("/create")
    @Throws(ExceptionHandler::class)
    fun createCustomer(customer: Customer): Customer {
        if (customer.customerName?.let { factory.customerDataObject.getCustomerByName(it) } != null) {
            throw WebApplicationException("Customer name already exist", Response.Status.BAD_REQUEST)
        }
        val uId = factory.customerDataObject.insertCustomer(customer)
        return factory.customerDataObject.getCustomerById(uId)
    }

    /**
     * Update by Customer Id
     * @param customerId
     * @param customer
     * @return
     * @throws ExceptionHandler
     */
    @PUT
    @Path("/{customerId}")
    @Throws(ExceptionHandler::class)
    fun updateCustomer(@PathParam("customerId") customerId: Long, customer: Customer): Response {
        val updateCount = factory.customerDataObject.updateCustomer(customerId, customer)
        return if (updateCount == 1) {
            Response.status(Response.Status.OK).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    /**
     * Delete by Customer Id
     * @param customerId
     * @return
     * @throws ExceptionHandler
     */
    @DELETE
    @Path("/{customerId}")
    @Throws(ExceptionHandler::class)
    fun deleteCustomer(@PathParam("customerId") customerId: Long): Response {
        val deleteCount = factory.customerDataObject.deleteCustomer(customerId)
        return if (deleteCount == 1) {
            Response.status(Response.Status.OK).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    companion object {

        private val log = Logger.getLogger(CustomerService::class.java)
    }


}