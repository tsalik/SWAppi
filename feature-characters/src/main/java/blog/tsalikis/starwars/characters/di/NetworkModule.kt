package blog.tsalikis.starwars.characters.di

import android.content.Context
import blog.tsalikis.starwars.characters.datasource.AndroidConnectivityCheck
import blog.tsalikis.starwars.characters.datasource.ConnectivityCheck
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val serverUrl = "https://swapi-graphql.eskerda.vercel.app/"

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(serverUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideConnectivityCheck(@ApplicationContext context: Context): ConnectivityCheck {
        return AndroidConnectivityCheck(context)
    }
}
