package com.doug.moneytransferapi.service

import com.doug.moneytransferapi.data.DataObjectFactory
import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.CustomerTransaction
import com.doug.moneytransferapi.model.MoneyTransaction
import org.apache.log4j.Logger
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
class TransferService {

    private val factory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2)

    /**
     * Transfer fund between two accounts.
     * @param transfer
     * @return
     * @throws ExceptionHandler
     */
    @POST
    @Path("/money")
    @Throws(ExceptionHandler::class)
    fun transferFund(transfer: CustomerTransaction): Response {

        val currency = transfer.currencyCode
        if (MoneyTransaction.INSTANCE.validateCurrencyCode(currency!!)) {
            val updateCount = factory.accountDataObject.transferAccountBalance(transfer)
            return if (updateCount == 2) {
                Response.status(Response.Status.OK).build()
            } else {
                // transfer failed
                throw WebApplicationException("Transaction failed", Response.Status.BAD_REQUEST)
            }

        } else {
            throw WebApplicationException("Currency Code Invalid ", Response.Status.BAD_REQUEST)
        }
    }

    companion object {

        private val log = Logger.getLogger(CustomerService::class.java)
    }

}