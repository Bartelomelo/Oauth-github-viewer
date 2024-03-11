package com.example.githuboauthviewer.ui.github

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.githuboauthviewer.data.network.repository.GithubRepository
import com.example.githuboauthviewer.data.utils.AppPreferences
import com.example.githuboauthviewer.ui.base.ViewModelFactory
import com.example.githuboauthviewer.ui.github.ui.theme.GithubOauthViewerTheme

class GithubActivity : ComponentActivity() {
    private lateinit var viewModel: GithubViewModel
    private lateinit var repository: GithubRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = GithubRepository(AppPreferences(this))
        val viewModelFactory = ViewModelFactory(repository = repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[GithubViewModel::class.java]
        setContent {
            GithubOauthViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    githubScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun githubScreen(viewModel: GithubViewModel) {
    val token = viewModel.token.observeAsState()
    val user = viewModel.user.observeAsState()
    LaunchedEffect(key1 = viewModel.token) {
        viewModel.getToken()
        if (token.value != "") {
            Log.d("token", token.value!!)
            viewModel.getUser("Bearer ${token.value}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (user.value?.isSuccessful == true) {
            Row {
                profileImage(imageUrl = user.value?.body()!!.avatar_url)
            }
            Row {
                Text(text = "Username: ${user.value?.body()!!.name}")
            }
            Row {
                Text(text = "User repos: ${user.value?.body()!!.public_repos}")
            }
        } else {
            CircularProgressIndicator()
            Log.d("GithubApi error", user.value?.code().toString())
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun profileImage(imageUrl: String) {
    Box(
        modifier = Modifier
            .height(150.dp)
            .width(150.dp)
    ) {
        val painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(500)
                transformations(
                    CircleCropTransformation()
                )
            }
        )
        val state = painter.state
        Image(painter = painter, contentDescription = "UserAvatar")
        if (state is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GithubOauthViewerTheme {
        //Greeting("Android")
    }
}