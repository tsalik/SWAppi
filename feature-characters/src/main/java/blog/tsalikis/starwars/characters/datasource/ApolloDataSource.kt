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
        if (connectivityCheck.isNetworkAvailable()) {
            val response = apolloClient.query(GetPeopleQuery()).execute()
            return when {
                response.exception != null -> Either.Left(Errors.Generic)
                response.hasErrors() -> Either.Left(Errors.Generic)
                else -> {
                    val allPersons =
                        response.data?.allPeople?.people?.filterNotNull() ?: emptyList()
                    Either.Right(
                        allPersons.mapToStarWarsPerson()
                    )
                }
            }
        } else {
            return Either.Left(Errors.NoConnection)
        }
    }

    override suspend fun personDetails(id: String): ContentError<StarWarsPlanet> {
        if (connectivityCheck.isNetworkAvailable()) {
            val response = apolloClient.query(GetPersonQuery(id)).execute()
            return when {
                response.exception != null -> Either.Left(Errors.Generic)
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
        } else {
            return Either.Left(Errors.NoConnection)
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
}
