fun main() {
    fun part1(input: List<String>): Int {
        val gamma = List(input.first().length) {
            i -> input.groupingBy { it[i] }.eachCount().maxByOrNull { it.value }!!.key
        }
        val epsilon = gamma.map { if (it == '0') '1' else '0' }

        return gamma.joinToString("").toInt(2) * epsilon.joinToString("").toInt(2)
    }

    fun part2(input: List<String>): Int {
        val oxygen = input.toMutableList()
        val co2 = input.toMutableList()

        for (i in 0 until input.first().length) {
            val common = oxygen.groupingBy { it[i] }.eachCount().maxWithOrNull(compareBy({ it.value }, {it.key}))!!.key
            val uncommon = co2.groupingBy { it[i] }.eachCount().minWithOrNull(compareBy({ it.value }, {it.key}))!!.key

            oxygen.removeAll { it[i] != common }
            co2.removeAll { it[i] != uncommon }
        }

        return oxygen.single().toInt(2) * co2.single().toInt(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
