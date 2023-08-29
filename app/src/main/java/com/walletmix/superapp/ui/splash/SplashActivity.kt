package com.walletmix.superapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.walletmix.superapp.R
import com.walletmix.superapp.ScannerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //going to use the lifecycle scope for splash
        //apply the lottie library to load the animation
        //try to see the documentation
        val splashDurationMillis = 1960L
        lifecycleScope.launch {
            delay(splashDurationMillis)
            startActivity(Intent(this@SplashActivity, ScannerActivity::class.java))
            finish()
        }
//        Handler().postDelayed({
//            startActivity(Intent(this@SplashActivity, ScannerActivity::class.java))
//            finish()
//        }, splashDurationMillis)
    }
}


