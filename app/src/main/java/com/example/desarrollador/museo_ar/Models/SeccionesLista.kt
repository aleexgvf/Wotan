package com.example.desarrollador.museo_ar.Models

class SeccionesLista {
    var Image: String? = null
    var Name : String? = null


    constructor():this("","") {

    }


    constructor(Image: String?, Name: String?) {
        this.Image = Image
        this.Name = Name
    }
}