package com.pokemon.pokedex.model.entity

import com.pokemon.pokedex.model.Name
import com.pokemon.pokedex.model.Pokemon
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
    val name: NameEntity,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn
    val type: List<PokemonTypeEntity>,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    val base: BaseEntity,

    var caught: Boolean = false
) {
    fun toModel(language: String?): Pokemon {
        var name: Name? = null
        when (language) {
            "english" -> {
                name = Name(english = this.name.english)
            }
            "japanese" -> {
                name = Name(japanese = this.name.japanese)
            }
            "chinese" -> {
                name = Name(chinese = this.name.chinese)
            }
            "french" -> {
                name = Name(french = this.name.french)
            }
            else -> {
                name = Name(
                    english = this.name.english,
                    japanese = this.name.japanese,
                    chinese = this.name.chinese,
                    french = this.name.french
                )
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
            if (name.english == inputName) {
                Name(english = name.english)
            } else if (name.japanese == inputName) {
                Name(japanese = name.japanese)
            } else if (name.chinese == inputName) {
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
