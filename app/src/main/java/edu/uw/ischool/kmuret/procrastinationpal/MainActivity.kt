package edu.uw.ischool.kmuret.procrastinationpal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var pieChartButton: ImageButton

    private val taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize views
        todoRecyclerView = findViewById(R.id.todoRecyclerView)
        fabAddTask = findViewById(R.id.fabAddTask)
        pieChartButton = findViewById(R.id.pieChartButton)
        taskAdapter = TaskAdapter(taskList)
        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.adapter = taskAdapter

        // floating add new task button
        fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TASK)
        }

        // open pie chart page
        pieChartButton.setOnClickListener {
            val intent = Intent(this, PieChartActivity::class.java)
            intent.putParcelableArrayListExtra("tasks", ArrayList(taskList))
            startActivity(intent)
        }

        // Load testing tasks (change later when storage is figured out)
        loadDummyTasks()
    }

    // Handle result from AddTaskActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            val newTask = data?.getParcelableExtra<Task>("task")
            newTask?.let {
                taskList.add(it)  // Add the new task to the list
                taskAdapter.notifyDataSetChanged()  // update the adapter to display the new task
            }
        }
    }

    // example testing data
    private fun loadDummyTasks() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()) // Corrected format
        taskList.add(Task("Finish assignment", dateFormat.parse("2024-11-20 03:00 PM")?.time ?: 0L, false))
        taskList.add(Task("Grocery shopping", dateFormat.parse("2024-11-18 10:30 AM")?.time ?: 0L, false))
        taskList.add(Task("Plan meeting", dateFormat.parse("2024-11-19 01:15 PM")?.time ?: 0L, true))
        taskAdapter.notifyDataSetChanged()
    }



    companion object {
        const val REQUEST_CODE_ADD_TASK = 1
    }
}
