package org.sgova.paintmemo

import org.sgova.paintmemo.mode.*
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JPanel

class PaintPanel() : JPanel(), MouseListener, MouseMotionListener {
	
	val paper = Paper()
	
	var drawingFigure: Figure? = null
		private set

    var createFigure: (x: Int, y: Int, options: FigureOptions) -> Figure = fun(x, y, options) = FreehandFigure(x, y, options)
	var currentColor: Color = Color.BLACK
    var currentStroke: Int = 2
	
	init {
		setDoubleBuffered(true)
		
		addMouseListener(this)
		addMouseMotionListener(this)
	}
	
	override fun paintComponent(g1: Graphics) {
        val g = g1 as Graphics2D

		g.color = Color.WHITE
		g.fillRect(0, 0, width, height)

        // 図形を描画
		paper.figures.forEach {
			paintFigure(g, it)
		}

        // 描画する図形のプレビュー
		if (drawingFigure != null) {
			paintFigure(g, drawingFigure!!)
		}
	}

    // Figureを描画
	fun paintFigure(g: Graphics2D, figure: Figure) {
		g.color = figure.options.color
        g.stroke = BasicStroke(figure.options.stroke.toFloat())
		figure.reshape(g)
	}
	
	override fun mouseReleased(e: MouseEvent) {
		if (drawingFigure != null) {
			paper.pushFigure(drawingFigure!!)
			
			drawingFigure = null
			repaint()
		}
	}
	
	override fun mouseDragged(e: MouseEvent) {
		drawingFigure?.x = e.x
		drawingFigure?.y = e.y
		
		repaint()
	}

	override fun mousePressed(e: MouseEvent) {
		if (drawingFigure == null) {
            val options = FigureOptions(currentColor, currentStroke)
            drawingFigure = createFigure(e.x, e.y, options)
		}
	}
	
	override fun mouseClicked(e: MouseEvent) {}
	override fun mouseEntered(e: MouseEvent) {}
	override fun mouseExited(e: MouseEvent) {}
	
	override fun mouseMoved(e: MouseEvent) {}
}