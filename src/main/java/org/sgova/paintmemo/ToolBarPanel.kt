package org.sgova.paintmemo

import com.google.common.collect.Iterables
import org.sgova.paintmemo.mode.*
import org.sgova.paintmemo.save.FigureImageWriter
import org.sgova.paintmemo.save.FigureWriter
import java.awt.Color
import java.awt.Dimension
import java.awt.Event
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import java.awt.image.WritableRaster
import java.awt.image.ColorModel
import java.awt.image.PixelGrabber
import java.util.*


class ToolBarPanel(private val paintPanel: PaintPanel) : JPanel(), DocumentListener {

    val layout = FlowLayout()

    val drawingText = JTextField(15)

    class FigureButton(displayName: String,
                       default: Boolean = false,
                       val create: (x: Int, y: Int, options: FigureOptions) -> Figure) : JToggleButton(displayName) {
        init {
            isSelected = default
        }
    }

    // 描画する図形を選択
	val freehand = FigureButton("自由", true) { x, y, options -> FreehandFigure(x, y, options) }
    val line = FigureButton("／") { x, y, options -> LineFigure(x, y, options) }
	val rectangle = FigureButton("□") { x, y, options -> RectangleFigure(x, y, options) }
    val text = FigureButton("文字") { x, y, options -> TextFigure(x, y, drawingText.text, options) }
	val figures: Array<FigureButton> = arrayOf(freehand, line, rectangle, text)
	class ColorItem(val color: Color, val displayName: String) {
		override fun toString(): String {
			return displayName
		}
	}

    // 色を変更するコンボボックス
	val colorBox = JComboBox(arrayOf(
		ColorItem(Color.BLACK, "黒"),
		ColorItem(Color.RED, "赤"),
		ColorItem(Color.BLUE, "青"),
		ColorItem(Color.GREEN, "緑"),
		ColorItem(Color.YELLOW, "黄"),
        ColorItem(Color.WHITE, "白")
	))

    // 線の太さを変更するコンボボックス
    val strokeBox = JTextField("2", 2).also {
        it.document.addDocumentListener(this)
    }

    // 元に戻すボタン
    val undoButton = JButton("↲").also {
        // ショートカットキーの設定
        it.actionMap.put("undo", object : AbstractAction("undo") {
            override fun actionPerformed(e: ActionEvent?) = undo(e)
        })
        it.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK), "undo")

        // 動作の設定
        it.addActionListener(this::undo)
    }

    // やり直しボタン
    val redoButton = JButton("↱").also {
        // ショートカットキーの設定
        it.actionMap.put("redo", object : AbstractAction("redo") {
            override fun actionPerformed(e: ActionEvent?) = redo(e)
        })
        it.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK), "redo")

        // 動作の設定
        it.addActionListener(this::redo)
    }

    // 背景色を変更
    val backgroundColorBox = JComboBox(arrayOf(
        ColorItem(Color.WHITE, "白"),
        ColorItem(Color.BLACK, "黒"),
        ColorItem(Color(0, 0, 0, 0), "透明")
    )).also {
        it.addItemListener(this::onChangeBackgroundColor)
        it.preferredSize = Dimension(60, 19)
    }

    // 保存ボタン
    val saveButton = JButton("保存").also {
        it.addActionListener(this::save)
    }

	init {
		val background = Color(235, 235, 235)
		this.background = background

        // undo,redoボタン
        add(undoButton)
        add(redoButton)

        // 描画する文字
        add(drawingText)

        // 図形選択ボタンの設定
		figures.forEach {
			it.background = background
			it.addActionListener(this::onChangeFigure)
			add(it)
		}

        // 色選択コンボボックスの設定
        colorBox.preferredSize = Dimension(50,19)
		colorBox.background = background
		colorBox.addItemListener(this::onChangeColor)
		colorBox.selectedIndex = 0
		add(colorBox)

        // 線の太さ
        add(strokeBox)

        // 背景色選択
        add(backgroundColorBox)

        // 保存ボタン
        add(saveButton)
	}
	
	fun onChangeColor(e: ItemEvent) {
		val selectedItem = e.item
		if (selectedItem is ColorItem) {
			paintPanel.currentColor = selectedItem.color
		}
	}

    fun onChangeBackgroundColor(e: ItemEvent) {
        val selectedItem = e.item
        if (selectedItem is ColorItem) {
            paintPanel.setCurrentBackground(selectedItem.color)
        }
    }
	
	fun onChangeFigure(e: ActionEvent) {
        // 描画する図形を設定
        val figure: FigureButton? = figures.firstOrNull{ e.source == it }
        if (figure != null) {
            setCurrentFigure(figure.create)
        }

        // 他のトグルボタンの選択を解除する
		figures.forEach {
			if (e.source != it) {
                it.isSelected = false
			}
		}
	}
	
	fun setCurrentFigure(create: (x: Int, y: Int, options: FigureOptions) -> Figure) {
		paintPanel.createFigure = create
	}

    fun onStrokeChanged() {
        val stroke = strokeBox.text.toIntOrNull()
        if (stroke != null) {
            paintPanel.currentStroke = stroke
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun undo(e: ActionEvent?) {
        paintPanel.undo()
    }

    @Suppress("UNUSED_PARAMETER")
    fun redo(e: ActionEvent?) {
        paintPanel.redo()
    }

    @Suppress("UNUSED_PARAMETER")
    fun save(e: ActionEvent) {
        val chooser = JFileChooser()

        val selected = chooser.showSaveDialog(this)
        if (selected == JFileChooser.APPROVE_OPTION) {
            val file = chooser.selectedFile
            val figureWriter = createFigureWriter(file.getAbsolutePath())

            figureWriter.write(paintPanel.width, paintPanel.height, Iterables.toArray(paintPanel.paper.figures, Figure::class.java), file)
        }
    }
    
    private fun endsWith(filepath: String, ends: String): Boolean {
        return filepath.toUpperCase().endsWith(ends)
    }

    fun createFigureWriter(filepath: String): FigureWriter {
        return when {
            endsWith(filepath, ".jpg") || endsWith(filepath, ".jpeg") -> FigureImageWriter(FigureImageWriter.Type.JPEG, paintPanel.currentBackgroundColor)
            endsWith(filepath, ".png") -> FigureImageWriter(FigureImageWriter.Type.PNG, paintPanel.currentBackgroundColor)
            endsWith(filepath, ".gif") -> FigureImageWriter(FigureImageWriter.Type.GIF, paintPanel.currentBackgroundColor)
            else -> FigureImageWriter(FigureImageWriter.Type.JPEG, paintPanel.currentBackgroundColor)
        }
    }

    override fun removeUpdate(e: DocumentEvent?) = onStrokeChanged()
    override fun insertUpdate(e: DocumentEvent?) = onStrokeChanged()
    override fun changedUpdate(e: DocumentEvent?) = onStrokeChanged()
}
