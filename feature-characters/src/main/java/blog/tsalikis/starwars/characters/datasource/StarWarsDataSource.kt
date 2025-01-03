package blog.tsalikis.starwars.characters.datasource

interface StarWarsDataSource {
    suspend fun allCharacters(): List<String>
}
