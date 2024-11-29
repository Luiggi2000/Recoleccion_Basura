package com.example.miprimerapractica.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.miprimerapractica.R
import com.example.miprimerapractica.adapters.ReportAdapter
import com.example.miprimerapractica.models.Report
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.QuerySnapshot


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

        recyclerView = view.findViewById(R.id.recyclerReports)
        recyclerView.layoutManager = LinearLayoutManager(context)
        reportList = mutableListOf()

        // Aquí le pasamos el listener de clic
        reportAdapter = ReportAdapter(reportList) { report ->
            openReportDetails(report)  // Llamamos al método que maneja la acción del clic
        }

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
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)  // Asegúrate de que R.id.fragment_container sea el contenedor de tus fragmentos
            ?.addToBackStack(null)  // Añadir a la pila de retroceso
            ?.commit()
    }
}
