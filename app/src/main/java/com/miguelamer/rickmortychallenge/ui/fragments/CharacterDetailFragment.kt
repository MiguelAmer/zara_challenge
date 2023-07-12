package com.miguelamer.rickmortychallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.miguelamer.rickmortychallenge.CharacterResult
import com.miguelamer.rickmortychallenge.R
import com.miguelamer.rickmortychallenge.databinding.FragmentCharacterDetailBinding
import com.miguelamer.rickmortychallenge.ui.CharactersViewModel
import com.miguelamer.rickmortychallenge.ui.RickMortyActivity
import com.miguelamer.rickmortychallenge.ui.fragments.CharactersListFragment.Companion.EXTRA_CHARACTER

class CharacterDetailFragment: Fragment() {

    lateinit var binding: FragmentCharacterDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(arguments)
    }

    private fun initViews(arguments: Bundle?) {
        val character = arguments?.getSerializable(EXTRA_CHARACTER)
        if (character is CharacterResult) {
            with(binding) {
                Glide.with(root).load(character.image).into(imageviewCharacterImage)
                textviewCharacterName.text = character.name
                textviewCharacterSpecies.text = character.species
                textviewCharacterGender.text = character.gender
                textviewCharacterOrigin.text = character.origin.name
                textviewCharacterStatus.text = character.status
            }
        }
    }


}