package com.kunsan.poetry.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.kunsan.poetry.R
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(modifier: Modifier = Modifier,onTimeout: () -> Unit) {

    Box(modifier = modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        val currentOnTimeOut by rememberUpdatedState(onTimeout)

        LaunchedEffect(Unit){
            delay(2000)
            currentOnTimeOut()
        }
        Image(painterResource(id = R.drawable.ic_logo), contentDescription = null)
    }

}