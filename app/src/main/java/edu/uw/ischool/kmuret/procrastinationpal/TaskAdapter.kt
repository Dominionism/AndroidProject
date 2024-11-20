package edu.uw.ischool.kmuret.procrastinationpal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskCheckedChanged: (position: Int, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task, position)
    }

    override fun getItemCount(): Int = taskList.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskName: TextView = itemView.findViewById(R.id.taskName)
        private val taskDeadline: TextView = itemView.findViewById(R.id.taskDeadline)
        private val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)

        fun bind(task: Task, position: Int) {
            taskName.text = task.name

            // format deadline timestamp to 12-hour AM/PM format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(task.deadlineTimestamp))
            taskDeadline.text = formattedDate

            taskCheckBox.setOnCheckedChangeListener(null)
            taskCheckBox.isChecked = task.isCompleted

            taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onTaskCheckedChanged(position, isChecked)
            }
        }
    }
}