fun main() {
    fun part1(input: String): Int {
        val parsed = Packet.parse(input)

        fun recSum(p: Packet): Int = when(p) {
            is Literal -> p.v.toInt(2)
            is Op0 -> p.v.toInt(2) + p.subPackets.sumOf { recSum(it) }
            is Op1 -> p.v.toInt(2) + p.subPackets.sumOf { recSum(it) }
        }

        return recSum(parsed)
    }

    fun part2(input: String): Long = Packet.parse(input).value()

    val input = readInput("Day16").first()
        .map {
            it.digitToInt(16)
                .toString(2)
                .padStart(4, '0')
        }
        .joinToString("")
    println(part1(input))
    println(part2(input))
}

private sealed class Packet {
    abstract fun length(): Int
    abstract fun value(): Long

    companion object {
        fun parse(s: String): Packet {
            val v = s.take(3)
            val packet: Packet = when (val type = s.substring(3, 6)) {
                "100" -> {
                    var ind = 0
                    val data = s.substring(6)

                    if (data.isEmpty()) return Literal(v, type, "")

                    while (data[ind * 5] == '1') {
                        ind += 1
                    }
                    Literal(v, type, s.substring(6, 6 + (ind + 1) * 5))
                }
                else -> {
                    when (val opType = s.substring(6, 7)) {
                        "0" -> {
                            val length = s.substring(7, 22)
                            var accL = 0
                            val packets = mutableListOf<Packet>()
                            while (accL < length.toInt(2)) {
                                parse(s.substring(22 + accL)).let {
                                    packets.add(it)
                                    accL += it.length()
                                }
                            }
                            Op0(v, type, opType, length, packets)
                        }
                        "1" -> {
                            val packetsNum = s.substring(7, 18)
                            var accN = 0
                            var accL = 0
                            val packets = mutableListOf<Packet>()
                            while (accN < packetsNum.toInt(2)) {
                                parse(s.substring(18 + accL)).let {
                                    packets.add(it)
                                    accN += 1
                                    accL += it.length()
                                }
                            }
                            Op0(v, type, opType, packetsNum, packets)
                        }
                        else -> throw Error("Unknown op type")
                    }
                }
            }
            return packet
        }
    }
}

private data class Literal(val v: String, val type: String, val payload: String) : Packet() {
    override fun length(): Int = v.length + type.length + payload.length
    override fun value(): Long = payload.chunked(5)
            .joinToString("") { it.substring(1) }
            .toLong(2)
}

private sealed class Operator : Packet()
private data class Op0(val v: String, val id: String, val opType: String, val subPacketsCount: String, val subPackets: List<Packet>) : Operator() {
    override fun length(): Int = v.length + id.length + opType.length + subPacketsCount.length + subPackets.sumOf { it.length() }
    override fun value(): Long = idToReducer[id.toInt(2)]!!(subPackets)
}

private data class Op1(val v: String, val id: String, val opType: String, val subPacketsLength: String, val subPackets: List<Packet>) : Operator() {
    override fun length(): Int = v.length + id.length + opType.length + subPacketsLength.length + subPackets.sumOf { it.length() }
    override fun value(): Long = idToReducer[id.toInt(2)]!!(subPackets)
}

private val idToReducer = mapOf<Int, (List<Packet>) -> Long>(
    0 to { l -> l.sumOf { it.value() } },
    1 to { l -> l.fold(1) { acc, v -> acc * v.value() } },
    2 to { l -> l.minOf { it.value() } },
    3 to { l -> l.maxOf { it.value() } },
    5 to { l -> if (l.first().value() > l.last().value()) 1 else 0 },
    6 to { l -> if (l.first().value() < l.last().value()) 1 else 0 },
    7 to { l -> if (l.first().value() == l.last().value()) 1 else 0 }
)
