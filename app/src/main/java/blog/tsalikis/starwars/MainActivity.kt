package blog.tsalikis.starwars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import blog.tsalikis.starwars.characters.details.details
import blog.tsalikis.starwars.characters.ui.CHARACTERS
import blog.tsalikis.starwars.characters.ui.characters
import blog.tsalikis.starwars.design.theme.StarWarsAppTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TRANSITION_MILLIS = 500

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsAppTheme {
                Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = CHARACTERS,
                        enterTransition = {
                            fadeIn(tween(TRANSITION_MILLIS))
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(
                                    TRANSITION_MILLIS
                                )
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(
                                    TRANSITION_MILLIS
                                )
                            )
                        },
                        popExitTransition = {
                            fadeOut(tween(TRANSITION_MILLIS))
                        }
                    ) {
                        characters(onDetails = { id, name ->
                            navController.navigate(
                                route = "characterDetails/$id/$name",
                            )
                        })
                        details()
                    }
                }
            }
        }
    }
}
