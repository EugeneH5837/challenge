package com.pokemon.pokedex.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pokemon.pokedex.model.Pokemon
import com.pokemon.pokedex.model.entity.PokemonEntity
import com.pokemon.pokedex.repository.PokemonRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class PokemonService(
  val pokemonRepository: PokemonRepository
) {

  private val logger = KotlinLogging.logger {}

  val objectMapper = ObjectMapper()

  fun getPokemonById(id: Long, language: String): Pokemon {

    val pokemonEntity = pokemonRepository.getById(id)

    return pokemonEntity.toModel(language)
  }

  fun updatePokemonCaughtStatus(id: Long, caught: Boolean){
    val pokemonEntity = pokemonRepository.getById(id)
    pokemonEntity.caught = caught
  }

  fun loadPokemon(){
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    val file = this::class.java.classLoader.getResource("pokemon.json").readText()
    val pokemonList: List<PokemonEntity> = objectMapper.readValue(file)
    logger.info("$pokemonList")
    pokemonRepository.saveAll(pokemonList)
  }
}