package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import retrofit2.http.GET

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

        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.activity_list_item, android.R.id.text1, characterAdapter.filteredDataSet)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.simple_menu, menu)
        val buscar = menu.findItem(R.id.characterSearcher)
        val searchView = buscar?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Toast.makeText(requireContext(), "Buscar resultados con : $query", Toast.LENGTH_SHORT).show()
                    characterAdapter.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                characterAdapter.filter(newText)
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.characterSearcher -> {
                characterAdapter.dataSet

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onGoToFullCharacter(character : Character){
        listener?.onGoToFullCharacter(character)
    }

    private fun setObservers() {
        viewModel.characters.observe(this) { characters ->
            Log.d("CharacterListFragment", "Received characters: $characters")

            activity?.runOnUiThread {
                characterAdapter.dataSet.clear()
                characterAdapter.dataSet.addAll(characters)
                characterAdapter.filter("")
                listener?.toggleLoadingBar(false)
            }

        }
    }
}
