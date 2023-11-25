package com.example.arquitecturaretrofit

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.arquitecturaretrofit.databinding.ComicBlockBinding

class ComicCharactersAdapter(val onClickCharacter: ((Character) -> Unit)?) : RecyclerView.Adapter<ComicCharactersAdapter.ComicCharactersViewHolder>(){

    var dataSet: MutableList<Character> = mutableListOf()

    inner class ComicCharactersViewHolder(val binding: ComicBlockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicCharactersViewHolder {
        val view = ComicBlockBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ComicCharactersViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(viewHolder: ComicCharactersViewHolder, position: Int) {
        val item = dataSet[position];
        with(viewHolder.binding) {
            comicName.text = item.name
            comicName.movementMethod = ScrollingMovementMethod()
            comicImage.load("${item.imageUrl}/portrait_incredible.${item.imageExtension}")
            if(onClickCharacter != null){
                root.setOnClickListener{
                    onClickCharacter.invoke(item)
                }
            }
        }
    }
}