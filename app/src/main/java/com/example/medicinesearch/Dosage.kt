package com.example.medicinesearch

data class Dosage(
    val medicineId: Int,
    val form: String,
    val dosagePerKg: Double?,
    val dosagePerAge: Double?,
    val dosagePerBmi: Double?,
    val frequency: String,
    val takingConditions: String
)