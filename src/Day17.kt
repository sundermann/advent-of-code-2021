import kotlin.math.max

fun main() {
    data class State(val point: Point = Point(0, 0), val vX: Int, val vY: Int, val maxHeight: Int = 0, val vXStart: Int = vX, val vYStart: Int = vY) {
        fun nextXVel() : Int {
            if (vX > 0) return vX - 1
            if (vX < 0) return vX + 1
            return 0
        }

        fun step() = State(Point(point.x + vX, point.y + vY), nextXVel(), vY - 1, max(maxHeight, point.y + vY), vXStart, vYStart)
    }

    data class TargetArea(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int)
    fun String.toTargetArea() = substringAfter("target area: x=")
        .split(", y=")
        .flatMap { it.split("..") }
        .map { it.toInt() }
        .let { (xMin, xMax, yMin, yMax) -> TargetArea(xMin, xMax, yMin, yMax) }

    fun Point.within(target: TargetArea) = x in target.xMin..target.xMax && y in target.yMin .. target.yMax
    fun Point.passed(target: TargetArea) = y < target.yMax

    fun part1(input: List<String>): Int {
        val target = input.first().toTargetArea()

        val possible = mutableListOf<State>()
        var initialStates = (-250..250).flatMap { vX ->
            (-250 .. 250).map { vY->
                 State(vX = vX, vY = vY)
            }
        }

        while(initialStates.any { !it.point.passed(target) }) {
            initialStates = initialStates
                .map { it.step() }

            possible += initialStates.filter { it.point.within(target) }
        }

        return possible.maxOf { it.maxHeight }
    }

    fun part2(input: List<String>): Int {
        val target = input.first().toTargetArea()

        val possible = mutableListOf<State>()
        var initialStates = (-250..250).flatMap { vX ->
            (-250 .. 250).map { vY->
                State(vX = vX, vY = vY)
            }
        }

        while(initialStates.any { !it.point.passed(target) }) {
            initialStates = initialStates
                .map { it.step() }

            possible += initialStates.filter { it.point.within(target) }
        }

        return possible.distinctBy { Pair(it.vXStart, it.vYStart) }.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}