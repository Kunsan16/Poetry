package com.kunsan.poetry.data

data class WeatherData(
    val humidity: Int,
    val pm25: Int,
    val rainfall: Int,
    val temperature: Int,
    val updateTime: String,
    val visibility: String,
    val weather: String,
    val windDirection: String,
    val windPower: Int
)