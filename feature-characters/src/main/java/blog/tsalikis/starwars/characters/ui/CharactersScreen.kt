package blog.tsalikis.starwars.characters.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme

@Composable
fun CharactersScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Star Wars characters should show here")
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersScreenPreview() {
    StarWarsAppTheme {
        CharactersScreen(modifier = Modifier.fillMaxSize())
    }
}