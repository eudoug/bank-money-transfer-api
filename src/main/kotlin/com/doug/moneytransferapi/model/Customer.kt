package com.doug.moneytransferapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class Customer {

    @JsonIgnore
    var customerId: Long = 0

    @JsonProperty(required = true)
    var customerName: String? = null
        private set

    @JsonProperty(required = true)
    var emailAddress: String? = null
        private set

    constructor() {}

    constructor(customerName: String, emailAddress: String) {
        this.customerName = customerName
        this.emailAddress = emailAddress
    }

    constructor(customerId: Long, customerName: String, emailAddress: String) {
        this.customerId = customerId
        this.customerName = customerName
        this.emailAddress = emailAddress
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val customer = other as Customer

        if (customerId != customer.customerId) return false
        return if (customerName != customer.customerName) false else emailAddress == customer.emailAddress

    }

    override fun hashCode(): Int {
        var result = (customerId xor customerId.ushr(32)).toInt()
        result = 31 * result + customerName.hashCode()
        result = 31 * result + emailAddress.hashCode()
        return result
    }

    override fun toString(): String {
        return "customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\''.toString() +
                ", emailAddress='" + emailAddress + '\''.toString() +
                '}'.toString()
    }
}