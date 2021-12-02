fun main() {
    fun part1(moves: List<Move>): Int {
        val (x, y, _) = moves.fold(Position(0, 0, 0)) { acc, a ->
            when (a) {
                is Down -> Position(acc.x, acc.y + a.distance, 0)
                is Forward -> Position(acc.x + a.distance, acc.y, 0)
                is Up -> Position(acc.x, acc.y - a.distance, 0)
            }
        }
        return x * y
    }

    fun part2(moves: List<Move>): Int {
        val (x, y, _) = moves.fold(Position(0, 0, 0)) { acc, a ->
            when (a) {
                is Down -> Position(acc.x, acc.y, acc.aim + a.distance)
                is Up -> Position(acc.x, acc.y, acc.aim - a.distance)
                is Forward -> Position(acc.x + a.distance, acc.y + (acc.aim * a.distance), acc.aim)
            }
        }
        return x * y
    }

    val input = readInput("Day02").map { Move.parse(it) }
    println(part1(input))
    println(part2(input))
}

sealed class Move {
    companion object Parser {
        fun parse(m: String): Move {
            val (direction, distance) = m.split(" ")[0] to m.split(" ")[1]
            return when (direction) {
                "forward" -> Forward(distance.toInt())
                "up" -> Up(distance.toInt())
                "down" -> Down(distance.toInt())
                else -> TODO()
            }
        }
    }
}

class Forward(val distance: Int) : Move()
class Up(val distance: Int) : Move()
class Down(val distance: Int) : Move()

data class Position(val x: Int, val y: Int, val aim: Int)