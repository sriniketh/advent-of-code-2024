fun main() {

    fun part1(input: List<String>): Long {
        return input.map { processInput(it) }.sumOf { (result, numbers) ->
            if (numbers.canEvaluateTo(result, ::canEvaluateToResultForPart1)) {
                result
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Long {
        return input.map { processInput(it) }.sumOf { (result, numbers) ->
            if (numbers.canEvaluateTo(result, ::canEvaluateToResultForPart2)) {
                result
            } else {
                0
            }
        }
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private fun processInput(string: String): Pair<Long, List<Long>> {
    val parts = string.split(""":""".toRegex())
    return Pair(parts[0].trim().toLong(), parts[1].trim().split("""\s""".toRegex()).map { it.toLong() })
}

private fun List<Long>.canEvaluateTo(
    result: Long,
    canEvaluateResult: (MutableList<Long>, Long, Long) -> Boolean
): Boolean {
    if (this.isEmpty()) return result == 0L
    val numbers = this.toMutableList()
    val candidate = numbers.removeAt(0)
    return canEvaluateResult(numbers, result, candidate)
}

private fun canEvaluateToResultForPart1(numbers: MutableList<Long>, result: Long, resultSoFar: Long): Boolean {
    if (numbers.isEmpty()) {
        return result == resultSoFar
    }
    val candidate = numbers.removeAt(0)
    return canEvaluateToResultForPart1(numbers.toMutableList(), result, candidate + resultSoFar) or
            canEvaluateToResultForPart1(numbers, result, candidate * resultSoFar)
}

private fun canEvaluateToResultForPart2(numbers: MutableList<Long>, result: Long, resultSoFar: Long): Boolean {
    if (numbers.isEmpty()) {
        return result == resultSoFar
    }
    val candidate = numbers.removeAt(0)
    return canEvaluateToResultForPart2(numbers.toMutableList(), result, candidate + resultSoFar) or
            canEvaluateToResultForPart2(numbers.toMutableList(), result, candidate * resultSoFar) or
            canEvaluateToResultForPart2(numbers, result, "$resultSoFar$candidate".toLong())
}
