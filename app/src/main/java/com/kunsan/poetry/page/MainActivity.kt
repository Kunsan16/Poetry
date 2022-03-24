package com.kunsan.poetry.page

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kunsan.poetry.ui.theme.PoetryTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    private val USER_PREFERENCES = "user_preferences"

    private val dataStore by preferencesDataStore(
        name = USER_PREFERENCES
    )

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(dataStore)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets {
                PoetryTheme {
                    rememberSystemUiController().run {
                        // 设置状态栏颜色
                        setStatusBarColor(
                            color = Color.Transparent,
                            darkIcons = MaterialTheme.colors.isLight
                        )
//                        // 将状态栏和导航栏设置为color
//                        setSystemBarsColor(color = color, darkIcons = darkIcons)
                        // 设置导航栏颜色
                        setNavigationBarColor(color = Color.Transparent)
                    }
                    MainScreen()
                }

            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.findLocalToken()

        }
        lifecycleScope.launch {

            viewModel.poetry.observe(this@MainActivity) { poetries ->
                Log.e("诗歌朗诵 ", poetries.data.origin.title)
//                PoetryRecite(poetries.data.origin)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {

    Surface {
        val transitionState = remember { MutableTransitionState(SplashState.Shown) }
        val transition = updateTransition(transitionState, label = "splashTransition")
        val splashAlpha by transition.animateFloat(
            transitionSpec = { tween(durationMillis = 100) }, label = "splashAlpha"
        ) {
            if (it == SplashState.Shown) 1f else 0f
        }
        val contentAlpha by transition.animateFloat(
            transitionSpec = { tween(durationMillis = 300) }, label = "contentAlpha"
        ) {
            if (it == SplashState.Shown) 0f else 1f
        }
        val contentTopPadding by transition.animateDp(
            transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = "contentTopPadding"
        ) {
            if (it == SplashState.Shown) 100.dp else 0.dp
        }
        Box {
            LandingScreen(  modifier = Modifier.alpha(splashAlpha),
                onTimeout = { transitionState.targetState = SplashState.Completed })
            MainContent(modifier = Modifier.alpha(contentAlpha),
                topPadding = contentTopPadding,viewModel)
        }
    }
}

@Composable
private fun MainContent( modifier: Modifier = Modifier,
                         topPadding: Dp = 0.dp,viewModel: MainViewModel = viewModel()) {
    Column(modifier = modifier) {
        Box(modifier = modifier.fillMaxSize(),contentAlignment = Alignment.Center){
            val poetry by viewModel.poetry.observeAsState()
            Column {
                Text(text = poetry?.data?.content ?:"nothing",fontSize = 24.sp,
                    maxLines = 1,
                    softWrap = false)

                Row(modifier = Modifier.fillMaxWidth().padding(0.dp, 12.dp),
                    horizontalArrangement = Arrangement.End) {
                    Text(modifier = Modifier,
                        text = "--${poetry?.data?.origin?.author}" ,
                        fontSize = 20.sp,
                        textAlign = TextAlign.End)
                    Text(text = "《${poetry?.data?.origin?.title}》" ,
                        fontSize = 20.sp,
                        textAlign = TextAlign.End)
                }

            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PoetryTheme {

    }
}

enum class SplashState { Shown, Completed }