package com.example.arquitecturaretrofit

import android.util.Log
import retrofit2.HttpException
import java.io.Serializable
import java.util.Date

object ComicRepository {
    var characterId : Int = 0
    var offset = 0
    var limit = 30

    suspend fun fetchComics() : List<Comic> {
        val timeStamp = Date().time.toString()
        val comics = MarvelClient.service.getComics(
            characterId = characterId,
            apiKey = BuildConfig.PUBLIC_KEY,
            ts = timeStamp,
            hash = "$timeStamp${BuildConfig.PRIVATE_KEY}${BuildConfig.PUBLIC_KEY}".md5().toHex(),
            limit = 10,
            offset = 0
        )
        offset += limit
        return obtainComicData(comics)
    }

    suspend fun fetchAllComics() : List<Comic> {
        val timeStamp = Date().time.toString()
        val comics = MarvelClient.service.getComics(
            characterId = characterId,
            apiKey = BuildConfig.PUBLIC_KEY,
            ts = timeStamp,
            hash = "$timeStamp${BuildConfig.PRIVATE_KEY}${BuildConfig.PUBLIC_KEY}".md5().toHex(),
            offset = offset,
            limit = limit
        )
        offset += limit
        return obtainComicData(comics)
    }

    fun obtainComicData(comicsResponseWrapper: GetComicsResponseWrapper) : List<Comic>{
        if(comicsResponseWrapper.code != 200){
            throw Exception("Call was not successful, code: ${comicsResponseWrapper.code}")
        }
        val comics = mutableListOf<Comic>()
        for (comicReceived in comicsResponseWrapper.data.results){
            val comic = Comic(
                id = comicReceived.id,
                name = comicReceived.title,
                imageUrl = comicReceived.thumbnail.path,
                imageExtension = comicReceived.thumbnail.extension,
                description = comicReceived.description,
                issueNumber = comicReceived.issueNumber,
            )
            comics.add(comic)
        }
        return comics
    }
}

data class Comic(
    val id : Int,
    val name : String,
    val imageUrl : String,
    val imageExtension : String,
    val description: String?,
    val issueNumber: Int?,
) : Serializable

data class GetComicsResponseWrapper(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val data: DataComics,
    val etag: String,
    val status: String
)

data class DataComics(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<ResultComics>,
    val total: Int
)

data class ResultComics(
    val characters: Characters,
    val collectedIssues: List<Any>,
    val collections: List<Any>,
    val creators: Creators,
    val dates: List<DateComics>,
    val description: String,
    val diamondCode: String,
    val digitalId: Int,
    val ean: String,
    val events: Events,
    val format: String,
    val id: Int,
    val images: List<Image>,
    val isbn: String,
    val issn: String,
    val issueNumber: Int,
    val modified: String,
    val pageCount: Int,
    val prices: List<Price>,
    val resourceURI: String,
    val series: SeriesComics,
    val stories: Stories,
    val textObjects: List<TextObject>,
    val thumbnail: Thumbnail,
    val title: String,
    val upc: String,
    val urls: List<Url>,
    val variantDescription: String,
    val variants: List<Variant>
)

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Creators(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemX>,
    val returned: Int
)

data class DateComics(
    val date: String,
    val type: String
)

data class Image(
    val extension: String,
    val path: String
)

data class Price(
    val price: Double,
    val type: String
)

data class SeriesComics(
    val name: String,
    val resourceURI: String
)

data class TextObject(
    val language: String,
    val text: String,
    val type: String
)

data class Variant(
    val name: String,
    val resourceURI: String
)

data class ItemX(
    val name: String,
    val resourceURI: String,
    val role: String
)