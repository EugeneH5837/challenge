package com.pokemon.pokedex.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Pokemon(
    val id: Long,
    val name: Name?,
    val type: List<String>,
    val base: Base,
    var caught: Boolean
)
