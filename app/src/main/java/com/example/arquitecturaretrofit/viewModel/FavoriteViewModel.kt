package com.example.arquitecturaretrofit.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arquitecturaretrofit.model.FavoriteRepository
import com.example.arquitecturaretrofit.network.FavoriteCharacter
import kotlinx.coroutines.launch

class FavoriteViewModel(val context : Context) : ViewModel(){
    private val favoriteRepository = FavoriteRepository
    private val _favorites = MutableLiveData<List<FavoriteCharacter>>()

    val favorites : LiveData<List<FavoriteCharacter>>
        get() = _favorites

    init{
        refreshFavorites()
    }

    fun refreshFavorites(){
        viewModelScope.launch{
            favoriteRepository.fetchFavorites(context).run{
                _favorites.postValue(this)
            }
        }
    }

    fun insertFavorite(fav : FavoriteCharacter){
        viewModelScope.launch{
            favoriteRepository.insertFavorite(fav, context)
        }
    }

    fun updateFavorite(fav : FavoriteCharacter){
        viewModelScope.launch{
            favoriteRepository.updateFavorite(fav, context)
        }
    }

    fun removeFavorite(favId : Int){
        viewModelScope.launch{
            favoriteRepository.removeFavorite(favId, context)
        }
    }

    fun isFavorite(favId : Int) : Boolean{
        return favoriteRepository.isFavorite(favId, context)
    }
}