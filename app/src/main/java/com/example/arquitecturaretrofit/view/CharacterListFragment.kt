package com.example.arquitecturaretrofit.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.arquitecturaretrofit.databinding.FragmentCharacterListBinding
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.arquitecturaretrofit.model.Character
import com.example.arquitecturaretrofit.viewModel.CharacterViewModel
import com.example.arquitecturaretrofit.R
import com.example.arquitecturaretrofit.adapter.CharacterAdapter

class CharacterListFragment : Fragment() {

    private lateinit var binding : FragmentCharacterListBinding
    private var listener : CharacterListFragmentInterface? = null
    private var viewModel = CharacterViewModel()
    private val characterAdapter = CharacterAdapter(::onGoToFullCharacter)
    private val handler = Handler()
    private val delay = 3000L
    private var lastQuery: String? = null

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
        binding = FragmentCharacterListBinding.inflate(layoutInflater, container, false)
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchCharacters()
    }

    private fun searchCharacters() {
        binding.searchCharacter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(
                    requireContext(),
                    "Buscar resultados con : $query",
                    Toast.LENGTH_SHORT
                ).show()
                characterAdapter.dataSet.clear()
                viewModel.queryString = query
                viewModel.filterCharacters(resetCache = true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.queryString = newText
                    characterAdapter.dataSet.clear()
                    viewModel.refreshCharacters(resetCache = true)
                } else {
                    // Cancelar ejecuciones pendientes del temporizador
                    handler.removeCallbacksAndMessages(null)

                    // Programar una nueva ejecución después de 5 segundos
                    lastQuery = newText
                    handler.postDelayed({
                        if (newText == lastQuery) {
                            // Realizar la llamada al método después de 5 segundos
                            viewModel.queryString = newText
                            characterAdapter.dataSet.clear()
                            viewModel.filterCharacters(resetCache = true)
                        }
                    }, delay)
                }
                return false
            }
        })
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = characterAdapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    listener?.toggleLoadingBar(true)
                    if (lastQuery.isNullOrEmpty()) {
                        viewModel.refreshCharacters()
                    } else {
                        viewModel.filterCharacters()
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchCharacter -> {
                characterAdapter.dataSet

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply{
            title = resources.getString(R.string.character_list_fragment_title)
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(Color.parseColor("#407bb9"))
        )
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