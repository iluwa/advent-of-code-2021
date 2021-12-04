fun main() {
    fun part1(boards: List<Board>, numberSeq: List<Int>): Int {
        var winnerPoints: Int? = null
        for (n in numberSeq) {
            winnerPoints = boards.mapNotNull { it.nextState(n) }.maxOrNull()
            if (winnerPoints != null) break
        }
        return winnerPoints!!
    }

    fun part2(boards: List<Board>, numberSeq: List<Int>): Int {
        val notWinningBoards = boards.toMutableList()
        val winners = mutableListOf<Int>()
        for (n in numberSeq) {
            notWinningBoards.removeAll { b ->
                when (val points = b.nextState(n)) {
                    null -> false
                    else -> {
                        winners.add(points)
                        true
                    }
                }
            }
        }
        return winners.last()
    }


    val input = readInput("Day04")
    val numberSeq = input[0].split(",").map { it.toInt() }

    println(part1(parseBoards(input), numberSeq))
    println(part2(parseBoards(input), numberSeq))
}

fun parseBoards(input: List<String>): List<Board> {
    val boards = mutableListOf<Board>()
    var i = 1
    while (i < input.size) {
        when {
            input[i].isEmpty() -> {
                i++
                continue
            }
            else -> {
                boards.add(Board.parse(input.subList(i, i + 5)))
                i += 5
                continue
            }
        }
    }
    return boards
}

class Board(private val board: List<MutableList<Pair<Int, Boolean>>>) {
    private val rowsState = mutableMapOf<Int, Int>()
    private val columnsState = mutableMapOf<Int, Int>()

    companion object {
        fun parse(l: List<String>): Board {
            val board = l.map { it.trim().split("""\s+""".toRegex()) }
                .map { it.map { s -> Pair(s.toInt(), false) } }
                .map { it.toMutableList() }
            return Board(board)
        }
    }

    fun nextState(number: Int): Int? {
        if (isWinner()) throw Error()
        extLoop@ for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].first == number) {
                    board[i][j] = Pair(board[i][j].first, true)
                    rowsState[i] = (rowsState[i] ?: 0) + 1
                    columnsState[j] = (columnsState[j] ?: 0) + 1
                    if (isWinner()) {
                        return getPoints(number)
                    }
                }
            }
        }
        return null
    }

    private fun isWinner(): Boolean = rowsState.any { (_, v) -> v == 5 } ||
            columnsState.any { (_, v) -> v == 5 }

    private fun getPoints(n: Int): Int = n * board.flatten()
        .filter { (_, crossed) -> !crossed }
        .map { it.first }
        .fold(0) { acc, a -> acc + a }
}
