package com.example.flexiwidget.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class VolumeUpdateWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Fetch current device volume and update all volume widgets
        VolumeWidget1x2().updateAll(context)
        VolumeWidget2x1().updateAll(context)
        return Result.success()
    }
}
