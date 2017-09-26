package org.sgova.paintmemo

import org.sgova.paintmemo.mode.Figure
import java.util.Stack

class Paper() {
	
	val figures = Stack<Figure>()
	
	fun pushFigure(figure: Figure) {
		figures.push(figure)
	}
	
	@Deprecated("Not Implemented")
	fun undo() {
		
	}
	
	@Deprecated("Not Implemented")
	fun redo() {
		
	}
	
}