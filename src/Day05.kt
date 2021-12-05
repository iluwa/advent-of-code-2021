import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

typealias Coordinate = Pair<Int, Int>

fun main() {
    fun part1(lines: List<Line>, maxX: Int, maxY: Int): Int {
        val nonDiagonalLines = lines
            .filter { l -> l.start.first == l.end.first || l.start.second == l.end.second }

        val field = List(maxX + 1) { MutableList(maxY + 1) { 0 } }
        for (l in nonDiagonalLines) {
            l.coordinates().forEach {
                field[it.first][it.second] = field[it.first][it.second] + 1
            }
        }
        return field.flatten().count { it > 1 }
    }

    fun part2(lines: List<Line>, maxX: Int, maxY: Int): Int {
        val field = List(maxX + 1) { MutableList(maxY + 1) { 0 } }
        for (l in lines) {
            l.coordinates().forEach {
                field[it.first][it.second] = field[it.first][it.second] + 1
            }
        }
        return field.flatten().count { it > 1 }
    }


    val input = readInput("Day05")
    val lines = input.map { it.split(" -> ") }
        .map { l -> Line.of(parseCoordinate(l[0]), parseCoordinate(l[1])) }

    val maxX = lines.maxOf { max(it.start.first, it.end.first) }
    val maxY = lines.maxOf { max(it.start.second, it.end.second) }

    println(part1(lines, maxX, maxY))
    println(part2(lines, maxX, maxY))
}

fun parseCoordinate(s: String): Coordinate {
    return s.split(",")
        .map { it.toInt() }
        .zipWithNext()[0]
}

sealed class Line(open val start: Coordinate, open val end: Coordinate) {
    companion object {
        fun of(start: Coordinate, end: Coordinate): Line {
            return when {
                start.first == end.first -> Vertical(start, end)
                start.second == end.second -> Horizontal(start, end)
                else -> Diagonal(start, end)
            }
        }
    }

    abstract fun coordinates(): List<Coordinate>
}

class Horizontal(override val start: Coordinate, override val end: Coordinate) : Line(start, end) {
    override fun coordinates(): List<Coordinate> {
        return (min(start.first, end.first)..max(start.first, end.first))
            .map { Pair(it, start.second) }
    }
}

class Vertical(override val start: Coordinate, override val end: Coordinate) : Line(start, end) {
    override fun coordinates(): List<Coordinate> {
        return (min(start.second, end.second)..max(start.second, end.second))
            .map { Pair(start.first, it) }
    }
}

class Diagonal(override val start: Coordinate, override val end: Coordinate) : Line(start, end) {
    override fun coordinates(): List<Coordinate> {
        val fX: Int.(Int) -> Int = if (start.first > end.first) {
            Int::minus
        } else {
            Int::plus
        }
        val fY: Int.(Int) -> Int = if (start.second > end.second) {
            Int::minus
        } else {
            Int::plus
        }
        val dist = abs(start.first - end.first)
        return (0..dist).map { i -> Pair(start.first.fX(i), start.second.fY(i)) }
    }
}
