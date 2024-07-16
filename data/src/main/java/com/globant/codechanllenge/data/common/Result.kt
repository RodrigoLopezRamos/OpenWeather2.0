package com.globant.codechanllenge.data.common

data class Result<out T>(val status: Status, val data: T?, val error: Error? = null) {

    enum class Status {
        OK,
        ERROR,
        LOADING,
    }
    companion object {

        fun <T> success(data: T): Result<T> {
            return Result(Status.OK, data, null)
        }

        fun <T> error(throwable: Throwable? = null): Result<T> {
            return Result(Status.ERROR, null, HttpErrorParser.parseError(throwable))
        }

        fun <T> loading(): Result<T> {
            return Result(Status.LOADING, null, null)
        }
    }

}