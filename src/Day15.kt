import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line -> line.toCharArray().map { it.digitToInt() } }
            .getLowestCost()
    }

    fun part2(input: List<String>): Int {
        return input.map { line -> line.toCharArray().map { it.digitToInt() } }
            .transformToField()
            .getLowestCost()
    }

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

private fun List<List<Int>>.transformToField(): List<List<Int>> {
    val column: List<List<Int>> = (0..4).flatMap { i -> this.increment2D(i) }
    return (0..4).map { i -> column.increment2D(i) }.reduce { acc, nextArr -> acc.merge(nextArr) }
}

private fun List<List<Int>>.merge(other: List<List<Int>>): List<List<Int>> {
    return this.mapIndexed { i, row -> row + other[i] }
}

private fun List<List<Int>>.getLowestCost(): Int {
    val pq = PriorityQueue<Triple<Int, Int, Int>> { a, b -> a.third - b.third }

    val riskArr: List<MutableList<Int>> =
        this.indices.map { this.first().indices.map { Int.MAX_VALUE }.toMutableList() }
    riskArr[0][0] = 0

    pq.add(Triple(0, 0, 0))
    while (!pq.isEmpty()) {
        val (i, j, dist) = pq.poll()
        listOf(i - 1 to j, i to j - 1, i + 1 to j, i to j + 1).map { (x, y) ->
            if (x in this.indices && y in this.first().indices && riskArr[x][y] > dist + this[x][y]) {
                riskArr[x][y] = dist + this[x][y]
                pq.add(Triple(x, y, riskArr[x][y]))
            }
        }
    }
    return riskArr.last().last()
}

private fun List<List<Int>>.increment2D(n: Int): List<List<Int>> =
    this.map { it.increment1D(n) }

private fun List<Int>.increment1D(n: Int): List<Int> = this.map { if (it + n > 9) (it + n) % 9 else it + n }
