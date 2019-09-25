package com.doug.moneytransferapi.data

import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Customer

interface CustomerDataObject {

    val getAllCustomer: List<Customer>

    @Throws(ExceptionHandler::class)
    fun getCustomerById(customerId: Long): Customer

    @Throws(ExceptionHandler::class)
    fun getCustomerByName(customerName: String): Customer?

    @Throws(ExceptionHandler::class)
    fun insertCustomer(customer: Customer): Long

    @Throws(ExceptionHandler::class)
    fun updateCustomer(customerId: Long?, customer: Customer): Int

    @Throws(ExceptionHandler::class)
    fun deleteCustomer(customerId: Long): Int

}