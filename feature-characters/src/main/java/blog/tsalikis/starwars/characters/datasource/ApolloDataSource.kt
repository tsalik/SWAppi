package blog.tsalikis.starwars.characters.datasource

import arrow.core.Either
import blog.tsalikis.starwars.characters.domain.ContentError
import blog.tsalikis.starwars.characters.domain.Errors
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import blog.tsalikis.starwars.characters.domain.StarWarsPlanet
import blog.tsalikis.starwars.datasource.graphql.GetPeopleQuery
import blog.tsalikis.starwars.datasource.graphql.GetPersonQuery
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject

class ApolloDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    private val connectivityCheck: ConnectivityCheck,
) : StarWarsDataSource {

    override suspend fun allCharacters(): ContentError<List<StarWarsCharacter>> {
        val response = apolloClient.query(GetPeopleQuery()).execute()
        return when {
            response.exception != null -> handleExceptions()
            response.hasErrors() -> Either.Left(Errors.Generic)
            else -> {
                val allPersons =
                    response.data?.allPeople?.people?.filterNotNull() ?: emptyList()
                Either.Right(
                    allPersons.mapToStarWarsPerson()
                )
            }
        }
    }

    override suspend fun personDetails(id: String): ContentError<StarWarsPlanet> {
        val response = apolloClient.query(GetPersonQuery(id)).execute()
        return when {
            response.exception != null -> handleExceptions()
            response.hasErrors() -> Either.Left(Errors.Generic)
            else -> {
                val planet =
                    response.data?.person?.homeworld
                if (planet?.name != null) {
                    Either.Right(
                        StarWarsPlanet(
                            name = planet.name,
                            diameter = planet.diameter,
                            climates = planet.climates?.filterNotNull() ?: emptyList(),
                        )
                    )
                } else {
                    Either.Left(Errors.NotFound)
                }
            }
        }
    }

    private fun List<GetPeopleQuery.Person>.mapToStarWarsPerson(): List<StarWarsCharacter> {
        return filterNot { personQuery ->
            personQuery.name == null
        }.map { personQuery ->
            StarWarsCharacter(
                name = requireNotNull(personQuery.name),
                heightInCm = personQuery.height,
                massInKg = personQuery.mass,
                id = personQuery.id,
            )
        }
    }

    private fun <T> handleExceptions(): ContentError<T> {
        return if (connectivityCheck.isNetworkAvailable().not()) {
            Either.Left(Errors.NoConnection)
        } else {
            Either.Left(Errors.Generic)
        }
    }
}
