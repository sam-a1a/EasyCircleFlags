package com.sam.easycircleflags

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CircleFlag(
    countryCode: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Flag of $countryCode",
    size: Dp = 48.dp,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderPainter: Painter? = null,
    placeholderColor: Color? = null,
    errorPainter: Painter? = null,
    errorColor: Color? = null,
    imageLoader: ImageLoader = CircleFlagImageLoader.get(LocalContext.current)
) {
    val context = LocalContext.current

    val placeholder: Painter = placeholderPainter
        ?: placeholderColor?.let { ColorPainter(it) }
        ?: painterResource(R.drawable.ic_flag_placeholder)

    val error: Painter = errorPainter
        ?: errorColor?.let { ColorPainter(it) }
        ?: painterResource(R.drawable.ic_flag_placeholder)

    // Rebuilt only when the target changes: composables re-run on every frame of a
    // scroll, and an ImageRequest is not free to allocate.
    //
    // The request deliberately does not pin a size. Coil then resolves one from the
    // layout constraints, which the size modifier below makes exact. Asking for
    // Size.ORIGINAL instead would rasterise every flag at the SVG's own 512x512 - about
    // 1 MB per flag in memory, against roughly 80 KB at a 48dp display size.
    //
    // A code that is not a usable flag name resolves to null data, which AsyncImage
    // draws with `error` (its `fallback` parameter defaults to it) rather than throwing
    // out of composition and taking the screen down with it.
    val imageRequest = remember(context, countryCode) {
        ImageRequest.Builder(context)
            .data(CircleFlagUrls.getFlagUrlOrNull(countryCode))
            .crossfade(true)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        contentScale = contentScale,
        placeholder = placeholder,
        error = error,
        imageLoader = imageLoader
    )
}