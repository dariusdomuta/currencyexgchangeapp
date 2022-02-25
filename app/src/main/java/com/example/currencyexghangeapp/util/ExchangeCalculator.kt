package com.example.currencyexghangeapp.util

import com.example.currencyexghangeapp.repository.model.Exchange
import java.util.*

/**
 * Receives an exchange object and maps it into a Graph structure. To compute the combined exchange rates
 * it uses Dijkstra's algorithm for finding the shortest path in a graph, between two nodes. As the path
 * is computed, the rates are multiplied towards the final value.
 */
class ExchangeCalculator(val exchange: Exchange) {
    private val graph = Graph()

    init {
        initializeGraph()
    }

    fun getExchangeRateBetween(from: String, to: String): Float {
        val computedGraph = calculateShortestPathFromSource(graph, graph.getNodes().first { it.name == from})
        val rate = computedGraph?.getNodes()?.first { it.name == to }?.distanceRatePair?.rate ?: 0f
        return rate
    }

    private fun initializeGraph() {
        val nodeNamesList = mutableListOf<String>()
        exchange.rates.forEach { rate ->
            if (!nodeNamesList.contains(rate.to.name)) {
                nodeNamesList.add(rate.to.name)
            }
            if (!nodeNamesList.contains(rate.from.name)) {
                nodeNamesList.add(rate.from.name)
            }
        }
        val nodes = nodeNamesList.map { Node(it) }

        exchange.rates.forEach { exchange ->
            val from = exchange.from.name
            val to = exchange.to.name

            val sourceNode = nodes.firstOrNull { it.name == from }
            sourceNode?.addDestination(
                nodes.first { it.name == to },
                DistanceRatePair(1, exchange.rate)
            )
        }
        nodes.forEach {
            graph.addNode(it)
        }
    }

    private fun calculateShortestPathFromSource(graph: Graph?, source: Node): Graph? {
        source.setDistance(0)
        val settledNodes: MutableSet<Node> = HashSet()
        val unsettledNodes: MutableSet<Node> = HashSet()
        unsettledNodes.add(source)
        while (unsettledNodes.size != 0) {
            val currentNode = getLowestDistanceNode(unsettledNodes.toSet())
            if (currentNode != null) {
                unsettledNodes.remove(currentNode)
                for ((adjacentNode, edgeWeight) in currentNode!!.adjacentNodes) {
                    if (!settledNodes.contains(adjacentNode)) {
                        calculateMinimumDistance(adjacentNode, edgeWeight, currentNode)
                        unsettledNodes.add(adjacentNode)
                    }
                }
                settledNodes.add(currentNode)
            }
        }
        return graph
    }

    private fun calculateMinimumDistance(
        evaluationNode: Node,
        edgeWeigh: DistanceRatePair, sourceNode: Node
    ) {
        val sourceDistance: Int = sourceNode.getDistance()
        if (sourceDistance + edgeWeigh.distance < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh.distance)
            evaluationNode.setRate(sourceNode.distanceRatePair.rate * edgeWeigh.rate)
            val shortestPath = LinkedList(sourceNode.shortestPath)
            shortestPath.add(sourceNode)
            evaluationNode.shortestPath = shortestPath
        }
    }

    private fun getLowestDistanceNode(unsettledNodes: Set<Node>): Node? {
        var lowestDistanceNode: Node? = null
        var lowestDistance = Int.MAX_VALUE
        for (node in unsettledNodes) {
            val nodeDistance = node.getDistance()
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance
                lowestDistanceNode = node
            }
        }
        return lowestDistanceNode
    }
}

class Graph {
    private var nodes: MutableSet<Node> = HashSet()

    fun addNode(node: Node) {
        nodes.add(node)
    }

    fun getNodes() = nodes.toSet()
}

class Node(
    var name: String,
    var shortestPath: List<Node> = LinkedList<Node>(),
    var distanceRatePair: DistanceRatePair = DistanceRatePair(Integer.MAX_VALUE, 1.0f),
    var adjacentNodes: MutableMap<Node, DistanceRatePair> = HashMap()
) {
    fun addDestination(destination: Node, distanceRatePair: DistanceRatePair) {
        adjacentNodes[destination] = distanceRatePair
    }

    fun getDistance() = distanceRatePair.distance

    fun setDistance(value: Int) {
        distanceRatePair.distance = value
    }

    fun setRate(value: Float) {
        distanceRatePair.rate = value
    }
}

class DistanceRatePair(var distance: Int, var rate: Float)
