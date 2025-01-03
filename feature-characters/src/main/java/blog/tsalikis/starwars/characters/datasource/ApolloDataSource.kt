package blog.tsalikis.starwars.characters.datasource

import arrow.core.Either
import blog.tsalikis.starwars.characters.domain.ContentError
import blog.tsalikis.starwars.characters.domain.Errors
import blog.tsalikis.starwars.datasource.graphql.AllCharactersQuery
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject

class ApolloDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    private val connectivityCheck: ConnectivityCheck,
) : StarWarsDataSource {

    override suspend fun allCharacters(): ContentError<List<String>> {
        if (connectivityCheck.isNetworkAvailable()) {
            val response = apolloClient.query(AllCharactersQuery()).execute()
            return when {
                response.exception != null -> Either.Left(Errors.Generic)
                response.hasErrors() -> Either.Left(Errors.Generic)
                else -> {
                    val allPersons =
                        response.data?.allPeople?.people?.filterNotNull() ?: emptyList()
                    Either.Right(
                        allPersons.filterNot { it.name == null }
                            .map { requireNotNull(it.name) }
                    )
                }
            }
        } else {
            return Either.Left(Errors.NoConnection)
        }
    }
}
