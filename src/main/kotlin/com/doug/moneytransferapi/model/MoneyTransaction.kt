package com.doug.moneytransferapi.model

import org.apache.log4j.Logger
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

/**
 * Utilities class to operate on money
 */
enum class MoneyTransaction {

    INSTANCE;

    /**
     * @param inputCurrencyCode String Currency code to be validated
     * @return true if currency code is valid
     */
    fun validateCurrencyCode(inputCurrencyCode: String): Boolean {
        try {
            val instance = Currency.getInstance(inputCurrencyCode)
            if (log.isDebugEnabled) {
                log.debug("Validate Currency Code: " + instance.symbol)
            }
            return instance.currencyCode == inputCurrencyCode
        } catch (e: Exception) {
            log.warn("Validation Failed: ", e)
        }

        return false
    }

    companion object {

        internal var log = Logger.getLogger(MoneyTransaction::class.java)
        //zero amount with scale 4 and financial rounding mode
        val zeroAmount = BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)!!
    }

}
