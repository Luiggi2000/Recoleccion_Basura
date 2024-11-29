package com.example.miprimerapractica.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.miprimerapractica.R
import com.example.miprimerapractica.models.Report
import com.google.firebase.firestore.FirebaseFirestore

class ReportDetailFragment : Fragment() {
    private lateinit var tvDescription: TextView
    private lateinit var ivImage: ImageView
    private lateinit var btnMarkAsResolved: Button
    private lateinit var report: Report

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_detail, container, false)

        // Obtener el reporte pasado como argumento
        report = arguments?.getParcelable("REPORT") ?: return view

        // Inicializar vistas
        tvDescription = view.findViewById(R.id.tvDescription)
        ivImage = view.findViewById(R.id.ivImage)
        btnMarkAsResolved = view.findViewById(R.id.btnMarkAsResolved)

        // Mostrar los detalles del reporte
        tvDescription.text = report.description
        Glide.with(requireContext()).load(report.imageUrl).into(ivImage)

        // Acción para marcar como resuelto
        btnMarkAsResolved.setOnClickListener {
            markReportAsResolved(report)
        }

        return view
    }

    private fun markReportAsResolved(report: Report) {
        val db = FirebaseFirestore.getInstance()

        // Cambiar el estado del reporte en la base de datos
        db.collection("reports")
            .document(report.id)  // Asume que 'id' es el campo único de identificación
            .update("status", "resolved")  // Cambiar el campo 'status' en Firestore
            .addOnSuccessListener {
                Toast.makeText(context, "Reporte marcado como resuelto", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al actualizar el reporte", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        // Método estático para crear el fragmento con el reporte
        fun newInstance(report: Report): ReportDetailFragment {
            val fragment = ReportDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable("REPORT", report)
            fragment.arguments = bundle
            return fragment
        }
    }
}
