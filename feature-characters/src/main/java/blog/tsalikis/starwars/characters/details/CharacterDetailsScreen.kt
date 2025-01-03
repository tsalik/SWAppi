package blog.tsalikis.starwars.characters.details

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val CHARACTER_DETAILS = "characterDetails"

fun NavGraphBuilder.details() {
    composable(CHARACTER_DETAILS) {
        CharacterDetailsScreen()
    }
}

@Composable
fun CharacterDetailsScreen() {
    Box {
        Text("Details screen should show here")
    }
}
