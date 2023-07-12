package com.miguelamer.rickmortychallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miguelamer.rickmortychallenge.repository.CharactersRepository

class CharactersViewModelProviderFactory(
    val charactersRepository: CharactersRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharactersViewModel(charactersRepository) as T
    }
}