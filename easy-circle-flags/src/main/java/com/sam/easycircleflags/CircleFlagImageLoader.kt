package com.sam.easycircleflags

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.svg.SvgDecoder
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

/**
 * The [ImageLoader] that [CircleFlag] uses when the caller does not supply one.
 *
 * Exactly one instance exists per process, and that matters more than it looks: an
 * [ImageLoader] owns a memory cache sized as a share of the app heap, and the OkHttp
 * client behind it owns a connection pool and a dispatcher thread pool. Creating one
 * per flag multiplies all of that by the number of flags on screen.
 */
object CircleFlagImageLoader {

    /**
     * Flags decode small - a 48dp flag is ~80 KB at 3x - so a modest share of the heap
     * still holds several hundred of them. Coil's own default is 20%, which is greedy
     * for a loader that only ever holds flags and which may sit alongside the host
     * app's own image loader.
     */
    private const val MEMORY_CACHE_PERCENT = 0.10

    /**
     * Every flag comes from the same host, and OkHttp allows only 5 concurrent calls per
     * host by default, which throttles the first paint of a grid. The CDN speaks HTTP/2,
     * so the extra calls multiplex onto the connection that is already open rather than
     * opening new ones.
     */
    private const val MAX_CONCURRENT_REQUESTS_PER_HOST = 12

    @Volatile
    private var instance: ImageLoader? = null

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .dispatcher(Dispatcher().apply { maxRequestsPerHost = MAX_CONCURRENT_REQUESTS_PER_HOST })
            .build()
    }

    /**
     * Returns the shared flag loader, building it on first use.
     *
     * Safe to call from any thread and cheap to call repeatedly; callers are expected to
     * call it per composition rather than hold onto the result.
     */
    fun get(context: Context): ImageLoader {
        instance?.let { return it }
        return synchronized(this) {
            instance ?: create(context.applicationContext).also { instance = it }
        }
    }

    private fun create(context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
                add(OkHttpNetworkFetcherFactory(callFactory = { httpClient }))
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, MEMORY_CACHE_PERCENT)
                    .build()
            }
            .build()
}

/**
 * Installs the flag loader as the app-wide Coil singleton, so that `AsyncImage` calls
 * outside this library can decode SVGs too.
 *
 * Apps that already configure their own [SingletonImageLoader.Factory] do not need this;
 * [CircleFlag] works either way.
 */
val CircleFlagImageLoaderFactory: SingletonImageLoader.Factory =
    SingletonImageLoader.Factory { context -> CircleFlagImageLoader.get(context) }
