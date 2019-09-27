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


    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null || javaClass != other.javaClass)
            return false

        val that = other as CustomerTransaction?

        if (currencyCode != currencyCode)
            return false
        if (amount != amount)
            return false
        return if (fromAccountId != fromAccountId) false
            else toAccountId == toAccountId

    }

    override fun hashCode(): Int {
        var result = currencyCode.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + fromAccountId.hashCode()
        result = 31 * result + toAccountId.hashCode()
        return result
    }

    override fun toString(): String {
        return """UserTransaction{currencyCode='$currencyCode', amount=$amount, fromAccountId=$fromAccountId, toAccountId=$toAccountId}"""
    }

}
