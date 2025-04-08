package com.example.medicinesearch

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout) // Подключаем drawer_layout.xml

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Настраиваем кнопку для открытия меню
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Включаем кнопку в ActionBar для открытия бокового меню
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Обработчик нажатия на элементы меню
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                }
                R.id.nav_auth -> startActivity(Intent(this, AuthenticationActivity::class.java))
                R.id.nav_reg-> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_dosage -> startActivity(Intent(this, DosageCalculatorActivity::class.java))
            }
            drawerLayout.closeDrawers()
            true
        }

        val btnSearchMedicines = findViewById<Button>(R.id.btn_search_medicines)
        val btnDosageCalculator = findViewById<Button>(R.id.btn_dosage_calculator)

        btnSearchMedicines.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }


        btnDosageCalculator.setOnClickListener {
            startActivity(Intent(this, DosageCalculatorActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}