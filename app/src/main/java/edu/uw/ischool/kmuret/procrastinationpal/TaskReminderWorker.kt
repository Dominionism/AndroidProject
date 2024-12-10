package edu.uw.ischool.kmuret.procrastinationpal

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

class TaskReminderWorker(appContext: Context, workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val phoneNumber = inputData.getString("phoneNumber")
        val message = inputData.getString("message")

        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        return Result.success()
    }
}