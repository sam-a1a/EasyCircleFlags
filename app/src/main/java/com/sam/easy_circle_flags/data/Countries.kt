package com.sam.easy_circle_flags.data

import java.text.Collator
import java.util.Locale

/** A country as the sample lists it: the code the library needs, plus a label to show. */
data class Country(
    val code: String,
    val name: String
)

/**
 * Every ISO 3166-1 alpha-2 country, labelled in [locale] and ordered for reading in it.
 *
 * Taken from the platform rather than hardcoded, which keeps the labels in the user's
 * language and the list current with the JDK. All 249 codes have a flag in the Circle
 * Flags set, so the grid renders without gaps.
 */
fun isoCountries(locale: Locale = Locale.getDefault()): List<Country> {
    // Collator, not sortedBy: a plain string sort puts "Åland" after "Zimbabwe" and
    // misplaces every accented name in between.
    val byName = Collator.getInstance(locale)
    return Locale.getISOCountries()
        .map { code ->
            Country(
                code = code.lowercase(Locale.ROOT),
                name = Locale.Builder().setRegion(code).build().getDisplayCountry(locale)
            )
        }
        .sortedWith { a, b -> byName.compare(a.name, b.name) }
}

/**
 * The subset of [countries] whose name or code matches [query].
 *
 * Matching is diacritic-insensitive by way of the collator's primary strength, so
 * "aland" finds "Åland Islands"; an empty query keeps everything.
 */
fun searchCountries(countries: List<Country>, query: String): List<Country> {
    val trimmed = query.trim()
    if (trimmed.isEmpty()) return countries

    val needle = trimmed.lowercase(Locale.getDefault())
    return countries.filter { country ->
        country.code.startsWith(needle) ||
            country.name.lowercase(Locale.getDefault()).contains(needle) ||
            country.name.foldDiacritics().contains(needle.foldDiacritics())
    }
}

private fun String.foldDiacritics(): String =
    java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase(Locale.getDefault())
