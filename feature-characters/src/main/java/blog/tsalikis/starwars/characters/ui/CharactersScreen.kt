package blog.tsalikis.starwars.characters.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme

@Composable
fun CharactersScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.charactersFlow.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            Column {
                Text(
                    stringResource(R.string.characters_screen_title),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface, thickness = 1.dp)
            }
        },
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
            when (state) {
                is CharactersState.Failure -> {
                    val error = state as CharactersState.Failure
                    ErrorScreen(error.title, error.message, onRetry = {
                        viewModel.fetchCharacters()
                    })
                }

                CharactersState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                is CharactersState.Success -> {
                    val success = state as CharactersState.Success
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        itemsIndexed(
                            items = success.characters,
                            itemContent = { index, item ->
                                CharacterItem(
                                    character = item,
                                    showDivider = index < success.characters.lastIndex,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersScreenPreview() {
    StarWarsAppTheme {
        CharactersScreen(modifier = Modifier.fillMaxSize())
    }
}
