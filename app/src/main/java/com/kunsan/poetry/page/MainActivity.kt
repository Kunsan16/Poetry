package com.kunsan.poetry.page

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.kunsan.poetry.data.Origin
import com.kunsan.poetry.ui.theme.PoetryTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    private val USER_PREFERENCES = "user_preferences"

    private val dataStore by preferencesDataStore(
        name = USER_PREFERENCES
    )

    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory(dataStore)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PoetryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.findLocalToken()

        }
        lifecycleScope.launch {
            viewModel.poetry.observe(this@MainActivity){  poetries ->
                Log.e("诗歌朗诵 ",poetries.data.origin.title)
//                PoetryRecite(poetries.data.origin)
            }
        }
    }
}

@Composable
fun PoetryRecite(origin:Origin) {
   Column {
       Text(text = origin.title)
   }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PoetryTheme {

    }
}