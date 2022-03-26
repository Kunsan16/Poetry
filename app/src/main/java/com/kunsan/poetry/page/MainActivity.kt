package com.kunsan.poetry.page

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kunsan.poetry.R
import com.kunsan.poetry.ui.theme.PoetryTheme
import com.kunsan.poetry.ui.theme.Purple200
import com.kunsan.poetry.ui.theme.Purple500
import com.kunsan.poetry.utils.renderPoetry
import com.kunsan.poetry.widget.WavesLoadingIndicator
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
            transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
            label = "contentTopPadding"
        ) {
            if (it == SplashState.Shown) 100.dp else 0.dp
        }
        Box {
            LandingScreen(modifier = Modifier.alpha(splashAlpha),
                onTimeout = { transitionState.targetState = SplashState.Completed })
            MainContent(
                modifier = Modifier.alpha(contentAlpha),
                viewModel
            )
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Box(modifier = modifier.fillMaxSize()) {
        val poetry by viewModel.poetry.observeAsState()
        WavesLoadingIndicator(modifier = Modifier.fillMaxSize(), color = Purple500, progress = .4f)
        Text(
            color = Purple500,
            modifier = Modifier.fillMaxSize(),
            text = poetry?.data?.matchTags?.first() ?: "唐", fontSize = 188.sp,
            fontFamily = FontFamily(Font(R.font.yegenyou, weight = FontWeight.Bold))
        )
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = poetry?.data?.content?.renderPoetry() ?: "nothing",
                lineHeight = 46.sp,
                fontSize = 36.sp,
                fontFamily = FontFamily(Font(R.font.yegenyou, weight = FontWeight.Bold)),
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier
                        .background(Color.Red, RectangleShape)
                        .padding(6.dp),
                    text = "${poetry?.data?.origin?.author}",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.wanghanz, weight = FontWeight.Bold)),
                    textAlign = TextAlign.End
                )
                Text(
                    text = "《${poetry?.data?.origin?.title}》",
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
        val showDialogState: Boolean by viewModel.showDialog.collectAsState()
        Image(
            painterResource(id = R.drawable.ic_brush), contentDescription = null,
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(horizontal = 12.dp)
                .clickable(onClick = {
                    viewModel.openDialog()
                })
        )
        if (showDialogState) {
            PersonalPoetryDialog(show = showDialogState, onDismiss = { },
                onConfirm = {
                    viewModel.onDialogDismiss()
                })
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