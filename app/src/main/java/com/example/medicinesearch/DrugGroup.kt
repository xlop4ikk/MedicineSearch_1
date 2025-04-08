package com.example.medicinesearch

data class DrugGroup(
    val name: String,
    val drugList: List<Drug>,
    var isExpanded: Boolean = false
)