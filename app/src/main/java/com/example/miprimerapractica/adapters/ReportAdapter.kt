package com.example.miprimerapractica.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.miprimerapractica.R
import com.example.miprimerapractica.models.Report

class ReportAdapter(
    private val reportList: List<Report>, // Lista de reportes
    private val onItemClick: (Report) -> Unit, // Callback para clic en un reporte
    private val onStatusChange: (Report, String) -> Unit // Callback para cambiar estado
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]

        // Configurar vistas
        holder.descriptionTextView.text = report.description
        Glide.with(holder.itemView.context).load(report.imageUrl).into(holder.reportImageView)

        // Acción al hacer clic en el ítem
        holder.itemView.setOnClickListener {
            onItemClick(report) // Llama al callback con el reporte actual
        }

        // Acción al hacer clic en el botón de cambiar estado
        holder.btnMarkAsResolved.setOnClickListener {
            onStatusChange(report, "resolved") // Llama al callback para cambiar estado a "resolved"
        }
    }

    override fun getItemCount(): Int = reportList.size

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        val reportImageView: ImageView = itemView.findViewById(R.id.ivRImage)
        val btnMarkAsResolved: Button = itemView.findViewById(R.id.btnMarkAsResolved)
    }
}
