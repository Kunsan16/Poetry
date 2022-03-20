package com.kunsan.poetry.data

data class PoetryResponse(
    val data: Data,
    val status: String
){

    fun isSuccessful() = status == "success"
}