package org.sgova.paintmemo.save

import org.sgova.paintmemo.mode.Figure
import java.io.File

interface FigureWriter {
    fun write(width: Int, height: Int, figures: Array<Figure>, file: File)
}
