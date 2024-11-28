package com.example.miprimerapractica.models

import com.google.firebase.firestore.GeoPoint

data class Task(
    val id: String = "",
    val description: String = "",
    val assignedTo: String? = null,
    val status: String = "Pendiente",
    val location: GeoPoint? = null,
    val imageUrl: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
