package com.example.medicinesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DrugAdapter(
    private var drugs: List<Drug>,
    private val onDrugClick: (Drug) -> Unit
) : RecyclerView.Adapter<DrugAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.drugName)
        val typeTextView: TextView = view.findViewById(R.id.drugType)
        val activeSubstanceTextView: TextView = view.findViewById(R.id.drugActiveSubstance)
        val formTextView: TextView = view.findViewById(R.id.drugForm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drug = drugs[position]
        holder.nameTextView.text = drug.name
        holder.typeTextView.text = "Тип: ${drug.type}"
        holder.activeSubstanceTextView.text = "Активное вещество: ${drug.activeSubstance}"
        holder.formTextView.text = "Форма: ${drug.form}"

        holder.itemView.setOnClickListener {
            onDrugClick(drug) // Передаём выбранное лекарство
        }
    }

    override fun getItemCount(): Int = drugs.size

    fun filterList(newList: List<Drug>) {
        drugs = newList
        notifyDataSetChanged()
    }
}
