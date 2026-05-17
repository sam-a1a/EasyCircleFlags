package com.sam.easycircleflags

object CircleFlagUrls {
    private const val BASE_URL = "https://hatscripts.github.io/circle-flags/flags"

    fun getFlagUrl(countryCode: String): String {
        require(countryCode.isNotBlank()) { "Country code must not be blank" }
        return "$BASE_URL/${countryCode.lowercase()}.svg"
    }
}