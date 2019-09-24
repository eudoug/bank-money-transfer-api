package com.doug.moneytransferapi.data

import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Account
import com.doug.moneytransferapi.model.CustomerTransaction
import java.math.BigDecimal

interface AccountDataObject {

    val allAccounts: List<Account>
    @Throws(ExceptionHandler::class)
    fun getAccountById(accountId: Long): Account

    @Throws(ExceptionHandler::class)
    fun createAccount(account: Account): Long

    @Throws(ExceptionHandler::class)
    fun deleteAccountById(accountId: Long): Int

    @Throws(ExceptionHandler::class)
    fun transferAccountBalance(customerTransaction: CustomerTransaction): Int
    /**
     *
     * @param accountId customer accountId
     * @param deltaAmount amount to be debit.
     */
    @Throws(ExceptionHandler::class)
    fun updateAccountBalance(accountId: Long, deltaAmount: BigDecimal): Int



}
