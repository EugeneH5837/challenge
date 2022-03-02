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
                criteriaBuilder.like(root.join<PokemonEntity, PokemonTypeEntity>("type").get("type"), type)
            }
        }

        fun hasName(name: String): Specification<PokemonEntity> {
            return Specification { root, query, criteriaBuilder ->
                val names: Join<PokemonEntity, NameEntity> = root.join("name")

                criteriaBuilder.or(
                    criteriaBuilder.like(names.get("english"), name),
                    criteriaBuilder.like(names.get("japanese"), name),
                    criteriaBuilder.like(names.get("chinese"), name),
                    criteriaBuilder.like(names.get("french"), name),
                )
            }
        }
    }
}
