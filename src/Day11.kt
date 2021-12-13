fun main() {
    fun part1(input: List<List<Int>>): Int {
        var tmp = input
        var count = 0
        for (i in 1..100) {
            tmp = tmp.increment()
            while (tmp.readyToFlash(10)) {
                tmp = tmp.flash()
            }
            count += tmp.countNum(0)
        }
        return count
    }

    fun part2(input: List<List<Int>>): Int {
        var tmp = input
        var i = 0
        while (tmp.countNum(0) != 100) {
            tmp = tmp.increment()
            while (tmp.readyToFlash(10)) {
                tmp = tmp.flash()
            }
            i++
        }
        return i
    }

    val input = readInput("Day11")
    val field = input.map { it.toCharArray() }
        .map { line -> line.map { Character.getNumericValue(it) } }
    println(part1(field))
    println(part2(field))
}

private fun Point.getSurroundingPoints(arr: List<List<Int>>): List<Point> = listOf(
    Point(x - 1, y - 1),
    Point(x - 1, y),
    Point(x - 1, y + 1),
    Point(x, y - 1),
    Point(x, y + 1),
    Point(x + 1, y - 1),
    Point(x + 1, y),
    Point(x + 1, y + 1)
).filter { it.x in arr.indices && it.y in arr.first().indices }

private data class Point(val x: Int, val y: Int)

private fun List<List<Int>>.increment(): List<List<Int>> = this.map { row -> row.map { it + 1 } }

private fun List<List<Int>>.increment(points: List<Point>): List<List<Int>> = this.increment(points, 1)

private fun List<List<Int>>.increment(point: Point, value: Int): List<List<Int>> = this.increment(listOf(point), value)

private fun List<List<Int>>.increment(points: List<Point>, value: Int): List<List<Int>> {
    return this.mapIndexed { i, row ->
        row.mapIndexed { j, n -> if (points.contains(Point(i, j))) n + value else n }
    }
}

private fun List<List<Int>>.readyToFlash(n: Int): Boolean = this.any { row -> row.any { it >= n } }

private fun List<List<Int>>.countNum(n: Int): Int = this.map { row -> row.filter { it == n } }.sumOf { it.size }

private fun List<List<Int>>.flash(): List<List<Int>> {
    var flashed = this
    for (i in flashed.indices) {
        for (j in flashed.first().indices) {
            if (flashed[i][j] >= 10) {
                flashed = flashed.increment(
                    Point(i, j).getSurroundingPoints(flashed)
                        .filter { flashed[it.x][it.y] != 0 }
                )
                flashed = flashed.increment(Point(i, j), -flashed[i][j])
            }
        }
    }
    return flashed
}
