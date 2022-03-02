package com.pokemon.pokedex.repository

import au.com.console.jpaspecificationdsl.join
import com.pokemon.pokedex.model.entity.PokemonEntity
import com.pokemon.pokedex.model.entity.PokemonTypeEntity
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.web.bind.annotation.RestController
import javax.persistence.criteria.Join

@RestController
interface PokemonRepository : JpaRepository<PokemonEntity, Long>, JpaSpecificationExecutor<PokemonEntity> {
//  fun getPokemonEntitiesByCaughtAndTypeContainsAndNameContains(
//    caught: Boolean,
//    type: List<PokemonTypeEntity>,
//    name: NameEntity
//  )

    @Query(value = "SELECT * FROM POKEMON_ENTITY pe WHERE :types IN pe.types", nativeQuery = true)
    fun getEntities(types: String): List<PokemonEntity>

//  @Query(value = "SELECT * \n" +
//          "FROM POKEMON_ENTITY PE, POKEMON_ENTITY_TYPE PET\n" +
//          "WHERE PET.POKEMON_ENTITY_ID = PE.ID \n" +
//          "AND :type = PET.TYPE OR :type is null \n" +
//          "LIMIT 50"
//    , nativeQuery = true)
    @Query(
        value = "SELECT DISTINCT ON(ID) *\n \n" +
            "FROM POKEMON_ENTITY PE\n" +
            "INNER JOIN POKEMON_ENTITY_TYPE PET ON \n" +
            "PE.ID = PET.POKEMON_ENTITY_ID \n" +
            "AND ((:type = PET.TYPE) OR :type is null) \n" +
            "LIMIT 50",
        nativeQuery = true
    )
    fun testNative(type: String?): List<PokemonEntity>

    @Query(value = "SHOW SCHEMAS", nativeQuery = true)
    fun showSchemas()
//  fun getPokemonEntitiesByType(type: List<String>) : List<PokemonEntity>

//  fun findAllByType_Type(type: String?): List<PokemonEntity>

    @Query
    fun findAllByType_Type(type: String): Specification<PokemonEntity> {
        return Specification { root, query, criteriaBuilder ->
            val types: Join<PokemonEntity, PokemonTypeEntity> = root.join("type")

            criteriaBuilder.equal(
                types.get<PokemonTypeEntity>("type"),
                type
            )
        }
//    return Specification<PokemonEntity> {
    }
}
//
//  @Query(nativeQuery = true, value = ":query")
//  fun testString(query: String) : List<PokemonEntity>
