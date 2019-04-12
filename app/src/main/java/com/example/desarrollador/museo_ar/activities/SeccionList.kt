package com.example.desarrollador.museo_ar.Activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.desarrollador.museo_ar.R
import android.widget.*
import com.example.desarrollador.museo_ar.extension.goToActivity
import com.example.desarrollador.museo_ar.login.LoginActivity
import com.example.desarrollador.museo_ar.models.Pintura
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.iesoluciones.WotanAR.UnityPlayerActivity


class SeccionList : AppCompatActivity() {

    private lateinit var mRecylerView : RecyclerView
    private lateinit var ref: DatabaseReference
    private lateinit var showProgress: ProgressBar
    private lateinit var pathSecciones: String
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var imageViewFlechaAtras: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seccion_list)

        pathSecciones= intent.getStringExtra("pathSecciones")
        ref = FirebaseDatabase.getInstance().reference.child("Seccion").child(pathSecciones).child("Pinturas")
        mRecylerView = findViewById(R.id.reyclerview)
        mRecylerView.layoutManager = LinearLayoutManager(this)
        showProgress = findViewById(R.id.progress_bar)
        imageViewFlechaAtras = findViewById(R.id.imageViewFlechaAtrasSecciones)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        firebaseData()

        imageViewFlechaAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, UnityPlayerActivity::class.java)
            startActivity(intent)
        }
    }
   private fun firebaseData() {

        val option = FirebaseRecyclerOptions.Builder<Pintura>()
            .setQuery(ref, Pintura::class.java)
            .setLifecycleOwner(this)
            .build()

        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Pintura, MyViewHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(this@SeccionList).inflate(R.layout.list_layout,parent,false)
                return MyViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Pintura) {

                val placeid = getRef(position).key.toString()
                //Log.w("reference",placeid)
                ref.child(placeid).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(this@SeccionList, "Error Occurred "+ p0.toException(), Toast.LENGTH_SHORT).show()
                    }
                    override fun onDataChange(p0: DataSnapshot) {

                        showProgress.visibility = if(itemCount == 0) View.VISIBLE else View.GONE
                        holder.txt_name.text = model.name
                        holder.itemView.setOnClickListener{
                            val intent = Intent(holder.itemView.context,PinturaInfoActivity::class.java)
                            intent.putExtra("pathSecciones",pathSecciones)
                            intent.putExtra("pathPinturas",placeid)
                            //Log.w("reference",placeid)
                            holder.itemView.context.startActivity(intent)
                        }
                    }
                })
            }
        }
        mRecylerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }
    private class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        internal var txt_name: TextView = itemView!!.findViewById(R.id.Display_title)

    }
}

