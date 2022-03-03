package com.pokemon.pokedex.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pokemon.pokedex.model.Pokemon
import com.pokemon.pokedex.model.entity.PokemonEntity
import com.pokemon.pokedex.repository.PokemonJpaRepository
import com.pokemon.pokedex.repository.PokemonSpecifications
import com.pokemon.pokedex.repository.PokemonTypeJpaRepository
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@Service
class PokemonService(
    val pokemonRepository: PokemonJpaRepository,
    val pokemonTypeRepository: PokemonTypeJpaRepository,
) {

    private val logger = KotlinLogging.logger {}

    val objectMapper = ObjectMapper()

    fun getPokemonById(id: Long, language: String?): Pokemon {
        val pokemonEntity = pokemonRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find pokemon with ID: $id") }

        return pokemonEntity.toModel(language)
    }

    fun getAllPokemonTypes(): List<String> {
        return pokemonTypeRepository.getDistinctByType()
    }

    fun updatePokemonCaughtStatus(id: Long, caught: Boolean) {
        val pokemonEntity = pokemonRepository.getById(id)
        pokemonEntity.caught = caught
        try {
            pokemonRepository.save(pokemonEntity)
        } catch (ex: Exception) {
            throw RuntimeException("Unable to mark pokemon's caught status as $caught, check your pokemon storage.")
        }
    }

    fun getAllPokemonByFilter(type: Array<String>?, caught: Boolean?, name: String?, language: String?, pageable: Pageable): List<Pokemon> {
        return pokemonRepository.findAll(buildSpec(type, name, caught), pageable)
            .stream()
            .map { it ->
                if (language != null) {
                    it.toModel(language)
                } else if (name != null) {
                    it.toModelWithCorrespondingName(name)
                } else {
                    it.toModel(null)
                }
            }
            .collect(Collectors.toList())
    }

    private fun buildSpec(
        type: Array<String>?,
        name: String?,
        caught: Boolean?
    ): Specification<PokemonEntity>? {

        val listOfSpecification = ArrayList<Specification<PokemonEntity>>()

        type?.forEach {
            listOfSpecification.add(PokemonSpecifications.hasType(it))
        }

        if (name != null) {
            listOfSpecification.add(PokemonSpecifications.hasName(name))
        }

        if (caught != null) {
            listOfSpecification.add(PokemonSpecifications.isCaught(caught))
        }

        var specifications: Specification<PokemonEntity>? = null

        for (i in listOfSpecification.indices) {
            specifications = if (specifications == null) {
                where(listOfSpecification[i])
            } else {
                specifications.and(listOfSpecification[i])
            }
        }

        return specifications
    }

    fun loadPokemon() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val file = this::class.java.classLoader.getResource("data/pokemon.json").readText()
        val pokemonList = objectMapper.readValue<List<PokemonEntity>>(file)

        pokemonRepository.saveAll(pokemonList)
    }
}
