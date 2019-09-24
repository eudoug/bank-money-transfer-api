package com.doug.moneytransferapi.service

import com.doug.moneytransferapi.data.DataObjectFactory
import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Account
import com.doug.moneytransferapi.model.MoneyTransaction
import org.apache.log4j.Logger
import java.math.BigDecimal
import java.math.RoundingMode
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


/**
 * Account Service
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
class AccountService {

    private val factory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2)


    /**
     * Find all accounts
     * @return
     * @throws ExceptionHandler
     */
    val allAccounts: List<Account>
        @GET
        @Path("/all")
        @Throws(ExceptionHandler::class)
        get() = factory.accountDataObject.allAccounts

    /**
     * Find by account id
     * @param accountId
     * @return
     * @throws ExceptionHandler
     */
    @GET
    @Path("/{accountId}")
    @Throws(ExceptionHandler::class)
    fun getAccount(@PathParam("accountId") accountId: Long): Account? {
        return factory.accountDataObject.getAccountById(accountId)
    }

    /**
     * Find balance by account Id
     * @param accountId
     * @return
     * @throws ExceptionHandler
     */
    @GET
    @Path("/{accountId}/balance")
    @Throws(ExceptionHandler::class)
    fun getBalance(@PathParam("accountId") accountId: Long) {
        val account =
            factory.accountDataObject.getAccountById(accountId) ?: throw WebApplicationException(
                "Account not found",
                Response.Status.NOT_FOUND
            )
        return
    }

    /**
     * Create Account
     * @param account
     * @return
     * @throws ExceptionHandler
     */
    @PUT
    @Path("/create")
    @Throws(ExceptionHandler::class)
    fun createAccount(account: Account): Account? {
        val accountId = factory.accountDataObject.createAccount(account)
        return factory.accountDataObject.getAccountById(accountId)
    }

    /**
     * Deposit amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws ExceptionHandler
     */
    @PUT
    @Path("/{accountId}/deposit/{amount}")
    @Throws(ExceptionHandler::class)
    fun deposit(@PathParam("accountId") accountId: Long, @PathParam("amount") amount: BigDecimal): Account? {

        if (amount <= MoneyTransaction.zeroAmount) {
            throw WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST)
        }

        factory.accountDataObject.updateAccountBalance(accountId, amount.setScale(4, RoundingMode.HALF_EVEN))
        return factory.accountDataObject.getAccountById(accountId)
    }

    /**
     * Withdraw amount by account Id
     * @param accountId
     * @param amount
     * @return
     * @throws ExceptionHandler
     */
    @PUT
    @Path("/{accountId}/withdraw/{amount}")
    @Throws(ExceptionHandler::class)
    fun withdraw(@PathParam("accountId") accountId: Long, @PathParam("amount") amount: BigDecimal): Account? {

        if (amount.run { compareTo(MoneyTransaction.zeroAmount) } <= 0) {
            throw WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST)
        }
        val delta = amount.negate()
        if (log.isDebugEnabled)
            log.debug("Withdraw service: delta change to account  $delta Account ID = $accountId")
        factory.accountDataObject.updateAccountBalance(accountId, delta.setScale(4, RoundingMode.HALF_EVEN))
        return factory.accountDataObject.getAccountById(accountId)
    }


    /**
     * Delete amount by account Id
     * @param accountId
     * @return
     * @throws ExceptionHandler
     */
    @DELETE
    @Path("/{accountId}")
    @Throws(ExceptionHandler::class)
    fun deleteAccount(@PathParam("accountId") accountId: Long): Response {
        val deleteCount = factory.accountDataObject.deleteAccountById(accountId)
        return if (deleteCount == 1) {
            Response.status(Response.Status.OK).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    companion object {

        private val log = Logger.getLogger(AccountService::class.java)
    }

}