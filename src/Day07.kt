import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val crabs = input.single().split(",").map { it.toInt() }

        return (crabs.minOrNull()!!..crabs.maxOrNull()!!).minOf { i ->
            crabs.sumOf { abs(it - i) }
        }
    }

    fun part2(input: List<String>): Int {
        val crabs = input.single().split(",").map { it.toInt() }

        return (crabs.minOrNull()!!..crabs.maxOrNull()!!).minOf { i ->
            crabs.sumOf { val n = abs(it - i); n * (n + 1) / 2 }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}