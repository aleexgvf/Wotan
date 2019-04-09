package com.example.desarrollador.museo_ar.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.desarrollador.museo_ar.R
import android.view.*
import android.widget.*
import com.example.desarrollador.museo_ar.extension.goToActivity
import com.example.desarrollador.museo_ar.login.LoginActivity
import com.example.desarrollador.museo_ar.models.Secciones
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.iesoluciones.WotanAR.UnityPlayerActivity

var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Seccion")

class MainActivity : AppCompatActivity() {

    private lateinit var mrecylerview : RecyclerView
    private lateinit var show_progress: ProgressBar
    private lateinit var imageViewFlechaAtras: ImageView
    private lateinit var floatingActionButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mrecylerview = findViewById(R.id.reyclerview)
        mrecylerview.layoutManager = LinearLayoutManager(this)
        show_progress = findViewById(R.id.progress_bar)
        imageViewFlechaAtras = findViewById(R.id.imageViewFlechaAtras)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        firebaseData()


        //val app = application as MyApplication

       /* RequirementsWizardFactory
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
                }) */

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, UnityPlayerActivity::class.java)
            startActivity(intent)
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
        val option = FirebaseRecyclerOptions.Builder<Secciones>()
            .setQuery(ref, Secciones::class.java)
            .setLifecycleOwner(this)
            .build()
        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Secciones, MyViewHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(this@MainActivity).inflate(R.layout.list_layout,parent,false)
                return MyViewHolder(itemView)
            }
            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Secciones) {
                val placeid = getRef(position).key.toString()
                ref.child(placeid).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Error Occurred "+ p0.toException(), Toast.LENGTH_SHORT).show()
                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        show_progress.visibility = if(itemCount == 0) View.VISIBLE else View.GONE
                        holder.txt_name.setText(model.Name)
                        holder.itemView.setOnClickListener{
                            val intent = Intent(holder.itemView.context,SeccionList::class.java)
                            intent.putExtra("pathSecciones",placeid)
                            holder.itemView.context.startActivity(intent)
                        }
                    }
                })

            }

        }
        mrecylerview.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }
    private class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        internal var txt_name: TextView = itemView!!.findViewById<TextView>(R.id.Display_title)

    }
}

