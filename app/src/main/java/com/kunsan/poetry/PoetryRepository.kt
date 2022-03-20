package com.kunsan.poetry

import com.kunsan.poetry.net.HttpManager
import kotlinx.coroutines.flow.flow

object PoetryRepository {


    fun token() =
        flow {
            emit(HttpManager.getWebservice().token())
        }


    fun poetry(token:String) =
        flow {
            emit(HttpManager.getWebservice().poetry(token))
        }
}