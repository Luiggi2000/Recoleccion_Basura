package com.example.miprimerapractica.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.miprimerapractica.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID
import java.util.*


class ReportFragment : Fragment() {

    private lateinit var etDescription: EditText
    private lateinit var btnUploadImage: Button
    private lateinit var btnSubmitReport: Button
    private lateinit var ivSelectedImage: ImageView
    private lateinit var progressBar: ProgressBar

    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        etDescription = view.findViewById(R.id.etDescription)
        btnUploadImage = view.findViewById(R.id.btnUploadImage)
        btnSubmitReport = view.findViewById(R.id.btnSubmitReport)
        ivSelectedImage = view.findViewById(R.id.ivSelectedImage)
        progressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = View.GONE

        // Inicializar referencia a Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference

        // Acción para abrir la galería
        btnUploadImage.setOnClickListener { openImagePicker() }

        // Acción para enviar el reporte
        btnSubmitReport.setOnClickListener { submitReport() }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1001) // Código de solicitud para la galería
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data // Obtén la URI de la imagen seleccionada
            ivSelectedImage.setImageURI(imageUri) // Muestra la imagen en el ImageView
        }
    }

    private fun submitReport() {
        val description = etDescription.text.toString()

        if (description.isEmpty() || imageUri == null) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        // Generar un nombre único para la imagen
        val fileName = "images/${UUID.randomUUID()}.jpg"

        // Referencia al archivo en Firebase Storage
        val fileReference = storageReference.child(fileName)

        // Subir la imagen a Firebase Storage
        fileReference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // Obtener la URL de descarga de la imagen subida
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    saveReportToFirestore(description, imageUrl) // Guardar el reporte en Firestore
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveReportToFirestore(description: String, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()

        val reportData = hashMapOf(
            "description" to description,
            "imageUrl" to imageUrl,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("reports").add(reportData)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Reporte enviado con éxito", Toast.LENGTH_SHORT).show()
                etDescription.text.clear()
                ivSelectedImage.setImageResource(0)
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error al enviar reporte", Toast.LENGTH_SHORT).show()
            }
    }
}
