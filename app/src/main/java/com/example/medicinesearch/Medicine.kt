package com.example.medicinesearch

data class Medicine(
    val name: String,
    val category: String,
    val minAge: Int,
    val prohibitedForPregnant: Boolean,
    val isCalculated: Boolean,
    val fixedDosageAdult: String?,
    val fixedDosageChild: String?,
    val calcDosagePerKgAdult: Double?,
    val calcDosagePerKgChild: Double?,
    val maxDosage: Double?,  // 🔹 Новое поле
    val instruction: String,
    val prohibitedDiseases: String?
)