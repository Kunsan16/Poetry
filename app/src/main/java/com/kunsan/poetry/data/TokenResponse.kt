package com.kunsan.poetry.data

data class TokenResponse(
    val data: String,
    val status: String
){

    fun isSuccessful() = status == "success"
}