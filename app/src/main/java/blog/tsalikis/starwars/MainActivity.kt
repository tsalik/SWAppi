package blog.tsalikis.starwars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import blog.tsalikis.starwars.characters.details.CHARACTER_DETAILS
import blog.tsalikis.starwars.characters.details.details
import blog.tsalikis.starwars.characters.ui.CHARACTERS
import blog.tsalikis.starwars.characters.ui.characters
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = CHARACTERS) {
                    characters(onDetails = {
                        navController.navigate(CHARACTER_DETAILS)
                    })
                    details()
                }
            }
        }
    }
}
