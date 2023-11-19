package com.example.arquitecturaretrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ComicViewModel(characterId : Int) : ViewModel() {
    private val comicRepository = ComicRepository
    private val _comics = MutableLiveData<List<Comic>>()

    val comics : LiveData<List<Comic>>
        get() = _comics

    init{
        comicRepository.characterId = characterId
        comicRepository.offset = 0
        refreshComics()
    }

    fun refreshComics(){
        viewModelScope.launch{
            comicRepository.fetchComics().run{
                _comics.postValue(this)
            }
        }
    }
}