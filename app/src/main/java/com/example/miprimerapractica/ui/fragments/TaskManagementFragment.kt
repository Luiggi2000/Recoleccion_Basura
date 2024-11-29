package com.example.miprimerapractica.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.miprimerapractica.adapters.ReportAdapter
import com.example.miprimerapractica.models.Report
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miprimerapractica.R

class TaskManagementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var reportList: MutableList<Report>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_task_management, container, false)

        recyclerView = view.findViewById(R.id.recyclerTasks)
        recyclerView.layoutManager = LinearLayoutManager(context)
        reportList = mutableListOf()

        // Iniciamos el adaptador, pasamos los dos listeners
        reportAdapter = ReportAdapter(reportList, { report ->
            // Acción al hacer clic en un reporte, ver detalles
            openReportDetailFragment(report)
        }, { report, status ->
            // Acción para cambiar el estado del reporte
            updateReportStatus(report, status)
        })

        recyclerView.adapter = reportAdapter

        // Obtener los reportes desde Firestore
        getReportsFromFirestore()

        return view
    }

    // Método para obtener los reportes desde Firestore
    private fun getReportsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("reports")
            .get()
            .addOnSuccessListener { documents ->
                // Verifica si hay documentos en la colección
                if (documents.size() > 0) {
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


    // Método para abrir el fragmento de detalles del reporte
    private fun openReportDetailFragment(report: Report) {
        // Crea una instancia del ReportDetailFragment y pasa el reporte
        val reportDetailFragment = ReportDetailFragment.newInstance(report)

        // Navegar al fragmento de detalles
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_graph, reportDetailFragment)
            .addToBackStack(null) // Agregar a la pila de retroceso para permitir navegación
            .commit()
    }

    // Esta función se usa para actualizar el estado del reporte en la base de datos
    private fun updateReportStatus(report: Report, status: String) {
        val db = FirebaseFirestore.getInstance()

        // Actualizar el estado del reporte en Firestore
        db.collection("reports")
            .document(report.id)
            .update("status", status)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Estado del reporte actualizado a $status",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al actualizar el estado", Toast.LENGTH_SHORT).show()
            }
    }
}
