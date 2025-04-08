package com.example.medicinesearch

data class Drug(
    val name: String,
    val type: String, // Новый параметр (тип лекарства)
    val activeSubstance: String,
    val form: String // Новый параметр (форма выпуска)
)
