package com.example.desarrollador.museo_ar.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.desarrollador.museo_ar.R
import android.view.*
import android.widget.*
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.example.desarrollador.museo_ar.Extension.goToActivity
import com.example.desarrollador.museo_ar.Login.LoginActivity
import com.example.desarrollador.museo_ar.Models.Secciones
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.iesoluciones.WotanAR.UnityPlayerActivity

var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Seccion")

class MainActivity : AppCompatActivity() {

    private lateinit var mrecyclerview : RecyclerView
    private lateinit var showProgress: ProgressBar
    private lateinit var imageViewFlechaAtras: ImageView
    private lateinit var floatingButtonMainAR: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mrecyclerview = findViewById(R.id.reyclerview)
        mrecyclerview.layoutManager = LinearLayoutManager(this)
        showProgress = findViewById(R.id.progress_bar)
        imageViewFlechaAtras = findViewById(R.id.imageViewFlechaAtras)
        floatingButtonMainAR = findViewById(R.id.floatingButtonMainAR)
        firebaseData()

        val app = application as MyApplication

        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                onRequirementsFulfilled = {
                    Log.d("app", "requirements fulfilled")
                    app.enableBeaconNotifications()
                },
                onRequirementsMissing = { requirements ->
                    Log.e("app", "requirements missing: " + requirements)
                },

                onError = { throwable ->
                    Log.e("app", "requirements error: " + throwable)
                })

        floatingButtonMainAR.setOnClickListener {
            val INTENT = Intent(this, UnityPlayerActivity::class.java)
            startActivity(INTENT)
        }

        imageViewFlechaAtras.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            goToActivity<LoginActivity>{
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            finish()
        }

    }

    fun firebaseData() {
        val OPTION = FirebaseRecyclerOptions.Builder<Secciones>()
            .setQuery(ref, Secciones::class.java)
            .setLifecycleOwner(this)
            .build()
        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Secciones, MyViewHolder>(OPTION){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val ITEM_VIEW = LayoutInflater.from(this@MainActivity).inflate(R.layout.list_layout,parent,false)
                return MyViewHolder(ITEM_VIEW)
            }
            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Secciones) {
                val PLACE_ID = getRef(position).key.toString()
                ref.child(PLACE_ID).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Error Occurred "+ p0.toException(), Toast.LENGTH_SHORT).show()
                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        showProgress.visibility = if(itemCount == 0) View.VISIBLE else View.GONE
                        holder.txtName.setText(model.Name)
                        holder.itemView.setOnClickListener{
                            val INTENT = Intent(holder.itemView.context,SeccionList::class.java)
                            INTENT.putExtra("pathSecciones",PLACE_ID)
                            holder.itemView.context.startActivity(INTENT)
                        }
                    }
                })

            }

        }
        mrecyclerview.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }
    private class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        internal var txtName: TextView = itemView!!.findViewById<TextView>(R.id.Display_title)

    }
}

