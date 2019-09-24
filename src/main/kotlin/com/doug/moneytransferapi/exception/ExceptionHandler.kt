package com.doug.moneytransferapi.exception

class ExceptionHandler : Exception {

    constructor(msg: String) : super(msg) {}

    constructor(msg: String, cause: Throwable) : super(msg, cause) {}

    companion object {

        private val serialVersionUID = 1L
    }
}
