package org.sgova.paintmemo

import org.sgova.paintmemo.mode.*
import java.awt.*
import java.awt.event.*
import javax.swing.JPanel

class PaintPanel() : JPanel(), MouseListener, MouseMotionListener, KeyListener {

    val paper = Paper()
	
	var drawingFigure: Figure? = null
		private set

    var createFigure: (x: Int, y: Int, options: FigureOptions) -> Figure = fun(x, y, options) = FreehandFigure(x, y, options)
	var currentColor: Color = Color.BLACK
    var currentStroke: Int = 2
    var currentBackgroundColor = Color.WHITE
    val transparentBackgroundColor = Color(235, 235, 235) // 背景が透明の時の背景色

    var isOnlyVerticalLine = false
    var isOnlyHorizontalLine = false

    fun setCurrentBackground(color: Color) {
        currentBackgroundColor = color
        repaint()
    }

	init {
        isDoubleBuffered = true
        isFocusable = true

		addMouseListener(this)
		addMouseMotionListener(this)
        addKeyListener(this)
	}

	override fun paintComponent(g1: Graphics) {
        val g = g1 as Graphics2D

        g.clearRect(0, 0, width, height)

        // 背景が透明だった場合の背景色
        if (currentBackgroundColor.alpha <= 0) {
            g.color = transparentBackgroundColor
            g.fillRect(0, 0, width, height)
        }

		g.color = currentBackgroundColor
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

    fun undo() {
        paper.undo()
        repaint()
    }

    fun redo() {
        paper.redo()
        repaint()
    }

	override fun mouseReleased(e: MouseEvent) {
		if (drawingFigure != null) {
			paper.pushFigure(drawingFigure!!)

			drawingFigure = null
			repaint()
		}

        requestFocus()
	}

	override fun mouseDragged(e: MouseEvent) {
        if (drawingFigure != null) {
            // isOnlyVerticalLineがtrueならx座標を固定する
            if (!isOnlyVerticalLine) {
                drawingFigure?.x = e.x
            } else {
                drawingFigure?.x = drawingFigure?.startX!!
            }
            // isOnlyHorizontalLineがtrueならy座標を固定する
            if (!isOnlyHorizontalLine) {
                drawingFigure?.y = e.y
            } else {
                drawingFigure?.y = drawingFigure?.startY!!
            }

            repaint()
        }
	}

	override fun mousePressed(e: MouseEvent) {
        if (drawingFigure == null) {
            val options = FigureOptions(currentColor, currentStroke)
            drawingFigure = createFigure(e.x, e.y, options)
        }
    }

	override fun keyReleased(e: KeyEvent?) {
        isOnlyVerticalLine = false
        isOnlyHorizontalLine = false
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e?.keyCode == KeyEvent.VK_CONTROL) {
            isOnlyVerticalLine = true
        } else if (e?.keyCode == KeyEvent.VK_ALT) {
            isOnlyHorizontalLine = true
        }
    }

    override fun keyTyped(e: KeyEvent?) {}
	override fun mouseClicked(e: MouseEvent) {}
	override fun mouseEntered(e: MouseEvent) {}
	override fun mouseExited(e: MouseEvent) {}

	override fun mouseMoved(e: MouseEvent) {}
}