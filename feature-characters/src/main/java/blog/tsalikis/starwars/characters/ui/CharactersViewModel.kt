package blog.tsalikis.starwars.characters.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.characters.datasource.StarWarsDataSource
import blog.tsalikis.starwars.characters.domain.Errors
import blog.tsalikis.starwars.characters.domain.StarWarsCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ANR_THRESHOLD = 5_000L

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val starWarsDataSource: StarWarsDataSource,
) : ViewModel() {

    private val _charactersFlow = MutableStateFlow<CharactersState>(CharactersState.Loading)
    val charactersFlow: StateFlow<CharactersState> = _charactersFlow.onStart { fetchCharacters() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(ANR_THRESHOLD),
            CharactersState.Loading
        )

    fun fetchCharacters() {
        viewModelScope.launch {
            _charactersFlow.update { CharactersState.Loading }
            val result = starWarsDataSource.allCharacters()
            _charactersFlow.update {
                result.fold(
                    { error ->
                        val (title, message) =
                            when (error) {
                                Errors.Generic ->
                                    R.string.error_generic_title to
                                        R.string.error_generic_message

                                Errors.NoConnection ->
                                    R.string.error_no_connection_title to
                                        R.string.error_no_connection_message
                            }
                        CharactersState.Failure(title = title, message = message)
                    },
                    { data ->
                        CharactersState.Success(data.toImmutableList())
                    }
                )
            }
        }
    }
}

sealed interface CharactersState {
    data object Loading : CharactersState
    data class Success(val characters: ImmutableList<StarWarsCharacter>) : CharactersState
    data class Failure(@StringRes val title: Int, @StringRes val message: Int) : CharactersState
}
