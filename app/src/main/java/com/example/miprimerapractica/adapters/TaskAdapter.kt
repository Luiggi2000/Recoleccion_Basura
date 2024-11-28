package com.example.miprimerapractica.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.miprimerapractica.R
import com.example.miprimerapractica.models.Task

class TaskAdapter(
    private val tasks: List<Task>,
    private val onTaskAction: (Task, String) -> Unit // Acción: Asignar/Actualizar
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.tvDescription)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val assignButton: Button = view.findViewById(R.id.btnAssign)
        val completeButton: Button = view.findViewById(R.id.btnComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.description.text = task.description
        holder.status.text = "Estado: ${task.status}"

        // Mostrar u ocultar botones según el estado
        holder.assignButton.visibility = if (task.status == "Pendiente") View.VISIBLE else View.GONE
        holder.completeButton.visibility = if (task.status == "En progreso") View.VISIBLE else View.GONE

        // Acciones de los botones
        holder.assignButton.setOnClickListener { onTaskAction(task, "Asignar") }
        holder.completeButton.setOnClickListener { onTaskAction(task, "Completar") }
    }

    override fun getItemCount() = tasks.size
}
