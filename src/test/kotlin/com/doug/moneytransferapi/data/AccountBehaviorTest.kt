package com.doug.moneytransferapi.data

import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Account
import junit.framework.Assert.assertTrue
import org.apache.log4j.Logger
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountBehaviorTest {

    private val log = Logger.getLogger(AccountBehaviorTest::class.java)

    private val dataObjectFactory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2)

    @Before
    fun setup() {
        // Prepare test database and test data by executing sql script demo.sql
        log.debug("setting up test database and sample data....")
        dataObjectFactory.generateApplicationData()
    }

    @After
    fun tearDown() {
        //TearDown
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldGetAllAccounts() {
        val allAccounts = dataObjectFactory.accountDataObject.allAccounts
        assertTrue(allAccounts.size > 1)
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldGetAccountById() {
        val account = dataObjectFactory.accountDataObject.getAccountById(1L)
        assertEquals(account.customerName,"Ajani Goldmane")
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldCreateAccount() {
        val balance = BigDecimal(1530).setScale(4, RoundingMode.HALF_EVEN)
        val account = Account("Jace Beleren", balance, "BRL")
        val aid = dataObjectFactory.accountDataObject.insertAccount(account)
        val accountAfterCreation = dataObjectFactory.accountDataObject.getAccountById(aid)
        assertEquals(account.customerName,"Jace Beleren")
        assertEquals(accountAfterCreation.currencyCode,"BRL")
        accountAfterCreation.balance?.equals(balance)?.let { assertTrue(it) }
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldDeleteAccount() {
        val rowCount = dataObjectFactory.accountDataObject.deleteAccountById(2L)
        // assert one row(user) deleted
        assertEquals(rowCount, 1)
    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldntDeleteNonExistingAccount() {
        val rowCount = dataObjectFactory.accountDataObject.deleteAccountById(500L)
        // assert no customer deleted
        assertEquals(rowCount, 0)

    }

    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldUpdateAccountBalanceSufficientFund() {

        val deltaDeposit = BigDecimal(50).setScale(4, RoundingMode.HALF_EVEN)
        val afterDeposit = BigDecimal(350).setScale(4, RoundingMode.HALF_EVEN)
        val rowsUpdated = dataObjectFactory.accountDataObject.updateAccountBalance(1L, deltaDeposit)
        assertEquals(rowsUpdated,1)
        assertEquals(dataObjectFactory.accountDataObject.getAccountById(1L).balance!!, afterDeposit)
        val deltaWithDraw = BigDecimal(-50).setScale(4, RoundingMode.HALF_EVEN)
        val afterWithDraw = BigDecimal(300).setScale(4, RoundingMode.HALF_EVEN)
        val rowsUpdatedW = dataObjectFactory.accountDataObject.updateAccountBalance(1L, deltaWithDraw)
        assertEquals(rowsUpdatedW, 1)
        assertEquals(dataObjectFactory.accountDataObject.getAccountById(1L).balance!!, afterWithDraw)

    }

    @Test(expected = ExceptionHandler::class)
    @Throws(ExceptionHandler::class)
    fun iShouldntUpdateAccountBalanceNotEnoughFund() {
        val deltaWithDraw = BigDecimal(-50000).setScale(4, RoundingMode.HALF_EVEN)
        val rowsUpdatedW = dataObjectFactory.accountDataObject.updateAccountBalance(1L, deltaWithDraw)
        assertEquals(rowsUpdatedW, 0)

    }
}