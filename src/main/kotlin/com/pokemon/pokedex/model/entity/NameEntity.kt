package com.pokemon.pokedex.model.entity

import javax.persistence.Embeddable

@Embeddable
data class NameEntity(
  val english: String,
  val japanese: String,
  val chinese: String,
  val french: String
)