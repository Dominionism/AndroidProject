package edu.uw.ischool.kmuret.procrastinationpal

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChartActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        pieChart = findViewById(R.id.pieChart)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // get task list from main
        val taskList = intent.getParcelableArrayListExtra<Task>("tasks") ?: return

        // calculate data
        val completedTasks = taskList.count { it.isCompleted }
        val pendingTasks = taskList.size - completedTasks

        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(completedTasks.toFloat(), "Completed"))
        entries.add(PieEntry(pendingTasks.toFloat(), "Pending"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            resources.getColor(R.color.green, null), //completed
            resources.getColor(R.color.dark_blue, null)  //pending
        )
        dataSet.valueTextSize = 16f

        val pieData = PieData(dataSet)
        pieChart.data = pieData

        // Customize chart appearance
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.setEntryLabelTextSize(14f)
        pieChart.centerText = "Task Progress"
        pieChart.setCenterTextSize(18f)
        pieChart.animateY(1000)
        pieChart.setExtraOffsets(50f, 0f, 50f, 0f)
        val legend = pieChart.legend
        legend.textSize = 18f
    }
}
