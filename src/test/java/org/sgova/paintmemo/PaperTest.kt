package org.sgova.paintmemo

import org.junit.Test
import org.sgova.paintmemo.mode.RectangleFigure
import java.awt.Color

import org.junit.Assert.assertEquals
import org.sgova.paintmemo.mode.FigureOptions

class PaperTest {

    val paper = Paper()

    @Test
    fun testPushFigure() {
        val figure = RectangleFigure(0, 0, FigureOptions(Color.BLACK, 1))
        paper.pushFigure(figure)

        assertEquals(paper.figures[0], figure)
    }
}