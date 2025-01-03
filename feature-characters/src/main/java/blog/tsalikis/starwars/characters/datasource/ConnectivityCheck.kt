package blog.tsalikis.starwars.characters.datasource

interface ConnectivityCheck {

    fun isNetworkAvailable(): Boolean
}
