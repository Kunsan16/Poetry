package com.kunsan.poetry.utils

import java.lang.StringBuilder

fun String.renderPoetry():String{
    return replace("，","，\n" )
}