package blog.tsalikis.starwars.characters.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor() : ViewModel() {

    val charactersFlow: StateFlow<CharactersState> = MutableStateFlow(CharactersState.Loading)
}

sealed interface CharactersState {
    data object Loading : CharactersState
}
