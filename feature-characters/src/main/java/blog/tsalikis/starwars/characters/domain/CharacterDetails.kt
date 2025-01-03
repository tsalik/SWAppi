package blog.tsalikis.starwars.characters.domain

data class StarWarsPlanet(
    val name: String?,
    val diameter: Int?,
    val climates: List<String>,
)