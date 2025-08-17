package com.example.fitnesstracker

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var btnReset: Button
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        barChart = findViewById(R.id.barChart)
        btnReset = findViewById(R.id.btnReset)
        sharedPrefs = getSharedPreferences("stepPrefs", Context.MODE_PRIVATE)

        setupChart()

        btnReset.setOnClickListener {
            resetSteps()
        }
    }

    private fun setupChart() {
        val entries = ArrayList<BarEntry>()
        val labels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        for (i in 1..7) {
            val steps = sharedPrefs.getInt("day_$i", 0)
            entries.add(BarEntry((i - 1).toFloat(), steps.toFloat()))
        }

        val dataSet = BarDataSet(entries, "Steps")
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        // Highlight current day
        val calendar = Calendar.getInstance()
        val todayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
        dataSet.colors = entries.mapIndexed { index, _ ->
            if (index == todayIndex) Color.parseColor("#87CEEB") else Color.BLUE
        }

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.data = barData
        barChart.invalidate()
    }

    private fun resetSteps() {
        val editor = sharedPrefs.edit()
        for (i in 1..7) editor.putInt("day_$i", 0)
        editor.apply()

        setupChart()
        Toast.makeText(this, "Steps reset!", Toast.LENGTH_SHORT).show()
    }
}
