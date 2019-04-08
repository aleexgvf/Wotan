package com.example.desarrollador.museo_ar.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desarrollador.museo_ar.R
import com.example.desarrollador.museo_ar.Utils.CircleTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info.view.*


class infoFragment : Fragment() {

    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    private fun setUpCurrentUser()
    {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI()
    {
        _view.textViewName.text = currentUser.displayName?.let{currentUser.displayName} ?:
                kotlin.run { getString(R.string.info_no_name) }
        _view.textViewEmail.text = currentUser.email
        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).resize(300,300)
                .centerCrop().transform(CircleTransform()).into(_view.textViewProfileImage)
        } ?: run {
            Picasso.get().load(R.drawable.avatar).resize(300,300)
                .centerCrop().transform(CircleTransform()).into(_view.textViewProfileImage)
        }
    }
}
