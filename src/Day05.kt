fun main() {

    fun part1(input: List<String>): Int {
        val dependenciesMap = buildDependenciesMap(input)
        val updates = getUpdates(input)

        val middlePageSum = updates.sumOf { update ->
            val pages = update.split(",")
            val visited = hashSetOf<String>()
            if (isUpdateValid(pages, dependenciesMap, visited)) {
                pages[(pages.size - 1) / 2].toInt()
            } else 0
        }
        return middlePageSum
    }

    fun part2(input: List<String>): Int {
        val dependenciesMap = buildDependenciesMap(input)
        val updates = getUpdates(input)

        val middlePageSum = updates.sumOf { update ->
            val pages = update.split(",")
            val visited = hashSetOf<String>()
            if (isUpdateValid(pages, dependenciesMap, visited)) {
                0
            } else {
                val correctedPages = correctPageOrder(pages, dependenciesMap)
                correctedPages[(correctedPages.size - 1) / 2].toInt()
            }
        }
        return middlePageSum
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private fun correctPageOrder(pages: List<String>, dependenciesMap: Map<String, List<String>>): List<String> {
    val adjacencyList = mutableMapOf<String, MutableList<String>>()
    pages.forEach { page ->
        val neighbors = adjacencyList.getOrDefault(page, mutableListOf())
        dependenciesMap[page]
            ?.filter { dependency -> dependency in pages }
            ?.forEach { dependency -> neighbors.add(dependency) }
        adjacencyList[page] = neighbors
    }
    val correctedPageOrder = mutableListOf<String>()
    val queue = ArrayDeque<String>().apply {
        adjacencyList
            .firstNotNullOfOrNull { (vertex, neighbors) -> if (neighbors.isEmpty()) vertex else null }
            ?.let { vertex -> addFirst(vertex) }
    }
    while (queue.isNotEmpty()) {
        queue.removeFirstOrNull()?.let { vertexWithNoInDegree ->

            adjacencyList.remove(vertexWithNoInDegree)
            correctedPageOrder.add(vertexWithNoInDegree)

            adjacencyList.forEach { (vertex, neighbors) ->
                neighbors.remove(vertexWithNoInDegree)
                adjacencyList[vertex] = neighbors
                if (neighbors.isEmpty()) {
                    queue.addFirst(vertex)
                }
            }
        }
    }
    return correctedPageOrder.reversed()
}

private fun isUpdateValid(
    pages: List<String>,
    dependenciesMap: Map<String, List<String>>,
    visited: HashSet<String>
): Boolean = pages.all { page -> dependenciesMap[page].orEmpty().none { it in visited } && visited.add(page) }

private fun getUpdates(input: List<String>): List<String> {
    val lineBreak = input.indexOf("")
    return input.slice(lineBreak + 1..input.size - 1)
}

private fun buildDependenciesMap(input: List<String>): Map<String, List<String>> {
    val lineBreak = input.indexOf("")
    return input.slice(0..lineBreak - 1)
        .map { string ->
            val pages = string.split("|")
            pages[0] to pages[1]
        }
        .groupBy({ it.first }, { it.second })
}
