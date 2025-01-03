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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme
import kotlinx.collections.immutable.toImmutableList

const val CHARACTERS = "characters"

fun NavGraphBuilder.characters(onDetails: (String, String) -> Unit) {
    composable(CHARACTERS) {
        val viewModel = hiltViewModel<CharactersViewModel>()
        val state by viewModel.charactersFlow.collectAsStateWithLifecycle()
        CharactersScreen(
            state = state,
            onRetry = { viewModel.fetchCharacters() },
            onDetails = onDetails
        )
    }
}

@Composable
fun CharactersScreen(
    state: CharactersState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onDetails: (String, String) -> Unit,
) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when (state) {
                is CharactersState.Failure -> {
                    ErrorScreen(state.title, state.message, onRetry = {
                        onRetry.invoke()
                    })
                }

                CharactersState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                is CharactersState.Success -> {
                    val listState = rememberLazyListState()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        state = listState,
                    ) {
                        itemsIndexed(
                            items = state.characters,
                            itemContent = { index, item ->
                                CharacterItem(
                                    character = item,
                                    showDivider = index < state.characters.lastIndex,
                                    modifier = Modifier.fillMaxWidth(),
                                    onDetails = { onDetails(item.id, item.name) }
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
fun CharactersScreenSuccessPreview() {
    StarWarsAppTheme {
        CharactersScreen(
            state = CharactersState.Success(
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
            ),
            modifier = Modifier.fillMaxSize(),
            onRetry = {},
            onDetails = { _, _ -> }
        )
    }
}
