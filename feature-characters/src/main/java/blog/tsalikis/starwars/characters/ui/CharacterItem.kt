package blog.tsalikis.starwars.characters.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme

@Composable
fun CharacterItem(
    character: StarWarsCharacter,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
    onDetails: () -> Unit
) {
    Surface(modifier = modifier.clickable { onDetails.invoke() }) {
        Column {
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
            if (showDivider) {
                HorizontalDivider(thickness = 1.dp)
            }
        }
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
            showDivider = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onDetails = {}
        )
    }
}
