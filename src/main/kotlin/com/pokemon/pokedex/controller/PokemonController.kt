package com.pokemon.pokedex.controller

import com.pokemon.pokedex.model.Pokemon
import com.pokemon.pokedex.model.entity.PokemonEntity
import com.pokemon.pokedex.service.PokemonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/api/v1/pokemon")
class PokemonController(
    val pokemonService: PokemonService
) {

    @GetMapping("/{id}")
    fun getPokemonById(
        @PathVariable id: Long,
        @RequestParam language: String
    ): ResponseEntity<Pokemon> {

        return ResponseEntity.ok(pokemonService.getPokemonById(id, language))
    }

    @PostMapping("/{id}")
    fun updatePokemonCaughtStatus(
        @PathVariable id: Long,
        @RequestParam caught: Boolean
    ): ResponseEntity<String> {
        pokemonService.updatePokemonCaughtStatus(id, caught)

        return ResponseEntity.noContent().build()
    }

    @GetMapping("/types")
    fun getPokemonTypes() {

    }

    @GetMapping("/list")
    fun getAllPokemonWithFilter(
        @RequestParam name: String,
        @RequestParam type: String,
        @RequestParam caught: Boolean
    ) {

    }

    @GetMapping("/loaddata")
    fun loadData() {
        pokemonService.loadPokemon()
    }
}