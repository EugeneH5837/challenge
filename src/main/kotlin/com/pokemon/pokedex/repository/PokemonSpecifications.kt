package com.pokemon.pokedex.repository

import com.pokemon.pokedex.model.entity.NameEntity
import com.pokemon.pokedex.model.entity.PokemonEntity
import com.pokemon.pokedex.model.entity.PokemonTypeEntity
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.Join

class PokemonSpecifications {
    companion object {
        fun isCaught(caught: Boolean): Specification<PokemonEntity> {
            return Specification { root, query, criteriaBuilder ->
                criteriaBuilder.equal(root.get<PokemonEntity>("caught"), caught)
            }
        }

        fun hasType(type: String): Specification<PokemonEntity> {
            return Specification { root, query, criteriaBuilder ->
                query.distinct(true)
                criteriaBuilder.like(
                    criteriaBuilder.lower(
                        root.join<PokemonEntity, PokemonTypeEntity>("type").get("type"))
                    , type.lowercase()
                )
            }
        }

        fun hasName(name: String): Specification<PokemonEntity> {
            return Specification { root, query, criteriaBuilder ->
                val names: Join<PokemonEntity, NameEntity> = root.join("name")

                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(names.get("english")), name.lowercase()),
                    criteriaBuilder.like(criteriaBuilder.lower(names.get("japanese")), name.lowercase()),
                    criteriaBuilder.like(criteriaBuilder.lower(names.get("chinese")), name.lowercase()),
                    criteriaBuilder.like(criteriaBuilder.lower(names.get("french")), name.lowercase()),
                )
            }
        }
    }
}
