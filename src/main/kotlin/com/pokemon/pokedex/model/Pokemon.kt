package com.pokemon.pokedex.model

data class Pokemon(
    val id: Long,
    val name: String,
    val type: List<String>,
    val base: Base,
    var caught: Boolean
)
