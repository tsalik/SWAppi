package blog.tsalikis.starwars.characters.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    val id = savedStateHandle.get<String>("id") ?: throw IllegalArgumentException("Missing id")
    val name = savedStateHandle.get<String>("name") ?: throw  IllegalArgumentException("Missing name")
}