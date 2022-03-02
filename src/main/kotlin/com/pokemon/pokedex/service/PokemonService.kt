package com.pokemon.pokedex.service

//import au.com.console.jpaspecificationdsl.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pokemon.pokedex.model.Pokemon
import com.pokemon.pokedex.model.entity.NameEntity
import com.pokemon.pokedex.model.entity.PokemonEntity
import com.pokemon.pokedex.model.entity.PokemonTypeEntity
import com.pokemon.pokedex.repository.PokemonRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.Predicate

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

  fun getAllPokemonByFilter(type: String?, caught: Boolean?, pageable: Pageable) : Page<PokemonEntity>{
    return pokemonRepository.findAll(hasType(type!!), pageable)
  }

  fun isCaught(caught: Boolean): Specification<PokemonEntity>{
    return Specification{root, query, criteriaBuilder ->
      criteriaBuilder.equal(root.get<PokemonEntity>("caught"), caught)
    }
  }

  fun hasType(type: String): Specification<PokemonEntity>{
    return Specification{root, query, criteriaBuilder ->
      query.distinct(true)
      criteriaBuilder.like(root.join<PokemonEntity, PokemonTypeEntity>("type").get("type"), type)
    }
  }

  fun hasName(name: String): Specification<PokemonEntity> {
    return Specification{ root, query, criteriaBuilder ->
      val names: Join<PokemonEntity, NameEntity> = root.join("name")

      criteriaBuilder.or(
        criteriaBuilder.like(names.get("english"), name),
        criteriaBuilder.like(names.get("japanese"), name),
        criteriaBuilder.like(names.get("chinese"), name),
        criteriaBuilder.like(names.get("french"), name),
      )
    }
  }

  fun findBySpec(
    type: String?,
    name: String?,
    caught: Boolean?
  ): Specification<PokemonEntity>? {

    var listOfSpecification = ArrayList<Specification<PokemonEntity>>()

    if(type != null){
      listOfSpecification.add(hasType(type))
    }

    if(name != null){
      listOfSpecification.add(hasName(name))
    }

    if(caught != null){
      listOfSpecification.add(isCaught(caught))
    }

    var specifications: Specification<PokemonEntity>? = null

    for(i in listOfSpecification.indices){
      specifications = if(specifications == null){
        where(listOfSpecification[i])
      }
      else{
        specifications.or(listOfSpecification[i])
      }
    }

    return specifications
//    return pokemonRepository.findAll(specifications)
  }

  fun loadPokemon(){
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    val file = this::class.java.classLoader.getResource("pokemon.json").readText()
    val pokemonList: List<PokemonEntity> = objectMapper.readValue(file)
    logger.info("$pokemonList")
    pokemonRepository.saveAll(pokemonList)
  }
}