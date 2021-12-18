sealed class SnailfishNumber {
    abstract fun magnitude(): Int
    abstract fun findNestedPair(depth: Int): SnailfishPair?
    abstract fun findSplitRegularNumber(max: Int): RegularNumber?
    abstract fun traverse(keep: SnailfishPair): List<SnailfishNumber>
    abstract fun replace(actions: Map<SnailfishNumber, SnailfishNumber>): SnailfishNumber

    fun reduce(): Map<SnailfishNumber, SnailfishNumber>? {
        findNestedPair(4)?.let { pair ->
            val actions = mutableMapOf<SnailfishNumber, SnailfishNumber>(pair to RegularNumber(0))
            val neighbours = traverse(pair)
            val i = neighbours.indexOf(pair)
            (neighbours.getOrNull(i - 1) as? RegularNumber)?.let { actions[it] = RegularNumber(it.x + (pair.left as RegularNumber).x) }
            (neighbours.getOrNull(i + 1) as? RegularNumber)?.let { actions[it] = RegularNumber(it.x + (pair.right as RegularNumber).x) }
            return actions
        }

        findSplitRegularNumber(10)?.let {
            return mapOf(it to SnailfishPair(RegularNumber(it.x / 2), RegularNumber((it.x + 1) / 2)))
        }

        return null
    }
}

class RegularNumber(val x: Int) : SnailfishNumber() {
    override fun magnitude() = x
    override fun findNestedPair(depth: Int) = null
    override fun findSplitRegularNumber(max: Int) = if (x >= max) this else null
    override fun traverse(keep: SnailfishPair) = listOf(this)
    override fun replace(actions: Map<SnailfishNumber, SnailfishNumber>): SnailfishNumber {
        actions[this]?.let { return it }
        return this
    }

    override fun toString() = x.toString()
}

class SnailfishPair(val left: SnailfishNumber, val right: SnailfishNumber) : SnailfishNumber() {
    override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
    override fun findNestedPair(depth: Int): SnailfishPair? {
        if (depth == 0) return this
        left.findNestedPair(depth - 1)?.let { return it }
        right.findNestedPair(depth - 1)?.let { return it }
        return null
    }

    override fun findSplitRegularNumber(max: Int): RegularNumber? {
        left.findSplitRegularNumber(max)?.let { return it }
        right.findSplitRegularNumber(max)?.let { return it }
        return null
    }

    override fun traverse(keep: SnailfishPair) =
        if (this == keep) listOf(this) else left.traverse(keep) + right.traverse(keep)

    override fun replace(actions: Map<SnailfishNumber, SnailfishNumber>): SnailfishNumber {
        actions[this]?.let { return it }
        return SnailfishPair(left.replace(actions), right.replace(actions))
    }

    override fun toString() = "[$left, $right]"
}

fun main() {

    fun String.toSnailfishNumber(): SnailfishNumber {
        val sb = StringBuilder(this)

        fun parse(): SnailfishNumber {
            return when (sb.first()) {
                '[' -> {
                    sb.delete(0, 1)
                    SnailfishPair(parse(), parse())
                }
                ']', ',' -> {
                    sb.delete(0, 1)
                    parse()
                }
                else -> {
                    val i = sb.takeWhile { it.isDigit() }
                    sb.delete(0, i.length)
                    RegularNumber(i.toString().toInt())
                }
            }
        }
        return parse()
    }

    fun add(a: SnailfishNumber, b: SnailfishNumber): SnailfishNumber {
        var current: SnailfishNumber = SnailfishPair(a, b)
        var r = current.reduce()
        while(r != null) {
            current = current.replace(r)
            r = current.reduce()
        }
        return current
    }

    fun part1(input: List<String>): Int {
        return input.map { it.toSnailfishNumber() }
            .reduce { a, b -> add(a, b) }.magnitude()
    }

    fun part2(input: List<String>): Int {
        val sn = input.map { it.toSnailfishNumber() }

        return sn.flatMap { a -> sn.map { Pair(a, it) } }
            .filter { (a, b) -> a != b }
            .maxOf { (a, b) -> add(a, b).magnitude() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}