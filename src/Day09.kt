typealias Point2D = Pair<Int, Int>

fun main() {
    fun part1(heightmap: List<List<Int>>): Int {
        return heightmap.getMinPoints()
            .map { heightmap[it.first][it.second] }
            .sumOf { it + 1 }
    }

    fun part2(heightmap: List<List<Int>>): Int {
        return heightmap.getMinPoints().asSequence()
            .map { basin(heightmap, it, mutableSetOf()) }
            .sortedByDescending { it.size }
            .take(3)
            .map { it.size }
            .reduce { a, b -> a * b }
    }

    val input = readInput("Day09")
    val heightmap: List<List<Int>> = input.map { it.toList().map { c -> c.digitToInt() } }
    println(part1(heightmap))
    println(part2(heightmap))
}

private fun List<List<Int>>.getMinPoints(): List<Point2D> {
    val res = mutableListOf<Point2D>()
    for (i in this.indices) {
        for (j in this[i].indices) {
            if (Point2D(i, j).getAdjacentPoints(this, mutableSetOf())
                    .none { this[it.first][it.second] <= this[i][j] }
            ) {
                res.add(Point2D(i, j))
            }
        }
    }
    return res.toList()
}

private fun getFromBoundary(n: Int, r: IntRange): Int? = when {
    n < r.first -> null
    n > r.last -> null
    else -> n
}

private fun Point2D.getAdjacentPoints(
    arr: List<List<Int>>,
    visited: Set<Point2D>
): List<Point2D> {
    val top = getFromBoundary(this.first - 1, arr.indices)?.let { Point2D(it, this.second) }
    val bottom = getFromBoundary(this.first + 1, arr.indices)?.let { Point2D(it, this.second) }
    val left = getFromBoundary(this.second - 1, arr[this.first].indices)?.let { Point2D(this.first, it) }
    val right = getFromBoundary(this.second + 1, arr[this.first].indices)?.let { Point2D(this.first, it) }
    return listOfNotNull(top, bottom, left, right).filter { !visited.contains(it) }
}


private fun basin(arr: List<List<Int>>, point: Point2D, visited: MutableSet<Point2D>): Set<Point2D> {
    val adjacentPoints = point.getAdjacentPoints(arr, visited)
    for (p in adjacentPoints) {
        if (arr[p.first][p.second] != 9) {
            visited.add(p)
            visited.addAll(basin(arr, p, visited))
        }
    }
    return visited
}
