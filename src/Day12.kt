fun main() {

    fun String.isSmallCave() = all { it.isLowerCase() }

    fun edges(input: List<String>): Map<String, List<String>> {
        val map = mutableMapOf<String, MutableList<String>>()
        input.forEach { line ->
            val (from, to) = line.split("-")
            map.getOrPut(from) { mutableListOf() }.add(to)
            map.getOrPut(to) { mutableListOf() }.add(from)
        }
        return map
    }

    fun paths(edges: Map<String, List<String>>, currentPath: List<String>, visitTwice: Boolean = false): Int {
        if (currentPath.last() == "end") {
            return 1
        }

        val noVisitTwices = edges[currentPath.last()]!!
            .filterNot { it in currentPath && it.isSmallCave() }
            .sumOf { paths(edges, currentPath + it, visitTwice) }

        val visitTwices = edges[currentPath.last()]!!
            .filter { visitTwice && it in currentPath && it.isSmallCave() && it != "end" && it != "start" }
            .sumOf { paths(edges, currentPath + it, false) }

        return noVisitTwices + visitTwices
    }


    fun part1(input: List<String>): Int {
        return paths(edges(input), listOf("start"))
    }

    fun part2(input: List<String>): Int {
        return paths(edges(input), listOf("start"), true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}