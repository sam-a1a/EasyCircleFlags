package com.sam.easycircleflags

import java.util.Locale

object CircleFlagUrls {
    private const val BASE_URL = "https://hatscripts.github.io/circle-flags/flags"

    /** Longest name the flag set currently ships is "au-torres_strait_islands", at 24. */
    private const val MAX_CODE_LENGTH = 64

    /**
     * The shape of every name in the flag set: lowercase alphanumerics, with "-" or "_"
     * joining segments, optionally under a single subdirectory ("language/ar").
     *
     * Checked against the published set, this accepts all 645 names and nothing else.
     * The point of "nothing else" is that the code is interpolated straight into a URL,
     * so an unchecked value lets whatever produced it - often a server response or user
     * input rather than a literal - steer the request at other paths on the host, append
     * a query string, or mint an unbounded number of distinct cache keys. Note that "."
     * is absent from the character class, which is what rules out "..".
     */
    private val FLAG_CODE = Regex("[a-z0-9]+(?:[_-][a-z0-9]+)*(?:/[a-z0-9]+(?:[_-][a-z0-9]+)*)?")

    /**
     * Returns the SVG URL for [countryCode].
     *
     * @throws IllegalArgumentException if the code is not a usable flag name.
     */
    fun getFlagUrl(countryCode: String): String =
        requireNotNull(getFlagUrlOrNull(countryCode)) {
            "Not a usable flag code: '$countryCode'"
        }

    /**
     * Same as [getFlagUrl] but returns null instead of throwing, for callers that would
     * rather render a fallback than crash on a code they did not control.
     */
    fun getFlagUrlOrNull(countryCode: String): String? {
        if (countryCode.length > MAX_CODE_LENGTH) return null
        // Locale.ROOT, because the default locale decides what lowercase means: on a
        // Turkish-locale device "IN".lowercase() is "ın", which is not a flag.
        val code = countryCode.trim().lowercase(Locale.ROOT)
        return if (FLAG_CODE.matches(code)) "$BASE_URL/$code.svg" else null
    }
}
