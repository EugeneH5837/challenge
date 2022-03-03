package com.pokemon.pokedex.controller

import com.pokemon.pokedex.model.Pokemon
import com.pokemon.pokedex.service.PokemonService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pokemon")
class PokemonController(
    val pokemonService: PokemonService
) {

    @GetMapping("/{id}")
    fun getPokemonById(
        @PathVariable id: Long,
        @RequestParam(required = false) language: String? = null
    ): ResponseEntity<Pokemon> {

        return ResponseEntity.ok(pokemonService.getPokemonById(id, language))
    }

    @PutMapping("/{id}")
    fun updatePokemonCaughtStatus(
        @PathVariable id: Long,
        @RequestParam caught: Boolean
    ): ResponseEntity<String> {
        pokemonService.updatePokemonCaughtStatus(id, caught)

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/name/{name}")
    fun updatePokemonCaughtStatusByName(
        @PathVariable name: String,
        @RequestParam caught: Boolean
    ): ResponseEntity<String> {
        return ResponseEntity.ok("asdf")
    }

    @GetMapping("/types")
    fun getPokemonTypes(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(pokemonService.getAllPokemonTypes())
    }

    @GetMapping("/list")
    fun getAllPokemonWithFilter(
        @RequestParam(required = false) type: Array<String>?,
        @RequestParam(required = false) caught: Boolean?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) language: String?,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int
    ): ResponseEntity<List<Pokemon>> {
        return ResponseEntity.ok(
            pokemonService.getAllPokemonByFilter(
                type,
                caught,
                name?.lowercase(),
                language,
                PageRequest.of(page, size)
            )
        )
    }

    @GetMapping("/loaddata")
    fun loadData() {
        pokemonService.loadPokemon()
    }

    @GetMapping("/testArr")
    fun testArrReq(
        @RequestParam test: Array<String>
    ): ResponseEntity<Array<String>> {

        return ResponseEntity.ok(test)
    }
}
