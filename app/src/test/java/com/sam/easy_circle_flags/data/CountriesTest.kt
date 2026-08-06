package com.sam.easy_circle_flags.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class CountriesTest {

    private val countries = isoCountries(Locale.ENGLISH)

    @Test
    fun `covers every iso country`() {
        assertEquals(Locale.getISOCountries().size, countries.size)
    }

    @Test
    fun `codes are lowercase, which is the form the flag urls use`() {
        assertTrue(countries.all { it.code == it.code.lowercase(Locale.ROOT) })
        assertTrue(countries.all { it.code.length == 2 })
    }

    @Test
    fun `every country carries a label`() {
        assertTrue(countries.none { it.name.isBlank() })
        assertEquals("Germany", countries.first { it.code == "de" }.name)
    }

    @Test
    fun `names are ordered for the locale, not by code point`() {
        val names = countries.map { it.name }
        assertEquals(names.sortedWith(java.text.Collator.getInstance(Locale.ENGLISH)), names)
    }

    @Test
    fun `search matches a code prefix`() {
        val hits = searchCountries(countries, "de")
        assertTrue(hits.any { it.code == "de" })
    }

    @Test
    fun `search matches part of a name, ignoring case`() {
        val hits = searchCountries(countries, "kingdom")
        assertTrue(hits.any { it.code == "gb" })
    }

    @Test
    fun `search ignores diacritics`() {
        val hits = searchCountries(countries, "aland")
        assertTrue("expected Åland Islands for 'aland'", hits.any { it.code == "ax" })
    }

    @Test
    fun `an empty or blank query keeps the whole list`() {
        assertEquals(countries.size, searchCountries(countries, "").size)
        assertEquals(countries.size, searchCountries(countries, "   ").size)
    }

    @Test
    fun `a query that matches nothing yields nothing`() {
        assertTrue(searchCountries(countries, "zzzzzz").isEmpty())
    }

    @Test
    fun `search is case insensitive for both codes and names`() {
        assertTrue(searchCountries(countries, "DE").any { it.code == "de" })
        assertTrue(searchCountries(countries, "GeRmAnY").any { it.code == "de" })
    }

    @Test
    fun `a name fragment can match several countries`() {
        val hits = searchCountries(countries, "united").map { it.code }
        assertTrue("expected the United States among $hits", "us" in hits)
        assertTrue("expected the United Kingdom among $hits", "gb" in hits)
    }

    @Test
    fun `results never include a country the query cannot explain`() {
        val hits = searchCountries(countries, "jp")
        assertFalse("Germany should not answer a search for 'jp'", hits.any { it.code == "de" })
        assertTrue(hits.any { it.code == "jp" })
    }
}
