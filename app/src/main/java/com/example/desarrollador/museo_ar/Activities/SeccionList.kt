package com.example.desarrollador.museo_ar.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.desarrollador.museo_ar.R
import android.widget.*
import com.example.desarrollador.museo_ar.Extension.goToActivity
import com.example.desarrollador.museo_ar.Login.LoginActivity
import com.example.desarrollador.museo_ar.Models.Pinturas
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_seccion_list.*

class SeccionList : AppCompatActivity() {

    private lateinit var mrecylerview : RecyclerView
    private lateinit var ref: DatabaseReference
    private lateinit var show_progress: ProgressBar
    private lateinit var pathString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seccion_list)
        pathString= intent.getStringExtra("pathString")
        ref = FirebaseDatabase.getInstance().reference.child("Seccion").child(pathString).child("Pinturas")
        mrecylerview = findViewById(R.id.reyclerview)
        mrecylerview.layoutManager = LinearLayoutManager(this)
        show_progress = findViewById(R.id.progress_bar)
        firebaseData()

        imageViewFlechaAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
   private fun firebaseData() {

        val option = FirebaseRecyclerOptions.Builder<Pinturas>()
            .setQuery(ref, Pinturas::class.java)
            .setLifecycleOwner(this)
            .build()

        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Pinturas, MyViewHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(this@SeccionList).inflate(R.layout.list_layout,parent,false)
                return MyViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Pinturas) {

                val placeid = getRef(position).key.toString()
                //Log.w("reference",placeid)
                ref.child(placeid).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(this@SeccionList, "Error Occurred "+ p0.toException(), Toast.LENGTH_SHORT).show()
                    }
                    override fun onDataChange(p0: DataSnapshot) {

                        show_progress.visibility = if(itemCount == 0) View.VISIBLE else View.GONE
                        holder.txt_name.text = model.Name
                        holder.itemView.setOnClickListener{
                            val intent = Intent(holder.itemView.context,PinturaInfoActivity::class.java)
                            intent.putExtra("pathSecciones",pathString)
                            intent.putExtra("pathPinturas",placeid)
                            //Log.w("reference",placeid)
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

        internal var txt_name: TextView = itemView!!.findViewById(R.id.Display_title)

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

