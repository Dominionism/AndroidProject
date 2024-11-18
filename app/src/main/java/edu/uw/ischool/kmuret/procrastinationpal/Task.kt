package edu.uw.ischool.kmuret.procrastinationpal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val name: String,
    val deadlineTimestamp: Long,
    var isCompleted: Boolean
) : Parcelable

