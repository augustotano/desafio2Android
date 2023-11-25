package com.example.arquitecturaretrofit.model

import android.content.Context
import androidx.room.Room
import com.example.arquitecturaretrofit.network.AppDatabase
import com.example.arquitecturaretrofit.network.FavoriteCharacter

object FavoriteRepository {

    suspend fun fetchFavorites(applicationContext : Context): List<FavoriteCharacter> {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favorite-db"
        ).allowMainThreadQueries().build()

        val favoriteDao = db.favoriteDao()
        return favoriteDao.getAll()
    }

    suspend fun insertFavorite(fav : FavoriteCharacter, applicationContext : Context) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favorite-db"
        ).allowMainThreadQueries().build()
        val favoriteDao = db.favoriteDao()
        favoriteDao.insertAll(fav)
    }

    suspend fun updateFavorite(fav : FavoriteCharacter, applicationContext : Context){
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favorite-db"
        ).allowMainThreadQueries().build()
        val favoriteDao = db.favoriteDao()
        favoriteDao.updateFavorite(fav)
    }

    suspend fun removeFavorite(favId : Int, applicationContext : Context){
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favorite-db"
        ).allowMainThreadQueries().build()
        val favoriteDao = db.favoriteDao()
        favoriteDao.delete(favId)
    }

    fun isFavorite(favId : Int, context : Context) : Boolean{
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "favorite-db"
        ).allowMainThreadQueries().build()

        val favoriteDao = db.favoriteDao()
        return favoriteDao.fetchFavorite(favId).size > 0;
    }
}