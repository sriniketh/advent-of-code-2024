import kotlin.text.replace

fun main() {

    fun part1(input: String): Int {
        return calculateSum(input)
    }

    fun part2(input: String): Int {
        val regexToDropDontAndDo = """don't\(\).*?do\(\)""".toRegex()
        val lineWithDontAndDoRemoved = input.replace(regexToDropDontAndDo, "")

        val regexToDropDont = """don't\(\).*""".toRegex()
        val lineWithDontRemoved = lineWithDontAndDoRemoved.replace(regexToDropDont, "")

        return calculateSum(lineWithDontRemoved)
    }

    val input = readInputAsString("Day03")
    val inputRemovingLineBreaks = input.replace("""[\n\r\t]""".toRegex(), "")
    part1(inputRemovingLineBreaks).println()
    part2(inputRemovingLineBreaks).println()
}

private fun calculateSum(validLine: String): Int {
    var sum = 0
    val regex = """mul\([0-9]{1,3},[0-9]{1,3}\)""".toRegex()
    regex.findAll(validLine)
        .map { it.value }
        .forEach { match ->
            val numbersSubstring = match.substringAfter('(').substringBefore(')')
            val numbers = numbersSubstring.split(',').map { it.toInt() }
            numbers.reduce { acc, num -> acc * num }.also { sum += it }
        }
    return sum
}
