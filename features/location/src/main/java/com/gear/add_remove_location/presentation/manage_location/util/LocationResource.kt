package com.gear.add_remove_location.presentation.manage_location.util


sealed class LocationResource<T>(val data: T? = null, val message: UIText? = null){
    class Success<T>(data: T?): LocationResource<T>(data)
    class Error<T>(message: UIText,data: T? = null): LocationResource<T>(data,message)
    class Loading<T>: LocationResource<T>()
}