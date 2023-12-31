package com.example.arquitecturaretrofit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arquitecturaretrofit.model.Character
import com.example.arquitecturaretrofit.model.CharacterRepository
import kotlinx.coroutines.launch

class ComicCharacterViewModel(comicId : Int) : ViewModel() {
    private val comicCharacterRepository = CharacterRepository
    private val _comicCharacter = MutableLiveData<List<Character>>()

    val comicCharacter : LiveData<List<Character>>
        get() = _comicCharacter

    init{
        comicCharacterRepository.comicId = comicId
        comicCharacterRepository.offset = 0
        refreshComicCharacter()
    }

    fun refreshComicCharacter(){
        viewModelScope.launch{
            comicCharacterRepository.fetchAllCharacters().run{
                _comicCharacter.postValue(this)
            }
        }
    }
}