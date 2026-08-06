package com.sam.easy_circle_flags.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sam.easy_circle_flags.data.Country
import com.sam.easycircleflags.CircleFlag

const val FlagGridTag = "flag-grid"

/**
 * The whole point of the sample: several hundred flags in a scrolling grid.
 *
 * This is the case the library is built for, and the one that used to hurt - every
 * CircleFlag on screen once built its own ImageLoader and its own HTTP stack.
 */
@Composable
fun FlagGrid(
    countries: List<Country>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    header: (@Composable () -> Unit)? = null
) {
    LazyVerticalGrid(
        // Adaptive rather than a fixed count, so the grid reflows on tablets and in
        // landscape instead of stretching a phone layout.
        columns = GridCells.Adaptive(minSize = 104.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.testTag(FlagGridTag)
    ) {
        if (header != null) {
            // Spans the row so the header scrolls with the grid rather than sitting in
            // a separate column above it.
            item(key = "header", span = { GridItemSpan(maxLineSpan) }) { header() }
        }

        // Keyed by code: without it, filtering the list makes Compose reuse cells
        // against the wrong items and the flags visibly shuffle.
        items(countries, key = { it.code }) { country ->
            FlagCell(country)
        }
    }
}

@Composable
private fun FlagCell(country: Country, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        CircleFlag(
            countryCode = country.code,
            contentDescription = country.name,
            size = 56.dp
        )
        Text(
            text = country.code.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = country.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
