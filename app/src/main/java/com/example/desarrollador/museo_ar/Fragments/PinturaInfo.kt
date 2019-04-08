package com.example.desarrollador.museo_ar.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.desarrollador.museo_ar.R
import com.google.firebase.database.DatabaseReference

class PinturaInfo : Fragment() {

    private lateinit var _view: View
    lateinit var mrecylerview : RecyclerView
    lateinit var ref: DatabaseReference
    lateinit var show_progress: ProgressBar
    lateinit var mbackButton: ImageView
    lateinit var pathString: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)


        return _view


    }

}
