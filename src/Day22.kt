import kotlin.math.max
import kotlin.math.min

fun main() {

    infix fun IntRange.intersects(other: IntRange): Boolean =
        first <= other.last && last >= other.first

    infix fun IntRange.intersect(other: IntRange): IntRange =
        max(first, other.first)..min(last, other.last)

    fun IntRange.isInitialization() = first >= -50 && last <= 50

    data class Cube(val on: Boolean, val x: IntRange, val y: IntRange, val z: IntRange) {
        fun volume() = (x.count().toLong() * y.count().toLong() * z.count().toLong()) * if (on) 1 else -1

        fun intersect(other: Cube): Cube? = if (!intersects(other)) null
        else Cube(!on, x intersect other.x, y intersect other.y, z intersect other.z)

        fun intersects(other: Cube) = x intersects other.x && y intersects other.y && z intersects other.z
    }

    fun String.toCube(): Cube {
        val ranges = this.replace("on x=|off x=".toRegex(), "")
            .replace(",y=|,z=".toRegex(), "..")
            .split("..")
            .map { it.toInt() }

        return Cube(this.startsWith("on"), ranges[0] .. ranges[1], ranges[2] .. ranges[3], ranges[4] .. ranges[5])
    }

    fun part1(input: List<String>): Int {
        val cubes = input.map { it.toCube() }
        val grid = Array(101) { Array(101) { BooleanArray(101) } }

        cubes.filter { it.x.isInitialization() && it.y.isInitialization() && it.z.isInitialization() }
            .forEach { c -> for (x in c.x) for (y in c.y) for (z in c.z) { grid[x + 50][y + 50][z + 50] = c.on } }

        return grid.sumOf { it.sumOf { it.count { it } } }
    }

    fun part2(input: List<String>): Long {
        val cubes = input.map { it.toCube() }
        val volumes = mutableListOf<Cube>()

        cubes.forEach { c ->
            // subtract intersection with each cube
            volumes.addAll(volumes.mapNotNull { it.intersect(c) })
            // then add the whole cube
            if (c.on) volumes.add(c)
        }

        return volumes.sumOf { it.volume() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 590784)
    val testInput2 = readInput("Day22_test2")
    check(part2(testInput2) == 2758514936282235L)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}