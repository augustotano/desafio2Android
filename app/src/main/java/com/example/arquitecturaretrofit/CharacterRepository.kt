package com.example.arquitecturaretrofit

import java.io.Serializable
import java.util.Date

object CharacterRepository {
    var offset = 0;
    var limit = 30;
    suspend fun fetchCharacters(resetCache: Boolean = false) : List<Character> {
        if (resetCache) {
            offset = 0
        }
        val timeStamp = Date().time.toString()
        val characters = MarvelClient.service.getCharacters(
            apiKey = BuildConfig.PUBLIC_KEY,
            ts = timeStamp,
            hash = "$timeStamp${BuildConfig.PRIVATE_KEY}${BuildConfig.PUBLIC_KEY}".md5().toHex(),
            offset = offset,
            limit = limit,
        )
        offset += limit
        return obtainCharacterAndComicData(characters)
    }

    suspend fun filterCharacters(filterName: String ?= null, resetCache: Boolean = false) : List<Character> {
        if (resetCache) {
            offset = 0
        }
        val timeStamp = Date().time.toString()
        val characters = MarvelClient.service.getCharacters(
            apiKey = BuildConfig.PUBLIC_KEY,
            ts = timeStamp,
            hash = "$timeStamp${BuildConfig.PRIVATE_KEY}${BuildConfig.PUBLIC_KEY}".md5().toHex(),
            offset = offset,
            limit = limit,
            nameStartsWith = filterName
        )
        offset += limit
        return obtainCharacterAndComicData(characters)
    }

    fun obtainCharacterAndComicData(characterResponseWrapper : GetCharactersResponseWrapper) : List<Character>{
        if(characterResponseWrapper.code != 200){
            throw Exception("Call was not successful, code: ${characterResponseWrapper.code}")
        }
        val characterList = mutableListOf<Character>()
        for (characterReceived in characterResponseWrapper.data.results){
            val character = Character(
                id = characterReceived.id,
                name = characterReceived.name,
                description = characterReceived.description,
                imageUrl = characterReceived.thumbnail.path,
                imageExtension = characterReceived.thumbnail.extension,
            )
            characterList.add(character)
        }
        return characterList
    }
}

data class Character(
    val id : Int,
    val name : String,
    val imageUrl : String,
    val imageExtension : String,
    val description : String,
) : Serializable

data class GetCharactersResponseWrapper(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val data: Data,
    val etag: String,
    val status: String
)

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)

data class Result(
    val comics: Comics,
    val description: String,
    val events: Events,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val stories: Stories,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXXX>,
    val returned: Int
)

data class Thumbnail(
    val extension: String,
    val path: String
)

data class Url(
    val type: String,
    val url: String
)

data class Item(
    val name: String,
    val resourceURI: String
)

data class ItemXXX(
    val name: String,
    val resourceURI: String,
    val type: String
)
