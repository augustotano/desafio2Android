package com.example.arquitecturaretrofit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.arquitecturaretrofit.databinding.FragmentComicInfoBinding


class ComicInfoFragment(var comic: Comic) : Fragment() {

    private lateinit var binding : FragmentComicInfoBinding
    private var viewModel = ComicCharacterViewModel(comicId = comic.id)
    private var listener : ComicInfoFragment.ComicInfoFragmentInterface? = null
    private val characterAdapter = ComicCharactersAdapter(::onGoToFullCharacter)

    interface ComicInfoFragmentInterface{
        fun goToAllCharacters(comicId : Int)
        fun onGoToFullCharacter(character: Character)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? ComicInfoFragment.ComicInfoFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement ComicInfoFragmentInterface")
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(com.example.arquitecturaretrofit.R.string.comic_info_fragment_title)
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#407bb9")))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComicInfoBinding.inflate(layoutInflater, container, false)

        binding.image.load("${comic?.imageUrl}.${comic?.imageExtension}")

        if (comic?.name.isNullOrEmpty())
            binding.name.text = "No name found"
        else
            binding.name.text = comic?.name

        if (comic?.description.isNullOrEmpty())
            binding.description.text = "No description found"
        else
            binding.description.text = comic?.description

        binding.issueNumber.text = comic?.issueNumber?.toString()

        binding.characterView.adapter = characterAdapter
        binding.characterView.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)

        binding.characterView.addOnScrollListener( object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.refreshComicCharacter()
                }
            }
        })

        binding.seeAllCharactersButton.setOnClickListener{
            goToAllCharacters()
        }

        return binding.root
    }

    private fun setObservers() {
        viewModel.comicCharacter.observe(this) { characters ->
            characterAdapter.dataSet.addAll(characters)
            characterAdapter.notifyDataSetChanged()
        }
    }

    fun goToAllCharacters(){
        listener?.goToAllCharacters(comic.id)
    }

    fun onGoToFullCharacter(character: Character){
        listener?.onGoToFullCharacter(character)
    }

}