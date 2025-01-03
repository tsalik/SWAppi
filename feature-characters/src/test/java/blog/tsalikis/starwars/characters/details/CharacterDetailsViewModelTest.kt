package blog.tsalikis.starwars.characters.details

import androidx.lifecycle.SavedStateHandle
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

}