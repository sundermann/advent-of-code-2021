fun main() {

    fun LongArray.rotate(n: Int) =
        let { sliceArray(n until size) + sliceArray(0 until n) }

    fun part1(input: List<String>): Long {
        var fish = LongArray(9).apply { input.single().split(",").map { it.toInt() }.forEach { this[it] += 1L } }

        repeat(80) {
            fish = fish.rotate(1)
            fish[6] += fish[8]
        }

        return fish.sum()
    }

    fun part2(input: List<String>): Long {
        var fish = LongArray(9).apply { input.single().split(",").map { it.toInt() }.forEach { this[it] += 1L } }

        repeat(256) {
            fish = fish.rotate(1)
            fish[6] += fish[8]
        }

        return fish.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}