package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
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


class CharacterListFragment : Fragment() {

    private lateinit var binding: FragmentCharacterListBinding
    private lateinit var arrayAdapter: ArrayAdapter<Character>
    private var listener: CharacterListFragmentInterface? = null
    private var viewModel = CharacterViewModel()
    private val characterAdapter = CharacterAdapter(::onGoToFullCharacter)
    private val handler = Handler()
    private val delay = 5000L
    private var lastQuery: String? = null


    interface CharacterListFragmentInterface {
        fun onGoToFullCharacter(character: Character)
        fun toggleLoadingBar(active: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? CharacterListFragmentInterface
        if (listener == null) {
            throw ClassCastException("Listener needs to implement CharacterListFragmentInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.activity_list_item,
            android.R.id.text1,
            characterAdapter.filteredDataSet
        )
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
                viewModel.queryString = query
                viewModel.filterCharacters(resetCache = true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "New text is empty", Toast.LENGTH_SHORT).show()
                    viewModel.queryString = newText
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
    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = resources.getString(R.string.character_list_fragment_title)
        }
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

    fun onGoToFullCharacter(character: Character) {
        listener?.onGoToFullCharacter(character)
    }

    private fun setObservers() {
        viewModel.characters.observe(this) { characters ->
            activity?.runOnUiThread {
                characterAdapter.dataSet.clear()
                characterAdapter.dataSet.addAll(characters)
                listener?.toggleLoadingBar(false)
            }
        }
    }
}

