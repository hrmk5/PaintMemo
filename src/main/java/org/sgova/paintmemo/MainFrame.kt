package org.sgova.paintmemo

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.UIManager

class MainFrame : JFrame() {
	
	val WIDTH = 500
	val HEIGHT = 500
	
	val mainPanel = PaintPanel()
	val toolBarPanel = ToolBarPanel(mainPanel)
	val layout = GridBagLayout()
	
	init {
		setTitle("PaintMemo")
		setDefaultCloseOperation(EXIT_ON_CLOSE)
		setSize(500, 500)
		setLayout(layout)

        // ツールバーを追加
		contentPane.add(toolBarPanel, GridBagConstraints().apply {
			gridx = 0
			gridy = 0
			weightx = 1.0
			weighty = 0.0
			fill = GridBagConstraints.BOTH
		})

        // 描画パネルを追加
		contentPane.add(mainPanel, GridBagConstraints().apply {
			gridx = 0
			gridy = 1
			weightx = 1.0
			weighty = 1.0
			fill = GridBagConstraints.BOTH
		})

		// Look&Feelを変更
		val lafClassName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
		try {
			UIManager.setLookAndFeel(lafClassName)
			SwingUtilities.updateComponentTreeUI(this)
		} catch (e: Exception) {
			System.err.println("Error setting look and feel " + lafClassName)
			System.err.println(e.stackTrace)
		}
		
		setVisible(true)
	}
}