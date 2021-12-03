fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { it.toCharArray() }
            .map { it.map { c -> Character.getNumericValue(c) } }
            .reduce { acc, a -> acc.addByIndex(a) }
            .map { if (it > input.size / 2) 1 else 0 }
            .joinToString(separator = "")
            .let { it.toInt(2) * swapZerosAndOnes(it).toInt(2) }
    }

    fun part2(input: List<String>): Int {
        return calculateRating(input, Strategy.MOST_COMMON).toInt(2) *
                calculateRating(input, Strategy.LEAST_COMMON).toInt(2)
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

enum class Strategy {
    MOST_COMMON, LEAST_COMMON
}

fun calculateRating(l: List<String>, strategy: Strategy): String {
    var res = l
    for (i in 0 until l.first().length) {
        val onesPercent = res.size.toDouble() / charSum(res, i)
        val filter = when (strategy) {
            Strategy.MOST_COMMON -> if (onesPercent <= 2) 1 else 0
            Strategy.LEAST_COMMON -> if (onesPercent <= 2) 0 else 1
        }
        res = res.filter { it[i] == filter.digitToChar() }
        if (res.size == 1) break
    }
    if (res.size != 1) {
        throw Error("Not found")
    }
    return res[0]
}

fun charSum(l: List<String>, i: Int): Int {
    var acc = 0
    for (j in l.indices) {
        acc += Character.getNumericValue(l[j][i])
    }
    return acc
}

fun List<Int>.addByIndex(l: List<Int>): List<Int> = this.mapIndexed { i, v -> l[i] + v }

fun swapZerosAndOnes(s: String): String {
    return s.map { if (it == '0') '1' else '0' }.joinToString(separator = "")
}