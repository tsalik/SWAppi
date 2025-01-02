package blog.tsalikis.starwars.characters

import app.cash.turbine.test
import blog.tsalikis.starwars.characters.ui.CharactersState
import blog.tsalikis.starwars.characters.ui.CharactersViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CharactersViewModelTest {

    private val viewModel = CharactersViewModel()

    @Test
    fun `should show loading when initially fetching characters`() = runTest {
        viewModel.charactersFlow.test {
            assertThat(awaitItem()).isEqualTo(CharactersState.Loading)
        }
    }
}
