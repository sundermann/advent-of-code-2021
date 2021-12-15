import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList().map { it.digitToInt() } }

        val distances = Array(grid.size) { IntArray(grid[0].size) { Int.MAX_VALUE } }
        distances[0][0] = 0
        val queue = PriorityQueue<Triple<Int, Int, Int>>(compareBy {it.first} )
        queue.add(Triple(0, 0, 0))

        while (queue.isNotEmpty()) {
            val (distance, x, y) = queue.poll()
            if (distance > distances[x][y])
                continue

            for ((dx, dy) in listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))) {
                val neighbourX = x + dx
                val neighbourY = y + dy
                if (neighbourX in grid.indices && neighbourY in grid[0].indices
                    && distance + grid[neighbourX][neighbourY] < distances[neighbourX][neighbourY]) {
                    distances[neighbourX][neighbourY] = distance + grid[neighbourX][neighbourY]
                    queue.add(Triple(distances[neighbourX][neighbourY], neighbourX, neighbourY))
                }
            }
        }

        return distances[grid.size - 1][grid[0].size - 1]
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toList().map { it.digitToInt() } }

        val newGrid = mutableListOf<MutableList<Int>>()
        (0 until 5).forEach { x ->
            (0 until 5).forEach { y ->
                grid.indices.forEach { i ->
                    if (y == 0) newGrid += mutableListOf<Int>()
                    grid[0].indices.forEach { j ->
                        newGrid[x * grid.size + i] += (grid[i][j] + x + y - 1) % 9 + 1
                    }
                }
            }
        }

        return part1(newGrid.map { it.joinToString("") })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}