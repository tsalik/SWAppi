package blog.tsalikis.starwars.characters.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme

@Composable
fun CharactersScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.charactersFlow.collectAsStateWithLifecycle()
    Box(modifier = modifier) {
        when (state) {
            is CharactersState.Error -> Text(state.toString())
            CharactersState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is CharactersState.Success -> {
                val success = state as CharactersState.Success
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = success.characters,
                        itemContent = { item ->
                            CharacterItem(item, modifier = Modifier.fillMaxWidth())
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterItem(character: StarWarsCharacter, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = character.name, style = MaterialTheme.typography.titleMedium)
                if (character.heightInCm != null) {
                    Text(stringResource(R.string.height, character.heightInCm))
                }
                if (character.massInKg != null) {
                    Text(stringResource(R.string.mass, character.massInKg))
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
            )
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

@Preview(showBackground = true)
@Composable
fun CharactersItemPreview() {
    StarWarsAppTheme {
        CharacterItem(
            character = StarWarsCharacter(
                name = "Luke Skywalker",
                heightInCm = 172,
                massInKg = 77.00,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}
