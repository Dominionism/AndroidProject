package edu.uw.ischool.kmuret.procrastinationpal

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class EditTaskActivity : AppCompatActivity() {
    private lateinit var editTaskName: EditText
    private lateinit var editTaskDeadline: Button
    private lateinit var saveTaskButton: Button
    private lateinit var cancelButton: Button

    private var selectedTimestamp: Long = 0
    private var isCompleted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        editTaskName = findViewById(R.id.editTaskName)
        editTaskDeadline = findViewById(R.id.editTaskDeadline)
        saveTaskButton = findViewById(R.id.saveTaskButton)
        cancelButton = findViewById(R.id.cancelButton)

        val task = intent.getParcelableExtra<Task>("task")

        task?.let {
            editTaskName.setText(it.name)
            selectedTimestamp = it.deadlineTimestamp
            isCompleted = it.isCompleted

            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            editTaskDeadline.text = dateFormat.format(Date(selectedTimestamp))
        }

        editTaskDeadline.setOnClickListener {
            showDateTimePicker()
        }

        saveTaskButton.setOnClickListener {
            val taskName = editTaskName.text.toString().trim()

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedTimestamp == 0L) {
                Toast.makeText(this, "Please select a deadline", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTask = Task(taskName, selectedTimestamp, isCompleted)

            val resultIntent = Intent().apply {
                putExtra("task", updatedTask)
                putExtra("position", intent.getIntExtra("position", -1))
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePicker = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        selectedTimestamp = calendar.timeInMillis

                        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                        editTaskDeadline.text = dateFormat.format(calendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                timePicker.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}