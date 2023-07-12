package com.miguelamer.rickmortychallenge.repository

import com.miguelamer.rickmortychallenge.api.RetrofitInstance

class CharactersRepository {

    suspend fun getCharacters(pageNumber: Int) = RetrofitInstance.api.getCharacters(pageNumber)

    suspend fun searchCharacters(name: String, charactersSearchPage: Int) = RetrofitInstance.api.searchCharacters(name, charactersSearchPage)
}