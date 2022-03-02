package com.pokemon.pokedex.repository

import com.pokemon.pokedex.model.entity.PokemonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.RestController

@RestController
interface PokemonRepository: JpaRepository<PokemonEntity, Long> {

}