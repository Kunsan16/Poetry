package com.kunsan.poetry.data

data class Origin(
    val author: String,
    val content: List<String>,
    val dynasty: String,
    val title: String,
    val translate: List<String>
)