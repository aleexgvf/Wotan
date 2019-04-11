package com.example.desarrollador.museo_ar.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.desarrollador.museo_ar.Extension.goToActivity
import com.example.desarrollador.museo_ar.Login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

//login y mainactivity flow
class MainEmpyActivity : AppCompatActivity() {


    private val M_AUTH: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(M_AUTH.currentUser == null){
            goToActivity<LoginActivity>(){
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }

        }else{
            goToActivity<MainActivity>(){
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
        finish()
    }
}
