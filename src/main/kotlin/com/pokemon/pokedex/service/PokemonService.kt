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
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class PokemonService(
    val pokemonJpaRepository: PokemonJpaRepository,
    val pokemonTypeRepository: PokemonTypeJpaRepository,
) {

    private val logger = KotlinLogging.logger {}

    val objectMapper = ObjectMapper()

    fun getPokemonById(id: Long, language: String?): Pokemon {
        val pokemonEntity = pokemonJpaRepository.getById(id)

        return pokemonEntity.toModel(language)
    }

    fun getAllPokemonTypes(): List<String> {
        return pokemonTypeRepository.getDistinctByType()
    }

    fun updatePokemonCaughtStatus(id: Long, caught: Boolean) {
        val pokemonEntity = pokemonJpaRepository.getById(id)
        pokemonEntity.caught = caught
        try {
            pokemonJpaRepository.save(pokemonEntity)
        } catch (ex: Exception) {
            throw RuntimeException("Unable to mark pokemon's caught status as $caught, check your pokemon storage.")
        }
    }

    fun getAllPokemonByFilter(type: String?, caught: Boolean?, name: String?, language: String?, pageable: Pageable): List<Pokemon> {
        return pokemonJpaRepository.findAll(buildSpec(type, name, caught), pageable)
            .stream()
            .map { it ->
                if (name != null && language != null) {
                    it.toModelWithSpecifiedNameAndLang(name, language)
                } else if (name != null) {
                    it.toModelWithCorrespondingName(name)
                } else if (language != null) {
                    it.toModel(language)
                } else {
                    it.toModel(null)
                }
            }
            .collect(Collectors.toList())
    }

    private fun buildSpec(
        type: String?,
        name: String?,
        caught: Boolean?
    ): Specification<PokemonEntity>? {

        var listOfSpecification = ArrayList<Specification<PokemonEntity>>()

        if (type != null) {
            listOfSpecification.add(PokemonSpecifications.hasType(type))
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
        val pokemonList: List<PokemonEntity> = objectMapper.readValue(file)
        logger.info("$pokemonList")
        pokemonJpaRepository.saveAll(pokemonList)
    }
}
