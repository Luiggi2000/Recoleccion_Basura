package com.example.miprimerapractica.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.miprimerapractica.R
import com.example.miprimerapractica.models.Report

class ReportAdapter(
    private val reports: List<Report>,
    private val onReportClick: (Report) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.tvDescription)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val image: ImageView = view.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.description.text = report.description
        holder.status.text = report.status

        // Cargar imagen desde la URL usando Glide o Picasso
        Glide.with(holder.itemView.context)
            .load(report.imageUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener { onReportClick(report) }
    }

    override fun getItemCount() = reports.size
}
