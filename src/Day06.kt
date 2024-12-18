import Direction.Companion.toDirection

fun main() {

    fun part1(input: List<String>): Int {
        val map = input.map { string -> string.toCharArray() }
        map.asSequence().flatMapIndexed { rowIndex, row ->
            row.asSequence().mapIndexedNotNull { colIndex, char ->
                if (char.isGuard()) Pair(rowIndex, colIndex) else null
            }
        }.firstOrNull()?.let { (rowIndex, colIndex) ->
            predictMove(map, rowIndex, colIndex)
        }
        return map.sumOf { row -> row.count { char -> char == 'X' } }
    }

    fun part2(input: List<String>): Int {
        val map = input.map { string -> string.toCharArray() }
        var obstructionsPlaced = 0
        map.asSequence().flatMapIndexed { rowIndex, row ->
            row.asSequence().mapIndexedNotNull { colIndex, char ->
                if (char.isGuard()) Pair(rowIndex, colIndex) else null
            }
        }.firstOrNull()?.let { (guardX, guardY) ->
            for (i in 0..map.size - 1) {
                for (j in 0..map[0].size - 1) {
                    if (!map[i][j].isGuard() && !map[i][j].isObstacle()) {
                        map[i][j] = '#'
                        if (checkForLoopInGuardPath(map, guardX, guardY)) ++obstructionsPlaced
                        map[i][j] = '.'
                    }
                }
            }
        }
        return obstructionsPlaced
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private fun checkForLoopInGuardPath(map: List<CharArray>, guardX: Int, guardY: Int): Boolean {
    data class Obstacle(
        val pos: Pair<Int, Int>,
        val direction: Direction
    )

    var x = guardX
    var y = guardY
    var currentDirection = map[x][y].toDirection()
    val visited = hashSetOf<Obstacle>()
    while (isMoveWithinBounds(map, x, y)) {
        when (currentDirection) {
            Direction.NORTH -> {
                if (!isMoveWithinBounds(map, x - 1, y)) break
                else if (map[x - 1][y].isObstacle()) {
                    val obstacle = Obstacle(Pair(x - 1, y), currentDirection)
                    if (visited.contains(obstacle)) {
                        return true
                    } else {
                        visited.add(obstacle)
                    }
                    currentDirection = Direction.EAST
                } else x -= 1
            }

            Direction.EAST -> {
                if (!isMoveWithinBounds(map, x, y + 1)) break
                else if (map[x][y + 1].isObstacle()) {
                    val obstacle = Obstacle(Pair(x, y + 1), currentDirection)
                    if (visited.contains(obstacle)) {
                        return true
                    } else {
                        visited.add(obstacle)
                    }
                    currentDirection = Direction.SOUTH
                } else y += 1
            }

            Direction.SOUTH -> {
                if (!isMoveWithinBounds(map, x + 1, y)) break
                else if (map[x + 1][y].isObstacle()) {
                    val obstacle = Obstacle(Pair(x - +1, y), currentDirection)
                    if (visited.contains(obstacle)) {
                        return true
                    } else {
                        visited.add(obstacle)
                    }
                    currentDirection = Direction.WEST
                } else x += 1
            }

            Direction.WEST -> {
                if (!isMoveWithinBounds(map, x, y - 1)) break
                else if (map[x][y - 1].isObstacle()) {
                    val obstacle = Obstacle(Pair(x, y - 1), currentDirection)
                    if (visited.contains(obstacle)) {
                        return true
                    } else {
                        visited.add(obstacle)
                    }
                    currentDirection = Direction.NORTH
                } else y -= 1
            }
        }
    }
    return false
}

private fun predictMove(map: List<CharArray>, i: Int, j: Int) {
    var x = i
    var y = j
    var currentDirection = map[x][y].toDirection()
    while (isMoveWithinBounds(map, x, y)) {
        map[x][y] = 'X'
        when (currentDirection) {
            Direction.NORTH -> {
                if (!isMoveWithinBounds(map, x - 1, y)) break
                else if (map[x - 1][y].isObstacle()) currentDirection = Direction.EAST
                else x -= 1
            }

            Direction.EAST -> {
                if (!isMoveWithinBounds(map, x, y + 1)) break
                else if (map[x][y + 1].isObstacle()) currentDirection = Direction.SOUTH
                else y += 1
            }

            Direction.SOUTH -> {
                if (!isMoveWithinBounds(map, x + 1, y)) break
                else if (map[x + 1][y].isObstacle()) currentDirection = Direction.WEST
                else x += 1
            }

            Direction.WEST -> {
                if (!isMoveWithinBounds(map, x, y - 1)) break
                else if (map[x][y - 1].isObstacle()) currentDirection = Direction.NORTH
                else y -= 1
            }
        }
    }
}

private fun isMoveWithinBounds(map: List<CharArray>, x: Int, y: Int): Boolean {
    return x >= 0 && x < map.size && y >= 0 && y < map[x].size
}

private fun Char.isObstacle(): Boolean = this == '#'

private fun Char.isGuard(): Boolean = this in Direction.entries.map { it.value }

private enum class Direction(val value: Char) {
    NORTH('^'),
    SOUTH('v'),
    EAST('>'),
    WEST('<');

    companion object {
        fun Char.toDirection(): Direction = when (this) {
            '>' -> EAST
            '<' -> WEST
            '^' -> NORTH
            'v' -> SOUTH
            else -> throw IllegalStateException("invalid char value $this")
        }
    }
}
