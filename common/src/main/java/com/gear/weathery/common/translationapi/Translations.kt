package com.gear.weathery.common.translationapi

class Translations : ArrayList<TranslationsItem>()

data class TranslationsItem(
    val translations: List<Translation>
)

data class Translation(
    val text: String,
    val to: String
)