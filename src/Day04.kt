fun List<MutableList<Int>>.checkWin(): Boolean {
    val rowWin = this.any { it.all { it == -1 } }
    val colWin = this.indices.any { li -> this.indices.all { this[it][li] == -1 } }

    return rowWin || colWin
}

fun main() {

    fun part1(input: List<String>): Int {
        val numbers = input.first().split(",").map { it.toInt() }
        val boards = input.drop(1)
            .filter { it.isNotBlank() }
            .chunked(5)
            .map { board ->
                board.map { line ->
                    line.split(" ", "  ")
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }.toMutableList()
                }
            }

        for (num in numbers) {
            boards.forEach { it.forEach { it.replaceAll { if (it == num) -1 else it } } }
            val win = boards.find { it.checkWin() }
            if (win != null) return win.flatten().filterNot { it == -1 }.sum() * num
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val numbers = input.first().split(",").map { it.toInt() }
        var boards = input.drop(1)
            .filter { it.isNotBlank() }
            .chunked(5)
            .map { board ->
                board.map { line ->
                    line.split(" ", "  ")
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }.toMutableList()
                }
            }

        for (num in numbers) {
            boards.forEach { it.forEach { it.replaceAll { if (it == num) -1 else it } } }
            if (boards.size > 1)
                boards = boards.filterNot { it.checkWin() }
            else if (boards.single().checkWin()) // keep playing until the board wins
                return boards.single().flatten().filterNot { it == -1 }.sum() * num
        }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
