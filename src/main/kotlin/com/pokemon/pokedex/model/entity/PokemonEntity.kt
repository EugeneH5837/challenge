package com.pokemon.pokedex.model.entity

import com.pokemon.pokedex.model.Name
import com.pokemon.pokedex.model.Pokemon
import com.pokemon.pokedex.utility.Constants.CHINESE
import com.pokemon.pokedex.utility.Constants.ENGLISH
import com.pokemon.pokedex.utility.Constants.FRENCH
import com.pokemon.pokedex.utility.Constants.JAPANESE
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.stream.Collectors
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class PokemonEntity(

    @Id
    val id: Long,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    var name: NameEntity,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn
    val type: List<PokemonTypeEntity>,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    val base: BaseEntity,

    var caught: Boolean = false
) {
    fun toModel(language: String? = null): Pokemon {
        val name: Name
        when (language) {
            ENGLISH -> {
                name = Name(english = this.name.english)
            }
            JAPANESE -> {
                name = Name(japanese = this.name.japanese)
            }
            CHINESE -> {
                name = Name(chinese = this.name.chinese)
            }
            FRENCH -> {
                name = Name(french = this.name.french)
            }
            null -> {
                name = Name(
                    english = this.name.english,
                    japanese = this.name.japanese,
                    chinese = this.name.chinese,
                    french = this.name.french
                )
            }
            else -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid language passed as param")
            }
        }

        return Pokemon(
            id,
            name,
            type.stream().map {
                it.type
            }.collect(Collectors.toList()),
            base.toModel(),
            caught
        )
    }

    fun toModelWithCorrespondingName(inputName: String): Pokemon {
        val getNameInOneLanguage: Name =
            if (name.english.lowercase() == inputName) {
                Name(english = name.english)
            } else if (name.japanese.lowercase() == inputName) {
                Name(japanese = name.japanese)
            } else if (name.chinese.lowercase() == inputName) {
                Name(chinese = name.chinese)
            } else {
                Name(french = name.french)
            }

        return Pokemon(
            id,
            getNameInOneLanguage,
            type.stream().map {
                it.type
            }.collect(Collectors.toList()),
            base.toModel(),
            caught
        )
    }
}
