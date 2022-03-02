package com.pokemon.pokedex.model.entity

import com.pokemon.pokedex.model.Type
import javax.persistence.Embeddable

@Embeddable
data class PokemonTypeEntity(
  val type: String
) {
  fun toModel(): Type {
    return Type(
      type
    )
  }
}