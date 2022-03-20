package com.kunsan.poetry.data

data class Data(
    val cacheAt: String,
    val content: String,
    val id: String,
    val matchTags: List<String>,
    val origin: Origin,
    val popularity: Int,
    val recommendedReason: String
)