package org.sgova.paintmemo.mode

import java.awt.Color
import java.awt.Graphics

class RectangleFigure(override var startX: Int, override var startY: Int, override val options: FigureOptions) : Figure {
    
    override var x: Int = -1
    override var y: Int = -1
    
    override fun reshape(g: Graphics) {
        val startPosX = Math.min(startX, x)
        val startPosY = Math.min(startY, y)
        val width = Math.abs(startX - x)
        val height = Math.abs(startY - y)
        
        g.drawRect(startPosX, startPosY, width, height)
    }
}
