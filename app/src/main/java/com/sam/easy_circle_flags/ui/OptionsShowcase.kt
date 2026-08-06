package com.sam.easy_circle_flags.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sam.easy_circle_flags.R
import com.sam.easycircleflags.CircleFlag

/**
 * A short tour of what CircleFlag takes beyond a country code, sitting above the grid.
 *
 * Doubles as a live check of the states that are otherwise awkward to see: the
 * placeholder while a flag is in flight, and the error painter for a code that cannot
 * resolve.
 */
@Composable
fun OptionsShowcase(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Label(stringResource(R.string.showcase_sizes))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            for (size in listOf(24.dp, 32.dp, 48.dp, 64.dp)) {
                SizeSample(size)
            }
        }

        Label(stringResource(R.string.showcase_fallbacks))
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Sample(stringResource(R.string.showcase_tinted)) {
                CircleFlag(
                    countryCode = "jp",
                    placeholderColor = MaterialTheme.colorScheme.surfaceVariant,
                    size = 48.dp
                )
            }
            Sample(stringResource(R.string.showcase_unknown)) {
                // Not a code the flag set has. It resolves to no URL at all, and the
                // library draws the error painter rather than throwing.
                CircleFlag(
                    countryCode = "zzz",
                    errorColor = Color(0xFFE0E0E0),
                    size = 48.dp
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
private fun SizeSample(size: Dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircleFlag(countryCode = "br", size = size)
        Text(
            text = "${size.value.toInt()}dp",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun Sample(label: String, content: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        content()
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
