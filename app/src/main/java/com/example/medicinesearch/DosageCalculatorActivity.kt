package com.example.medicinesearch

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog

class DosageCalculatorActivity : AppCompatActivity() {

    private lateinit var dbHelper: DosageDataBaseHelper
    private lateinit var searchDrug: SearchView
    private lateinit var listDrugs: ListView
    private lateinit var tvSelectedDrug: TextView
    private lateinit var cbPregnant: CheckBox
    private lateinit var btnCalculate: Button
    private lateinit var tvDosageResult: TextView
    private lateinit var tvInstruction: TextView
    private lateinit var spinnerSex: Spinner
    private var selectedDrug: String? = null
    private lateinit var drugsAdapter: ArrayAdapter<String>
    private lateinit var drugsList: List<String>
    private lateinit var btnSelectDiseases: Button
    private lateinit var tvSelectedDiseases: TextView
    private val chronicDiseasesList = arrayOf("Гастрит", "Диабет", "Гипертония", "Печеночная недостаточность", "Остеопороз", "Почечная недостаточность", "Глаукома", "Бронхиальная астма", "Аритмия", "Эпилепсия", "Сердечная недостаточность", "Кишечная непроходимость")
    private val selectedDiseases = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dosage_calculator)
        btnSelectDiseases = findViewById(R.id.btn_select_diseases)
        tvSelectedDiseases = findViewById(R.id.tv_selected_diseases)

        btnSelectDiseases.setOnClickListener {
            showDiseaseSelectionDialog()
        }

        dbHelper = DosageDataBaseHelper(this)

        spinnerSex = findViewById(R.id.spinner_sex)
        searchDrug = findViewById(R.id.search_drug)
        listDrugs = findViewById(R.id.list_drugs)
        tvSelectedDrug = findViewById(R.id.tv_selected_drug)
        cbPregnant = findViewById(R.id.cb_pregnant)
        btnCalculate = findViewById(R.id.btn_calculate)
        tvDosageResult = findViewById(R.id.tv_dosage_result)
        tvInstruction = findViewById(R.id.tv_instruction)

        setupDrugSearch()
        setupSexSpinner()

        btnCalculate.setOnClickListener {
            calculateDosage()
        }
    }
    private fun setupSexSpinner() {
        val sexOptions = listOf("Мужской", "Женский")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sexOptions)
        spinnerSex.adapter = adapter
    }
    private fun setupDrugSearch() {
        drugsList = dbHelper.getAllDrugs()
        drugsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, drugsList)
        listDrugs.adapter = drugsAdapter

        searchDrug.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && drugsList.contains(query)) {
                    selectDrug(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                drugsAdapter.filter.filter(newText)
                listDrugs.visibility = if (newText.isNullOrEmpty()) View.GONE else View.VISIBLE
                return false
            }
        })
        listDrugs.setOnItemClickListener { _, _, position, _ ->
            val selected = drugsAdapter.getItem(position)
            if (selected != null) {
                selectDrug(selected)
            }
        }
    }
    private fun selectDrug(drug: String) {
        selectedDrug = drug
        tvSelectedDrug.text = "Выбранное лекарство: $drug"
        listDrugs.visibility = View.GONE
    }
    private fun showDiseaseSelectionDialog() {
        val checkedItems = BooleanArray(chronicDiseasesList.size) { selectedDiseases.contains(chronicDiseasesList[it]) }

        AlertDialog.Builder(this)
            .setTitle("Выберите хронические заболевания")
            .setMultiChoiceItems(chronicDiseasesList, checkedItems) { _, which, isChecked ->
                if (isChecked) selectedDiseases.add(chronicDiseasesList[which]) else selectedDiseases.remove(chronicDiseasesList[which])
            }
            .setPositiveButton("OK") { _, _ ->
                tvSelectedDiseases.text = "Выбраны: ${if (selectedDiseases.isEmpty()) "Нет" else selectedDiseases.joinToString(", ")}"
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun calculateDosage() {
        val age = findViewById<EditText>(R.id.et_age).text.toString().toIntOrNull()
        val weight = findViewById<EditText>(R.id.et_weight).text.toString().toDoubleOrNull()
        val isPregnant = cbPregnant.isChecked

        if (selectedDrug == null || age == null || weight == null) {
            Toast.makeText(this, "Введите корректные данные", Toast.LENGTH_SHORT).show()
            return
        }

        val medicine = dbHelper.getMedicineByName(selectedDrug!!)
        // Проверка возраста
        if (age < medicine.minAge) {
            tvDosageResult.text = "Это лекарство противопоказано детям младше ${medicine.minAge} лет."
            return
        }
        // Проверка беременности
        if (isPregnant && medicine.prohibitedForPregnant) {
            tvDosageResult.text = "Это лекарство нельзя принимать во время беременности."
            return
        }

        val prohibitedDiseases = medicine.prohibitedDiseases?.split(", ")?.filter { it.isNotBlank() } ?: emptyList()
        val conflictingDiseases = selectedDiseases.intersect(prohibitedDiseases.toSet())

        if (conflictingDiseases.isNotEmpty()) {
            tvDosageResult.text = "Это лекарство противопоказано при: ${conflictingDiseases.joinToString(", ")}"
            return
        }

        val dosageInfo = StringBuilder()
        dosageInfo.append("Лекарство: ${medicine.name}\n")
        dosageInfo.append("Категория: ${medicine.category}\n")

        val dosage: String = if (medicine.isCalculated) {
            val calculatedDosage = if (age < 12) weight * (medicine.calcDosagePerKgChild ?: 0.0)
            else weight * (medicine.calcDosagePerKgAdult ?: 0.0)

            val maxDosage = medicine.maxDosage ?: Double.MAX_VALUE
            if (calculatedDosage > maxDosage) {
                dosageInfo.append("Максимально допустимая дозировка: ${"%.2f".format(maxDosage)} мг\n")
                "%.2f".format(maxDosage) + " мг"
            } else {
                "%.2f".format(calculatedDosage) + " мг"
            }
        } else {
            if (age < 12) medicine.fixedDosageChild ?: "Нет данных"
            else medicine.fixedDosageAdult ?: "Нет данных"
        }

        dosageInfo.append("Дозировка: $dosage\n")
        dosageInfo.append("Инструкция: ${medicine.instruction}\n")

        if (prohibitedDiseases.isNotEmpty()) {
            dosageInfo.append("\nПротивопоказания: ${prohibitedDiseases.joinToString(", ")}\n")
        }

        tvDosageResult.text = dosageInfo.toString()
    }
}