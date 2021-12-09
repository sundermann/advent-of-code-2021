fun main() {

    fun List<List<Int>>.neighbours(x: Int, y: Int): List<Point> {
        val up = if (x - 1 >= 0) Point(x - 1, y) else null
        val down = if (x + 1 < this.size) Point(x + 1, y) else null
        val left = if (y - 1 >= 0) Point(x, y - 1) else null
        val right = if (y + 1 < this[x].size) Point(x, y + 1) else null

        return listOfNotNull(up, down, left, right)
    }

    fun List<List<Int>>.basin(x: Int, y: Int): Set<Point> {
        var basin = setOf(Point(x,y))
        var nextBasin: Set<Point>

        while(true) {
            nextBasin = basin.toMutableSet()
            for (point in basin) {
                nextBasin.addAll(this@basin.neighbours(point.x, point.y).filter { this@basin[it.x][it.y] != 9 })
            }

            if (nextBasin.size == basin.size) return nextBasin
            basin = nextBasin.toSet()
        }
    }

    fun part1(input: List<String>): Int {
        val heightmap = List(input.size) { MutableList(input.first().length) { 0 } }
        input.forEachIndexed { i, line -> line.toCharArray().forEachIndexed { j, height -> heightmap[i][j] = Character.getNumericValue(height) } }

        return heightmap.mapIndexed { x, line ->
            line.mapIndexed { y, height ->
                if (heightmap.neighbours(x, y).map { heightmap[it.x][it.y] }. all { height < it }) height + 1 else 0 } }
            .flatten().sum()
    }

    fun part2(input: List<String>): Int {
        val heightmap = List(input.size) { MutableList(input.first().length) { 0 } }
        input.forEachIndexed { i, line -> line.toCharArray().forEachIndexed { j, height -> heightmap[i][j] = Character.getNumericValue(height) } }

        return heightmap
            .asSequence()
            .mapIndexed { x, line ->
                line.mapIndexed { y, height ->
                    if (heightmap.neighbours(x, y).map { heightmap[it.x][it.y] }.all { height < it }) Point(x,y) else null} }
            .flatten()
            .filterNotNull()
            .map { heightmap.basin(it.x, it.y) }
            .sortedByDescending { it.size }
            .take(3)
            .map { it.size }
            .reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}