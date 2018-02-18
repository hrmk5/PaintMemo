package org.sgova.paintmemo.mode

import java.awt.Graphics

class LineFigure(override var startX: Int, override var startY: Int, override val options: FigureOptions) : Figure {

    override var x = -1
    override var y = -1

    override fun reshape(g: Graphics) {
        g.drawLine(startX, startY, x, y)
    }
}
