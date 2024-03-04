package com.example.githuboauthviewer.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.githuboauthviewer.data.GITHUB_CLIENT_ID
import com.example.githuboauthviewer.data.GITHUB_CLIENT_SECRET
import com.example.githuboauthviewer.ui.auth.ui.theme.GithubOauthViewerTheme
import com.example.githuboauthviewer.ui.github.GithubActivity
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues

class AuthActivity : ComponentActivity() {

    private lateinit var service: AuthorizationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = AuthorizationService(this)
        setContent {
            GithubOauthViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { githubAuth() }) {
                            Text(text = "Log In")
                        }
                    }

                }
            }
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
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
                        Log.d("Github Token", token!!)
                        Toast.makeText(this, token, Toast.LENGTH_LONG).show()
                        val intent = Intent(this, GithubActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun githubAuth() {
        val redirectUri = Uri.parse("githuboauthviewer://github.com/Bartelomelo/Oauth-github-viewer")
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
}