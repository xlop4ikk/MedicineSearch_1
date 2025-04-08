package com.example.medicinesearch

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DrugDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "drugs.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_DRUGS = "drugs"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TYPE = "type"
        const val COLUMN_ACTIVE_SUBSTANCE = "active_substance"
        const val COLUMN_FORM = "form"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_DRUGS (
              $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_ACTIVE_SUBSTANCE TEXT,
                $COLUMN_FORM TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)

        insertInitialData(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val drugs = listOf(
            Drug("Парацетамол", "Жаропонижающее", "Парацетамол", "Таблетка"),
            Drug("Колдрекс", "Противопростудное", "Парацетамол", "Таблетка"),
            Drug("Терафлю", "Противопростудное", "Парацетамол", "Таблетка"),
            Drug("Ибупрофен", "Противовоспалительное", "Ибупрофен", "Таблетка"),
            Drug("Нурофен", "Обезболивающее", "Ибупрофен", "Таблетка"),
            Drug("Азитромицин", "Антибиотик", "Азитромицин", "Таблетка"),
            Drug("Диклофенак", "Противовоспалительное", "Диклофенак", "Таблетка"),
            Drug("Вольтарен", "Обезболивающее", "Диклофенак", "Таблетка"),
            Drug("Сумамед", "Антибиотик", "Азитромицин", "Таблетка"),
            Drug("Амоксициллин", "Антибиотик", "Амоксициллин", "Таблетка"),
            Drug("Флемоксин", "Антибиотик", "Амоксициллин", "Таблетка"),
            Drug("Лоратадин", "Антигистаминное", "Лоратадин", "Таблетка"),
            Drug("Эриус", "Антигистаминное", "Лоратадин", "Таблетка"),
            Drug("Кларитин", "Антигистаминное", "Лоратадин", "Таблетка"),
            Drug("Рибомунул", "Иммуномодулятор", "Лоратадин", "Таблетка"),
            Drug("Иммунал", "Иммуномодулятор", "Гепарин", "Таблетка"),
            Drug("Циклоферон", "Противовирусное", "Гепарин", "Таблетка"),
            Drug("Арбидол", "Противовирусное", "Умифеновир", "Таблетка"),
            Drug("Ремантадин", "Противовирусное", "Ремантадин", "Таблетка"),
            Drug("Тамифлю", "Противовирусное", "Осельтамивир", "Таблетка"),
            Drug("Зиннат", "Антибиотик", "Цефуроксим", "Таблетка"),
            Drug("Цефазолин", "Антибиотик", "Цефазолин", "Таблетка"),
            Drug("Цефтриаксон", "Антибиотик", "Цефтриаксон", "Таблетка"),
            Drug("Цефиксим", "Антибиотик", "Цефиксим", "Таблетка"),
            Drug("Аугментин", "Антибиотик", "Амоксициллин+Клавулановая кислота", "Таблетка"),
            Drug("Амоксиклав", "Антибиотик", "Амоксициллин+Клавулановая кислота", "Таблетка"),
            Drug("Курантил", "Антикоагулянт", "Дипиридамол", "Таблетка"),
            Drug("Актовегин", "Ноотропное", "Актовегин", "Таблетка"),
            Drug("Мексидол", "Ноотропное", "Этилметилгидроксипиридина сукцинат", "Таблетка"),
            Drug("Цитрамон", "Обезболивающее", "Парацетамол+Аспирин+Кофеин", "Таблетка"),
            Drug("Кеторол", "Обезболивающее", "Кеторолак", "Таблетка"),
            Drug("Найз", "Противовоспалительное", "Нимесулид", "Таблетка"),
            Drug("Диклак", "Противовоспалительное", "Диклофенак", "Таблетка"),
            Drug("Гриппферон", "Противовирусное", "Интерферон альфа-2b", "Таблетка"),
            Drug("Виферон", "Противовирусное", "Интерферон альфа-2b", "Таблетка"),
            Drug("Анаферон", "Иммуномодулятор", "Анаферон", "Таблетка"),
            Drug("Кагоцел", "Противовирусное", "Кагоцел", "Таблетка"),
            Drug("Ингавирин", "Противовирусное", "Ингавирин", "Таблетка"),
            Drug("Трентал", "Сосудорасширяющее", "Пентоксифиллин", "Таблетка"),
            Drug("Вазонит", "Сосудорасширяющее", "Пентоксифиллин", "Таблетка"),
            Drug("Эскузан", "Венотоник", "Эсцин", "Таблетка"),
            Drug("Детралекс", "Венотоник", "Диосмин+Гесперидин", "Таблетка"),
            Drug("Флебодиа", "Венотоник", "Диосмин", "Таблетка"),
            Drug("Троксерутин", "Венотоник", "Троксерутин", "Таблетка"),
            Drug("Кардиомагнил", "Антиагрегант", "Ацетилсалициловая кислота+Магния гидроксид", "Таблетка"),
            Drug("Аспирин Кардио", "Антиагрегант", "Ацетилсалициловая кислота", "Таблетка"),
            Drug("Милдронат", "Кардиопротектор", "Мельдоний", "Таблетка"),
            Drug("Рибоксин", "Кардиопротектор", "Инозин", "Таблетка"),
            Drug("Панангин", "Кардиопротектор", "Аспарагинат калия+Магния аспарагинат", "Таблетка"),
            Drug("Предуктал", "Кардиопротектор", "Триметазидин", "Таблетка"),
            Drug("Кавинтон", "Ноотропное", "Винпоцетин", "Таблетка"),
            Drug("Вестибо", "Противовестибулярное", "Бетагистин", "Таблетка"),
            Drug("Бетасерк", "Противовестибулярное", "Бетагистин", "Таблетка"),
            Drug("Циннаризин", "Противовестибулярное", "Циннаризин", "Таблетка")
        )

        drugs.forEach { insertDrug(db, it) }
    }

    private fun insertDrug(db: SQLiteDatabase, drug: Drug) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, drug.name)
            put(COLUMN_TYPE, drug.type)
            put(COLUMN_ACTIVE_SUBSTANCE, drug.activeSubstance)
            put(COLUMN_FORM, drug.form)
        }
        db.insert(TABLE_DRUGS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DRUGS")
        onCreate(db)
    }

    fun getAllDrugs(): List<Drug> {
        val db = readableDatabase
        val drugList = mutableListOf<Drug>()

        val cursor = db.query(TABLE_DRUGS, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            drugList.add(
                Drug(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE_SUBSTANCE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FORM))
                )
            )
        }
        cursor.close()
        return drugList
    }

    fun getDrugsByName(query: String?): List<Drug> {
        val db = readableDatabase
        val drugList = mutableListOf<Drug>()

        val selection = if (!query.isNullOrEmpty()) "$COLUMN_NAME LIKE ?" else null
        val selectionArgs = if (!query.isNullOrEmpty()) arrayOf("$query%") else null

        val cursor = db.query(TABLE_DRUGS, null, selection, selectionArgs, null, null, null)

        while (cursor.moveToNext()) {
            drugList.add(
                Drug(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE_SUBSTANCE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FORM))
                )
            )
        }
        cursor.close()
        return drugList
    }

    fun getAnalogs(activeSubstance: String): List<Drug> {
        val db = readableDatabase
        val drugList = mutableListOf<Drug>()

        val cursor = db.query(
            TABLE_DRUGS, null, "$COLUMN_ACTIVE_SUBSTANCE = ?",
            arrayOf(activeSubstance), null, null, null
        )

        while (cursor.moveToNext()) {
            drugList.add(
                Drug(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE_SUBSTANCE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FORM))
                )
            )
        }
        cursor.close()
        return drugList
    }
}