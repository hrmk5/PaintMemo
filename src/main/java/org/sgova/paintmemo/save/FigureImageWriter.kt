package org.sgova.paintmemo.save

import org.sgova.paintmemo.mode.Figure
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class FigureImageWriter(val type: Type, val backgroundColor: Color) : FigureWriter {

    enum class Type(val formatName: String) {
        JPEG("jpeg"),
        PNG("png"),
        GIF("gif"),
    }

    override fun write(width: Int, height: Int, figures: Array<Figure>, file: File) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = image.graphics as Graphics2D

        // 背景を描画
        g.color = backgroundColor
        g.fillRect(0, 0, width, height)

        // 図形をすべて描画
        for (figure in figures) {
            g.color = figure.options.color
            g.stroke = BasicStroke(figure.options.stroke.toFloat())
            figure.reshape(g)
        }

        ImageIO.write(image, type.formatName, file)
    }
}
