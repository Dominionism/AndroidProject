package edu.uw.ischool.kmuret.procrastinationpal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
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
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    @SuppressLint("NotifyDataSetChanged")
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

    companion object {
        const val REQUEST_CODE_ADD_TASK = 1
    }
}