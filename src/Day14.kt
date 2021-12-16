import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: List<String>): Int {
        val template = input.first()
        val rules = input.drop(2).map { it.split(" -> ") }
            .associate { it.first() to it.last() }

        var state = template
        for (i in 1..10) {
            state = state.windowed(2)
                .map { rules[it]?.let { v -> "${it.first()}${v}${it.last()}" } ?: it }
                .reduce { acc, s -> acc + s.drop(1) }
        }

        val freq = state.groupingBy { it }.eachCount()
        val max = freq.values.maxOrNull()!!
        val min = freq.values.minOrNull()!!
        return max - min
    }

    fun part2(input: List<String>): Long {
        val template = input.first()
        val rules = input.drop(2).map { it.split(" -> ") }
            .associate { it.first() to it.last().toCharArray().first() }

        val maxStep = 40
        val cache = mutableMapOf<Pair<Int, String>, Map<Char, Long>>()

        fun String.process(step: Int): Map<Char, Long> = when {
            step == maxStep -> emptyMap()
            rules.containsKey(this) -> cache[Pair(step, this)] ?: run {
                mapOf(rules[this]!! to 1L).mergeSum(
                    "${this.first()}${rules[this]}".process(step + 1)
                ).mergeSum(
                    "${rules[this]}${this.last()}".process(step + 1)
                ).also { cache[Pair(step, this)] = it }
            }
            else -> emptyMap()
        }

        val freq = template.groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .mergeSum(
                template.windowed(2)
                    .map { it.process(0) }
                    .reduce { a, b -> a.mergeSum(b) }
            )

        val max = freq.values.maxOrNull()!!
        val min = freq.values.minOrNull()!!
        return max - min
    }

    val input = readInput("Day14")

    println(part1(input))
    println(part2(input))

}

private fun Map<Char, Long>.mergeSum(other: Map<Char, Long>) = this.toMutableMap().apply {
    other.forEach { (key, value) ->
        merge(key, value) { currentValue, addedValue ->
            currentValue + addedValue
        }
    }
}