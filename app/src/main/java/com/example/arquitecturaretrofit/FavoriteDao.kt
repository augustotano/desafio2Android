package com.example.arquitecturaretrofit

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity
data class FavoriteCharacter (
    @PrimaryKey var characterId : Int,
    @ColumnInfo(name = "name") var name : String?,
    @ColumnInfo(name = "imageUrl") var imageUrl : String?,
    @ColumnInfo(name = "imageExtension") var imageExtension : String?,
    @ColumnInfo(name = "description") var description : String?) {}
@Dao
interface FavoriteDao {
    @Query("SELECT * FROM FavoriteCharacter")
    fun getAll(): List<FavoriteCharacter>

    @Query("DELETE FROM FavoriteCharacter")
    fun nukeTable()

    @Insert
    fun insertAll(vararg character: FavoriteCharacter)

    @Update
    fun updateFavorite(character : FavoriteCharacter)

    @Query("DELETE FROM FavoriteCharacter WHERE characterId = :favoriteCharacterId")
    fun delete(favoriteCharacterId: Int)

    @Query("SELECT characterId FROM FavoriteCharacter WHERE characterId = :id")
    fun fetchFavorite(id : Int) : List<FavoriteCharacter>
}