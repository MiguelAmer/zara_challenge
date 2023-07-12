package com.miguelamer.rickmortychallenge.api

import com.miguelamer.rickmortychallenge.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersAPI {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterResponse>

    @GET("character/")
    suspend fun searchCharacters(
        @Query("name") page: String,
        @Query("page") charactersSearchPage: Int
    ): Response<CharacterResponse>
}