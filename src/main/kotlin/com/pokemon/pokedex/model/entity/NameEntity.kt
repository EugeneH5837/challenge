package com.pokemon.pokedex.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class NameEntity(
  val english: String,
  val japanese: String,
  val chinese: String,
  val french: String
) {
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue
  @JsonIgnore
  var id: Long? = null
}