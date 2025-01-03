package blog.tsalikis.starwars.characters.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import arrow.core.Either
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.characters.datasource.StarWarsDataSource
import blog.tsalikis.starwars.characters.domain.Errors
import blog.tsalikis.starwars.characters.domain.StarWarsPlanet
import blog.tsalikis.starwars.util.CoroutineTestExtension
import blog.tsalikis.starwars.util.InstantExecutorExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class, InstantExecutorExtension::class)
class CharacterDetailsViewModelTest {

    private val starWarsDataSource = mock<StarWarsDataSource>()
    private val viewModel by lazy {
        CharacterDetailsViewModel(
            savedStateHandle =
            SavedStateHandle(mapOf("id" to "cGVvcGxlOjE", "name" to "Luke Skywalker")),
            starWarsDataSource
        )
    }

    @Test
    fun `should show idle with the character's name initially`() = runTest {
        viewModel.detailsFlow.test {
            assertThat(awaitItem()).isEqualTo(CharacterDetailsState.Idle("Luke Skywalker"))
        }
    }

    @Test
    fun `should show success upon fetching the person's details`() = runTest {
        viewModel.detailsFlow.test {
            awaitItem()
            whenever(starWarsDataSource.personDetails("cGVvcGxlOjE"))
                .thenReturn(
                    Either.Right(
                        StarWarsPlanet(
                            name = "Tatooine",
                            diameter = 10465,
                            climates = listOf("arid"),
                        )
                    )
                )

            viewModel.fetchDetails()

            assertThat(awaitItem()).isEqualTo(CharacterDetailsState.Loading)
            assertThat(awaitItem()).isEqualTo(
                CharacterDetailsState.Success(
                    StarWarsPlanet(
                        name = "Tatooine",
                        diameter = 10465,
                        climates = listOf("arid"),
                    )
                )
            )
        }
    }

    @Test
    fun `should show failure when there is an error fetching the person's details`() = runTest {
        viewModel.detailsFlow.test {
            awaitItem()
            whenever(starWarsDataSource.personDetails("cGVvcGxlOjE"))
                .thenReturn(
                    Either.Left(
                        Errors.Generic
                    )
                )

            viewModel.fetchDetails()

            assertThat(awaitItem()).isEqualTo(CharacterDetailsState.Loading)
            assertThat(awaitItem()).isEqualTo(
                CharacterDetailsState.Failure(
                    R.string.error_generic_title,
                    R.string.error_generic_message
                )
            )
        }
    }

}