fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }
            .mapSlidingWindow(1) { a, b -> if (a.sum() < b.sum()) 1 else 0 }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }
            .mapSlidingWindow(3) { a, b -> if (a.sum() < b.sum()) 1 else 0 }
            .sum()
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

fun <T, U> List<T>.mapSlidingWindow(windowSize: Int, f: (List<T>, List<T>) -> U): List<U> {
    val res = mutableListOf<U>()
    for (i in 0 until this.size - windowSize) {
        res.add(f(this.subList(i, i + windowSize), this.subList(i + 1, i + 1 + windowSize)))
    }
    return res
}
