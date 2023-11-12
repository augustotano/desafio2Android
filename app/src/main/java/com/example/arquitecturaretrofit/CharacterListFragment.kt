package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.arquitecturaretrofit.databinding.FragmentCharacterListBinding

class CharacterListFragment : Fragment() {

    private lateinit var binding : FragmentCharacterListBinding
    private var listener : CharacterListFragmentInterface? = null
    private var viewModel = CharacterViewModel()
    private val characterAdapter = CharacterAdapter(::onGoToFullCharacter)

    interface CharacterListFragmentInterface{
        fun onGoToFullCharacter(character : Character)
        fun toggleLoadingBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? CharacterListFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement CharacterListFragmentInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterListBinding.inflate(layoutInflater, container, false)

        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = characterAdapter
        binding.recyclerView.addOnScrollListener( object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    listener?.toggleLoadingBar()
                    viewModel.refreshCharacters()
                }
            }
        })
        return binding.root
    }

    fun onGoToFullCharacter(character : Character){
        listener?.onGoToFullCharacter(character)
    }

    private fun setObservers() {
        viewModel.characters.observe(this) { characters ->
            characterAdapter.dataSet.addAll(characters)
            characterAdapter.notifyDataSetChanged()
            listener?.toggleLoadingBar()
        }
    }
}