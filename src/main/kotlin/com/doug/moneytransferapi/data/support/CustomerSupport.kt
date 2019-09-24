package com.doug.moneytransferapi.data.support

import com.doug.moneytransferapi.data.DataFactory
import com.doug.moneytransferapi.data.CustomerDataObject
import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Customer
import org.apache.commons.dbutils.DbUtils
import org.apache.log4j.Logger
import java.sql.*
import java.util.ArrayList

class CustomerSupport : CustomerDataObject {



    /**
     * Find all customer
     */
    override val getAllCustomer: List<Customer>
        @Throws(ExceptionHandler::class)
        get() {
            var conn: Connection? = null
            var stmt: PreparedStatement? = null
            var rs: ResultSet? = null
            val customers = ArrayList<Customer>()
            try {
                conn = DataFactory.getConnection()
                stmt = conn.prepareStatement(SQL_GET_ALL_CUSTOMERS)
                rs = stmt.run { executeQuery() }
                while (rs.next()) {
                    val customer = Customer(rs.getLong("CustomerId"), rs.getString("CustomerName"), rs.getString("EmailAddress)"))
                    if (log.isDebugEnabled)
                        log.debug("getAllCustomers() Retrieve Customer: $customer")
                }
                return customers
            } catch (e: SQLException) {
                throw ExceptionHandler("Error reading customer data", e)
            } finally {
                DbUtils.closeQuietly(conn, stmt, rs)
            }
        }

    /**
     * Find customer by customerId
     */
    @Throws(ExceptionHandler::class)
    override fun getCustomerById(customerId: Long): Customer {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var customer: Customer? = null
        try {
            conn = DataFactory.getConnection()
            stmt = conn.prepareStatement(SQL_GET_CUSTOMER_BY_ID)
            stmt.setLong(1, customerId)
            rs = stmt.executeQuery()
            if (rs!!.next()) {
                customer = Customer(rs.getLong("customerId"), rs.getString("customerName"), rs.getString("emailAddress"))
                if (log.isDebugEnabled)
                    log.debug("getCustomerById(): Retrieve Customer: $customer")
            }
            return customer!!
        } catch (e: SQLException) {
            throw ExceptionHandler("Error reading customer data", e)
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs)
        }
    }

    /**
     * Find customer by customerName
     */
    @Throws(ExceptionHandler::class)
    override fun getCustomerByName(customerName: String): Customer? {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var customer: Customer? = null
        try {
            conn = DataFactory.getConnection()
            stmt = conn.prepareStatement(SQL_GET_CUSTOMER_BY_NAME)
            stmt!!.setString(1, customerName)
            rs = stmt.executeQuery()
            if (rs!!.next()) {
                customer = Customer(rs.getLong("CustomerId"), rs.getString("CustomerName"), rs.getString("EmailAddress"))
                if (log.isDebugEnabled) {
                    log.debug("Retrieve Customer: $customer")
                }
            }
            return customer
        } catch (e: SQLException) {
            throw ExceptionHandler("Error reading customer data", e)
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs)
        }
    }

    /**
     * Save Customer
     */
    @Throws(ExceptionHandler::class)
    override fun insertCustomer(customer: Customer): Long {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null
        var generatedKeys: ResultSet? = null
        try {
            conn = DataFactory.getConnection()
            stmt = conn.prepareStatement(SQL_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS)
            stmt.setString(1, customer.getCustomerName())
            stmt.setString(2, customer.getEmailAddress())
            val affectedRows = stmt.executeUpdate()
            if (affectedRows == 0) {
                log.error("insertCustomer(): Creating customer failed, no rows affected.$customer")
                throw ExceptionHandler("Customers Cannot be created")
            }
            generatedKeys = stmt.generatedKeys
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1)
            } else {
                log.error("insertCustomer():  Creating customer failed, no ID obtained.$customer")
                throw ExceptionHandler("Customers Cannot be created")
            }
        } catch (e: SQLException) {
            log.error("Error Inserting Customer :$customer")
            throw ExceptionHandler("Error creating customer data", e)
        } finally {
            DbUtils.closeQuietly(conn, stmt, generatedKeys)
        }

    }

    /**
     * Update Customer
     */
    @Throws(ExceptionHandler::class)
    override fun updateCustomer(customerId: Long?, customer: Customer): Int {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null

        try {
            conn = DataFactory.getConnection()
            stmt = conn.prepareStatement(SQL_UPDATE_CUSTOMER)
            stmt!!.setString(1, customer.getCustomerName())
            stmt.setString(2, customer.getEmailAddress())
            stmt.setLong(3, customerId!!)
            return stmt.executeUpdate()
        } catch (e: SQLException) {
            log.error("Error Updating Customer :$customer")
            throw ExceptionHandler("Error update customer data", e)
        } finally {
            DbUtils.closeQuietly(conn)
            DbUtils.closeQuietly(stmt)
        }
    }

    /**
     * Delete Customer
     */
    @Throws(ExceptionHandler::class)
    override fun deleteCustomer(customerId: Long): Int {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null

        try {
            conn = DataFactory.getConnection()
            stmt = conn!!.prepareStatement(SQL_DELETE_CUSTOMER_BY_ID)
            stmt!!.setLong(1, customerId)
            return stmt.executeUpdate()
        } catch (e: SQLException) {
            log.error("Error Deleting Customer :$customerId")
            throw ExceptionHandler("Error Deleting Customer Id:$customerId", e)
        } finally {
            DbUtils.closeQuietly(conn)
            DbUtils.closeQuietly(stmt)
        }
    }

    companion object {

        private val log = Logger.getLogger(CustomerSupport::class.java)
        private const val SQL_GET_CUSTOMER_BY_ID = "SELECT * FROM Customer WHERE CustomerId = ? "
        private const val SQL_GET_ALL_CUSTOMERS = "SELECT * FROM Customer"
        private const val SQL_GET_CUSTOMER_BY_NAME = "SELECT * FROM Customer WHERE CustomerName = ? "
        private const val SQL_INSERT_CUSTOMER = "INSERT INTO Customer (CustomerName, EmailAddress) VALUES (?, ?)"
        private const val SQL_UPDATE_CUSTOMER = "UPDATE Customer SET CustomerName = ?, EmailAddress = ? WHERE CustomerId = ? "
        private const val SQL_DELETE_CUSTOMER_BY_ID = "DELETE FROM Customer WHERE CustomerId = ? "

    }

}