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
import de.hdodenhof.circleimageview.CircleImageView


class ReportAdapter(
    private val reportList: List<Report>,
    private val onItemClick: (Report) -> Unit, // Para ver detalles
    private val onStatusChange: (Report, String) -> Unit // Para cambiar el estado
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]

        holder.descriptionTextView.text = report.description
        Glide.with(holder.itemView.context).load(report.imageUrl).into(holder.reportImageView)

        // Configurar el clic en el ítem
        holder.itemView.setOnClickListener {
            onItemClick(report)  // Pasar el reporte al listener
        }

        // Cambiar estado cuando se hace clic en el botón
        holder.btnMarkAsResolved.setOnClickListener {
            // Cambiar el estado a "resuelto"
            onStatusChange(report, "resolved")
        }
    }

    override fun getItemCount(): Int = reportList.size

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        val reportImageView: ImageView = itemView.findViewById(R.id.ivRImage)
        val btnMarkAsResolved: Button = itemView.findViewById(R.id.btnMarkAsResolved)
    }
}


