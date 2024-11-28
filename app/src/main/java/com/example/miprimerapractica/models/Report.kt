package com.example.miprimerapractica.models

import com.google.firebase.firestore.GeoPoint

data class Report(
    val id: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val status: String = "",
    val location: GeoPoint? = null
)
