package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.arquitecturaretrofit.databinding.FragmentFavoriteListBinding

class FavoriteListFragment : Fragment() {
    private lateinit var binding : FragmentFavoriteListBinding
    private var viewModel : FavoriteViewModel? = null
    private var listener : CharacterListFragment.CharacterListFragmentInterface? = null
    private val characterAdapter = CharacterAdapter(::onGoToFullCharacter)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //
        binding = FragmentFavoriteListBinding.inflate(layoutInflater, container, false)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = characterAdapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.favorite_fragment_title)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? CharacterListFragment.CharacterListFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement CharacterListFragment.CharacterListFragmentInterface")
        }
        viewModel = FavoriteViewModel(requireActivity().applicationContext)
        setObservers()
    }

    fun parseFavorites(favoriteCharacters : List<FavoriteCharacter>) : MutableList<Character>{
        val characterList = mutableListOf<Character>()
        for(favoriteCharacter in favoriteCharacters){
            characterList.add(Character(
                favoriteCharacter.characterId,
                favoriteCharacter.name ?: "",
                favoriteCharacter.imageUrl ?: "",
                favoriteCharacter.imageExtension ?: "",
                favoriteCharacter.description ?: ""
            ))
        }
        return characterList
    }

    fun onGoToFullCharacter(character : Character){
        listener?.onGoToFullCharacter(character)
    }

    private fun setObservers() {
        viewModel?.favorites?.observe(this) { favorites ->
            characterAdapter.dataSet.addAll(parseFavorites(favorites))
            characterAdapter.notifyDataSetChanged()
            listener?.toggleLoadingBar(false)
        }
    }

}