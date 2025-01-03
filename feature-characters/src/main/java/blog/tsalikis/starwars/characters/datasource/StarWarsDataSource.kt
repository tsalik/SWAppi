package blog.tsalikis.starwars.characters.datasource

import blog.tsalikis.starwars.characters.domain.ContentError
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import blog.tsalikis.starwars.characters.domain.StarWarsPlanet

interface StarWarsDataSource {
    suspend fun allCharacters(): ContentError<List<StarWarsCharacter>>
    suspend fun personDetails(id: String): ContentError<StarWarsPlanet>
}
