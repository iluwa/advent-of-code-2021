fun main() {
    fun part1(initial: List<Int>): Long {
        return initial.sumOf { children(it, 80) } + initial.size
    }

    fun part2(initial: List<Int>): Long {
        return initial.sumOf { children(it, 256) } + initial.size
    }

    val input = readInput("Day06")
    val initial = input.first().split(",").map { it.toInt() }

    println(part1(initial))
    println(part2(initial))
}

val cache = hashMapOf<Pair<Int, Int>, Long>()

fun children(initialState: Int, daysOfLiving: Int): Long {
    if (daysOfLiving < initialState + 1) return 0
    return cache[Pair(initialState, daysOfLiving)] ?: run {
        var sum: Long = ((daysOfLiving - initialState - 1) / 7 + if (initialState < daysOfLiving) 1 else 0).toLong()
        for (i in (initialState)..daysOfLiving step 7) {
            sum += children(9, daysOfLiving - i)
        }
        cache[Pair(initialState, daysOfLiving)] = sum
        return sum
    }

}
