package com.sam.easycircleflags

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.svg.SvgDecoder

val CircleFlagImageLoaderFactory: SingletonImageLoader.Factory =
    SingletonImageLoader.Factory { context ->
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }