package com.example.desarrollador.museo_ar.models


class Secciones {
    var Image: String? = null
    var Name : String? = null


    constructor():this("","") {

    }


    constructor(Image: String?, Name: String?) {
        this.Image = Image
        this.Name = Name
    }
}