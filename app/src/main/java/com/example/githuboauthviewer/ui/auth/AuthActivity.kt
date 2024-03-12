package com.example.githuboauthviewer.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.githuboauthviewer.R
import com.example.githuboauthviewer.data.utils.AppPreferences
import com.example.githuboauthviewer.data.utils.GITHUB_CLIENT_ID
import com.example.githuboauthviewer.data.utils.GITHUB_CLIENT_SECRET
import com.example.githuboauthviewer.data.network.repository.GithubRepository
import com.example.githuboauthviewer.ui.auth.ui.theme.GithubOauthViewerTheme
import com.example.githuboauthviewer.ui.base.ViewModelFactory
import com.example.githuboauthviewer.ui.github.GithubActivity
import com.example.githuboauthviewer.ui.github.ui.theme.Purple40
import com.example.githuboauthviewer.ui.github.ui.theme.loginColor
import com.example.githuboauthviewer.ui.github.ui.theme.primaryColor
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues

class AuthActivity : ComponentActivity() {

    private lateinit var service: AuthorizationService
    private lateinit var viewModel: AuthViewModel
    private lateinit var repository: GithubRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = AuthorizationService(this)
        repository = GithubRepository(AppPreferences(this))
        val viewModelFactory = ViewModelFactory(repository = repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
        setContent {
            GithubOauthViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = primaryColor
                ) {
                    AuthScreen()
                }
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val ex = AuthorizationException.fromIntent(it.data!!)
                val result = AuthorizationResponse.fromIntent(it.data!!)

                if (ex != null) {
                    Log.e("Github Auth", "launcher: $ex")
                } else {
                    Log.d("Github", "i'm here!")
                    val secret = ClientSecretBasic(GITHUB_CLIENT_SECRET)
                    val tokenRequest = result?.createTokenExchangeRequest()

                    service.performTokenRequest(tokenRequest!!, secret) { res, exception ->
                        if (exception != null) {
                            Log.e("Github Auth", "launcher: ${exception.error}")
                        } else {
                            val token = res?.accessToken
                            viewModel.saveToken(token!!)
                            val intent = Intent(this, GithubActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

    private fun githubAuth() {
        val redirectUri =
            Uri.parse("githuboauthviewer://github.com/Bartelomelo/Oauth-github-viewer")
        val authorizeUri = Uri.parse("https://github.com/login/oauth/authorize")
        val tokenUri = Uri.parse("https://github.com/login/oauth/access_token")

        val config = AuthorizationServiceConfiguration(authorizeUri, tokenUri)
        val request = AuthorizationRequest
            .Builder(config, GITHUB_CLIENT_ID, ResponseTypeValues.CODE, redirectUri)
            .setScopes("user,repo")
            .build()
        val intent = service.getAuthorizationRequestIntent(request)
        launcher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        service.dispose()
    }

    @Composable
    private fun AuthScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Image(painter = painterResource(id = R.drawable.github), contentDescription = null, modifier = Modifier.width(370.dp).height(140.dp))
            }
            Row(
                Modifier.padding(top = 16.dp)
            ) {
                Button(
                    onClick = { githubAuth() },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Purple40)
                ) {
                    Text(text = "Log In")
                }
            }
        }
    }
}
