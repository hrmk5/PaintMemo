package org.sgova.paintmemo.save

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.rules.TemporaryFolder
import org.sgova.paintmemo.mode.Figure
import org.sgova.paintmemo.mode.FigureOptions
import org.sgova.paintmemo.mode.LineFigure
import org.sgova.paintmemo.mode.RectangleFigure
import org.assertj.core.api.Assertions.*
import java.awt.Color
import java.io.File

class ImageFigureWriterTest : ExternalResource() {

    lateinit var writer: FigureImageWriter
    @Rule @JvmField val tempFolder = TemporaryFolder()
    lateinit var testFigures: Array<Figure>

    @Before
    fun init() {
        val options = FigureOptions(Color.CYAN, 5)
        testFigures = arrayOf(
                RectangleFigure(0, 5, options).apply { x = 100; y = 440 },
                LineFigure(0, 50, options).apply { x = 10; y = 40 },
                RectangleFigure(90, 60, options).apply { x = 190; y = 90 },
                RectangleFigure(86, 300, options).apply { x = 265; y = 40 },
                RectangleFigure(10, 478, options).apply { x = 451; y = 9 }
        )
    }

    fun createWriter(type: FigureImageWriter.Type) {
        writer = FigureImageWriter(type)
    }

    @Test
    fun testWriteToJPEG() {
        createWriter(FigureImageWriter.Type.JPEG)

        val file = tempFolder.newFile()

        writer.write(500, 500, testFigures, file)

        assertThat(file.length()).isEqualTo(24082)
    }

    @Test
    fun testWriteToPNG() {
        createWriter(FigureImageWriter.Type.PNG)

        val file = tempFolder.newFile()

        writer.write(100, 123, testFigures, file)

        assertThat(file.length()).isEqualTo(351)
    }

    @Test
    fun testWriteToGIF() {
        createWriter(FigureImageWriter.Type.GIF)

        val file = tempFolder.newFile()

        writer.write(300, 100, testFigures, file)

        assertThat(file.length()).isEqualTo(872)
    }
}
