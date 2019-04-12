package com.example.desarrollador.museo_ar.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.desarrollador.museo_ar.R
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.EditText
import com.example.desarrollador.museo_ar.extension.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        buttonGoLogIn.setOnClickListener {
            goToActivity<LoginActivity>(){
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }

        buttonCreateUser.setOnClickListener {
            val email= editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if(isValidEmailAndPassword(email,password)){
                signUpByEmail(email,password)
            }else{
                toast("make sure the password and email are correct")
            }
        }

        validarEPCP(editTextEmail, editTextPassword, editTextComfirmPassword)

    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){

                        toast("Se han enviado un email a tu correo, comfirmar antes de entrar a la aplicacion")
                        goToActivity<LoginActivity>{
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK  or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    }


                } else {

                   toast("Error, verfique los datos e intentelo de nuevo")
                }

            }
    }

    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return !email.isNullOrEmpty()&& !password.isNullOrEmpty() &&
                password == editTextComfirmPassword.text.toString()
    }

    //Método para validar en los editText que la estructura del Email este correcto y que
    //la contraseña lleve un patron en especifico de 6 caracteres como minimo
    //(1 Mayuscula, 1 Minuscula, 1 Número y 1 Caracter especial)
    private fun validarEPCP(email: EditText, password: EditText, confirmPassword: EditText )
    {

        email.validate {
            if(!isValidEmail(it))
                email.error = "El email no es valido."
        }

        password.validate{
            if(isValidPassword(it))
                password.error = "La contraseña debe tener al menos 1 minuscula, 1 mayuscula, 1 número y un caracter y 6 caracteres como minimo."
        }

        confirmPassword.validate {
            if(!isValidEmailAndPassword(password.text.toString(), it))
                confirmPassword.error = "La contraseña debe de conicidir. Por favor, intente de nuevo."
        }

    }



}
