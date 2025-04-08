package com.example.medicinesearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RecommendedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended)

        val btnPressure = findViewById<Button>(R.id.btn_pressure)
        val btnSugar = findViewById<Button>(R.id.btn_sugar)
        val btnCholesterol = findViewById<Button>(R.id.btn_cholesterol)
        val btnHeart = findViewById<Button>(R.id.btn_heart)

        btnPressure.setOnClickListener { startHealthInput("Проблемы с давлением") }
        btnSugar.setOnClickListener { startHealthInput("Диабет") }
        btnCholesterol.setOnClickListener { startHealthInput("Холестерин") }
        btnHeart.setOnClickListener { startHealthInput("Проблемы с сердцем") }
    }

    private fun startHealthInput(category: String) {
        val intent = Intent(this, HealthInputActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
}