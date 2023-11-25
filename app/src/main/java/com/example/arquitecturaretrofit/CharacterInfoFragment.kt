package com.example.arquitecturaretrofit

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.arquitecturaretrofit.R.drawable.avd_heart_empty
import com.example.arquitecturaretrofit.R.drawable.avd_heart_full
import com.example.arquitecturaretrofit.R.drawable.favorite
import com.example.arquitecturaretrofit.databinding.FragmentCharacterInfoBinding
import com.example.arquitecturaretrofit.R.drawable.not_favorite

class CharacterInfoFragment(var character: Character) : Fragment() {

    private var comicViewModel = ComicViewModel(characterId = character.id)
    private var favoriteViewModel : FavoriteViewModel? = null
    private val comicAdapter = ComicAdapter(null)
    private var listener : CharacterInfoFragmentInterface? = null

    private lateinit var binding : FragmentCharacterInfoBinding

    interface CharacterInfoFragmentInterface{
        fun goToAllComics(characterId : Int)
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
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.character_info_fragment_title)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterInfoBinding.inflate(layoutInflater, container, false)

        setCharacterData()

        binding.comicsView.adapter = comicAdapter
        binding.comicsView.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)

        binding.seeAllComicsButton.setOnClickListener{
            goToAllComics()
        }

        binding.closeButton.setOnClickListener {
            closeFragment()
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

    private fun setCharacterData() {
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
    }

    private fun closeFragment() {
            val builder = requireContext().let { it1 ->
                AlertDialog.Builder(it1).setMessage(
                    "¿Volver a inicio?"
                ).setPositiveButton(
                    "Sí, volver."
                ) { _, _ ->
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
            builder?.setNegativeButton(
                "Cancelar"
            ) { dialog, _ ->
                // Cerrar el dialog
                dialog.dismiss()
            }
            builder?.show()
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
}