fun main() {

    fun Char.invalidScore() = when(this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> error("Invalid character")
    }

    fun Char.completionScore() = when(this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> error("Invalid character")
    }

    fun String.completionScore() = this.fold(0L) { acc, c -> acc * 5 + c.completionScore() }

    fun Char.isClosing() = when(this) {
        ')' -> true
        ']' -> true
        '}' -> true
        '>' -> true
        '(' -> false
        '[' -> false
        '{' -> false
        '<' -> false
        else -> error("Invalid character")
    }

    fun Char.opening() = when(this) {
        ')' -> '('
        ']' -> '['
        '}' -> '{'
        '>' -> '<'
        else -> error("Invalid character")
    }

    fun Char.closing() = when(this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> error("Invalid character")
    }

    fun Char.isOpening() = !this.isClosing()

    fun String.firstInvalidChar() : Char? {
        val stack = mutableListOf<Char>()
        this.toCharArray().forEach {
            if (it.isOpening()) stack.add(it)
            else if (stack.last() == it.opening())
                stack.removeLast()
            else return it
        }

        return null
    }

    fun String.completeInvalid() : String? {
        val stack = mutableListOf<Char>()
        this.toCharArray().forEach {
            if (it.isOpening()) stack.add(it)
            else if (stack.last() == it.opening())
                stack.removeLast()
            else return null
        }

        return stack.reversed().map { it.closing() }.joinToString("")
    }

    fun part1(input: List<String>): Int {
        return input.mapNotNull { it.firstInvalidChar() }.sumOf { it.invalidScore() }
    }

    fun <T: Comparable<T>> List<T>.median() = let { it.sorted()[it.size / 2] }

    fun part2(input: List<String>): Long {
        val scores = input.mapNotNull { it.completeInvalid() }
        return scores.map { it.completionScore() }.median()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}