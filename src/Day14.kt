fun main() {

    data class Rule(val from: String, val to: String)

    fun String.toRule() = split(" -> ").let { (from, to) -> Rule(from, to) }

    fun part1(input: List<String>): Int {
        var polymerTemplate = input.first()

        val rules = input.drop(2).map { it.toRule() }

        repeat(10) {
            polymerTemplate = polymerTemplate
                .windowed(2)
                .joinToString("") { pair ->
                rules.find { it.from == pair }
                    ?.let { pair[0] + it.to } ?: pair[0].toString()
            } + polymerTemplate.last()

        }

        val freq = polymerTemplate.groupingBy { it }.eachCount()
        val max = freq.maxOf { it.value }
        val min = freq.minOf { it.value }

        return max - min
    }

    fun part2(input: List<String>): Long {
        val polymerTemplate = input.first()
        val rules = input.drop(2).map { it.toRule() }

        var pieceCount = polymerTemplate
            .windowed(2)
            .groupingBy { it }
            .fold(0L) { acc, _ -> acc + 1 } // eachCount

        repeat(40) {
            pieceCount = pieceCount.flatMap { (pair , count) -> rules.find { it.from == pair }
                ?.let { listOf("${it.from[0]}${it.to}" to count, "${it.to}${it.from[1]}" to count) }
                ?: listOf(pair to count) }
                .groupingBy { it.first }.fold(0L) { acc, e -> acc + e.second }
        }

        val charCount =
            pieceCount.toList()
                .flatMap { (p, c) -> listOf(p[0] to c, p[1] to c) }
                .groupingBy { it.first }.fold(0L) { acc, e -> acc + e.second }
                .toMutableMap()

        charCount[polymerTemplate.first()] = charCount[polymerTemplate.first()]!! + 1
        charCount[polymerTemplate.last()] = charCount[polymerTemplate.last()]!! + 1

        val max = charCount.maxOf { it.value } / 2
        val min = charCount.minOf { it.value } / 2

        return max - min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}