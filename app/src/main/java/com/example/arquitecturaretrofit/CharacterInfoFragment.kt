package com.example.arquitecturaretrofit

import android.os.Build
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

private const val CHARACTER = "character"

class CharacterInfoFragment(var character: Character) : Fragment() {

    private var viewModel = ComicViewModel(characterId = character.id)
    private val comicAdapter = ComicAdapter()

    private lateinit var binding : FragmentCharacterInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "Creating fragment")
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "After super create fragment")
        Log.d("DEBUG", "Created fragment")
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterInfoBinding.inflate(layoutInflater, container, false)
        binding.name.text = character?.name ?: "No name found"
        binding.description.text = character?.description ?: "No description found"
        binding.description.movementMethod = ScrollingMovementMethod()
        binding.image.load("${character?.imageUrl}.${character?.imageExtension}")
        binding.comicsView.adapter = comicAdapter
        binding.comicsView.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    private fun setObservers() {
        viewModel.comics.observe(this) { comics ->
            comicAdapter.dataSet = comics
            comicAdapter.notifyDataSetChanged()
        }
    }
}