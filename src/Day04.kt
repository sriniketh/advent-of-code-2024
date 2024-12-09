fun main() {

    fun part1(input: List<String>): Int {
        var count = 0
        for (i in 0..input.size - 1) {
            for (j in 0..input[i].length - 1) {
                if (input[i][j] == 'X') {
                    count += countOccurrencesOfXMAS(input, i, j)
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (i in 0..input.size - 1) {
            for (j in 0..input[i].length - 1) {
                if (input[i][j] == 'A' && countOccurrencesOfMAS(input, i, j)) {
                    ++count
                }
            }
        }
        return count
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private fun countOccurrencesOfMAS(puzzle: List<String>, x: Int, y: Int): Boolean {
    var leftDiagonal = false
    if (isValidMove(puzzle, x - 1, y - 1) && isValidMove(puzzle, x + 1, y + 1)) {
        if (puzzle[x - 1][y - 1] == 'M' && puzzle[x + 1][y + 1] == 'S') {
            leftDiagonal = true
        } else if (puzzle[x - 1][y - 1] == 'S' && puzzle[x + 1][y + 1] == 'M') {
            leftDiagonal = true
        }
    }
    var rightDiagonal = false
    if (isValidMove(puzzle, x - 1, y + 1) && isValidMove(puzzle, x + 1, y - 1)) {
        if (puzzle[x - 1][y + 1] == 'M' && puzzle[x + 1][y - 1] == 'S') {
            rightDiagonal = true
        } else if (puzzle[x - 1][y + 1] == 'S' && puzzle[x + 1][y - 1] == 'M') {
            rightDiagonal = true
        }
    }
    return leftDiagonal && rightDiagonal
}

private fun countOccurrencesOfXMAS(puzzle: List<String>, x: Int, y: Int): Int {
    var count = 0
    SearchDirection.entries.forEach { direction ->
        if (checkOccurrenceOfXMAS(puzzle, x, y, direction)) ++count
    }
    return count
}

private fun checkOccurrenceOfXMAS(puzzle: List<String>, x: Int, y: Int, direction: SearchDirection): Boolean {
    val word = "XMAS"
    var i = x
    var j = y
    var index = 0
    while (isValidMove(puzzle, i, j) && index < word.length) {
        if (word[index] != puzzle[i][j]) break
        if (index == word.length - 1) return true

        ++index
        i = i + direction.xDiff
        j = j + direction.yDiff
    }
    return false
}

private fun isValidMove(puzzle: List<String>, x: Int, y: Int): Boolean {
    return x >= 0 && x < puzzle.size && y >= 0 && y < puzzle[x].length
}

private enum class SearchDirection(val xDiff: Int, val yDiff: Int) {
    NW(-1, -1),
    N(-1, 0),
    NE(-1, 1),
    E(0, 1),
    SE(1, 1),
    S(1, 0),
    SW(1, -1),
    W(0, -1)
}
