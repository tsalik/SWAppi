package blog.tsalikis.starwars.characters.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import blog.tsalikis.starwars.characters.R

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
        CharacterDetailsScreen(viewModel.id, viewModel.name)
    }
}

@Composable
fun CharacterDetailsScreen(id: String, name: String) {
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
        Surface(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Text("Click here to view homeword for $name{$id}")
        }
    }
}
