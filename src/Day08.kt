fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.split("|") }
            .map { it[1] }
            .flatMap { it.split(" ") }
            .filter { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
            .size
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val l = line.split("| ")
                .joinToString("")
                .split(" ")
                .map { it.sort() }
                .toList()

            val numbers = guessNumbers(l)
            line.split("| ")[1]
                .split(" ")
                .map { it.sort() }
                .map { numbers[it] }
                .joinToString("")
                .toInt()
        }
    }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun String.containsCharsFromString(s: String): Boolean {
    val set = this.toSet()
    return s.all { set.contains(it) }
}

private fun String.sort(): String = this.toSortedSet().joinToString("")

private fun guessNumbers(words: List<String>): Map<String, Int> {
    val uniq = words.toSet()

    val one = uniq.single { it.length == 2 }
    val four = uniq.single { it.length == 4 }
    val seven = uniq.single { it.length == 3 }
    val eight = uniq.single { it.length == 7 }

    val twoThreeFive = uniq.filter { it.length == 5 }
    val zeroSixNine = uniq.filter { it.length == 6 }
    val nine = zeroSixNine.single { it.containsCharsFromString(four) }
    val zero = zeroSixNine.filter { it != nine }.single { it.containsCharsFromString(seven) }
    val six = zeroSixNine.single { it != nine && it != zero }

    val three = twoThreeFive.single { it.containsCharsFromString(seven) }
    val five = twoThreeFive.filter { it != three }
        .single { nine.containsCharsFromString(it) }
    val two = twoThreeFive.single { it != three && it != five }

    return mapOf(
        zero to 0,
        one to 1,
        two to 2,
        three to 3,
        four to 4,
        five to 5,
        six to 6,
        seven to 7,
        eight to 8,
        nine to 9
    )
}
