package org.sgova.paintmemo

import org.sgova.paintmemo.mode.Figure
import java.util.Stack

class Paper() {
	
	val figures = Stack<Figure>()
    val redoFigures = Stack<Figure>()
	
	fun pushFigure(figure: Figure) {
		figures.push(figure)

        if (!redoFigures.empty()) {
            redoFigures.clear()
        }
	}
	
	fun undo() {
        if (!figures.empty()) {
            val figure = figures.pop()
            redoFigures.push(figure)
        }
	}
	
	fun redo() {
        if (!redoFigures.empty()) {
            val figure = redoFigures.pop()
            figures.push(figure)
        }
	}
	
}