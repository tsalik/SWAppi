package blog.tsalikis.starwars.characters.datasource

import blog.tsalikis.starwars.characters.domain.ContentError
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter

interface StarWarsDataSource {
    suspend fun allCharacters(): ContentError<List<StarWarsCharacter>>
}
