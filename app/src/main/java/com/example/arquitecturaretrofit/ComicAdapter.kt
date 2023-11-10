package com.example.arquitecturaretrofit

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.arquitecturaretrofit.databinding.ComicBlockBinding

class ComicAdapter() : RecyclerView.Adapter<ComicAdapter.ComicViewHolder>(){

    var dataSet: List<Comic> = emptyList()

    inner class ComicViewHolder(val binding: ComicBlockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val view = ComicBlockBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ComicViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(viewHolder: ComicViewHolder, position: Int) {
        val item = dataSet[position];
        with(viewHolder.binding) {
            comicName.text = item.name
            comicName.movementMethod = ScrollingMovementMethod()
            comicImage.load("${item.imageUrl}/portrait_incredible.${item.imageExtension}")
        }
    }
}