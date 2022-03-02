package com.pokemon.pokedex.model.entity

import com.pokemon.pokedex.model.Pokemon
import java.util.stream.Collectors
import javax.persistence.*

@Entity
data class PokemonEntity(

  @Id
  val id: Long,

  @Embedded
  @AttributeOverride(name = "name", column = Column(name = "name"))
  val name: NameEntity,

  @ElementCollection
  val type: List<PokemonTypeEntity>,

  @Embedded
  @AttributeOverride(name = "base", column = Column(name = "base"))
  val base: BaseEntity,

  var caught: Boolean = false
) {
  fun toModel(language: String): Pokemon {
    val getNameInSpecifiedLanguage =
      when(language){
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
          it -> it.type
      }.collect(Collectors.toList()),
      base.toModel(),
      caught
    )
  }
}