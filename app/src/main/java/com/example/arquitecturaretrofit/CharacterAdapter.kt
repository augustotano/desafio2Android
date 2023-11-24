package com.example.arquitecturaretrofit

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.arquitecturaretrofit.databinding.CharacterBlockBinding

class CharacterAdapter(var onItemClick : (Character) -> Unit) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>(){

    var dataSet: MutableList<Character> = mutableListOf()
    var filteredDataSet: MutableList<Character> = mutableListOf()

    init {
        filteredDataSet.addAll(dataSet)
    }

    inner class CharacterViewHolder(val binding: CharacterBlockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = CharacterBlockBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return CharacterViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(viewHolder: CharacterViewHolder, position: Int) {
        val item = dataSet[position];
        with(viewHolder.binding) {
            characterImage.load("${item.imageUrl}/portrait_incredible.${item.imageExtension}")
            characterName.text = item.name
            root.setOnClickListener{
                onItemClick(item)
            }
        }
    }

    fun filter(query: String?) {
        Log.d("CharacterAdapter", "Filtering with query: $query")
        filteredDataSet.clear()
        if (query.isNullOrEmpty()) {
            filteredDataSet.addAll(dataSet)
        } else {
            val lowerCaseQuery = query.lowercase()
            dataSet.filterTo(filteredDataSet) {
                it.name.lowercase().contains(lowerCaseQuery)
            }
        }
        notifyDataSetChanged()
    }
}