package com.example.fitnesstracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var totalSteps = 0
    private var previousTotalSteps = 0
    private var stepOffset = 0

    private lateinit var tvSteps: TextView
    private lateinit var tvDistance: TextView
    private lateinit var tvCalories: TextView
    private lateinit var btnHistory: Button
    private lateinit var btnReset: Button

    private lateinit var sharedPrefs: SharedPreferences

    private val PREFS_NAME = "stepPrefs"
    private val KEY_PREV_TOTAL = "prev_total"
    private val KEY_OFFSET = "step_offset"
    private val KEY_LAST_DAY = "last_day"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSteps = findViewById(R.id.tvSteps)
        tvDistance = findViewById(R.id.tvDistance)
        tvCalories = findViewById(R.id.tvCalories)
        btnHistory = findViewById(R.id.btnHistory)
        btnReset = findViewById(R.id.btnReset)

        sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        checkPermission()
        restoreSavedSteps()
        resetDailyIfNeeded()
        updateUI(totalSteps)

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnReset.setOnClickListener {
            resetSteps()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    1
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        stepSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        saveSteps()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            if (previousTotalSteps == 0) previousTotalSteps = event.values[0].toInt()

            totalSteps = event.values[0].toInt() - previousTotalSteps - stepOffset
            if (totalSteps < 0) totalSteps = 0

            updateUI(totalSteps)
        }
    }

    private fun updateUI(steps: Int) {
        tvSteps.text = "Steps: $steps"
        tvDistance.text = String.format("Distance: %.2f km", steps * 0.0008)
        tvCalories.text = String.format("Calories: %.1f kcal", steps * 0.04)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun resetSteps() {
        stepOffset += totalSteps
        totalSteps = 0
        updateUI(0)
        saveSteps()
    }

    private fun restoreSavedSteps() {
        // Load previously saved total and offset
        previousTotalSteps = sharedPrefs.getInt(KEY_PREV_TOTAL, 0)
        stepOffset = sharedPrefs.getInt(KEY_OFFSET, 0)
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        totalSteps = sharedPrefs.getInt("day_$today", 0)
    }

    private fun saveSteps() {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        sharedPrefs.edit()
            .putInt(KEY_PREV_TOTAL, previousTotalSteps)
            .putInt(KEY_OFFSET, stepOffset)
            .putInt("day_$today", totalSteps)
            .apply()
    }

    private fun resetDailyIfNeeded() {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val lastSavedDay = sharedPrefs.getInt(KEY_LAST_DAY, -1)

        if (today != lastSavedDay) {
            sharedPrefs.edit()
                .putInt("day_$today", 0)
                .putInt(KEY_LAST_DAY, today)
                .apply()
            totalSteps = 0
            stepOffset = 0
            previousTotalSteps = 0
        }
    }
}
