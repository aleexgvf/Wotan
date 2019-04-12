package com.example.desarrollador.museo_ar.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.desarrollador.museo_ar.R

class SplashActivity : AppCompatActivity() {
    private val splashTimeOut:Long=3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this,MainEmpyActivity::class.java))

            finish()
        },splashTimeOut)
    }
}
