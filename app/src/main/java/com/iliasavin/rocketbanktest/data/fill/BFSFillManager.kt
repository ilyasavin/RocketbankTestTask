package com.iliasavin.rocketbanktest.data.fill

import android.graphics.Point
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import java.util.*

class BFSFillManager : FillManager {
    private var rows = DEFAULT_PIXEL_SIZE
    private var columns = DEFAULT_PIXEL_SIZE

    override var image: PixelImage = PixelImage(rows, columns)
    override var startPixel: Point = Point(0, 0)
    override var speed: Long = DEFAULT_SPEED
    override lateinit var handleNext: (Point) -> Unit
    override var isRunning: Boolean = false
    private var queue: Queue<Point> = LinkedList()

    override fun setSize(rows: Int, columns: Int) {
        this.rows = rows
        this.columns = columns

        this.image = PixelImage(this.rows, this.columns)
    }

    override fun start() {
        if (image.pixels[startPixel.x][startPixel.y] != PixelColorState.EMPTY) return
        isRunning = true

        image.pixels[startPixel.x][startPixel.y] = PixelColorState.COLORED
        queue.add(startPixel)
        handleNext(startPixel)

        while (queue.isNotEmpty() && isRunning) {
            val pixel = queue.poll()
            // West
            if (pixel.y > 0) {
                val westPixel = Point(pixel.x, pixel.y - 1)
                if (image.pixels[pixel.x][pixel.y - 1] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x][pixel.y - 1] = PixelColorState.COLORED
                    queue.add(westPixel)
                    onNextWithDelay(westPixel)
                }
            }

            // East
            if (pixel.y < image.pixels.size - 1) {
                val eastPixel = Point(pixel.x, pixel.y + 1)
                if (image.pixels[pixel.x][pixel.y + 1] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x][pixel.y + 1] = PixelColorState.COLORED
                    queue.add(eastPixel)
                    onNextWithDelay(eastPixel)
                }
            }

            // North
            if (pixel.x > 0) {
                val northPixel = Point(pixel.x - 1, pixel.y)
                if (image.pixels[pixel.x - 1][pixel.y] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x - 1][pixel.y] = PixelColorState.COLORED
                    queue.add(northPixel)
                    onNextWithDelay(northPixel)
                }
            }

            // South
            if (pixel.x < image.pixels.first().size - 1) {
                val southPixel = Point(pixel.x + 1, pixel.y)
                if (image.pixels[pixel.x + 1][pixel.y] == PixelColorState.EMPTY) {
                    image.pixels[pixel.x + 1][pixel.y] = PixelColorState.COLORED
                    queue.add(southPixel)
                    onNextWithDelay(southPixel)
                }
            }
        }

        onComplete()
    }

    override fun clear() {
        isRunning = false
        queue.clear()
        image.clear()
        startPixel = Point(0, 0)
    }
}