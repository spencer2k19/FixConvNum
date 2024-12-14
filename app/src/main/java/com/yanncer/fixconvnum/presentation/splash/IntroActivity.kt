package com.yanncer.fixconvnum.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yanncer.fixconvnum.R
import com.yanncer.fixconvnum.presentation.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        lifecycleScope.launch {
            delay(2500)
            val intent = Intent(this@IntroActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}