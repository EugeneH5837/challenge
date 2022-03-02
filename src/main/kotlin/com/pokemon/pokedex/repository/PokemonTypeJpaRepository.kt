package com.pokemon.pokedex.repository

import com.pokemon.pokedex.model.entity.PokemonTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.web.bind.annotation.RestController

@RestController
interface PokemonTypeJpaRepository : JpaRepository<PokemonTypeEntity, Long> {
    @Query(
        value = "SELECT DISTINCT(PTE.TYPE) \n" +
            "FROM POKEMON_TYPE_ENTITY PTE \n" +
            "WHERE PTE.TYPE is not null",
        nativeQuery = true
    )
    fun getDistinctByType(): List<String>
}
