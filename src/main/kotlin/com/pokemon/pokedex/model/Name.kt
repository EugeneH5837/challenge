package com.pokemon.pokedex.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class Name(
    val english: String? = null,
    val japanese: String? = null,
    val chinese: String? = null,
    val french: String? = null
)
