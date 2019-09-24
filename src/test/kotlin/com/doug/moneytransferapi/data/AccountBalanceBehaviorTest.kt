package com.doug.moneytransferapi.data

import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.CustomerTransaction
import org.apache.log4j.Logger
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountBalanceBehaviorTest {

    private val log = Logger.getLogger(AccountBalanceBehaviorTest::class.java)
    private val dataObjectFactory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2)
    private val threadsCounter = 100

    @Before
    fun setup() {
        // Prepare test database and test data by executing sql script demo.sql
        log.debug("setting up test database and sample data....")
        dataObjectFactory.generateApplicationData()
    }

    @After
    fun tearDown() {
        // TearDown
    }

    // In this test I should transfer money between accounts
    @Test
    @Throws(ExceptionHandler::class)
    fun iShouldTransferMoneyBetweenAccountsWithSameCurrencyCode() {

        val accountDataObject = dataObjectFactory.accountDataObject
        val transferAmount = BigDecimal(67.54321).setScale(4, RoundingMode.HALF_EVEN)
        val transaction = CustomerTransaction("USD", transferAmount, 1L, 3L)
        val startTime = System.currentTimeMillis()

        accountDataObject.transferAccountBalance(transaction)
        val endTime = System.currentTimeMillis()

        log.info("TransferAccountBalance finished, time taken: " + (endTime - startTime) + "ms")

        val accountFrom = accountDataObject.getAccountById(1)
        val accountTo = accountDataObject.getAccountById(3)

        log.debug("Account From: $accountFrom")
        log.debug("Account From: $accountTo")

        assertEquals(accountFrom.balance?.compareTo(BigDecimal(232.4568).setScale(4, RoundingMode.HALF_EVEN)), 0)
        assertTrue(accountTo.balance?.equals(BigDecimal(567.5432).setScale(4, RoundingMode.HALF_EVEN))!!)
    }

    // In this test I should transfer a total of 200EUR from 700EUR balance in multi-threaded
    @Test
    @Throws(InterruptedException::class, ExceptionHandler::class)
    fun iShouldMultiThreadTransferMoneyBetweenAccounts() {
        val accountDataObject = dataObjectFactory.accountDataObject
        // A CountDownLatch is a construct that a thread waits on while other threads count down on the latch until it reaches zero
        val latch = CountDownLatch(threadsCounter)
        for (i in 0 until threadsCounter) {
            Thread(Runnable {
                try {
                    val transaction = CustomerTransaction("EUR",
                        BigDecimal(2).setScale(4, RoundingMode.HALF_EVEN), 4L, 5L)
                        accountDataObject.transferAccountBalance(transaction)
                } catch (e: Exception) {
                    log.error("Error occurred during multiple transfers ", e)
                } finally {
                    latch.countDown()
                }
            }).start()
        }

        latch.await()

        val accountFrom = accountDataObject.getAccountById(4)
        val accountTo = accountDataObject.getAccountById(5)

        log.debug("Account From: $accountFrom")
        log.debug("Account From: $accountTo")

        assertTrue(accountFrom.balance?.equals(BigDecimal(500).setScale(4, RoundingMode.HALF_EVEN))!!)
        assertTrue(accountTo.balance?.equals(BigDecimal(900).setScale(4, RoundingMode.HALF_EVEN))!!)

    }


}