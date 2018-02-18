package org.sgova.paintmemo.mode

import java.awt.Graphics
import java.awt.Point

class FreehandFigure(override var startX: Int, override var startY: Int, override val options: FigureOptions) : Figure {

    override var x: Int = -1
    override var y: Int = -1

    private val positions = mutableListOf<Point>()

    override fun reshape(g: Graphics) {
        positions.add(Point(x, y))

        positions.reduce { acc, pos ->
            g.drawLine(acc.x, acc.y, pos.x, pos.y)
            pos
        }
    }
}
