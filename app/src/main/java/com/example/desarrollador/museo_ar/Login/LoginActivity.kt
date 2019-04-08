package com.example.desarrollador.museo_ar.Login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.support.design.chip.ChipGroup
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioGroup
import com.example.desarrollador.museo_ar.Activities.MainActivity
import com.example.desarrollador.museo_ar.Extension.*
import com.example.desarrollador.museo_ar.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient : GoogleApiClient by lazy{ getGoogleApiClient()}
    private var callbackManager: CallbackManager= CallbackManager.Factory.create()
    private val RC_GOOGLE_SIGN_IN = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(mAuth.currentUser == null){
            toast("Nope")
        }else{
            toast("YEAH BOI")
            mAuth.signOut()
        }


        buttonLogIn.setOnClickListener{
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if(isValidEmailAndPassword(email,password)){
                logInByEmail(email,password)

            }else toast("Por favor intentelo de nuevo")
        }
        textViewForgotPassword.setOnClickListener{
            goToActivity<ForgotPassword_activity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }
        buttonSignUp.setOnClickListener{
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }

        //Login por Cuenta de Google
        buttonLogInGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent,RC_GOOGLE_SIGN_IN)
        }

        //Login por Facebook
        buttonLogInFacebook.setReadPermissions("email")
        buttonLogInFacebook.setOnClickListener{
            loginByFacebookAccountIntoFirebase()
        }

        validarEmailyPassword(editTextEmail, editTextPassword)

    }


    //Métodos para hacer login mediante Facebook
   private fun loginByFacebookAccountIntoFirebase(){
       buttonLogInFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
           override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
               goToActivity<MainActivity>()
               overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
           }

           override fun onCancel() {
                toast("Error en al iniciar Sesión por Facebook.")
           }

           override fun onError(error: FacebookException?) {
                toast("Error inesperado, por favor intente de nuevo.")
           }

       })
   }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Conseguir credentials
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        mAuth.signInWithCredential(credential)
            .addOnFailureListener{exception ->
                toast(exception.message.toString()) }
            .addOnSuccessListener {result ->
                //Conseguir Email
                val email = result.user.email
                toast("Login con facebook $email")
            }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    //Métodos para login con google
    private fun getGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()

    }

    private fun loginByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if(mGoogleApiClient.isConnected){
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            }
            goToActivity<MainActivity>{
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////

    private fun fillEditTexts(email: String, password: String){

    }


    //Metodo para comprobar si el usuario se ha "loggeado" de forma correcta.
    private fun logInByEmail(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                if(mAuth.currentUser!!.isEmailVerified)
                {
                    toast("Bienvenido!")
                    goToActivity<MainActivity>()
                    {
                        flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        cleanTxt()
                    }
                }
                else
                {
                    toast("El email no ha sido verificado. ")
                    cleanTxt()
                }

            }
            else
            {
                toast("Error inesperado, por favor intentelo de nuevo.")
                cleanTxt()
            }
        }
    }

    //validar que el correo y la contraseña no sean nulas o vacias
    private fun isValidEmailAndPassword(email:String, password: String): Boolean {
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_GOOGLE_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess){
                val account = result.signInAccount
                loginByGoogleAccountIntoFirebase(account!!)
            }
        }

        callbackManager.onActivityResult(requestCode,resultCode,data)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("Conexion fallida!")
    }

    //Método para limpiar los editText
    private fun cleanTxt()
    {
        editTextEmail.text.clear()
        editTextPassword.text.clear()
    }


    //Método para validar en los editText que la estructura del Email este correcto y que
    //la contraseña lleve un patron en especifico de 6 caracteres como minimo
    //(1 Mayuscula, 1 Minuscula, 1 Número y 1 Caracter especial)
    private fun validarEmailyPassword(email: EditText, password: EditText)
    {
        email.validate {
            if(!isValidEmail(it))
                email.error = "El email no es valido."
        }

        password.validate{
            if(isValidPassword(it))
                password.error = "La contraseña debe tener al menos 1 minuscula, 1 mayuscula, 1 número y un caracter y 6 caracteres como minimo."
        }
    }

}
