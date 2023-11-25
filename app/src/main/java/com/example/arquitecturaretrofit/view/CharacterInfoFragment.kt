package com.example.arquitecturaretrofit.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.arquitecturaretrofit.model.Character
import com.example.arquitecturaretrofit.model.Comic
import com.example.arquitecturaretrofit.adapter.ComicAdapter
import com.example.arquitecturaretrofit.viewModel.ComicViewModel
import com.example.arquitecturaretrofit.network.FavoriteCharacter
import com.example.arquitecturaretrofit.viewModel.FavoriteViewModel
import com.example.arquitecturaretrofit.R.drawable.avd_heart_empty
import com.example.arquitecturaretrofit.R.drawable.avd_heart_full
import com.example.arquitecturaretrofit.databinding.FragmentCharacterInfoBinding

class CharacterInfoFragment(var character: Character) : Fragment() {

    private val comicAdapter = ComicAdapter(::onGoToFullComic)
    private var comicViewModel = ComicViewModel(characterId = character.id)
    private var favoriteViewModel : FavoriteViewModel? = null
    private var listener : CharacterInfoFragmentInterface? = null

    private lateinit var binding : FragmentCharacterInfoBinding

    interface CharacterInfoFragmentInterface{
        fun goToAllComics(characterId : Int)
        fun onGoToFullComic(comic: Comic)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? CharacterInfoFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement CharacterInfoFragmentInterface")
        }
        favoriteViewModel = FavoriteViewModel(requireActivity().applicationContext)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(com.example.arquitecturaretrofit.R.string.character_info_fragment_title)
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(Color.parseColor("#407bb9"))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterInfoBinding.inflate(layoutInflater, container, false)
        if (character?.name.isNullOrEmpty())
            binding.name.text = "No name found"
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

        if(favoriteViewModel != null && favoriteViewModel!!.isFavorite(character.id)){
            binding.favoriteButton.setOnClickListener{
                onRemoveAsFavorite()
            }
            val drawable = resources.getDrawable(avd_heart_full, null) as AnimatedVectorDrawable
            drawable.start()
            binding.favoriteButton.setImageDrawable(drawable)
        }else{
            binding.favoriteButton.setOnClickListener{
                onMarkAsFavorite()
            }
            val drawable = resources.getDrawable(avd_heart_empty, null) as AnimatedVectorDrawable
            binding.favoriteButton.setImageDrawable(drawable)
        }

        return binding.root
    }

    private fun setObservers() {
        comicViewModel.comics.observe(this) { comics ->
            comicAdapter.dataSet.addAll(comics)
            comicAdapter.notifyDataSetChanged()
        }
    }

    fun goToAllComics(){
        listener?.goToAllComics(character.id)

    }

    fun onMarkAsFavorite(){
        var fav = FavoriteCharacter(character.id, character.name, character.imageUrl, character.imageExtension, character.description)
        favoriteViewModel?.insertFavorite(fav)
        val drawable = resources.getDrawable(avd_heart_full, null) as AnimatedVectorDrawable
        binding.favoriteButton.setImageDrawable(drawable)
        drawable.start()
        binding.favoriteButton.setOnClickListener{
            onRemoveAsFavorite()
        }
    }

    fun onRemoveAsFavorite(){
        favoriteViewModel?.removeFavorite(character.id)
        val drawable = resources.getDrawable(avd_heart_empty, null) as AnimatedVectorDrawable
        binding.favoriteButton.setImageDrawable(drawable)
        drawable.start()
        binding.favoriteButton.setOnClickListener{
            onMarkAsFavorite()
        }
    }

    fun onGoToFullComic(comic: Comic){
        listener?.onGoToFullComic(comic)
    }
}