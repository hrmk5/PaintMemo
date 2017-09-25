package org.sgova.paintmemo

import javax.swing.JFrame

class MainFrame : JFrame() {
	
	val WIDTH = 500
	val HEIGHT = 500
	
	val mainPanel = PaintPanel(WIDTH, HEIGHT)
	
	init {
		setTitle("PaintMemo")
		setDefaultCloseOperation(EXIT_ON_CLOSE)
		setSize(500, 500)
		setVisible(true)
		
		contentPane.add(mainPanel)
	}
}