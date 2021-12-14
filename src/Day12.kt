fun main() {
    fun part1(graph: Map<Node, List<Node>>): Int {
        val routes = graph.routes(Start, mapOf()) { cave, visited ->
            when (cave) {
                is Big -> false
                is Small -> visited.containsKey(cave)
            }
        }
        return routes.size
    }

    fun part2(graph: Map<Node, List<Node>>): Int {
        val routes = graph.routes(Start, mapOf()) { cave, visited ->
            when (cave) {
                is Big -> false
                is Small -> (visited[cave] ?: 0) > 1 ||
                        ((visited[cave] ?: 0) == 1 && visited.filter { it.key is Small }.values.any { it > 1 })
            }
        }
        return routes.size
    }

    val input = readInput("Day12")
    val graph: Map<Node, List<Node>> = input.map { it.split("-") }
        .map { it.sorted() }
        .flatMap { listOf(it.sorted(), it.sortedDescending()) }
        .map { it.map { s -> Node.of(s) } }
        .filter { it[1] != Start && it[0] != End }
        .groupBy({ it[0] }) { it[1] }
    println(part1(graph))
    println(part2(graph))
}

private fun Map<Node, List<Node>>.routes(
    node: Node,
    visited: Map<Cave, Int>,
    isVisited: (cave: Cave, Map<Cave, Int>) -> Boolean
): List<String> {
    return when (node) {
        is End -> return listOf(End.name)
        is Start -> return this[Start]!!.flatMap { routes(it, visited, isVisited) }.map { "${Start.name},$it" }
        is Cave -> {
            if (isVisited(node, visited)) return listOf()
            val visitedCopy = visited + Pair(node, (visited[node] ?: 0) + 1)
            return this[node]?.flatMap { routes(it, visitedCopy, isVisited) }?.map { "${node.name},$it" } ?: listOf()
        }
    }
}

sealed class Node(open val name: String) {
    companion object {
        fun of(s: String) = when (s) {
            "start" -> Start
            "end" -> End
            else -> Cave.of(s)
        }
    }
}

object Start : Node("start")
object End : Node("end")
sealed class Cave(override val name: String) : Node(name) {
    companion object {
        fun of(s: String) = when (s) {
            s.lowercase() -> Small(s)
            s.uppercase() -> Big(s)
            else -> throw Error("Wrong Cave")
        }
    }
}

data class Small(override val name: String) : Cave(name)
data class Big(override val name: String) : Cave(name)

