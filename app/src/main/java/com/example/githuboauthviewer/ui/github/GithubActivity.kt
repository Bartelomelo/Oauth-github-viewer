package com.example.githuboauthviewer.ui.github

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.githuboauthviewer.data.network.repository.GithubRepository
import com.example.githuboauthviewer.data.utils.AppPreferences
import com.example.githuboauthviewer.ui.auth.AuthActivity
import com.example.githuboauthviewer.ui.base.ViewModelFactory
import com.example.githuboauthviewer.ui.github.ui.theme.GithubOauthViewerTheme
import com.example.githuboauthviewer.ui.github.ui.theme.Purple40
import com.example.githuboauthviewer.ui.github.ui.theme.primaryColor
import com.example.githuboauthviewer.ui.github.ui.theme.secondaryColor

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
                    color = secondaryColor
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
            Row(
                Modifier.padding(top = 16.dp)
            ) {
                InfoCard("Username: ${user.value?.body()!!.login}")
            }
            Row(
                Modifier.padding(top = 16.dp)
            ) {
                InfoCard("User repos: ${user.value?.body()!!.public_repos}")
            }
            Row(
                Modifier
                    .padding(top = 16.dp)
            ) {
                InfoCard("On Github since: ${user.value?.body()!!.created_at.split("T")[0]}")
            }
        } else if (user.value?.code().toString() == "401") {
            Toast.makeText(LocalContext.current, "Zaloguj się ponownie.", Toast.LENGTH_SHORT).show()
            val intent = Intent(LocalContext.current, AuthActivity::class.java)
            startActivity(LocalContext.current, intent, null)
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun InfoCard(text: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 350.dp, height = 60.dp),
        colors = CardDefaults.cardColors(primaryColor)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .width(500.dp)
                .padding(16.dp),
            fontSize = 18.sp,
            color = Purple40,
            textAlign = TextAlign.Center,
        )
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