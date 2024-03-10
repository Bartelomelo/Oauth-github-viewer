package com.example.githuboauthviewer.ui.github

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.githuboauthviewer.data.AppPreferences
import com.example.githuboauthviewer.ui.base.ViewModelFactory
import com.example.githuboauthviewer.ui.github.ui.theme.GithubOauthViewerTheme

class GithubActivity : ComponentActivity() {
    private lateinit var viewModel: GithubViewModel
    private lateinit var preferences: AppPreferences
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = AppPreferences(this)
        val viewModelFactory = ViewModelFactory(preferences)
        viewModel = ViewModelProvider(this, viewModelFactory)[GithubViewModel::class.java]
        viewModel.getToken()
        viewModel.token.observe(this) {
            when (it) {
                "" -> {}
                else -> token = it
            }
        }
        //token = runBlocking { preferences.getToken.first()!! }

        setContent {
            GithubOauthViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row() {
                        Log.d("ROW", token)
                        Text(text = token)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GithubOauthViewerTheme {
        Greeting("Android")
    }
}