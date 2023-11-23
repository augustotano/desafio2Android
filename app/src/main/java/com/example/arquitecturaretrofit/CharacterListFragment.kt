package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.arquitecturaretrofit.databinding.FragmentCharacterListBinding

class CharacterListFragment : Fragment() {

    private lateinit var binding : FragmentCharacterListBinding
    private lateinit var arrayAdapter: ArrayAdapter<Character>
    private var listener : CharacterListFragmentInterface? = null
    private var viewModel = CharacterViewModel()
    private val characterAdapter = CharacterAdapter(::onGoToFullCharacter)

    interface CharacterListFragmentInterface{
        fun onGoToFullCharacter(character : Character)
        fun toggleLoadingBar(active : Boolean)
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
        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.activity_list_item, android.R.id.text1, characterAdapter.dataSet)
        binding = FragmentCharacterListBinding.inflate(layoutInflater, container, false)

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = characterAdapter
        binding.recyclerView.addOnScrollListener( object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    listener?.toggleLoadingBar(true)
                    viewModel.refreshCharacters()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(false)
            title = resources.getString(R.string.character_list_fragment_title)
        }
    }

    private fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater): Boolean {

        inflater.inflate(R.menu.main_menu, menu)
        val buscar = menu?.findItem(R.id.characterSearcher)
        val searchView = buscar?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Toast.makeText(requireContext(), "Buscar resultados con : ${query}", Toast.LENGTH_SHORT).show()
                    arrayAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(requireContext(), newText, Toast.LENGTH_SHORT).show()

                return false
            }
        })
            return true
    }


    fun onGoToFullCharacter(character : Character){
        listener?.onGoToFullCharacter(character)
    }

    private fun setObservers() {
        viewModel.characters.observe(this) { characters ->
            characterAdapter.dataSet.addAll(characters)
            characterAdapter.notifyDataSetChanged()
            listener?.toggleLoadingBar(false)
        }
    }
}