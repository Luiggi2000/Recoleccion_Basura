package com.example.miprimerapractica.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class Report(
    val id: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val status: String = "",
    val location: GeoPoint? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        imageUrl = parcel.readString() ?: "",
        status = parcel.readString() ?: "",
        location = if (parcel.readByte() == 1.toByte()) {
            val latitude = parcel.readDouble()
            val longitude = parcel.readDouble()
            GeoPoint(latitude, longitude)
        } else {
            null
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
        parcel.writeString(status)

        // Manejo de GeoPoint
        if (location != null) {
            parcel.writeByte(1)  // Indicar que el GeoPoint no es nulo
            parcel.writeDouble(location.latitude)  // Guardar latitud
            parcel.writeDouble(location.longitude)  // Guardar longitud
        } else {
            parcel.writeByte(0)  // Indicar que el GeoPoint es nulo
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Report> {
        override fun createFromParcel(parcel: Parcel): Report {
            return Report(parcel)
        }

        override fun newArray(size: Int): Array<Report?> {
            return arrayOfNulls(size)
        }
    }
}
