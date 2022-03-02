package com.pokemon.pokedex.model.entity

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

//  @Embedded
//  @AttributeOverride(name = "name", column = Column(name = "name"))
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    val name: NameEntity,

//  @ElementCollection
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn
    val type: List<PokemonTypeEntity>,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    val base: BaseEntity,

    var caught: Boolean = false
) {
    fun toModel(language: String): Pokemon {
        val getNameInSpecifiedLanguage =
            when (language) {
                "english" -> name.english
                "japanese" -> name.japanese
                "chinese" -> name.chinese
                "french" -> name.french
                else -> name.english
            }

        return Pokemon(
            id,
            getNameInSpecifiedLanguage,
            type.stream().map {
                it ->
                it.type
            }.collect(Collectors.toList()),
            base.toModel(),
            caught
        )
    }
}
