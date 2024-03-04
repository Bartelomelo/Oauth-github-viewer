package com.example.githuboauthviewer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.githuboauthviewer.ui.auth.AuthActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, AuthActivity::class.java))
    }
}
