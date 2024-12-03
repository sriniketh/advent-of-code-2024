import java.util.PriorityQueue
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val priorityQueue1 = PriorityQueue<Long>()
        val priorityQueue2 = PriorityQueue<Long>()
        input.map { line -> line.split(Regex("\\s+")) }
            .forEach { line ->
                priorityQueue1.add(line[0].toLong())
                priorityQueue2.add(line[1].toLong())
            }
        var totalDistance = 0L
        while (priorityQueue1.isNotEmpty() && priorityQueue2.isNotEmpty()) {
            totalDistance += abs(priorityQueue1.poll() - priorityQueue2.poll())
        }
        return totalDistance.toInt()
    }

    fun part2(input: List<String>): Int {
        val lists = input.map { line -> line.split(Regex("\\s+")) }
            .map { line -> line[0].toLong() to line[1].toLong() }
            .unzip()
        // return lists.first.sumOf { number -> lists.second.count { number == it } * number }.toInt()
        val frequencies = lists.second.groupingBy { it }.eachCount()
        return lists.first.sumOf { number -> frequencies.getOrDefault(number, 0) * number }.toInt()
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
