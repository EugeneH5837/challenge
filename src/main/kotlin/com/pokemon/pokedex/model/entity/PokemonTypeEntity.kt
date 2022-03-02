package com.pokemon.pokedex.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pokemon.pokedex.model.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class PokemonTypeEntity(
    val type: String
) {
    fun toModel(): Type {
        return Type(
            type
        )
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @JsonIgnore
    open var id: Long? = null
}
