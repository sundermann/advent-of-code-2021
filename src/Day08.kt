fun main() {

    fun part1(input: List<String>): Int {
        return input.map { it.split(" | ").last() }
            .flatMap { it.split(" ") }
            .count { it.length in listOf(2, 3, 4, 7) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val signals = line.split(" | ").first().split(" ").map { it.toSet() }.groupBy { it.size }

            val mapping = mutableMapOf(
                // Length 2, 3, 4 and 8 are unique
                1 to signals.getValue(2).single(),
                7 to signals.getValue(3).single(),
                4 to signals.getValue(4).single(),
                8 to signals.getValue(7).single()
            )

            // For the remaining ones subtract segments and check the remaining segments size
            mapping[2] = signals.getValue(5).single { (it - mapping[4]!!).size == 3 }
            mapping[3] = signals.getValue(5).single { (it - mapping[7]!!).size == 2 }
            mapping[5] = signals.getValue(5).single { (it - mapping[2]!!).size == 2 }

            mapping[6] = signals.getValue(6).single { (it - mapping[1]!!).size == 5 }
            mapping[0] = signals.getValue(6).single { (it - mapping[5]!!).size == 2 }
            mapping[9] = signals.getValue(6).single { (it - mapping[3]!!).size == 1 }

            val reversed = mapping.entries.associateBy({ it.value }) { it.key }
            line.split(" | ").last().split(" ")
                .fold(0) { acc: Int, s ->
                    10 * acc + reversed[s.toSet()]!!
                }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}