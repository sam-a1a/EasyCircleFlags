package com.sam.easycircleflags

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

/**
 * Fills a circle inscribed in the drawing area with [color].
 *
 * Used for the [CircleFlag] placeholder and error colours. ColorPainter would fill the
 * whole box, which puts a square where a round flag is about to appear - visible on the
 * first frame of every load and for as long as an unresolvable code is on screen.
 *
 * Callers who pass their own Painter keep full control; only the colour shorthands are
 * shaped this way.
 */
internal class CircleColorPainter(private val color: Color) : Painter() {

    override val intrinsicSize: Size get() = Size.Unspecified

    override fun DrawScope.onDraw() {
        val diameter = minOf(size.width, size.height)
        drawCircle(
            color = color,
            radius = diameter / 2f,
            center = Offset(size.width / 2f, size.height / 2f)
        )
    }

    override fun equals(other: Any?): Boolean =
        this === other || (other is CircleColorPainter && color == other.color)

    override fun hashCode(): Int = color.hashCode()

    override fun toString(): String = "CircleColorPainter(color=$color)"
}
