package com.gear.add_remove_location.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gear.add_remove_location.domain.repository.LocationFeatureRepo
import com.gear.add_remove_location.presentation.manage_location.ManageScreenState
import com.gear.weathery.common.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val service: LocationFeatureRepo
) : ViewModel() {
    private val _manageScreenState = mutableStateOf(ManageScreenState())
    val manageScreenState: State<ManageScreenState> = _manageScreenState

    fun onLocationSearch(query: String){
        viewModelScope.launch {
            when(val result = service.getLocations(query)){
                is Resource.Error -> {
                    _manageScreenState.value = _manageScreenState.value.copy(
                        locations = emptyList(),
                        isSuccessful = false,
                        error = result.message ?: "Unknown Error Occurred"
                    )
                }
                is Resource.Loading -> {
                    _manageScreenState.value = _manageScreenState.value.copy(
                        locations = emptyList(),
                        isSuccessful = false,
                        error = ""
                    )
                }
                is Resource.Success -> {
                   _manageScreenState.value = _manageScreenState.value.copy(
                       locations = result.data ?: emptyList(),
                       isSuccessful = true,
                       error = ""
                   )
                }
            }
        }
    }
}