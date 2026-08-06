package com.sam.easycircleflags

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Locale

class CircleFlagUrlsTest {

    private val base = "https://hatscripts.github.io/circle-flags/flags"

    @Test
    fun `builds url for a plain country code`() {
        assertEquals("$base/us.svg", CircleFlagUrls.getFlagUrl("us"))
    }

    @Test
    fun `accepts the shapes the flag set actually ships`() {
        // A subdivision, an underscored name, and a language flag under its subdirectory.
        assertEquals("$base/gb-eng.svg", CircleFlagUrls.getFlagUrl("gb-eng"))
        assertEquals("$base/european_union.svg", CircleFlagUrls.getFlagUrl("european_union"))
        assertEquals(
            "$base/au-torres_strait_islands.svg",
            CircleFlagUrls.getFlagUrl("au-torres_strait_islands")
        )
        assertEquals("$base/language/ar.svg", CircleFlagUrls.getFlagUrl("language/ar"))
    }

    @Test
    fun `uppercase codes are normalised`() {
        assertEquals("$base/de.svg", CircleFlagUrls.getFlagUrl("DE"))
        assertEquals("$base/gb-eng.svg", CircleFlagUrls.getFlagUrl("GB-ENG"))
    }

    @Test
    fun `surrounding whitespace is tolerated`() {
        assertEquals("$base/fr.svg", CircleFlagUrls.getFlagUrl("  fr  "))
    }

    /**
     * The regression this guards: with a default-locale lowercase(), "IN" becomes "ın"
     * on a Turkish device and the flag silently fails to load.
     */
    @Test
    fun `lowercasing does not depend on the default locale`() {
        val original = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))
            assertEquals("$base/in.svg", CircleFlagUrls.getFlagUrl("IN"))
        } finally {
            Locale.setDefault(original)
        }
    }

    @Test
    fun `rejects codes that would redirect the request`() {
        val hostile = listOf(
            "../../../etc/passwd",
            "..",
            "us/../../secret",
            "us?redirect=http://evil.example",
            "us#fragment",
            "us%2f..%2f",
            "https://evil.example/x",
            "//evil.example/x",
            "us .svg",
            "us\nInjected: header"
        )
        for (code in hostile) {
            assertNull("expected '$code' to be rejected", CircleFlagUrls.getFlagUrlOrNull(code))
        }
    }

    @Test
    fun `rejects malformed but harmless codes`() {
        val malformed = listOf("", "   ", "-us", "us-", "us_", "u--s", "language/", "/us", "a/b/c")
        for (code in malformed) {
            assertNull("expected '$code' to be rejected", CircleFlagUrls.getFlagUrlOrNull(code))
        }
    }

    @Test
    fun `rejects codes long enough to pollute the cache`() {
        assertNull(CircleFlagUrls.getFlagUrlOrNull("a".repeat(65)))
    }

    @Test
    fun `getFlagUrl throws on an unusable code`() {
        val error = assertThrows(IllegalArgumentException::class.java) {
            CircleFlagUrls.getFlagUrl("../evil")
        }
        assertEquals("Not a usable flag code: '../evil'", error.message)
    }
}
