package blog.tsalikis.starwars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import blog.tsalikis.starwars.characters.ui.CharactersScreen
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsAppTheme {
                Scaffold(modifier = Modifier.padding(16.dp).fillMaxSize()) { innerPadding ->
                    CharactersScreen(Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}
