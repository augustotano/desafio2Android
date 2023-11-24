package com.example.arquitecturaretrofit

import android.content.Context
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
import com.example.arquitecturaretrofit.databinding.FragmentCharacterInfoBinding


class CharacterInfoFragment(var character: Character) : Fragment() {

    private var viewModel = ComicViewModel(characterId = character.id)
    private val comicAdapter = ComicAdapter(null)
    private var listener : CharacterInfoFragmentInterface? = null

    private lateinit var binding : FragmentCharacterInfoBinding

    interface CharacterInfoFragmentInterface{
        fun goToAllComics(characterId : Int)
    }
    override fun onCreate(savedInstanceState: Bundle?) {1
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
        viewModel.comics.observe(this) { comics ->
            comicAdapter.dataSet.addAll(comics)
            comicAdapter.notifyDataSetChanged()
        }
    }

    fun goToAllComics(){
        listener?.goToAllComics(character.id)
    }
}