package com.example.medicinesearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class HealthInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_input)

        val category = intent.getStringExtra("CATEGORY") ?: "Общая диагностика"
        findViewById<TextView>(R.id.tv_category).text = category

        val etAge = findViewById<EditText>(R.id.et_age)
        val etWeight = findViewById<EditText>(R.id.et_weight)
        val etHeight = findViewById<EditText>(R.id.et_height)
        val etPressure = findViewById<EditText>(R.id.et_pressure)
        val etSugar = findViewById<EditText>(R.id.et_sugar)
        val etCholesterol = findViewById<EditText>(R.id.et_cholesterol)
        val etHeartRate = findViewById<EditText>(R.id.et_heart_rate)
        val spinnerGender = findViewById<Spinner>(R.id.spinner_gender)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)
        val btnBack = findViewById<Button>(R.id.btn_back)
        val tvRecommendation = findViewById<TextView>(R.id.tv_recommendation)

        // Настройка спиннера выбора пола
        val genderOptions = listOf("Мужской", "Женский")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)
        spinnerGender.adapter = adapter

        // Скрываем ненужные поля
        etPressure.visibility = View.GONE
        etSugar.visibility = View.GONE
        etCholesterol.visibility = View.GONE
        etHeartRate.visibility = View.GONE

        when (category) {
            "Проблемы с давлением" -> etPressure.visibility = View.VISIBLE
            "Диабет" -> etSugar.visibility = View.VISIBLE
            "Холестерин" -> etCholesterol.visibility = View.VISIBLE
            "Проблемы с сердцем" -> etHeartRate.visibility = View.VISIBLE
        }

        btnSubmit.setOnClickListener {
            val age = etAge.text.toString().toIntOrNull()
            val weight = etWeight.text.toString().toFloatOrNull()
            val height = etHeight.text.toString().toFloatOrNull()
            val gender = spinnerGender.selectedItem.toString()
            val pressure = etPressure.text.toString()
            val sugar = etSugar.text.toString().toFloatOrNull()
            val cholesterol = etCholesterol.text.toString().toFloatOrNull()
            val heartRate = etHeartRate.text.toString().toIntOrNull()

            if (age == null || weight == null || height == null ||
                (category == "Проблемы с давлением" && pressure.isEmpty()) ||
                (category == "Диабет" && sugar == null) ||
                (category == "Холестерин" && cholesterol == null) ||
                (category == "Проблемы с сердцем" && heartRate == null)
            ) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Сохранение в базу данных
            val db = HealthData(this, null)
            db.saveHealthData(1, age, weight, height, pressure, sugar ?: 0.0f)

            // Генерация рекомендации
            val recommendation = generateRecommendation(category, age, weight, height, gender, pressure, sugar, cholesterol, heartRate)
            tvRecommendation.text = recommendation
            tvRecommendation.visibility = View.VISIBLE

            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show()
        }

        // Кнопка назад
        btnBack.setOnClickListener {
            startActivity(Intent(this, RecommendedActivity::class.java))
            finish()
        }
    }

    private fun generateRecommendation(
        category: String,
        age: Int,
        weight: Float,
        height: Float,
        gender: String,
        pressure: String,
        sugar: Float?,
        cholesterol: Float?,
        heartRate: Int?
    ): String {
        return when (category) {
            "Проблемы с давлением" -> {
                val systolic = pressure.split("/").getOrNull(0)?.toIntOrNull() ?: 0
                val diastolic = pressure.split("/").getOrNull(1)?.toIntOrNull() ?: 0
                when {
                    systolic > 140 || diastolic > 90 -> "У вас повышенное давление. Советы:\n" +
                            "- Ограничьте соль в рационе\n" +
                            "- Увеличьте физическую активность\n" +
                            "- Снижайте уровень стресса\n" +
                            "- Контролируйте вес\n" +
                            "- Обратитесь к кардиологу"
                    systolic < 90 || diastolic < 60 -> "У вас пониженное давление. Советы:\n" +
                            "- Пейте больше воды\n" +
                            "- Ешьте соленую пищу\n" +
                            "- Избегайте длительного стояния\n" +
                            "- Посетите терапевта"
                    else -> "Ваше давление в норме."
                }
            }
            "Диабет" -> {
                when {
                    sugar != null && sugar > 7.0 -> "Высокий уровень сахара. Рекомендации:\n" +
                            "- Сократите углеводы\n" +
                            "- Увеличьте физическую активность\n" +
                            "- Контролируйте вес\n" +
                            "- Регулярно измеряйте сахар\n" +
                            "- Обратитесь к эндокринологу"
                    sugar != null && sugar < 3.5 -> "Слишком низкий уровень сахара. Советы:\n" +
                            "- Ешьте чаще\n" +
                            "- Держите сладости при себе\n" +
                            "- Контролируйте уровень глюкозы\n" +
                            "- Обратитесь к врачу"
                    else -> "Ваш уровень сахара в норме."
                }
            }
            "Холестерин" -> {
                when {
                    cholesterol != null && cholesterol > 6.2 -> "Высокий холестерин. Советы:\n" +
                            "- Сократите жирную пищу\n" +
                            "- Добавьте больше овощей\n" +
                            "- Увеличьте физическую активность\n" +
                            "- Проверьте уровень холестерина через 6 месяцев\n" +
                            "- Посетите терапевта"
                    else -> "Уровень холестерина в норме."
                }
            }
            "Проблемы с сердцем" -> {
                when {
                    heartRate != null && heartRate > 100 -> "Высокий пульс. Советы:\n" +
                            "- Избегайте стрессов\n" +
                            "- Ограничьте кофе и алкоголь\n" +
                            "- Контролируйте давление\n" +
                            "- Регулярно отдыхайте\n" +
                            "- Посетите кардиолога"
                    heartRate != null && heartRate < 50 -> "Низкий пульс. Советы:\n" +
                            "- Следите за самочувствием\n" +
                            "- Увеличьте физическую активность\n" +
                            "- Обратитесь к врачу, если есть слабость"
                    else -> "Ваш пульс в норме."
                }
            }
            else -> "Нет данных для рекомендации."
        }
    }
}