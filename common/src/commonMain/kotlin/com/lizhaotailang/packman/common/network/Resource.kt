package com.lizhaotailang.packman.common.network

data class Resource<out T>(

    val status: Status,

    val data: T?,

    val e: Exception?

) {

    companion object {

        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCEEDED, data, null)

        fun <T> failed(exception: Exception?, data: T?): Resource<T> =
            Resource(Status.FAILED, data, exception)

        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)

    }

}

enum class Status {

    SUCCEEDED,

    FAILED,

    LOADING

}
