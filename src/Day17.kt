fun main() {
    fun part1(input: List<String>): Int {
        val maxYSpeed = findAllPairs(input).maxOf { it.second }
        return ((maxYSpeed + 1) / 2) * maxYSpeed
    }

    fun part2(input: List<String>): Int {
        return findAllPairs(input).count()
    }

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

private fun findAllPairs(input: List<String>): List<Pair<Int, Int>> {
    val s = input.first().replace("target area: x=", "")
    val ranges = s.split(", y=")
    val minX = ranges.first().substringBefore("..").toInt()
    val maxX = ranges.first().substringAfter("..").toInt()

    val minY = ranges.last().substringBefore("..").toInt()
    val maxY = ranges.last().substringAfter("..").toInt()

    val res = mutableListOf<Pair<Int, Int>>()
    for (j in maxX downTo minY) {
        val steps = doesYScore(minY..maxY, j)
        if (steps.isNotEmpty()) {
            for (i in 1..maxX) {
                if (doesXScore(minX..maxX, i, steps)) {
                    res.add(Pair(i, j))
                }
            }
        }
    }
    return res
}

private fun doesXScore(r: IntRange, speed: Int, steps: List<Int>): Boolean {
    var x = 0
    var curSpeed = speed
    var step = 0
    while (x < r.last && curSpeed > 0) {
        x += curSpeed
        curSpeed -= 1
        step += 1
        if (x in r && steps.contains(step)) return true
    }
    if (curSpeed == 0) {
        return x in r && step <= steps.maxOrNull()!!
    }
    return false
}

private fun doesYScore(r: IntRange, speed: Int): List<Int> {
    var y = 0
    var curSpeed = speed
    var step = 0
    val res = mutableListOf<Int>()
    while (y > r.first) {
        y += curSpeed
        curSpeed -= 1
        step += 1
        if (y in r) res.add(step)
    }
    return res
}
