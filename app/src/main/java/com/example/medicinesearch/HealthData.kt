package com.example.medicinesearch

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HealthData(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "health_data.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = ("CREATE TABLE users (id INTEGER PRIMARY KEY, login TEXT, email TEXT, password TEXT)")
        db.execSQL(createUsersTable)

        val createHealthDataTable = ("CREATE TABLE health_data (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, age INTEGER, weight REAL, height REAL, blood_pressure TEXT, sugar_level REAL, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))")
        db.execSQL(createHealthDataTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS health_data")
        onCreate(db)
    }

    fun addUsers(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("password", user.password)
        db.insert("users", null, values)
        db.close()
    }

    fun getUser(login: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE login = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(login, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun saveHealthData(userId: Int, age: Int, weight: Float, height: Float, bloodPressure: String, sugarLevel: Float) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("user_id", userId)
        values.put("age", age)
        values.put("weight", weight)
        values.put("height", height)
        values.put("blood_pressure", bloodPressure)
        values.put("sugar_level", sugarLevel)
        db.insert("health_data", null, values)
        db.close()
    }
}