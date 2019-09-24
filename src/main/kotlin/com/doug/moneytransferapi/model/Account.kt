package com.doug.moneytransferapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

class Account {

    @JsonIgnore
    var accountId: Long = 0

    @JsonProperty(required = true)
    lateinit var customerName: String

    @JsonProperty(required = true)
    lateinit var balance: BigDecimal

    @JsonProperty(required = true)
    lateinit var currencyCode: String


    constructor() {}

    constructor(customerName: String, balance: BigDecimal, currencyCode: String) {
        this.customerName = customerName
        this.balance = balance
        this.currencyCode = currencyCode
    }

    constructor(accountId: Long, customerName: String, balance: BigDecimal, currencyCode: String) {
        this.accountId = accountId
        this.customerName = customerName
        this.balance = balance
        this.currencyCode = currencyCode
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val account = o as Account

        if (accountId != account.accountId) return false
        if (customerName != account.customerName) return false
        return if (balance != account.balance) false else {
            currencyCode == account.currencyCode
        }

    }

    override fun hashCode(): Int {
        var result = (accountId xor accountId.ushr(32)).toInt()
        result = 31 * result + customerName.hashCode()
        result = 31 * result + balance.hashCode()
        result = 31 * result + currencyCode.hashCode()
        return result
    }

    override fun toString(): String {
        return "Account{" +
                "accountId=" + accountId +
                ", customerName='" + customerName + '\''.toString() +
                ", balance=" + balance +
                ", currencyCode='" + currencyCode + '\''.toString() +
                '}'.toString()
    }
}
