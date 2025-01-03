package blog.tsalikis.starwars.characters.datasource

import arrow.core.Either
import blog.tsalikis.starwars.characters.domain.Errors
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
    fun `should return the character names on network success`() = runTest {
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
                          "name": "Luke Skywalker"
                        },
                        {
                          "name": "C-3PO"
                        }
                      ]
                    }
                  }
                }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = apolloDataSource.allCharacters()

        assertThat(result).isEqualTo(Either.Right(listOf("Luke Skywalker", "C-3PO")))
    }

    @Test
    fun `should return an internet connection error upon UnknownHostException`() = runTest {
        whenever(connectivityCheck.isNetworkAvailable()).thenReturn(false)

        val result = apolloDataSource.allCharacters()

        assertThat(result).isEqualTo(Either.Left(Errors.NoConnection))
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
    fun `should return generic error on GraphQL error`() = runTest {
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
