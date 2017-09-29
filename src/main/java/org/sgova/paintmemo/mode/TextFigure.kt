package org.sgova.paintmemo.mode

import java.awt.Graphics

class TextFigure(override var startX: Int, override var startY: Int, val text: String, override val options: FigureOptions) : Figure {

    override var x = startX
    override var y = startY

    override fun reshape(g: Graphics) {
        g.drawString(text, x, y)
    }
}