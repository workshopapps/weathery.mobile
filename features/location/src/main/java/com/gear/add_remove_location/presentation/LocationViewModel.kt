package com.gear.add_remove_location.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gear.add_remove_location.R
import com.gear.add_remove_location.domain.repository.LocationFeatureRepo
import com.gear.add_remove_location.presentation.manage_location.Action
import com.gear.add_remove_location.presentation.manage_location.ManageScreenState
import com.gear.add_remove_location.presentation.manage_location.util.LocationResource
import com.gear.add_remove_location.presentation.manage_location.util.UIText
import com.gear.weathery.location.api.Location
import com.gear.weathery.location.api.LocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val service: LocationFeatureRepo,
    private val local: LocationsRepository
) : ViewModel() {

    private val _savedLocations = mutableStateOf<List<Location>>(emptyList())
    val savedLocations: State<List<Location>> = _savedLocations

    private val _manageScreenState = mutableStateOf(ManageScreenState())
    val manageScreenState: State<ManageScreenState> = _manageScreenState

    private val _isOnSearchState = mutableStateOf(true)
    val isOnSearchState: State<Boolean> = _isOnSearchState

    private val _searchTextState = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _screenState = mutableStateOf(Action.SEARCH_SAVE)
    val screenState: State<Action> = _screenState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _resourceState =
        mutableStateOf<LocationResource<List<Location>>>(LocationResource.Loading())

    init {
        getSavedLocations()
    }

    private val saveItemMap: HashMap<Int, Location> = HashMap()
    private val deleteItemMap: HashMap<Int, Location> = HashMap()


    fun onLocationSearch(query: String) {
        _searchTextState.value = query
        if (query.length >= 3 && _isOnSearchState.value) {
            viewModelScope.launch {
                delay(500)
                _resourceState.value = service.getLocations(query)
            }

            when (_resourceState.value) {
                is LocationResource.Error -> {
                    _manageScreenState.value = _manageScreenState.value.copy(
                        locations = emptyList(),
                        isLoading = false
                    )

                    viewModelScope.launch {
                        _eventFlow.emit(
                            UIEvent.ShowSearchErrors(
                                _resourceState.value.message
                                    ?: UIText.StringResource(R.string.error_try_again_later)
                            )
                        )
                    }
                }
                is LocationResource.Loading -> {
                    _manageScreenState.value = _manageScreenState.value.copy(
                        locations = emptyList(),
                        isLoading = true
                    )
                }
                is LocationResource.Success -> {
                    _manageScreenState.value = _manageScreenState.value.copy(
                        locations = _resourceState.value.data ?: emptyList(),
                        isLoading = false
                    )
                }
            }

        }
    }

    fun setSearchState(selected: String) {
        _searchTextState.value = selected
        if (selected.isEmpty()) {
            _isOnSearchState.value = true
            _manageScreenState.value = _manageScreenState.value.copy(
                locations = emptyList()
            )
        } else {
            _isOnSearchState.value = !_isOnSearchState.value
        }
    }

    fun saveItemSelected(index: Int, location: Location, isSelected: Boolean) {
        if (isSelected) {
            saveItemMap[index] = location
        } else {
            saveItemMap.remove(index)
        }
    }

    fun saveLocations() {
        val locations = saveItemMap.values.distinct()
        if (locations.isNotEmpty()) {
            viewModelScope.launch {
                local.saveLocation(*locations.toTypedArray())
            }
        }
    }

    private fun getSavedLocations() {
        with(local) {
            locations.onEach {
                _savedLocations.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun setAction(action: Action) {
        _screenState.value = action
    }

    fun deleteItemSelected(index: Int, location: Location, isSelected: Boolean) {
        if (isSelected) {
            deleteItemMap[index] = location
        } else {
            deleteItemMap.remove(index)
        }
    }

    fun deleteLocations() {
        val locations = deleteItemMap.values
        if (locations.isNotEmpty()) {
            viewModelScope.launch {
                local.deleteLocation(*locations.toTypedArray())
            }
        }
    }

    sealed class UIEvent {
        data class ShowSearchErrors(val message: UIText) : UIEvent()
    }
}