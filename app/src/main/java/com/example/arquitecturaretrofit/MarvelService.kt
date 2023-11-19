package com.example.arquitecturaretrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.security.MessageDigest

fun String.md5(): ByteArray = MessageDigest.getInstance("MD5").digest(this.toByteArray(Charsets.UTF_8))
fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
interface MarvelService{
    @GET("characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String,
        @Query("ts") ts: String,
        @Query("hash") hash : String,
        @Query("offset") offset: Int,
        @Query("limit") limit : Int
    ) : GetCharactersResponseWrapper

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path ("characterId") characterId : Int,
        @Query("apikey") apiKey: String,
        @Query("ts") ts: String,
        @Query("hash") hash : String,
        @Query("offset") offset : Int,
        @Query("limit") limit : Int
    ) : GetComicsResponseWrapper
}

object MarvelClient{
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com:443/v1/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(MarvelService::class.java)
}
