fun main() {

    fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> {
        val i = first
        val j = second

        return (i - 1 .. i + 1).flatMap { x -> (j - 1 .. j + 1).map { y -> Pair(x, y) } }
            .filterNot { (x, y) -> i == x && j == y }
    }

    fun part1(input: List<String>): Int {
        val octopuses = input.map { it.map { it.digitToInt() }.toMutableList() }
        val indices = octopuses.indices.flatMap{ i -> octopuses.first().indices.map { j-> Pair(i, j) } }

        var flashes = 0
        repeat(100) {
            indices.forEach { (i, j) -> octopuses[i][j]++ }

            val flashed = mutableSetOf<Pair<Int, Int>>()
            while(true) {
                val flashing = indices.firstOrNull { (i, j) -> octopuses[i][j] > 9 && Pair(i, j) !in flashed } ?: break

                val neighbours = flashing.neighbours()
                    .filterNot { (i, j) -> i < 0 || j < 0 || i >= octopuses.size || j >= octopuses.first().size }

                neighbours.forEach{ (i, j) -> octopuses[i][j]++ }
                flashed += flashing
            }

            flashed.forEach { (i, j) -> octopuses[i][j] = 0 }

            flashes += flashed.size
        }

        return flashes
    }

    fun part2(input: List<String>): Int {
        val octopuses = input.map { it.map { it.digitToInt() }.toMutableList() }
        val indices = octopuses.indices.flatMap{ i -> octopuses.first().indices.map { j-> Pair(i, j) } }

        var step = 0
        while(true) {
            indices.forEach { (i, j) -> octopuses[i][j]++ }

            val flashed = mutableSetOf<Pair<Int, Int>>()
            while(true) {
                val flashing = indices.firstOrNull { (i, j) -> octopuses[i][j] > 9 && Pair(i, j) !in flashed } ?: break

                val neighbours = flashing.neighbours()
                    .filterNot { (i, j) -> i < 0 || j < 0 || i >= octopuses.size || j >= octopuses.first().size }

                neighbours.forEach{ (i, j) -> octopuses[i][j]++ }
                flashed += flashing
            }

            flashed.forEach { (i, j) -> octopuses[i][j] = 0 }
            step++
            if (flashed.size == indices.size) return step
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}