package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.arquitecturaretrofit.databinding.FragmentCharacterInfoBinding

class CharacterInfoFragment(var character: Character) : Fragment() {

    private var viewModel = ComicViewModel(characterId = character.id)
    private val comicAdapter = ComicAdapter(null)
    private var listener : CharacterInfoFragmentInterface? = null

    private lateinit var binding : FragmentCharacterInfoBinding

    interface CharacterInfoFragmentInterface{
        fun goToAllComics(characterId : Int)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "Creating fragment")
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "After super create fragment")
        Log.d("DEBUG", "Created fragment")
        setObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? CharacterInfoFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement CharacterInfoFragmentInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterInfoBinding.inflate(layoutInflater, container, false)
        if (character?.description.isNullOrEmpty())
            binding.description.text = "No name found"
        else
            binding.name.text = character?.name
        if (character?.description.isNullOrEmpty())
            binding.description.text = "No description found"
        else
            binding.description.text = character?.description
        binding.description.movementMethod = ScrollingMovementMethod()
        binding.image.load("${character?.imageUrl}.${character?.imageExtension}")
        binding.comicsView.adapter = comicAdapter
        binding.comicsView.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.seeAllComicsButton.setOnClickListener{
            goToAllComics()
        }
        return binding.root
    }

    private fun setObservers() {
        viewModel.comics.observe(this) { comics ->
            comicAdapter.dataSet.addAll(comics)
            comicAdapter.notifyDataSetChanged()
        }
    }

    fun goToAllComics(){
        listener?.goToAllComics(character.id)
    }
}