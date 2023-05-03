package com.example.kmmsandbox

open class MyService {
    private var errorMode: Boolean = false

    fun changeErrorMode(value: Boolean) {
        errorMode = value
    }

    protected fun failInErrorModeOn(predicate: () -> Boolean) {
        if (errorMode && predicate()) {
            throw MyException("Something went wrong!")
        }
    }
}

class MyException(message: String): Exception(message)