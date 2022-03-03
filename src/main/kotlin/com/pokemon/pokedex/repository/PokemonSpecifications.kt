package com.pokemon.pokedex.repository

import com.pokemon.pokedex.model.entity.NameEntity
import com.pokemon.pokedex.model.entity.PokemonEntity
import com.pokemon.pokedex.model.entity.PokemonTypeEntity
import com.pokemon.pokedex.utility.Constants.CAUGHT
import com.pokemon.pokedex.utility.Constants.CHINESE
import com.pokemon.pokedex.utility.Constants.ENGLISH
import com.pokemon.pokedex.utility.Constants.FRENCH
import com.pokemon.pokedex.utility.Constants.JAPANESE
import com.pokemon.pokedex.utility.Constants.NAME
import com.pokemon.pokedex.utility.Constants.TYPE
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.Join

class PokemonSpecifications {
    companion object {
        fun isCaught(caught: Boolean): Specification<PokemonEntity> {
            return Specification { root, query, criteriaBuilder ->
                criteriaBuilder.equal(root.get<PokemonEntity>(CAUGHT), caught)
            }
        }

        fun hasType(type: String): Specification<PokemonEntity> {
            return Specification { root, query, criteriaBuilder ->
                query.distinct(true)
                criteriaBuilder.like(
                    criteriaBuilder.lower(
                        root.join<PokemonEntity, PokemonTypeEntity>(TYPE).get(TYPE)
                    ),
                    type.lowercase()
                )
            }
        }

        fun hasName(name: String): Specification<PokemonEntity> {
            return Specification { root, query, criteriaBuilder ->
                val names: Join<PokemonEntity, NameEntity> = root.join(NAME)

                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(names.get(ENGLISH)), name.lowercase()),
                    criteriaBuilder.like(criteriaBuilder.lower(names.get(JAPANESE)), name.lowercase()),
                    criteriaBuilder.like(criteriaBuilder.lower(names.get(CHINESE)), name.lowercase()),
                    criteriaBuilder.like(criteriaBuilder.lower(names.get(FRENCH)), name.lowercase()),
                )
            }
        }
    }
}
