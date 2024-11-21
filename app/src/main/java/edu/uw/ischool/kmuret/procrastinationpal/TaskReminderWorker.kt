package edu.uw.ischool.kmuret.procrastinationpal

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

class TaskReminderWorker(appContext: Context, workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage("Enter#", null, "Do your tasks man", null, null)
        return Result.success()
    }
}