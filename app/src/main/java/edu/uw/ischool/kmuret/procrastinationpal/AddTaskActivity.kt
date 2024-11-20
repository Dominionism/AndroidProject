package edu.uw.ischool.kmuret.procrastinationpal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var taskName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)
        taskName = findViewById(R.id.taskNameEditText)

        val calendar = Calendar.getInstance()

        // Date Picker (that also allows past dates)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // set the calendar
                calendar.set(year, month, dayOfMonth)
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                dateEditText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // open DatePicker when the EditText is clicked
        dateEditText.setOnClickListener {
            datePickerDialog.show()
        }

        // Time Picker
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                // Set the calendar
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val selectedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
                timeEditText.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // Use false for 12-hour format
        )

        // open TimePicker when the EditText is clicked
        timeEditText.setOnClickListener {
            timePickerDialog.show()
        }

        // save button
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            // Combine date and time into a timestamp
            val selectedTimestamp = calendar.timeInMillis
            val task = Task(
                name = taskName.text.toString(),
                deadlineTimestamp = selectedTimestamp,
                isCompleted = false
            )
            // send the task back to main
            val resultIntent = Intent()
            resultIntent.putExtra("task", task)
            setResult(RESULT_OK, resultIntent)

            Toast.makeText(this, "Task Created!", Toast.LENGTH_SHORT).show()
            finish() // close
        }
    }
}
