package org.sgova.paintmemo

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class PaintPanel(width: Int, height: Int) : JPanel() {
	
	init {
		setPreferredSize(Dimension(width, height))
	}
	
	override fun paintComponent(g: Graphics) {
		g.color = Color.WHITE
		g.fillRect(0, 0, width, height)
	}
}