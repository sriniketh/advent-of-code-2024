fun main() {

    fun part1(input: List<String>): Int {
        var safeReports = 0
        input.forEach { report ->
            val levels = levels(report)
            if (isReportSafe(levels)) ++safeReports
        }
        return safeReports
    }

    fun part2(input: List<String>): Int {
        var safeReports = 0
        input.forEach { report ->
            val levels = levels(report)
            for (i in 0..levels.size - 1) {
                val levelsWithOneLevelRemoved = levels.toMutableList().apply { removeAt(i) }
                if (isReportSafe(levelsWithOneLevelRemoved)) {
                    ++safeReports
                    break
                }
            }
        }
        return safeReports
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun isReportSafe(levels: List<Long>): Boolean {
    val diffs = levels.takeIf { it.isNotEmpty() }
        ?.zipWithNext()
        ?.map { (first, second) -> first - second }
        .orEmpty()
    val isDescending = diffs.all { diff -> diff in 1..3 }
    return isDescending
}

private fun levels(report: String): List<Long> {
    val rawLevels = report.split("""\s""".toRegex()).map { it.toLong() }
    val levels = if (rawLevels[0] > rawLevels[rawLevels.size - 1]) rawLevels else rawLevels.reversed()
    return levels
}
