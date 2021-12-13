fun main() {

    data class Fold(val direction: Char, val coord: Int)
    data class Point(val x: Int, val y: Int) {
        fun applyFold(fold: Fold): Point {
            return when (fold.direction) {
                'x' -> {
                    if (x > fold.coord) {
                        Point(x - 2 * (x - fold.coord), y)
                    } else this
                }
                'y' -> this.flipped().applyFold(Fold('x', fold.coord)).flipped()
                else -> error("invalid fold direction")
            }
        }

        fun flipped() = Point(y, x)
    }

    fun String.toPoint() = split(",").let { Point(it[0].toInt(), it[1].toInt()) }
    fun String.toFold() = substringAfter("fold along ").split("=").let { Fold(it[0][0], it[1].toInt()) }

    fun part1(input: List<String>): Int {
        val split = input.indexOf("")

        val points = input.take(split).map { it.toPoint() }
        val folds = input.drop(split + 1).map { it.toFold() }

        return points.map { it.applyFold(folds.first()) }.toSet().count()
    }

    fun part2(input: List<String>): String {
        val split = input.indexOf("")

        val points = input.take(split).map { it.toPoint() }
        val folds = input.drop(split + 1).map { it.toFold() }

        val folded = folds.fold(points.toSet()) { p, fold -> p.map { it.applyFold(fold) }.toSet() }

        val xMax = folded.maxOf { p -> p.x }
        val yMax = folded.maxOf { p -> p.y }

        return (0..yMax).joinToString("\n") { y ->
            (0..xMax).joinToString("") { x ->
                if (folded.contains(Point(x, y))) "#" else "."
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    //check(part2(testInput) == 3509)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}