package com.example.desarrollador.museo_ar.Models

import java.util.*


class Rate(
    val userId: String = "",
    val text: String = "",
    val rate: Float = 0f,
    val createdAt: Date = Date() ,
    val profileImgURL: String = ""
)