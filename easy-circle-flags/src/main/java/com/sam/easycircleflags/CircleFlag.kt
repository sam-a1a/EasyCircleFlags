package com.sam.easycircleflags

import android.util.Log
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
import coil3.network.okhttp.OkHttpNetworkFetcherFactory  // ✅ Correct import
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import coil3.svg.SvgDecoder
import okhttp3.OkHttpClient

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
    errorColor: Color? = null
) {
    val context = LocalContext.current
    val url = CircleFlagUrls.getFlagUrl(countryCode)

    Log.d("CircleFlag", "Loading flag: countryCode=$countryCode url=$url")

    val placeholder: Painter = placeholderPainter
        ?: placeholderColor?.let { ColorPainter(it) }
        ?: painterResource(R.drawable.ic_flag_placeholder)

    val error: Painter = errorPainter
        ?: errorColor?.let { ColorPainter(it) }
        ?: painterResource(R.drawable.ic_flag_placeholder)

    val svgImageLoader = remember {
        ImageLoader.Builder(context.applicationContext)
            .components {
                // ✅ Use OkHttpNetworkFetcherFactory with a lambda returning OkHttpClient
                add(OkHttpNetworkFetcherFactory(callFactory = { OkHttpClient() }))
                add(SvgDecoder.Factory())
            }
            .build()
    }

    val imageRequest = ImageRequest.Builder(context)
        .data(url)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .listener(
            onStart = { Log.d("CircleFlag", "onStart: $url") },
            onSuccess = { _, _ -> Log.d("CircleFlag", "onSuccess: $url") },
            onError = { _, result -> Log.e("CircleFlag", "onError: $url", result.throwable) }
        )
        .build()

    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        contentScale = contentScale,
        placeholder = placeholder,
        error = error,
        imageLoader = svgImageLoader
    )
}