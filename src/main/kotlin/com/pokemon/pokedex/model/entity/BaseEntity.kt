package com.pokemon.pokedex.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.pokemon.pokedex.model.Base
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
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

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue
  @JsonIgnore
  open var id: Long? = null
}