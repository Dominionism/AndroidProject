package edu.uw.ischool.kmuret.procrastinationpal

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Parcelize
data class Task(
    val name: String,
    val deadlineTimestamp: Long,
    var isCompleted: Boolean
) : Parcelable

fun saveTasks(tasks: List<Task>, context: Context) {
    val sharedPreferences = context.getSharedPreferences("TasksPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val jsonString = gson.toJson(tasks)

    editor.putString("tasks", jsonString)
    editor.apply()

}

fun loadTasks(context: Context): MutableList<Task> {
    val sharedPreferences = context.getSharedPreferences("TasksPrefs", Context.MODE_PRIVATE)
    val jsonString = sharedPreferences.getString("tasks", null)

    return if (jsonString != null) {
        val gson = Gson()
        val taskListType = object : TypeToken<List<Task>>() {}.type
        gson.fromJson(jsonString, taskListType)
    } else {
        mutableListOf()
    }
}