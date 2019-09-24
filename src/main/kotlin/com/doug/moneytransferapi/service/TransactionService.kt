package com.doug.moneytransferapi.service

import com.doug.moneytransferapi.data.DataFactory
import com.doug.moneytransferapi.data.DataObjectFactory
import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.CustomerTransaction
import com.doug.moneytransferapi.model.MoneyTransaction
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
class TransactionService {

    private val dataFactory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2)

    /**
     * Transfer fund between two accounts.
     * @param transaction
     * @return
     * @throws ExceptionHandler
     */
    @POST
    @Throws(ExceptionHandler::class)
    fun transferFund(transaction: CustomerTransaction): Response {

        val currency = transaction.currencyCode
        if (MoneyTransaction.INSTANCE.validateCurrencyCode(currency)) {
            val updateCount = dataFactory.accountDataObject.transferAccountBalance(transaction)
            return if (updateCount == 2) {
                Response.status(Response.Status.OK).build()
            } else {
                // transaction failed
                throw WebApplicationException("Transaction failed", Response.Status.BAD_REQUEST)
            }

        } else {
            throw WebApplicationException("Currency Code Invalid ", Response.Status.BAD_REQUEST)
        }

    }

}