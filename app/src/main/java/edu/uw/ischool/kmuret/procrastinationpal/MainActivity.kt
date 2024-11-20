package edu.uw.ischool.kmuret.procrastinationpal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nl.dionsegijn.konfetti.xml.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var pieChartButton: ImageButton
    private lateinit var konfettiView: KonfettiView

    private val taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize views
        todoRecyclerView = findViewById(R.id.todoRecyclerView)
        fabAddTask = findViewById(R.id.fabAddTask)
        pieChartButton = findViewById(R.id.pieChartButton)
        konfettiView = findViewById(R.id.konfettiView)

        taskAdapter = TaskAdapter(taskList) { position, isChecked ->
            taskList[position].isCompleted = isChecked
            if (isChecked) {
                showConfetti()
            }
        }

        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.adapter = taskAdapter

        // floating add new task button
        fabAddTask.setOnClickListener {
            if (hasCompletedTasks()) {
                showConfetti()
            }
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TASK)
        }

        // open pie chart page
        pieChartButton.setOnClickListener {
            if (hasCompletedTasks()) {
                showConfetti()
            }
            val intent = Intent(this, PieChartActivity::class.java)
            intent.putParcelableArrayListExtra("tasks", ArrayList(taskList))
            startActivity(intent)
        }

        // Load testing tasks
        loadDummyTasks()
    }

    private fun hasCompletedTasks(): Boolean {
        return taskList.any { it.isCompleted }
    }

    private fun showConfetti() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        konfettiView.start(party)
    }

    // Handle result from AddTaskActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            val newTask = data?.getParcelableExtra<Task>("task")
            newTask?.let {
                taskList.add(it)
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadDummyTasks() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        taskList.add(Task("Finish assignment", dateFormat.parse("2024-11-20 03:00 PM")?.time ?: 0L, false))
        taskList.add(Task("Grocery shopping", dateFormat.parse("2024-11-18 10:30 AM")?.time ?: 0L, false))
        taskList.add(Task("Plan meeting", dateFormat.parse("2024-11-19 01:15 PM")?.time ?: 0L, true))
        taskAdapter.notifyDataSetChanged()
    }

    companion object {
        const val REQUEST_CODE_ADD_TASK = 1
    }
}