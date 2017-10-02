package org.sgova.paintmemo.save

import org.sgova.paintmemo.mode.Figure
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class FigureImageWriter(val type: Type) : FigureWriter {

    enum class Type(val formatName: String) {
        JPEG("jpeg"),
        PNG("png"),
        GIF("gif"),
    }

    override fun write(width: Int, height: Int, figures: Array<Figure>, file: File) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g = image.graphics

        for (figure in figures) {
            figure.reshape(g)
        }

        ImageIO.write(image, type.formatName, file)
    }
}
