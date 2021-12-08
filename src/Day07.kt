import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): String {
        return solve(input) { it }
    }

    fun part2(input: List<String>): String {
        return solve(input) { dist -> dist * (dist + 1) / 2 }
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun solve(input: List<String>, distanceCost: (Int) -> Int): String {
    val positions = input[0].split(",")
        .map { it.toInt() }

    val maxPosition = positions.maxOrNull()!!
    return (0..maxPosition).map { position ->
        positions.sumOf { crab ->
            distanceCost(abs(crab - position))
        }
    }.minOrNull().let { "Fuel=$it" }
}
