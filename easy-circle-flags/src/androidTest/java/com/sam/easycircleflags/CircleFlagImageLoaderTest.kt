package com.sam.easycircleflags

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CircleFlagImageLoaderTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    /**
     * A 512x512 circle, the shape every flag on the CDN has. Fed to the loader as bytes
     * so the test exercises the real decoder without depending on the network.
     */
    private val svg512 = """
        <svg xmlns="http://www.w3.org/2000/svg" width="512" height="512"
             viewBox="0 0 512 512"><circle cx="256" cy="256" r="256" fill="#f00"/></svg>
    """.trimIndent().toByteArray()

    /**
     * The fix this library exists on: one loader per process, not one per composable.
     * Each extra loader would bring its own memory cache and OkHttp connection pool.
     */
    @Test
    fun returnsTheSameLoaderEveryTime() {
        val first = CircleFlagImageLoader.get(context)
        val second = CircleFlagImageLoader.get(context)
        assertSame(first, second)
    }

    @Test
    fun sharesThatLoaderWithTheSingletonFactory() {
        assertSame(CircleFlagImageLoader.get(context), CircleFlagImageLoaderFactory.newImageLoader(context))
    }

    /**
     * Guards the memory fix: the flags are 512x512 SVGs, so decoding at their intrinsic
     * size costs about 1 MB each. The loader must honour the requested size instead.
     */
    @Test
    fun decodesAtTheRequestedSizeNotTheIntrinsicOne() = runBlocking {
        val result = CircleFlagImageLoader.get(context).execute(
            ImageRequest.Builder(context).data(svg512).size(144, 144).build()
        )

        assertTrue("expected a successful decode, got $result", result is SuccessResult)
        val image = (result as SuccessResult).image
        assertEquals(144, image.width)
        assertEquals(144, image.height)
    }

    @Test
    fun theSharedLoaderCanDecodeSvg() = runBlocking {
        val result = CircleFlagImageLoader.get(context).execute(
            ImageRequest.Builder(context).data(svg512).size(64, 64).build()
        )
        assertTrue("SvgDecoder is not registered on the shared loader", result is SuccessResult)
    }
}
