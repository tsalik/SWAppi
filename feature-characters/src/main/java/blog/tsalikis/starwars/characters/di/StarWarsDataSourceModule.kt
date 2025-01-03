package blog.tsalikis.starwars.characters.di

import blog.tsalikis.starwars.characters.datasource.ApolloDataSource
import blog.tsalikis.starwars.characters.datasource.StarWarsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StarWarsDataSourceModule {

    @Binds
    @Singleton
    fun bindsStarWarsDataSource(apolloDataSource: ApolloDataSource): StarWarsDataSource
}
