package blog.tsalikis.starwars.characters.datasource

import blog.tsalikis.starwars.characters.domain.ContentError

interface StarWarsDataSource {
    suspend fun allCharacters(): ContentError<List<String>>
}
