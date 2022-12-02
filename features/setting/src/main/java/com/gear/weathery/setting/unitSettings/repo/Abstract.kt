package com.gear.weathery.setting.unitSettings.repo

import com.gear.weathery.setting.unitSettings.Units
import kotlinx.coroutines.flow.Flow

interface Abstract {

    suspend fun saveUnits(units: Units)

    suspend fun getUnits(): Flow<Units>
}