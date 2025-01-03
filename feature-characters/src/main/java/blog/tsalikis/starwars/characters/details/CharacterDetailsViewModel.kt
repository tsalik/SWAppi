package blog.tsalikis.starwars.characters.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import blog.tsalikis.starwars.characters.datasource.StarWarsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}

sealed interface CharacterDetailsState {
    data class Idle(val name: String): CharacterDetailsState
}