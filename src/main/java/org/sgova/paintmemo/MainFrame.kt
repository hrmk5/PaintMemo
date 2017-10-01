package org.sgova.paintmemo

import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

class MainFrame : JFrame() {

    val WIDTH = 600
	val HEIGHT = 500
	
	val mainPanel = PaintPanel()
	val toolBarPanel = ToolBarPanel(mainPanel)
	val mainlayout = GridBagLayout()
	
	init {
        title = "PaintMemo"
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(WIDTH, HEIGHT)
        layout = mainlayout

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

        isVisible = true
	}
}