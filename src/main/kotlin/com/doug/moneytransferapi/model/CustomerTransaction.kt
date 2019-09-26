package com.doug.moneytransferapi.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal


class CustomerTransaction {


    @JsonProperty(required = true)
    var currencyCode: String? = null
        private set

    @JsonProperty(required = true)
    var amount: BigDecimal? = null
        private set

    @JsonProperty(required = true)
    var fromAccountId: Long? = null
        private set

    @JsonProperty(required = true)
    var toAccountId: Long? = null
        private set

    constructor() {}

    constructor(currencyCode: String, amount: BigDecimal, fromAccountId: Long?, toAccountId: Long?) {
        this.currencyCode = currencyCode
        this.amount = amount
        this.fromAccountId = fromAccountId
        this.toAccountId = toAccountId
    }


    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o == null || javaClass != o.javaClass)
            return false

        val that = o as CustomerTransaction?

        if (currencyCode != that!!.currencyCode)
            return false
        if (amount != that.amount)
            return false
        return if (fromAccountId != that.fromAccountId) {
            false
        } else toAccountId == that.toAccountId

    }

    override fun hashCode(): Int {
        var result = currencyCode.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + fromAccountId!!.hashCode()
        result = 31 * result + toAccountId!!.hashCode()
        return result
    }

    override fun toString(): String {
        return ("CustomerTransaction{" + "currencyCode='" + currencyCode + '\''.toString() + ", amount=" + amount + ", fromAccountId="
                + fromAccountId + ", toAccountId=" + toAccountId + '}'.toString())
    }

}
