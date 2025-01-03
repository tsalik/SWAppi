package blog.tsalikis.starwars.characters.datasource

import blog.tsalikis.starwars.datasource.graphql.AllCharactersQuery
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject

class ApolloDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) : StarWarsDataSource {

    override suspend fun allCharacters(): List<String> {
        val response = apolloClient.query(AllCharactersQuery()).execute()
        if (response.hasErrors()) {
            return emptyList()
        } else {
            val characters = requireNotNull(response.data)
            val allPeople = requireNotNull(characters.allPeople)
            val people = requireNotNull(allPeople.people)
            return people.map { it?.name ?: "" }
        }
    }
}
