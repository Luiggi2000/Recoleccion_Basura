package com.example.miprimerapractica.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miprimerapractica.R
import com.example.miprimerapractica.adapters.ReportAdapter
import com.example.miprimerapractica.models.Report
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var reportList: MutableList<Report>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerReports)
        recyclerView.layoutManager = LinearLayoutManager(context)
        reportList = mutableListOf()

        // Configurar el adaptador con listeners para clics y cambio de estado
        reportAdapter = ReportAdapter(reportList,
            onItemClick = { report ->
                openReportDetails(report) // Llama al método para mostrar los detalles
            },
            onStatusChange = { report, newStatus ->
                updateReportStatus(report, newStatus) // Actualiza el estado del reporte
            }
        )
        recyclerView.adapter = reportAdapter

        // Obtener los reportes desde Firestore
        getReportsFromFirestore()

        return view
    }

    private fun getReportsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("reports")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val report = document.toObject(Report::class.java)
                        reportList.add(report)
                    }
                    reportAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "No hay reportes disponibles", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al cargar los reportes", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openReportDetails(report: Report) {
        // Crear una instancia del fragmento de detalles del reporte
        val fragment = ReportDetailFragment.newInstance(report)

        // Reemplazar el fragmento actual por el fragmento de detalles
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)  // Asegúrate de que R.id.fragment_container sea el contenedor de tus fragmentos
            .addToBackStack(null)  // Añadir a la pila de retroceso
            .commit()
    }

    private fun updateReportStatus(report: Report, newStatus: String) {
        val db = FirebaseFirestore.getInstance()

        // Actualizar el estado del reporte en Firestore
        db.collection("reports")
            .document(report.id)
            .update("status", newStatus)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "El estado del reporte se actualizó a $newStatus",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al actualizar el estado del reporte", Toast.LENGTH_SHORT).show()
            }
    }
}
