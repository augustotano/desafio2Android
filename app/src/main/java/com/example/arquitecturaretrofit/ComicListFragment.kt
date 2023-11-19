package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.arquitecturaretrofit.databinding.FragmentComicListBinding

class ComicListFragment(var characterId: Int) : Fragment() {
    private lateinit var binding : FragmentComicListBinding
    private var listener : ComicListFragmentInterface? = null
    private var viewModel = ComicViewModel(characterId)
    private val comicAdapter = ComicAdapter(::onGoToFullComic)

    interface ComicListFragmentInterface{
        fun onGoToFullComic(comic : Comic)
        fun toggleLoadingBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? ComicListFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement ComicListFragmentInterface")
        }
        listener?.toggleLoadingBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComicListBinding.inflate(layoutInflater, container, false)

        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = comicAdapter
        binding.recyclerView.addOnScrollListener( object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    listener?.toggleLoadingBar()
                    viewModel.refreshComics()
                }
            }
        })
        return binding.root
    }

    fun onGoToFullComic(comic : Comic){
        listener?.onGoToFullComic(comic)
    }

    private fun setObservers() {
        viewModel.comics.observe(this) { comics ->
            comicAdapter.dataSet.addAll(comics)
            comicAdapter.notifyDataSetChanged()
            listener?.toggleLoadingBar()
        }
    }
}