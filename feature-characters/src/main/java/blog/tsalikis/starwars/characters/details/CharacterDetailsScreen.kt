package blog.tsalikis.starwars.characters.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.design.theme.Black80

const val CHARACTER_DETAILS = "characterDetails/{id}/{name}"

fun NavGraphBuilder.details() {
    composable(
        route = CHARACTER_DETAILS,
        arguments = listOf(
            navArgument("id") { type = NavType.StringType },
            navArgument("name") { type = NavType.StringType }
        )
    ) {
        val viewModel = hiltViewModel<CharacterDetailsViewModel>()
        val state by viewModel.detailsFlow.collectAsStateWithLifecycle()
        CharacterDetailsScreen(
            state,
            onViewDetails = { viewModel.fetchDetails() },
            onDismissDetails = { viewModel.onDismissDetails() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    state: CharacterDetailsState,
    onViewDetails: () -> Unit,
    onDismissDetails: () -> Unit,
) {
    Scaffold(
        topBar = {
            Column {
                Text(
                    stringResource(R.string.characters_screen_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when (state) {
                is CharacterDetailsState.Idle -> CharacterDetails(state.name, onViewDetails)
                is CharacterDetailsState.Loading -> {
                    CharacterDetails(state.name) { onViewDetails.invoke() }
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(
                                Alignment.Center
                            )
                            .background(Black80)
                    )
                }

                is CharacterDetailsState.Success -> {
                    Column {
                        CharacterDetails(state.characterName) { onViewDetails.invoke() }
                        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                        ModalBottomSheet(
                            onDismissRequest = onDismissDetails,
                            sheetState = sheetState,
                            containerColor = Color.White,
                            scrimColor = Black80
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(text = state.characterName, style = MaterialTheme.typography.headlineMedium)
                                Text(text = state.starWarsPlanet.toString(), style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

                else -> Text(state.toString(), modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun CharacterDetails(name: String, onViewDetails: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxSize()
    ) {
        Text(
            stringResource(R.string.character_details_idle_message, name),
            modifier = Modifier.clickable { onViewDetails.invoke() }
        )
    }
}
