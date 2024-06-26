package com.shushprasad.image_loader

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val i = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            startActivity(i)
            finish()
        }, 3000)
    }
}
