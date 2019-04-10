package com.example.desarrollador.museo_ar.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.desarrollador.museo_ar.Extension.goToActivity
import com.example.desarrollador.museo_ar.Login.LoginActivity
import com.example.desarrollador.museo_ar.Models.Pinturas
import com.example.desarrollador.museo_ar.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.iesoluciones.WotanAR.UnityPlayerActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pintura_info.*
import kotlinx.android.synthetic.main.activity_pintura_info.floatingActionButton
import kotlinx.android.synthetic.main.activity_pintura_info.view.*
import kotlinx.android.synthetic.main.activity_seccion_list.*

class PinturaInfoActivity : AppCompatActivity() {

    private lateinit var ref: DatabaseReference
    private lateinit var pathSecciones: String
    private lateinit var pathPinturas: String
    private lateinit var imageViewPintura: ImageView
    private lateinit var textViewNombre: TextView
    private lateinit var textViewDesc: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var toolvarView: android.widget.Toolbar
    private var  pinturasSubscription: ValueEventListener? = null
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var imageViewFlechaAtras: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pintura_info)
        pathSecciones = intent.getStringExtra("pathSecciones")
        pathPinturas = intent.getStringExtra("pathPinturas")
        ref = FirebaseDatabase.getInstance().getReference().child("Seccion").child(pathSecciones).child("Pinturas").child(pathPinturas)
        Log.w("url",ref.toString())
        imageViewPintura = findViewById(R.id.imageViewPintura)
        textViewNombre = findViewById(R.id.textViewNombre)
        ratingBar = findViewById(R.id.ratingBar)
        textViewDesc = findViewById(R.id.textViewDesc)
        toolvarView = findViewById(R.id.toolbarView)
        imageViewFlechaAtras = findViewById(R.id.imageViewFlechaAtrasPinturas)
        floatingActionButton =findViewById(R.id.floatingActionButton)
        firebaseData()

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, UnityPlayerActivity::class.java)
            startActivity(intent)
        }

        imageViewFlechaAtras.setOnClickListener {
            val intent = Intent(this, SeccionList::class.java)
            intent.putExtra("pathSecciones",pathSecciones)
            startActivity(intent)
            finish()
        }
    }

       private fun firebaseData(){
        pinturasSubscription = ref
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pinturas : Pinturas = dataSnapshot.getValue(Pinturas::class.java)!!
                    Picasso.get().load(pinturas!!.Imagen).into(imageViewPintura)
                    textViewDesc.text = Html.fromHtml("<div style='text-align: justify;'>${pinturas.Desc}</div>")
                    textViewNombre.text = pinturas.Name
                    ratingBar.rating = pinturas.Rating
                    toolvarView.title = pinturas.Name
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.general_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_log_out -> {
                FirebaseAuth.getInstance().signOut()
                goToActivity<LoginActivity>{
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}