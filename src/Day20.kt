fun main() {

    data class Vec2(val x: Int, val y: Int) {
        fun neighbours() = (x - 1 .. x + 1).flatMap { x -> (y - 1 .. y + 1).map { y -> Vec2(x, y) } }
    }

    data class BoundingBox(val min: Vec2, val max: Vec2)
    data class Image(val pixels: Map<Vec2, Boolean>) {
        fun boundingBox(): BoundingBox {
            return BoundingBox(
                    Vec2(pixels.keys.minOf { it.x }, pixels.keys.minOf { it.y }),
                    Vec2(pixels.keys.maxOf { it.x }, pixels.keys.maxOf { it.y })
                )
        }

        fun litPixels() = pixels.values.count { it }

        fun enhance(enhancement: String, background: Boolean = false): Image {
            val bounding = boundingBox()
            val enhancedImage = pixels.toMutableMap()

            for (x in bounding.min.x - 2 .. bounding.max.x + 2) {
                for (y in bounding.min.y - 2 .. bounding.max.y + 2) {
                    val p = Vec2(x, y)
                    val binary = p.neighbours().joinToString("") {
                        when (pixels[it]) {
                            false -> "0"
                            true -> "1"
                            null -> if (background) "1" else "0"
                        }
                    }.toInt(2)

                    enhancedImage[p] = enhancement[binary] == '#'
                }
            }

            return Image(enhancedImage)
        }
    }

    fun part1(input: List<String>): Int {
        val enhancement = input.first()

        var current = input.drop(2)
            .flatMapIndexed { x, line -> line.mapIndexed { y, c -> Vec2(x, y) to (c == '#') } }
            .let { Image(it.toMap()) }

        repeat(2) {
            current = current.enhance(enhancement, if (enhancement[0] == '#') (it % 2) == 1 else false)
        }

        return current.litPixels()
    }

    fun part2(input: List<String>): Int {
        val enhancement = input.first()

        var current = input.drop(2)
            .flatMapIndexed { x, line -> line.mapIndexed { y, c -> Vec2(x, y) to (c == '#') } }
            .let { Image(it.toMap()) }

        repeat(50) {
            current = current.enhance(enhancement, if (enhancement[0] == '#') (it % 2) == 1 else false)
        }

        return current.litPixels()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}