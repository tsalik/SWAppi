package blog.tsalikis.starwars.characters.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme

@Composable
fun CharactersScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.charactersFlow.collectAsStateWithLifecycle()
    Column(modifier = modifier) {
        Text(state.toString())
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersScreenPreview() {
    StarWarsAppTheme {
        CharactersScreen(modifier = Modifier.fillMaxSize())
    }
}
