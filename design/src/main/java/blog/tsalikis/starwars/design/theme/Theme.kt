package blog.tsalikis.starwars.design.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val StarWarsColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Blue,
    surface = Color.Transparent,
    onSurface = Color.White,
    background = Color.Transparent,
    onBackground = Color.White
)

@Composable
fun StarWarsAppTheme(
    content: @Composable () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(DeepBlue, Black)
    )
    val colorScheme = StarWarsColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
            content()
        }
    }
}
