package org.sgova.paintmemo

import org.sgova.paintmemo.mode.RectangleFigure
import java.awt.Color

import org.junit.Test
import org.junit.Before
import org.assertj.core.api.Assertions.*

import org.sgova.paintmemo.mode.Figure
import org.sgova.paintmemo.mode.FigureOptions
import java.util.*

class PaperTest {

    lateinit var paper: Paper
    lateinit var testFigure: Figure

    @Before
    fun init() {
        paper = Paper()
        testFigure = RectangleFigure(0, 0, FigureOptions(Color.BLACK, 1))
    }

    @Test
    fun testPushFigure() {
        paper.pushFigure(testFigure)

        assertThat(paper.figures[0]).isEqualTo(testFigure)
    }

    @Test
    fun testUndo() {
        paper.pushFigure(testFigure)
        paper.undo()

        assertThat(paper.figures.empty()).isTrue()
    }

    @Test
    fun testRedo() {
        paper.pushFigure(testFigure)
        paper.undo()
        paper.redo()

        assertThat(paper.figures[0]).isEqualTo(testFigure)
    }

    @Test
    fun testRedoNotPushed() {
        paper.pushFigure(testFigure)
        paper.redo()

        val figures = Stack<Figure>()
        figures.push(paper.figures[0])

        assertThat(paper.figures).isEqualTo(figures)
    }
}