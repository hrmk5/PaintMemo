package org.sgova.paintmemo

import org.sgova.paintmemo.mode.*
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

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
	
	val colorBox = JComboBox(arrayOf(
		ColorItem(Color.BLACK, "黒"),
		ColorItem(Color.RED, "赤"),
		ColorItem(Color.BLUE, "青"),
		ColorItem(Color.GREEN, "緑"),
		ColorItem(Color.YELLOW, "黄")
	))

    val strokeBox = JTextField("2", 2).also {
        it.document.addDocumentListener(this)
    }
	
	init {
		val background = Color(235, 235, 235)
		this.background = background

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

        add(strokeBox)
	}
	
	fun onChangeColor(e: ItemEvent) {
		val selectedItem = e.item
		if (selectedItem is ColorItem) {
			paintPanel.currentColor = selectedItem.color
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

    override fun removeUpdate(e: DocumentEvent?) = onStrokeChanged()
    override fun insertUpdate(e: DocumentEvent?) = onStrokeChanged()
    override fun changedUpdate(e: DocumentEvent?) = onStrokeChanged()
}