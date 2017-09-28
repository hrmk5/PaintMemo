package org.sgova.paintmemo

import java.awt.Color
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JToggleButton

class ToolBarPanel(private val paintPanel: PaintPanel) : JPanel() {
	
	val layout = FlowLayout()
	
	val freehand = JToggleButton("自由")
	val rectangle = JToggleButton("□")
	val triangle = JToggleButton("△")
	val ellipse = JToggleButton("○")
	val figures: Array<JToggleButton> = arrayOf(freehand, rectangle, triangle, ellipse)
	
	class ColorItem(val color: Color, val displayName: String) {
		override fun toString(): String {
			return displayName
		}
	}
	
	val colorBox = JComboBox(arrayOf(
		ColorItem(Color.BLACK, "黒"),
		ColorItem(Color.RED, "赤"),
		ColorItem(Color.BLUE, "青"),
		ColorItem(Color.GREEN, "緑"),
		ColorItem(Color.YELLOW, "黄")
	))
	
	init {
		val background = Color(235, 235, 235)
		this.background = background

        // 図形選択ボタンの設定
		figures.forEach {
			it.background = background
			it.addActionListener(this::onChangeFigure)
			add(it)
		}

        // 色選択コンボボックスの設定
		colorBox.maximumSize = colorBox.preferredSize
		colorBox.background = background
		colorBox.addItemListener(this::onChangeColor)
		colorBox.selectedIndex = 0
		add(colorBox)
	}
	
	fun onChangeColor(e: ItemEvent) {
		val selectedItem = e.item
		if (selectedItem is ColorItem) {
			paintPanel.currentColor = selectedItem.color
		}
	}
	
	fun onChangeFigure(e: ActionEvent) {
        // 描画する図形を設定
		when (e.source) {
			freehand -> 	setCurrentFigure("freehand")
			rectangle -> 	setCurrentFigure("rectangle")
			triangle -> 	setCurrentFigure("triangle")
			ellipse -> 		setCurrentFigure("ellipse")
		}

        // 他のトグルボタンの選択を解除する
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