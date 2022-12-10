package com.gear.weathery.common.`translation-api`

class Translations : ArrayList<TranslationsItem>()

data class TranslationsItem(
    val translations: List<Translation>
)

data class Translation(
    val text: String,
    val to: String
)