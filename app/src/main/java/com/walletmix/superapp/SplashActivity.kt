package com.walletmix.superapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashDurationMillis = 1960L

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, ScannerActivity::class.java))
            finish()
        }, splashDurationMillis)
    }
}


