package blog.tsalikis.starwars.characters.datasource

import arrow.core.Either
import blog.tsalikis.starwars.characters.domain.Errors
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import com.apollographql.apollo.ApolloClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ApolloDataSourceTest {

    private val mockWebServer = MockWebServer()
    private val apolloClient = ApolloClient.Builder()
        .serverUrl(mockWebServer.url("/").toUrl().toString())
        .build()
    private val connectivityCheck = mock<ConnectivityCheck>()
    private val apolloDataSource = ApolloDataSource(apolloClient, connectivityCheck)

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should return the characters who have non-null name on network success`() = runTest {
        whenever(connectivityCheck.isNetworkAvailable()).thenReturn(true)
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                  "data": {
                    "allPeople": {
                      "people": [
                        {
                          "name": "Luke Skywalker",
                          "id": "cGVvcGxlOjE="
                        },
                        {
                          "name": "C-3PO",
                          "id": "cGVvcGxlOjI="
                        },
                        {
                          "id": "cGVvcGxlOjM="
                        }
                      ]
                    }
                  }
                }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = apolloDataSource.allCharacters()

        assertThat(result).isEqualTo(
            Either.Right(
                listOf(
                    StarWarsCharacter(
                        name = "Luke Skywalker",
                        heightInCm = null,
                        massInKg = null,
                        id = "cGVvcGxlOjE="
                    ),
                    StarWarsCharacter(
                        name = "C-3PO",
                        heightInCm = null,
                        massInKg = null,
                        id = "cGVvcGxlOjI="
                    )
                )
            )
        )
    }

    @Test
    fun `should return generic error upon 500 HTTP error`() = runTest {
        whenever(connectivityCheck.isNetworkAvailable()).thenReturn(true)
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("")
        mockWebServer.enqueue(mockResponse)

        val result = apolloDataSource.allCharacters()

        assertThat(result).isEqualTo(Either.Left(Errors.Generic))
    }

    @Test
    fun `should return generic error on json error`() = runTest {
        whenever(connectivityCheck.isNetworkAvailable()).thenReturn(true)
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                malformed response
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = apolloDataSource.allCharacters()

        assertThat(result).isEqualTo(Either.Left(Errors.Generic))
    }
}
