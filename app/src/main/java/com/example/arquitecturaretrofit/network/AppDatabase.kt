package com.example.arquitecturaretrofit.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.arquitecturaretrofit.network.FavoriteCharacter
import com.example.arquitecturaretrofit.network.FavoriteDao

@Database(entities = [FavoriteCharacter::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}