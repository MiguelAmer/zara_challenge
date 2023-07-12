package com.miguelamer.rickmortychallenge.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelamer.rickmortychallenge.CharacterResponse
import com.miguelamer.rickmortychallenge.repository.CharactersRepository
import com.miguelamer.rickmortychallenge.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CharactersViewModel(
    private val charactersRepository: CharactersRepository
): ViewModel() {

    val characters: MutableLiveData<Resource<CharacterResponse>> = MutableLiveData()
    var charactersPage = 1
    var charactersSearchPage = 1
    var charactersStoredResponse: CharacterResponse? = null

    init {
        getCharacters()
    }

    fun getCharacters() = viewModelScope.launch {
        characters.postValue(Resource.Loading())
        val response = charactersRepository.getCharacters(charactersPage)
        characters.postValue(handleGetCharactersResponse(response))
    }

    fun searchCharacters(name: String) = viewModelScope.launch {
        characters.postValue(Resource.Loading())
        val response = charactersRepository.searchCharacters(name, charactersSearchPage)
        characters.postValue(handleSearchCharactersResponse(response))
    }

    private fun handleGetCharactersResponse(response: Response<CharacterResponse>): Resource<CharacterResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (charactersPage == 1) {
                    charactersStoredResponse = null
                }
                charactersPage++
                charactersSearchPage = 1
                if (charactersStoredResponse == null) {
                    charactersStoredResponse = it
                } else {
                    val oldCharacters = charactersStoredResponse?.results
                    val newCharacters = it.results
                    oldCharacters?.addAll(newCharacters)
                }
                return Resource.Success(charactersStoredResponse ?: it)
            }
        }
        return Resource.Error(message = response.message())
    }

    private fun handleSearchCharactersResponse(response: Response<CharacterResponse>): Resource<CharacterResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (charactersSearchPage == 1) {
                    charactersStoredResponse = null
                }
                charactersSearchPage++
                charactersPage = 1
                if (charactersStoredResponse == null) {
                    charactersStoredResponse = it
                } else {
                    val oldCharacters = charactersStoredResponse?.results
                    val newCharacters = it.results
                    oldCharacters?.addAll(newCharacters)
                }
                return Resource.Success(charactersStoredResponse ?: it)
            }
        }
        return Resource.Error(message = response.message())
    }
}