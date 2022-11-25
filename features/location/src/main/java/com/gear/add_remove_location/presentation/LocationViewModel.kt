package com.gear.add_remove_location.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gear.add_remove_location.domain.repository.LocationFeatureRepo
import com.gear.add_remove_location.presentation.manage_location.ManageScreenState
import com.gear.add_remove_location.presentation.save_location.SaveLocationState
import com.gear.weathery.common.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repo: LocationFeatureRepo
) : ViewModel() {

    private val _manageScreenState = mutableStateOf(ManageScreenState())
    val manageScreenState: State<ManageScreenState> = _manageScreenState

    private val _saveScreenState = mutableStateOf(SaveLocationState())
    val saveScreenState: State<SaveLocationState> = _saveScreenState

    fun onLocationSearch(query: String){
        viewModelScope.launch {
            val result = repo.getLocations(query)

            when(result){
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


    fun setLocationData(name: String, country: String) {
        _saveScreenState.value = _saveScreenState.value.copy(name = name, country = country)
    }
}