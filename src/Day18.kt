import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { MyNode.parse(it) }
            .reduce { acc, node -> acc + node }
            .magnitude()
    }

    fun part2(input: List<String>): Int {
        val list = input.map { MyNode.parse(it) }

        var max = Int.MIN_VALUE
        for (i in list.indices) {
            for (j in list.indices) {
                if (i == j) continue
                max = max((list[i] + list[j]).magnitude(), max)
            }
        }
        return max
    }

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}

private sealed class MyNode {
    abstract fun getPrettyView(): String
    abstract fun addLeft(value: Int): MyNode
    abstract fun addRight(value: Int): MyNode
    abstract fun magnitude(): Int

    companion object {
        fun parse(input: String): MyNode {
            return parseOnePair(input)
        }

        private fun parseOnePair(node: String): MyNode {
            val middleCommaIndex = findMiddleComma(node) ?: return Leaf(node.toInt())
            val first = node.substring(0, middleCommaIndex).substringAfter('[')
            val second = node.substring(middleCommaIndex + 1).substringBeforeLast(']')
            return Branch(parseOnePair(first), parseOnePair(second))
        }

        private fun findMiddleComma(pair: String): Int? {
            var bracketCount = 0
            for (i in pair.indices) {
                when (pair[i]) {
                    '[' -> bracketCount++
                    ']' -> bracketCount--
                    ',' -> if (bracketCount == 1) return i else continue
                }
            }
            return null
        }
    }

    fun reduce(): MyNode {
        var exploded = this.explodeIfPossible(1)
        while (exploded.first != NoExplosion) {
            exploded = exploded.second.explodeIfPossible(1)
        }

        val splitted = exploded.second.splitIfPossible()
        var reduced = splitted.second
        if (splitted.first != NoSplit) {
            reduced = reduced.reduce()
        }

        return reduced
    }

    private fun explodeIfPossible(level: Int): Pair<ExplodeResult, MyNode> {
        return when (this) {
            is Leaf -> {
                Pair(NoExplosion, this)
            }
            is Branch -> {
                if (level > 4 && _1 is Leaf && _2 is Leaf) {
                    return Pair(Exploded(_1.v, _2.v), Leaf(0))
                }
                val (leftRes, left) = this._1.explodeIfPossible(level + 1)
                val (rightRes, right) = when (leftRes) {
                    NoExplosion -> this._2.explodeIfPossible(level + 1)
                    is Exploded -> {
                        return Pair(leftRes.copy(addRight = null), Branch(left, _2.addLeft(leftRes.addRight ?: 0)))
                    }
                }
                return when (rightRes) {
                    NoExplosion -> Pair(NoExplosion, this)
                    is Exploded -> {
                        Pair(rightRes.copy(addLeft = null), Branch(_1.addRight(rightRes.addLeft ?: 0), right))
                    }
                }
            }
        }
    }

    private fun splitIfPossible(): Pair<SplitResult, MyNode> {
        return when (this) {
            is Branch -> {
                val (leftRes, left) = _1.splitIfPossible()
                return when (leftRes) {
                    is NoSplit -> {
                        val (rightRes, right) = _2.splitIfPossible()
                        return when (rightRes) {
                            NoSplit -> Pair(NoSplit, this)
                            Split -> Pair(Split, Branch(_1, right))
                        }
                    }
                    is Split -> Pair(leftRes, Branch(left, _2))
                }
            }
            is Leaf -> {
                return if (this.v >= 10) {
                    Pair(Split, this.split())
                } else {
                    Pair(NoSplit, this)
                }
            }
        }
    }

    sealed class ExplodeResult
    object NoExplosion : ExplodeResult()
    data class Exploded(val addLeft: Int?, val addRight: Int?) : ExplodeResult()

    sealed class SplitResult
    object NoSplit : SplitResult()
    object Split : SplitResult()

    operator fun plus(other: MyNode): MyNode {
        return Branch(this, other).reduce()
    }

    data class Branch(val _1: MyNode, val _2: MyNode) : MyNode() {
        override fun getPrettyView(): String = "[" + _1.getPrettyView() + "," + _2.getPrettyView() + "]"
        override fun addLeft(value: Int): MyNode = this.copy(_1 = _1.addLeft(value))
        override fun addRight(value: Int): MyNode = this.copy(_2 = _2.addRight(value))
        override fun magnitude(): Int = 3 * _1.magnitude() + 2 * _2.magnitude()
    }

    data class Leaf(val v: Int) : MyNode() {
        override fun getPrettyView(): String = v.toString()
        override fun addLeft(value: Int): MyNode = Leaf(v + value)
        override fun addRight(value: Int): MyNode = Leaf(v + value)
        override fun magnitude(): Int = v

        fun split(): Branch = Branch(
            Leaf(floor(v.toDouble() / 2).toInt()),
            Leaf(ceil(v.toDouble() / 2).toInt())
        )
    }

}


