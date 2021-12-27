import kotlin.math.max
import kotlin.math.min

fun main() {

    class DeterministicDice(var count: Int = 0, var rolls: Int = 0) {
        fun roll() = generateSequence {
            if (count == 100) count = 0
            rolls++
            ++count
        }
    }

    class Player(val position: Int, val score: Int = 0) {
        fun move(sum: Int): Player {
            val next = (position + sum - 1) % 10 + 1
            return Player(next, score + next)
        }
    }

    data class Wins(val player1: Long, val player2: Long) {
        operator fun plus(other: Wins) = Wins(player1 + other.player1, player2 + other.player2)
        operator fun times(other: Long) = Wins(player1 * other, player2 * other)
    }

    class Game(val player1: Player, val player2: Player, val player1Turn: Boolean = true) {
        fun play(sum: Int): Game {
            return when (player1Turn) {
                true -> Game(player1.move(sum), player2, false)
                false -> Game(player1, player2.move(sum), true)
            }
        }

        fun hasWinner(score: Int = 1000) = player1.score >= score || player2.score >= score
    }

    val sumFrequency = mapOf(3 to 1L, 4 to 3L, 5 to 6L, 6 to 7L, 7 to 6L, 8 to 3L, 9 to 1L)
    val cache = mutableMapOf<Game, Wins>()

    fun playQuantum(game: Game): Wins {
        if (game.hasWinner(21))
            return if (game.player1.score > game.player2.score) Wins(1, 0) else Wins(0, 1)
        if (game in cache) return cache[game]!!

        return sumFrequency.map { (sum, freq) -> playQuantum(game.play(sum)) * freq }
            .reduce { acc, wins -> acc + wins }
            .also { cache[game] = it }
    }

    fun part1(input: List<String>): Int {
        val p1 = Player(input[0].last().digitToInt())
        val p2 = Player(input[1].last().digitToInt())

        val dice = DeterministicDice()
        var game = Game(p1, p2)
        while(!game.hasWinner()) {
            game = game.play(dice.roll().take(3).sum())
        }

        return min(game.player1.score, game.player2.score) * dice.rolls
    }

    fun part2(input: List<String>): Long {
        val p1 = Player(input[0].last().digitToInt())
        val p2 = Player(input[1].last().digitToInt())

        val game = Game(p1, p2)
        val result = playQuantum(game)
        return max(result.player1, result.player2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}