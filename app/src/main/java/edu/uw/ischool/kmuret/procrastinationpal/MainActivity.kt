package edu.uw.ischool.kmuret.procrastinationpal

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var pieChartButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var konfettiView: KonfettiView
    private lateinit var motivation: ImageButton
    private var reminderPhoneNumber: String? = null
    private var reminderInterval: Long? = null

    private var taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    private val editTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedTask = result.data?.getParcelableExtra<Task>("task")
            val position = result.data?.getIntExtra("position", -1)

            if (updatedTask != null && position != null && position != -1) {
                taskList[position] = updatedTask
                taskAdapter.notifyItemChanged(position)
                saveTasks(taskList, this)
            }
        }
    }

    private val addTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newTask = result.data?.getParcelableExtra<Task>("task")
            newTask?.let {
                taskList.add(it)
                taskAdapter.notifyItemChanged(taskList.size - 1)
                saveTasks(taskList, this)

                // Schedule periodic work for task reminders
                scheduleTaskReminders()
            }
        }
    }

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            reminderPhoneNumber = result.data?.getStringExtra("number")
            reminderInterval = result.data?.getLongExtra("interval",16)
            scheduleTaskReminders()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        taskList = loadTasks(this)

        initializeViews()
        setupTaskAdapter()
        setupItemTouchHelper()
        setupButtonListeners()
    }

    private fun initializeViews() {
        todoRecyclerView = findViewById(R.id.todoRecyclerView)
        fabAddTask = findViewById(R.id.fabAddTask)
        pieChartButton = findViewById(R.id.pieChartButton)
        settingsButton = findViewById(R.id.settingButton)
        konfettiView = findViewById(R.id.konfettiView)
        motivation = findViewById(R.id.motivation_button)
    }

    private fun setupTaskAdapter() {
        taskAdapter = TaskAdapter(
            taskList,
            onTaskCheckedChanged = { position, isChecked ->
                taskList[position].isCompleted = isChecked
                if (isChecked) {
                    showConfetti()
                }
                saveTasks(taskList, this)
            },
            onDelete = { _, task ->
                saveTasks(taskList, this)
                Toast.makeText(this, "Deleted: ${task?.name}", Toast.LENGTH_SHORT).show()
            },
            onEditTask = { task ->
                val position = taskList.indexOf(task)
                val intent = Intent(this, EditTaskActivity::class.java).apply {
                    putExtra("task", task)
                    putExtra("position", position)
                }
                editTaskLauncher.launch(intent)
            }
        )

        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.adapter = taskAdapter
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    taskAdapter.removeTask(position)
                    saveTasks(taskList, this@MainActivity)
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
                    val paint = Paint().apply {
                        color = Color.RED
                    }

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
    }

    private fun setupButtonListeners() {
        fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }

        pieChartButton.setOnClickListener {
            val intent = Intent(this, PieChartActivity::class.java).apply {
                putParcelableArrayListExtra("tasks", ArrayList(taskList))
            }
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            settingsLauncher.launch(intent)
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


    private fun scheduleTaskReminders() {
        if (reminderPhoneNumber == null) {
            Toast.makeText(this, "Phone number is missing. Set it in settings.", Toast.LENGTH_SHORT).show()
            return
        }

        val messages = listOf(
            "Hey, genius, remember that thing you said you'd do? It's still waiting. Tick-tock.",
            "If procrastination were an Olympic sport, you’d have the gold. Finish your task!",
            "Your task called. It said, 'Stop ghosting me, you slacker.'",
            "Friendly reminder: Your future self is judging you for not doing this yet.",
            "You’ll binge Netflix but can’t finish one tiny task? Priorities, please.",
            "Stop scrolling, start doing. Unless you're Googling 'how to get my life together.'",
            "Breaking News: The task is still not done. Details at 11.",
            "Don’t make me come over there and finish this for you. You won't like it.",
            "If ignoring your responsibilities were a job, you’d be employee of the month.",
            "Your to-do list just texted me. It says, 'WTF, bro?'",
            "Oh look, another hour passed. Another hour you didn’t do the thing.",
            "I swear, if avoiding tasks burned calories, you'd be ripped by now. Get it done!",
            "You absolute clown, are you allergic to productivity? Do the task already!"
        )

        val randomMessage = messages.random()

        val inputData = workDataOf(
            "phoneNumber" to reminderPhoneNumber,
            "message" to randomMessage
        )

        val workRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(reminderInterval!!, TimeUnit.MINUTES).setInputData(inputData).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "TaskReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}