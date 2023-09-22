package com.example.proyecto_final_de_onboarding.data

sealed class Result<out T> {
    data class Success<out R>(val value: R) : Result<R>()
    data class Error(
        val message: String?,
        val throwable: Throwable?
    ) : Result<Nothing>()
}

