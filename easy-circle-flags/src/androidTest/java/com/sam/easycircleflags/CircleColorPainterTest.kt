package com.sam.easycircleflags

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CircleColorPainterTest {

    private val side = 64

    private fun renderRedCircle(): Bitmap {
        val target = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888).asImageBitmap()
        CanvasDrawScope().draw(
            density = Density(1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = Canvas(target),
            size = Size(side.toFloat(), side.toFloat())
        ) {
            with(CircleColorPainter(Color.Red)) { draw(size) }
        }
        return target.asAndroidBitmap()
    }

    @Test
    fun fills_the_centre_with_the_colour() {
        assertEquals(Color.Red.toArgb(), renderRedCircle().getPixel(side / 2, side / 2))
    }

    /** The whole point of the class: corners stay empty, so the shape reads as round. */
    @Test
    fun leaves_the_corners_unpainted() {
        val bitmap = renderRedCircle()
        val corners = listOf(0 to 0, side - 1 to 0, 0 to side - 1, side - 1 to side - 1)
        for ((x, y) in corners) {
            assertEquals("corner $x,$y should not be painted", 0, bitmap.getPixel(x, y))
        }
    }

    @Test
    fun equal_colours_compare_equal() {
        assertEquals(CircleColorPainter(Color.Blue), CircleColorPainter(Color.Blue))
        assertEquals(
            CircleColorPainter(Color.Blue).hashCode(),
            CircleColorPainter(Color.Blue).hashCode()
        )
    }
}
