import java.lang.Integer.max
import kotlin.math.abs

data class Point(val x: Int, val y: Int)
data class Line(val start: Point, val end: Point) {
    fun isHorizontal() = start.y == end.y
    fun isVertical() = start.x == end.x

    fun toCanonical() : Line {
        if (start.x == end.x && start.y < end.y) return this
        if (start.x < end.x) return this
        return Line(end, start)
    }

    fun coordinates() : List<Point> {
        with(toCanonical()) {
            return when {
                isHorizontal() -> (start.x..end.x).map { Point(it, end.y) }
                isVertical() -> (start.y..end.y).map { Point(start.x, it) }
                else -> {
                    val dx = end.x - start.x
                    val dy = end.y - start.y
                    val direction = if (dy == 0) 0 else dy / abs(dy)
                    (0..dx).map { d -> Point(start.x + d, start.y + direction * d) }
                }
            }
        }
    }
}

fun String.toPoint(): Point {
    val (x, y) = this.split(",").map { it.toInt() }
    return Point(x, y)
}

class Ocean(x: Int, y: Int) {
    private val board = Array(y + 1) { IntArray(x + 1) { 0 } }

    fun addLine(line: Line) {
        val coords = line.coordinates()
        coords.forEach { board[it.y][it.x] += 1 }
    }

    fun countIntersections() = board.sumOf { it.count { it >= 2 } }

    fun print() {
        board.forEach { println(it.joinToString("").replace('0', '.')) }
    }

    companion object {
        fun fromLines(lines: List<Line>) : Ocean {
            val xSize = lines.maxOf { max(it.start.x, it.end.x) }
            val ySize = lines.maxOf { max(it.start.y, it.end.y) }

            val ocean = Ocean(xSize, ySize)
            lines.forEach { ocean.addLine(it) }
            return ocean
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val lines = input.map { it.split(" -> ") }
            .map { (start, end) -> Line(start.toPoint(), end.toPoint()) }
            .filter { it.isVertical() || it.isHorizontal() }

        val ocean = Ocean.fromLines(lines)
        //ocean.print()
        return ocean.countIntersections()
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { it.split(" -> ") }
            .map { (start, end) -> Line(start.toPoint(), end.toPoint()) }

        val ocean = Ocean.fromLines(lines)
        //ocean.print()
        return ocean.countIntersections()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}