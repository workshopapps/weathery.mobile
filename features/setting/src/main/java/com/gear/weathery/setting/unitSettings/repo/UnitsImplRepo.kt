package com.gear.weathery.setting.unitSettings.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gear.weathery.setting.unitSettings.Units
import kotlinx.coroutines.flow.map


const val Datastore_NAME = "UNITS"

val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = Datastore_NAME)

class UnitsImplRepo (private val context: Context) {

    companion object {
        val TEMPERATURE = stringPreferencesKey("TEMPERATURE")
        val PRESSURE = stringPreferencesKey("PRESSURE")
        val WINDSPEED = stringPreferencesKey("WINDSPEED")
    }

    override suspend fun saveUnits(units: Units) {
        context.datastore.edit { unit ->
            unit[TEMPERATURE] = units.temperature
            unit[PRESSURE] = units.pressure
            unit[WINDSPEED] = units.windSpeed
        }

    }

    override suspend fun getUnits() = context.datastore.data.map { unit ->
        Units(
            temperature = unit[TEMPERATURE]?:"°C",
            pressure = unit[PRESSURE]?:"hPo",
            windSpeed = unit[WINDSPEED]?:"km/hr"
        )
    }
}