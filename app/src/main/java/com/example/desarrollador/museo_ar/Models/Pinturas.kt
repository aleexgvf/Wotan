package com.example.desarrollador.museo_ar.Models

class Pinturas {

    var Name : String? = null
    var Desc : String? = null
    var Imagen: String? = null
    var Rating: Float = 0f

    constructor():this("","","",0f) {

    }


    constructor(Name: String?, Desc: String?,Imagen: String?, Rating: Float) {
        this.Name = Name
        this.Desc = Desc
        this.Imagen = Imagen
        this.Rating = Rating
    }
}