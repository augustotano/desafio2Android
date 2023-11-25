package com.example.arquitecturaretrofit

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteCharacter::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}