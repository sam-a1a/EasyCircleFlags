package com.sam.easy_circle_flags

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.sam.easycircleflags.CircleFlagImageLoaderFactory

/**
 * Installs the library's loader as the app-wide Coil singleton.
 *
 * Not required to use CircleFlag - it falls back to the same shared loader on its own -
 * but it is what an app should do if it loads images of its own: Coil's singleton then
 * has the SVG decoder registered, and every image in the process goes through one memory
 * cache and one connection pool instead of two parallel sets.
 */
class EasyCircleFlagsApp : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext): ImageLoader =
        CircleFlagImageLoaderFactory.newImageLoader(context)
}
