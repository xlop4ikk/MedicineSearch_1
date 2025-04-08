package com.example.medicinesearch

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: DrugAdapter
    private lateinit var dbHelper: DrugDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        dbHelper = DrugDataBaseHelper(this)

        searchView = findViewById(R.id.SearchView)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val allDrugs = dbHelper.getAllDrugs()
        adapter = DrugAdapter(allDrugs) { selectedDrug ->
            onDrugSelected(selectedDrug)
        }
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterByDrugName(newText)
                return true
            }
        })
    }

    private fun filterByDrugName(query: String?) {
        val filteredDrugs = dbHelper.getDrugsByName(query)
        adapter.filterList(filteredDrugs)
    }

    private fun onDrugSelected(selectedDrug: Drug) {
        searchView.setQuery(selectedDrug.name, false)
        searchView.clearFocus()

        val analogs = dbHelper.getAnalogs(selectedDrug.activeSubstance)
        adapter.filterList(analogs)
    }
}