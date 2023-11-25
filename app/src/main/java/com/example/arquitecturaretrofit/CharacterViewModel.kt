package com.example.arquitecturaretrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val charactersRepository = CharacterRepository
    private val _characters = MutableLiveData<List<Character>>()
    var queryString: String? = null

    val characters : LiveData<List<Character>>
        get() = _characters

    init{
        refreshCharacters()
    }

    fun refreshCharacters(resetCache: Boolean = false){
        viewModelScope.launch{
            CharacterRepository.fetchCharacters(resetCache = resetCache).run {
                _characters.postValue(this)
            }
        }
    }

    fun filterCharacters(resetCache: Boolean = false){
        viewModelScope.launch{
            CharacterRepository.filterCharacters(filterName = queryString, resetCache = resetCache).run{
                _characters.postValue(this)
            }
        }
    }
}