package blog.tsalikis.starwars.characters.ui

import app.cash.turbine.test
import arrow.core.Either
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.characters.datasource.StarWarsDataSource
import blog.tsalikis.starwars.characters.domain.Errors
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import blog.tsalikis.starwars.util.CoroutineTestExtension
import blog.tsalikis.starwars.util.InstantExecutorExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class, InstantExecutorExtension::class)
class CharactersViewModelTest {

    private val starWarsDataSource = mock<StarWarsDataSource>()
    private val viewModel by lazy { CharactersViewModel(starWarsDataSource) }

    @Test
    fun `should show loading when initially fetching characters`() = runTest {
        viewModel.charactersFlow.test {
            assertThat(awaitItem()).isEqualTo(CharactersState.Loading)
        }
    }

    @Test
    fun `should show the character's names when retrieving the data successfully`() = runTest {
        whenever(starWarsDataSource.allCharacters()).thenReturn(
            Either.Right(
                listOf(
                    StarWarsCharacter(
                        name = "Luke Skywalker",
                        heightInCm = 172,
                        massInKg = 77.00,
                        id = "cGVvcGxlOjE="
                    ),
                    StarWarsCharacter(
                        name = "C-3PO",
                        heightInCm = 167,
                        massInKg = 75.00,
                        id = "cGVvcGxlOjI="
                    )
                )
            )
        )

        viewModel.charactersFlow.test {
            assertThat(awaitItem()).isEqualTo(CharactersState.Loading)

            viewModel.fetchCharacters()

            assertThat(awaitItem()).isEqualTo(
                CharactersState.Success(
                    listOf(
                        StarWarsCharacter(
                            name = "Luke Skywalker",
                            heightInCm = 172,
                            massInKg = 77.00,
                            id = "cGVvcGxlOjE="
                        ),
                        StarWarsCharacter(
                            name = "C-3PO",
                            heightInCm = 167,
                            massInKg = 75.00,
                            id = "cGVvcGxlOjI="
                        )
                    ).toImmutableList()
                )
            )
        }
    }

    @Test
    fun `should show connectivity error when the failure is NoConnection`() = runTest {
        whenever(starWarsDataSource.allCharacters()).thenReturn(Either.Left(Errors.NoConnection))

        viewModel.charactersFlow.test {
            awaitItem()

            viewModel.fetchCharacters()

            assertThat(awaitItem()).isEqualTo(
                CharactersState.Failure(
                    title = R.string.error_no_connection_title,
                    message = R.string.error_no_connection_message
                )
            )
        }
    }

    @Test
    fun `should show generic error when the failure is Generic`() = runTest {
        whenever(starWarsDataSource.allCharacters()).thenReturn(Either.Left(Errors.Generic))

        viewModel.charactersFlow.test {
            awaitItem()

            viewModel.fetchCharacters()

            assertThat(awaitItem()).isEqualTo(
                CharactersState.Failure(
                    title = R.string.error_generic_title,
                    message = R.string.error_generic_message
                )
            )
        }
    }
}
