package edu.uw.ischool.kmuret.procrastinationpal

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
    private lateinit var settingsButton: ImageButton
    private lateinit var konfettiView: KonfettiView
    private lateinit var motivation: ImageButton

    private var taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

//    private var workManager: WorkManager? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskList = loadTasks(this)

        todoRecyclerView = findViewById(R.id.todoRecyclerView)
        fabAddTask = findViewById(R.id.fabAddTask)
        pieChartButton = findViewById(R.id.pieChartButton)
        settingsButton = findViewById(R.id.settingButton)
        konfettiView = findViewById(R.id.konfettiView)
        motivation = findViewById(R.id.motivation_button)

        taskAdapter = TaskAdapter(
            taskList,
            onTaskCheckedChanged = { position, isChecked ->
                taskList[position].isCompleted = isChecked
                if (isChecked) {
                    showConfetti()
                }
            },
            onDelete = { _, task ->
                saveTasks(taskList, this)
                Toast.makeText(this, "Deleted: ${task?.name}", Toast.LENGTH_SHORT).show()
            }
        )

        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.adapter = taskAdapter

        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT
            ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                taskAdapter.removeTask(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val paint = Paint()

                paint.color = Color.RED

                if (dX < 0) {
                    c.drawRect(
                        itemView.right.toFloat() + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(),
                        paint
                    )
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        })

        itemTouchHelper.attachToRecyclerView(todoRecyclerView)

        fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TASK)
        }

        pieChartButton.setOnClickListener {
            val intent = Intent(this, PieChartActivity::class.java)
            intent.putParcelableArrayListExtra("tasks", ArrayList(taskList))
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        motivation.setOnClickListener {
            val intent = Intent(this, Motivation::class.java)
            startActivity(intent)
        }
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
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            val newTask = data?.getParcelableExtra<Task>("task")
            newTask?.let {
                taskList.add(it)
                taskAdapter.notifyDataSetChanged()
                saveTasks(taskList, this)
            }
            val workRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(16, TimeUnit.MINUTES).build()
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "TaskReminder",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }

    companion object {
        const val REQUEST_CODE_ADD_TASK = 1
    }
}
