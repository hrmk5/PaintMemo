package org.sgova.paintmemo

import java.awt.Color
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JPanel
import javax.swing.JToggleButton

class ToolBarPanel(private val paintPanel: PaintPanel) : JPanel(), ActionListener {
	
	val layout = FlowLayout()
	
	val freehand = JToggleButton("自由")
	val rectangle = JToggleButton("四角")
	val triangle = JToggleButton("三角")
	val ellipse = JToggleButton("円")
	val figures: Array<JToggleButton> = arrayOf(freehand, rectangle, triangle, ellipse)
	
	init {
		val background = Color(235, 235, 235)
		setBackground(background)
		
		figures.forEach {
			it.background = background
			it.addActionListener(this)
			add(it)
		}
	}
	
	override fun actionPerformed(e: ActionEvent) {
		// 作成する図形を変更
		when (e.source) {
			freehand -> 	setCurrentFigure("freehand")
			rectangle -> 	setCurrentFigure("rectangle")
			triangle -> 	setCurrentFigure("triangle")
			ellipse -> 		setCurrentFigure("ellipse")
		}
		
		// 他のトグルボタンの選択を解除
		figures.forEach {
			if (e.source != it) {
				it.setSelected(false)
			}
		}
	}
	
	fun setCurrentFigure(figureName: String) {
		paintPanel.createFigure = figureName
	}
}