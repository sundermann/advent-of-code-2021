enum class Direction { Forward, Up, Down }

data class Instruction(val command: Direction, val steps: Int) {
    fun toLocation() = when(command) {
        Direction.Forward -> Location(steps, 0)
        Direction.Up -> Location(0, -steps)
        Direction.Down -> Location(0, steps)
    }
}

data class Location(val horizontal: Int, val depth: Int) {
    operator fun plus(p: Location) = Location(horizontal + p.horizontal, depth + p.depth)
}

fun main() {

    fun String.toInstruction() : Instruction {
        val (direction, value) = this.split(" ")

        return when(direction) {
            "forward" -> Instruction(Direction.Forward, value.toInt())
            "down" -> Instruction(Direction.Down, value.toInt())
            "up" -> Instruction(Direction.Up, value.toInt())
            else -> error("Unexpected input")
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { it.toInstruction() }
            .fold(Location(0, 0)) { acc, inst -> acc + inst.toLocation() }
            .let { it.horizontal * it.depth }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInstruction() }
            .fold(Pair(Location(0, 0), 0)) { (location, aim), inst -> when(inst.command) {
                Direction.Forward -> Pair(Location(location.horizontal + inst.steps, location.depth + aim * inst.steps), aim)
                Direction.Up -> Pair(location, aim - inst.steps)
                Direction.Down -> Pair(location, aim + inst.steps)
            } }
            .let { (location, _) -> location.horizontal * location.depth }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
