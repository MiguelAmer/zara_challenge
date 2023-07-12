package com.miguelamer.rickmortychallenge


data class CharacterResponse(
    val info: Info,
    val results: MutableList<CharacterResult>
)