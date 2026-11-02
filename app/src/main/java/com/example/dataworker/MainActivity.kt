package com.example.dataworker


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {
    private lateinit var workManager: WorkManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(this)
        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= 33) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }


        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {


            val saveRequest =
                OneTimeWorkRequestBuilder<SaveDataWorker>()
                    .build()

            workManager.enqueueUniqueWork(
                "save_data",
                ExistingWorkPolicy.KEEP,
                saveRequest
            )
            Log.d("MainActivity", "Tâche de sauvegarde lancée")
            workManager.getWorkInfoByIdLiveData(saveRequest.id)
                .observe(this) { workInfo ->
                    workInfo?.let {
                        when (it.state) {
                            WorkInfo.State.ENQUEUED -> {
                                Log.d("WorkStatus", "Tâche en attente")
                            }
                            WorkInfo.State.RUNNING -> {
                                Log.d("WorkStatus", "Sauvegarde en cours...")
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Log.d("WorkStatus", "Sauvegarde réussie !")
                            }
                            WorkInfo.State.FAILED -> {
                                Log.d("WorkStatus", "Échec de la sauvegarde")
                            }
                            WorkInfo.State.CANCELLED -> {
                                Log.d("WorkStatus", "Tâche annulée")
                            }
                            WorkInfo.State.BLOCKED -> {
                                Log.d("WorkStatus", "Tâche bloquée")
                            }
                        }
                    }
                }
        }
    }
}
