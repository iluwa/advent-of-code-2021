private val brackets = mapOf(
    '(' to ')',
    '{' to '}',
    '[' to ']',
    '<' to '>'
)

fun main() {
    fun part1(input: List<String>): Int {
        val points = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
        return input.map { it.validateBrackets() }
            .filterIsInstance<IllegalBracket>()
            .mapNotNull { points[it.illegalBracket] }
            .sum()
    }

    fun part2(input: List<String>): Long {
        fun points(l: List<Char>): Long = l.map {
            when (it) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> throw Error("Unknown bracket")
            }
        }.fold(0L) { acc, a -> acc * 5 + a }

        return input.map { it.validateBrackets() }
            .filterIsInstance<NotEnoughBrackets>()
            .map { points(it.missingBrackets) }
            .sorted()
            .middle()
    }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun <T> List<T>.middle(): T = this[this.size / 2]

private fun String.validateBrackets(): ValidationResult {
    val q = ArrayDeque<Char>()
    for (c in this) {
        if (brackets.containsKey(c)) {
            q.add(c)
        } else {
            if (brackets[q.removeLast()] != c) return IllegalBracket(c)
        }
    }
    return when {
        q.isEmpty() -> Success
        else -> NotEnoughBrackets(q.map { brackets[it]!! }.reversed())
    }
}

sealed class ValidationResult
data class IllegalBracket(val illegalBracket: Char) : ValidationResult()
data class NotEnoughBrackets(val missingBrackets: List<Char>) : ValidationResult()
object Success : ValidationResult()
