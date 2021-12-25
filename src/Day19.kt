import kotlin.math.absoluteValue

fun main() {

    data class Vec3(val x: Int, val y: Int, val z: Int) {
        fun manhattanDistance(p: Vec3) = (this - p).let { it.x.absoluteValue + it.y.absoluteValue + it.z.absoluteValue }
        operator fun minus(p: Vec3) = Vec3(x - p.x, y - p.y, z - p.z)
        operator fun plus(p: Vec3) = Vec3(x + p.x, y + p.y, z + p.z)
        operator fun times(p: Vec3) = Vec3(x * p.x, y * p.y, z * p.z)

        fun rotations() = listOf(
            Vec3(x, y, z),
            Vec3(x, -z, y),
            Vec3(x, -y, -z),
            Vec3(x, z, -y),
            Vec3(-x, -y, z),
            Vec3(-x, -z, -y),
            Vec3(-x, y, -z),
            Vec3(-x, z, y),
            Vec3(-z, x, -y),
            Vec3(y, x, -z),
            Vec3(z, x, y),
            Vec3(-y, x, z),
            Vec3(z, -x, -y),
            Vec3(y, -x, z),
            Vec3(-z, -x, y),
            Vec3(-y, -x, -z),
            Vec3(-y, -z, x),
            Vec3(z, -y, x),
            Vec3(y, z, x),
            Vec3(-z, y, x),
            Vec3(z, y, -x),
            Vec3(-y, z, -x),
            Vec3(-z, -y, -x),
            Vec3(y, -z, -x),
        )
    }

    data class Transform(val rotationIndex: Int, val trans: Vec3)

    fun tryTransform(target: Set<Vec3>, source: Set<Vec3>): Transform? {
        for (i in 0 until 24) {
            val rotated = source.map { it.rotations()[i] }
            val translation = target.flatMap { targetPoint -> rotated.map { sourcePoint -> targetPoint - sourcePoint } }
                .groupingBy { it }
                .eachCount()
                .filterValues { it >= 12 }
                .keys.firstOrNull() ?: continue
            return Transform(i, translation)
        }
        return null
    }

    fun Vec3.transform(t: Transform) = this.rotations()[t.rotationIndex] + t.trans

    fun String.toVec3() = split(",").map { it.toInt() }.let { (x, y, z) -> Vec3(x, y, z) }

    fun part12(input: List<String>): Pair<Int, Int> {
        val pcs = input.mapIndexedNotNull { i, it -> if (it.startsWith("---") || i == input.size - 1) i else null }
            .zipWithNext()
            .map { (from, to) -> input.subList(from + 1, to).filter { it.isNotBlank() }.map { it.toVec3() }.toSet() }

        val positions = mutableListOf(Transform(0, Vec3(0, 0, 0)))
        val target = pcs.first().toMutableSet()
        val unaligned = pcs.drop(1).toMutableList()
        while(unaligned.isNotEmpty()) {
            val rand = unaligned.random()
            val targetTransform = tryTransform(target, rand)
            if (targetTransform != null) {
                target += rand.map { it.transform(targetTransform) }
                positions.add(targetTransform)
                unaligned.remove(rand)
            } else { // try merge other clouds
                val rand2 = unaligned.random()
                val otherTransform = tryTransform(rand, rand2)
                if (otherTransform != null) {
                    unaligned.remove(rand)
                    unaligned.remove(rand2)
                    unaligned.add(rand + rand2.map { it.transform(otherTransform) })
                    unaligned.add(rand2 + rand.map { it.transform(tryTransform(rand2, rand)!!) })
                }
            }
        }

        val distance = positions.flatMap { p1 -> positions.map { p1.trans.manhattanDistance(it.trans) } }.maxOrNull()!!

        return Pair(target.size, distance)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    val testResult = part12(testInput)
    check(testResult.first == 79)
    check(testResult.second == 3621)

    val input = readInput("Day19")
    val result = part12(input)
    println(result.first)
    println(result.second)
}