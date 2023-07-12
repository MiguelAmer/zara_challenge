package com.miguelamer.rickmortychallenge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.miguelamer.rickmortychallenge.R
import com.miguelamer.rickmortychallenge.repository.CharactersRepository

class RickMortyActivity : AppCompatActivity() {

    lateinit var viewModel: CharactersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rick_morty)

        val repository = CharactersRepository()
        val viewModelProviderFactory = CharactersViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(CharactersViewModel::class.java)
    }
}