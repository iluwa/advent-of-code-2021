fun main() {
    fun part1(commands: List<Command>, dots: List<Pair<Int, Int>>): Int {
        val max = when(commands.first()) {
            is FoldX -> dots.maxOf { it.first }
            is FoldY -> dots.maxOf { it.second }
        }
        return commands.first().apply(dots, max).size
    }

    fun part2(commands: List<Command>, dots: List<Pair<Int, Int>>): Int {
        var maxX = dots.maxOf { it.first }
        var maxY = dots.maxOf { it.second }

        var res = dots
        for (command in commands) {
            when (command) {
                is FoldX -> {
                    res = command.apply(res, maxX)
                    maxX = (maxX - 1) / 2
                }
                is FoldY -> {
                    res = command.apply(res, maxY)
                    maxY = (maxY - 1) / 2
                }
            }
        }

        res.prettyPrint()
        return res.size
    }

    val input = readInput("Day13")
    val (commands, field) = input.filter { it.isNotBlank() }
        .partition { it.matches("""(fold along).+""".toRegex()) }

    val dots = field.map { it.split(",") }
        .map { it.map { n -> n.toInt() } }
        .map { it.first() to it.last() }

    val parsedCommands = commands.map { Command.of(it) }
    println(part1(parsedCommands, dots))
    println(part2(parsedCommands, dots))
}

private fun List<Pair<Int, Int>>.prettyPrint() {
    val maxY = this.maxOf { it.second }
    val maxX = this.maxOf { it.first }
    for (y in 0..maxY) {
        for (x in 0..maxX) {
            print(if (this.any { it.first == x && it.second == y }) "#" else " ")
        }
        println()
    }
}

private sealed class Command(open val n: Int) {
    companion object {
        fun of(s: String) = when {
            s.matches("""(fold along x).+""".toRegex()) -> FoldX(s.replace("fold along x=", "").toInt())
            s.matches("""(fold along y).+""".toRegex()) -> FoldY(s.replace("fold along y=", "").toInt())
            else -> throw Error("An unknown command")
        }
    }

    fun apply(dots: List<Pair<Int, Int>>, max: Int): List<Pair<Int, Int>> = when (this) {
        is FoldY -> dots.map { if (it.second > this.n) it.copy(second = max - it.second) else it }.distinct()
        is FoldX -> dots.map { if (it.first > this.n) it.copy(first = max - it.first) else it }.distinct()
    }
}

private class FoldX(override val n: Int) : Command(n)
private class FoldY(override val n: Int) : Command(n)
