package blog.tsalikis.starwars.characters.ui

import app.cash.turbine.test
import blog.tsalikis.starwars.characters.datasource.StarWarsDataSource
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
            listOf(
                "Luke Skywalker",
                "C-3PO"
            )
        )

        viewModel.charactersFlow.test {
            assertThat(awaitItem()).isEqualTo(CharactersState.Loading)

            viewModel.fetchCharacters()

            assertThat(awaitItem()).isEqualTo(
                CharactersState.Characters(
                    listOf(
                        "Luke Skywalker",
                        "C-3PO"
                    )
                )
            )
        }
    }
}
