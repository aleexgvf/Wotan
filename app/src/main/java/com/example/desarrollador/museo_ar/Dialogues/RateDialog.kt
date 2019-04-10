package com.example.desarrollador.museo_ar.Dialogues

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import com.example.desarrollador.museo_ar.Extension.toast
import com.example.desarrollador.museo_ar.Models.NewRateEvent
import com.example.desarrollador.museo_ar.Models.Rate
import com.example.desarrollador.museo_ar.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import com.example.desarrollador.museo_ar.Utils.RxBus
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_rate.view.*


class RateDialog : DialogFragment()
{
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    override fun onCreateDialog(savedInstanceState: Bundle? ): Dialog {
        setUpCurrentUser()
        val builder = AlertDialog.Builder(context!!)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_rate, null)
        builder.setView(view)
            .setTitle(getString(R.string.dialog_title))
            .setPositiveButton(getString(R.string.dialog_ok)){ _, _ ->
                activity!!.toast("Añadiendio Comentario")
                val textRate = view.findViewById<EditText>(R.id.editTextRateFeedback).text.toString()
                Log.w("RateAdapter","Textrate: $textRate")
                if(textRate.isNotEmpty()){
                    val imgURL = currentUser.photoUrl?.toString() ?: run {""}
                    val rate = Rate(currentUser.uid,textRate, view.ratingBarFeedback.rating,Date(),imgURL)
                    Log.w("Dialog",view.ratingBarFeedback.rating.toString())
                    RxBus.publish(NewRateEvent(rate))
                }
             }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ ->
                activity!!.toast("Comentario Cancelado :(")
            }

        return builder.create()

    }

    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }
}