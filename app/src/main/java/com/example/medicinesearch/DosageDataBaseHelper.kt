package com.example.medicinesearch

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getDoubleOrNull


class DosageDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "medicines.db"
        private const val DATABASE_VERSION = 8

        const val TABLE_MEDICINES = "medicines"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_MIN_AGE = "min_age"
        const val COLUMN_PROHIBITED_FOR_PREGNANT = "prohibited_for_pregnant"
        const val COLUMN_IS_CALCULATED = "is_calculated"
        const val COLUMN_FIXED_DOSAGE_ADULT = "fixed_dosage_adult"
        const val COLUMN_FIXED_DOSAGE_CHILD = "fixed_dosage_child"
        const val COLUMN_CALC_DOSAGE_PER_KG_ADULT = "calc_dosage_per_kg_adult"
        const val COLUMN_CALC_DOSAGE_PER_KG_CHILD = "calc_dosage_per_kg_child"
        const val COLUMN_MAX_DOSAGE = "max_dosage"
        const val COLUMN_INSTRUCTION = "instruction"
        const val COLUMN_PROHIBITED_DISEASES = "prohibited_diseases"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createMedicinesTable = """
            CREATE TABLE $TABLE_MEDICINES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_MIN_AGE INTEGER,
                $COLUMN_PROHIBITED_FOR_PREGNANT INTEGER,
                $COLUMN_IS_CALCULATED INTEGER,
                $COLUMN_FIXED_DOSAGE_ADULT TEXT,
                $COLUMN_FIXED_DOSAGE_CHILD TEXT,
                $COLUMN_CALC_DOSAGE_PER_KG_ADULT REAL,
                $COLUMN_CALC_DOSAGE_PER_KG_CHILD REAL,
                $COLUMN_MAX_DOSAGE REAL,
                $COLUMN_INSTRUCTION TEXT,
                $COLUMN_PROHIBITED_DISEASES TEXT
            )
        """.trimIndent()

        db.execSQL(createMedicinesTable)
        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEDICINES")
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val medicines = listOf(
            // Фиксированные дозировки
            Medicine("Парацетамол (таблетка)", "Жаропонижающее и обезболивающее", 12, false, false, "500 мг", "-", null, null, null,"Принимать 3-4 раза в день.", "Печеночная недостаточность"),
            Medicine("Парацетамол (суспензия)", "Жаропонижающее и обезболивающее", 1, false, false, "-", "200 мг", null, null, null,"Каждые 4-6 часов.", "Печеночная недостаточность"),
            Medicine("Эреспал", "Противовоспалительное средство", 2, true, false, "80 мг", "80 мг", null, null, null,"2-3 раза в сутки.", "Гастрит"),
            Medicine("Лоратадин", "Антигистаминное средство", 2, false, false, "10 мг", "5 мг", null, null, null,"1 раз в сутки.", "Гипертония"),
            Medicine("Омепразол", "Ингибитор протонной помпы", 12, true, false, "20-40 мг", "-", null, null, null,"1-2 раза в сутки.", "Остеопороз"),
            Medicine("Цетрин", "Антигистаминное средство", 6, false, false, "10 мг", "10 мг", null, null, null,"1 раз в сутки.", "Почечная недостаточность"),
            Medicine("Фенистил", "Антигистаминное средство", 12, false, false, "6 мг", "-", null, null, null,"разделить на 3 приема пищи в течении суток.", "Глаукома"),
            Medicine("Фенистил детский", "Антигистаминное средство", 1, false, false, "-", "-", null, 0.1, null,"пока нет инструкции для этого лекарства.", "Бронхиальная астма"),
            Medicine("Азитромицин", "Антибиотик широкого спектра", 12, false, false, "500 мг", "-", null, null, null,"1 раз в сутки.", "Аритмия"),
            Medicine("Супрастин", "Антигистаминное средство", 3, true, false, "25 мг", "12.5 мг", null, null, null,"Для взрослых 3-4 раза в сутки, для детей до 12 лет 2 раза в сутки.", "Глаукома"),
            Medicine("Диазолин", "Антигистаминное средство", 3, true, false, "100 мг", "50 мг", null, null, null,"Для взрослых 1-3 раза в сутки, для детей до 12 лет 1-2 раза в день.", "Эпилепсия"),
            Medicine("Кларитин", "Антигистаминное средство", 2, true, false, "10 мг", "10 мг", null, null, null,"1 раз в сутки.", "Диабет"),
            Medicine("Зиртек", "Антигистаминное средство", 1, true, false, "10 мг", "2,5 мг", null, null, null,"1 раз в сутки.", "Эпилепсия"),
            Medicine("Амброксол", "Муколитическое средство", 2, false, false, "30 мг", "22,5 мг", null, null, null,"Для взрослых 2-3 раза в сутки, для детей до 12 лет 1-2 раза в сутки.", "Печеночная недостаточность"),
            Medicine("Лазолван", "Муколитическое средство", 2, false, false, "4 мг", "2 мг", null, null, null,"Для взрослых 3 раза в сутки, для детей до 12 лет 2 раза в сутки.", "Сердечная недостаточность"),
            Medicine("Но-шпа", "Смазмолитик", 6, true, false, "120-240 мг", "80 мг", null, null, null,"Расчитанная доза делится на 2-3 приема пищи.", "Сердечная недостаточность"),
            Medicine("Смекта", "Противодиарейное средство", 2, true, false, "3 пакетика", "2 пакетика", null, null, null,"Для взрослых 3 пакетика в сутки, для детей до 12 лет 2 пакетика в сутки.", "Кишечная непроходимость"),
            Medicine("Линекс", "Пробиотик", 2, false, false, "120 мг", "60 мг", null, null, null,"Взрослым по две капсулы 3 раза в день, для детей до 12 лет 1 капсула 3 раза в день.", "-"),
            Medicine("Фарингосепт", "Антисептик для горла", 3, false, false, "10 мг", "10 мг", null, null, null,"Для взрослых 4-5 таблетки в сутки, для детей до 12 лет 3 таблетки в сутки.", "-"),
            Medicine("Анальгин", "Жаропонижающее и обезболивающее", 12, true, false, "500 мг", "-", null, null, null,"1-2 таблетки в сутки.", "Бронхиальная астма"),
            Medicine("Арбидол", "Противовирусное средство", 3, false, false, "200 мг", "100 мг", null, null, null,"1 раз в сутки.", "Сердечная недостаточность"),
            Medicine("Кагоцел", "Противовирусное средство", 3, true, false, "24 мг", "12 мг", null, null, null,"3 раза в сутки.", "Галактоземия"),
            Medicine("Энтерофурил", "Противомикробное средство", 3, true, false, "200 мг", "200 мг", null, null, null,"3 раза в сутки.", "Непереносимость фруктозы"),
            Medicine("Регидрон", "Регидратационное средство", 0, false, false, "1 пакетик", "1 пакетик", null, null, null,"Содержимое одного пакетика растворить в 1 литре питьевой воды и употребить в течении 24 часов.", "Диабет"),
            Medicine("Мезим", "Ферментное средство", 12, false, false, "1-2 таблетки", "-", null, null, null,"Перед едой внутрь, не разжевывая и запивая водой.", "Острый панкреатит"),
            Medicine("Мотилиум", "Противорвотное средство", 12, true, false, "10 мг", "-", null, null, null,"3 раза в сутки.", "Пролактинома"),
            Medicine("Эспумизан", "Средство от метеоризма", 6, false, false, "80 мг", "-", null, null, null,"По 2 капсулы 3-5 раз в сутки. Детям препарат применяется по назначению врача.", "Кишечная непроходимость"),
            Medicine("Бепантен", "Ранозаживающее средство", 0, false, false, "-", "-", null, null, null,"Наносить тонким слоем 2-3 раза в сутки.", "-"),
            Medicine("Терафлю", "Комбинированное средство от простуды", 12, true, false, "1 пакетик", "-", null, null, null,"3 раза в сутки.", "Диабет"),
            Medicine("Колдрекс", "Комбинированное средство от простуды", 12, true, false, "1 пакетик", "-", null, null, null,"3 раза в сутки.", "Гипертония"),
            Medicine("Ингавирин", "Противовирусное средство", 18, true, false, "90 мг", "-", null, null, null,"1 раз в сутки.", "Дефицит лактазы"),
            Medicine("Ацикловир", "Противовирусное средство", 2, false, false, "200 мг", "200 мг", null, null, null,"4-5 раз в сутки.", "Почечная недостаточность"),
            Medicine("Зовиракс", "Противовирусное средство", 2, false, false, "200 мг", "200 мг", null, null, null,"3-4 раз в сутки.", "Почечная недостаточность"),


            // Рассчитываемые дозировки
            Medicine("Ацетаминофен", "Жаропонижающее, обезболивающее", 0, false, true, "-", "-", 10.0, 15.0, 1000.0,"Максимальная суточная доза – 60 мг/кг", "Печеночная недостаточность"),
            Medicine("Ибупрофен", "Противовоспалительное", 6, false, true, "-", "-", 10.0, 10.0, 800.0,"Принимать каждые 6-8 часов", "Бронхиальная астма"),
            Medicine("Амоксициллин", "Антибиотик", 3, false, true, "-", "-", 15.0, 15.0, 1500.0,"Курс лечения 7-10 дней", "Печеночная недостаточность")
        )

        medicines.forEach { insertMedicine(db, it) }
    }

    private fun insertMedicine(db: SQLiteDatabase, medicine: Medicine) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, medicine.name)
            put(COLUMN_CATEGORY, medicine.category)
            put(COLUMN_MIN_AGE, medicine.minAge)
            put(COLUMN_PROHIBITED_FOR_PREGNANT, if (medicine.prohibitedForPregnant) 1 else 0)
            put(COLUMN_IS_CALCULATED, if (medicine.isCalculated) 1 else 0)
            put(COLUMN_FIXED_DOSAGE_ADULT, medicine.fixedDosageAdult)
            put(COLUMN_FIXED_DOSAGE_CHILD, medicine.fixedDosageChild)
            put(COLUMN_CALC_DOSAGE_PER_KG_ADULT, medicine.calcDosagePerKgAdult)
            put(COLUMN_CALC_DOSAGE_PER_KG_CHILD, medicine.calcDosagePerKgChild)
            put(COLUMN_MAX_DOSAGE, medicine.maxDosage)  // 🔹 Добавили максимальную дозировку
            put(COLUMN_INSTRUCTION, medicine.instruction)
            put(COLUMN_PROHIBITED_DISEASES, medicine.prohibitedDiseases)
        }
        db.insert(TABLE_MEDICINES, null, values)
    }

    fun getAllDrugs(): List<String> {
        val db = readableDatabase
        val drugs = mutableListOf<String>()
        val cursor = db.query(TABLE_MEDICINES, arrayOf(COLUMN_NAME), null, null, null, null, null)

        while (cursor.moveToNext()) {
            drugs.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)))
        }
        cursor.close()
        return drugs
    }

    fun getMedicineByName(drugName: String): Medicine {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MEDICINES WHERE $COLUMN_NAME = ?", arrayOf(drugName))

        var medicine: Medicine? = null
        if (cursor.moveToFirst()) {
            medicine = Medicine(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIN_AGE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROHIBITED_FOR_PREGNANT)) == 1,
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CALCULATED)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIXED_DOSAGE_ADULT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIXED_DOSAGE_CHILD)),
                cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow(COLUMN_CALC_DOSAGE_PER_KG_ADULT)),
                cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow(COLUMN_CALC_DOSAGE_PER_KG_CHILD)),
                cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow(COLUMN_MAX_DOSAGE)), // Добавили
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROHIBITED_DISEASES))
            )
        }
        cursor.close()

        return medicine ?: throw IllegalArgumentException("Лекарство не найдено")
    }

    // Функция для безопасного получения `Double?` из курсора
    private fun Cursor.getDoubleOrNull(columnName: String): Double? {
        val index = getColumnIndexOrThrow(columnName)
        return if (!isNull(index)) getDouble(index) else null
    }
}