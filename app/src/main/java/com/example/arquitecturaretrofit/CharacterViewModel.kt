package com.example.arquitecturaretrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val charactersRepository = CharacterRepository
    private val _characters = MutableLiveData<List<Character>>()

    val characters : LiveData<List<Character>>
        get() = _characters

    init{
        refreshCharacters()
    }

    fun refreshCharacters(){
        viewModelScope.launch{
            CharacterRepository.fetchCharacters().run{
                _characters.postValue(this)
            }
        }
    }
}