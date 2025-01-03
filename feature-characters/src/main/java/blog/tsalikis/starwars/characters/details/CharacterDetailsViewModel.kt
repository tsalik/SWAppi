package blog.tsalikis.starwars.characters.details

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import blog.tsalikis.starwars.characters.R
import blog.tsalikis.starwars.characters.datasource.StarWarsDataSource
import blog.tsalikis.starwars.characters.domain.Errors
import blog.tsalikis.starwars.characters.domain.StarWarsPlanet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val apolloDataSource: StarWarsDataSource,
) : ViewModel() {

    val id = savedStateHandle.get<String>("id") ?: throw IllegalArgumentException("Missing id")
    val name =
        savedStateHandle.get<String>("name") ?: throw IllegalArgumentException("Missing name")

    private val _state = MutableStateFlow<CharacterDetailsState>(CharacterDetailsState.Idle(name))
    val detailsFlow: StateFlow<CharacterDetailsState>
        get() = _state

    fun fetchDetails() {
        viewModelScope.launch {
            _state.update { CharacterDetailsState.Loading }
            val result = apolloDataSource.personDetails(id)
            _state.update {
                result.fold(
                    { error ->
                        val (title, message) = when (error) {
                            Errors.Generic ->
                                R.string.error_generic_title to
                                        R.string.error_generic_message

                            Errors.NoConnection ->
                                R.string.error_no_connection_title to
                                        R.string.error_no_connection_message

                            Errors.NotFound -> R.string.error_no_planet_found_title to
                                    R.string.error_no_planet_found_message
                        }
                        CharacterDetailsState.Failure(title, message)
                    },
                    { data -> CharacterDetailsState.Success(data) }
                )
            }
        }
    }
}

sealed interface CharacterDetailsState {
    data class Idle(val name: String) : CharacterDetailsState
    data object Loading : CharacterDetailsState
    data class Success(val starWarsPlanet: StarWarsPlanet) : CharacterDetailsState
    data class Failure(
        @StringRes val title: Int,
        @StringRes val message: Int
    ) : CharacterDetailsState
}