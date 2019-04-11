package com.example.desarrollador.museo_ar.Login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.desarrollador.museo_ar.Extension.goToActivity
import com.example.desarrollador.museo_ar.Extension.isValidEmail
import com.example.desarrollador.museo_ar.Extension.toast
import com.example.desarrollador.museo_ar.Extension.validate
import com.example.desarrollador.museo_ar.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password_activity.*

class ForgotPassword_activity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_activity)


        editTextEmail.validate {
            editTextEmail.error = if(isValidEmail(it)) null else "Email is not valid."
        }


        buttonGoLogIn2.setOnClickListener{
            goToActivity<LoginActivity>(){
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        buttonForgot.setOnClickListener {
            val email = editTextEmail.text.toString()
            if(isValidEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast("Email has been to reset your password.")
                    goToActivity<LoginActivity>(){
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                }
            }else{
                toast("Please make sure the email address is correct.")
            }
        }
    }
}
