package com.example.desarrollador.museo_ar.dialogues

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import com.example.desarrollador.museo_ar.extension.toast
import com.example.desarrollador.museo_ar.models.NewRateEvent
import com.example.desarrollador.museo_ar.models.Rate
import com.example.desarrollador.museo_ar.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import com.example.desarrollador.museo_ar.utils.RxBus
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_rate.*



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
                activity!!.toast("Pressed Ok")
                val textRate = view.findViewById<EditText>(R.id.editTextRateFeedback).text.toString()
                if(textRate.isNotEmpty()){
                    val imgURL = currentUser.photoUrl?.toString() ?: run {""}
                    val rate = Rate(currentUser.uid,textRate, view.ratingBarFeedbackDialog!!.rating,Date(),imgURL)
                    RxBus.publish(NewRateEvent(rate))
                }
             }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ ->
                activity!!.toast("Pressed Cancel")
            }

        return builder.create()

    }

    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }
}