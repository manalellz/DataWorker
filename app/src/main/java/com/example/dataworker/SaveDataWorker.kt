package com.example.dataworker


import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SaveDataWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        Log.d("SaveDataWorker", "Début de la sauvegarde...")

        return try {
            Thread.sleep(3000)
            Log.d("SaveDataWorker", "Sauvegarde terminée avec succès !")
            NotificationHelper.showNotification(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Log.e("SaveDataWorker", "Erreur de sauvegarde", e)
            Result.failure()
        }
    }
}