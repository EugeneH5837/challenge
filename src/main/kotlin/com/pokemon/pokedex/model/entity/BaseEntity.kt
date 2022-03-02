package com.pokemon.pokedex.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.pokemon.pokedex.model.Base
import javax.persistence.Embeddable

@Embeddable
data class BaseEntity(

  @JsonProperty("HP")
  val hp: Long,

  @JsonProperty("Attack")
  val attack: Long,

  @JsonProperty("Defense")
  val defense: Long,

  @JsonProperty("Sp. Attack")
  val specialAttack: Long,

  @JsonProperty("Sp. Defense")
  val specialDefense: Long,

  @JsonProperty("Speed")
  val speed: Long
) {
  fun toModel() : Base {
    return Base(
      hp,
      attack,
      defense,
      specialAttack,
      specialDefense,
      speed
    )
  }
}