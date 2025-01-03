package blog.tsalikis.starwars.characters.di

import android.content.Context
import blog.tsalikis.starwars.characters.datasource.AndroidConnectivityCheck
import blog.tsalikis.starwars.characters.datasource.ConnectivityCheck
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val TEN_MB = 10 * 1024 * 1024

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val serverUrl = "https://swapi-graphql.eskerda.vercel.app/"

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        val cacheFactory = MemoryCacheFactory(maxSizeBytes = TEN_MB)
        return ApolloClient.Builder()
            .serverUrl(serverUrl)
            .normalizedCache(cacheFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideConnectivityCheck(@ApplicationContext context: Context): ConnectivityCheck {
        return AndroidConnectivityCheck(context)
    }
}
