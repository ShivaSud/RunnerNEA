package Services;

import DataStructures.Graph;
import DataStructures.HashTable;
import DataStructures.PriorityList;

import java.util.ArrayList;

public class DijkstrasAlgorithm<E> {
    private final Graph<E> graph;

    // Constructor to initialise the Dijkstra's Algorithm with the graph.
    public DijkstrasAlgorithm(Graph<E> graph) {
        this.graph = graph;
    }

    // Method to find the shortest path from startVertex to endVertex.
    public String path(E startVertex, E endVertex) {
        PriorityList<E> list = new PriorityList<>(); // List to store the vertices with their current distances.
        HashTable<E, Double> distances = new HashTable<>(); // Stores distances from startVertex to each vertex.
        ArrayList<E> nodes = graph.getNodes(); // List of all nodes in the graph.
        HashTable<E, String> paths = new HashTable<>(); // Stores the paths from startVertex to each vertex.

        // Initialise distances and paths
        distances.add(startVertex, 0.0);
        list.add(startVertex, 0);
        for (E node : nodes) {
            if (node != startVertex) {
                distances.add(node, Double.MAX_VALUE); // Set distance to maximum for non-start nodes.
                list.add(node, Integer.MAX_VALUE);
                paths.add(node, ""); // Empty path initially.
            }
        }
        paths.add(startVertex, startVertex.toString()); // Start node path is just the node itself.

        // Main loop to process each node
        while (list.asArray().length > 1) {
            E currentNode = list.pop(); // Get the node with the smallest distance.
            double currentDist = distances.item(currentNode); // Current distance.
            String currentPath = paths.item(currentNode); // Path to current node.
            HashTable<E, Double> neighbours = graph.get(currentNode); // Get neighbours of current node.

            // Iterate over each neighbor to calculate new distance and path.
            for (E neighbour : neighbours.getKeys()) {
                double newDist = currentDist + neighbours.item(neighbour); // Calculate new distance to neighbour.
                String newPath = currentPath + neighbour; // Construct new path to neighbour.
                if (newDist < distances.item(neighbour)) {
                    // If the new distance is shorter, update the distance and path.
                    distances.changeValue(neighbour, newDist);
                    paths.changeValue(neighbour, newPath);
                    list.changePriority(neighbour, newDist); // Update priority in the list.
                }
            }
        }
        return paths.item(endVertex); // Return the path to the endVertex.
    }

    // Method to find the distance of the shortest path from startVertex to endVertex.
    public double pathDistance(E startVertex, E endVertex) {
        PriorityList<E> list = new PriorityList<>(); // List to store the vertices with their current distances.
        HashTable<E, Double> distances = new HashTable<>(); // Stores distances from startVertex to each vertex.
        ArrayList<E> nodes = graph.getNodes(); // List of all nodes in the graph.

        // Initialise distances
        distances.add(startVertex, 0.0);
        list.add(startVertex, 0);
        for (E node : nodes) {
            if (node != startVertex) {
                distances.add(node, Double.MAX_VALUE); // Set distance to maximum for non-start nodes.
                list.add(node, Integer.MAX_VALUE);
            }
        }

        // Main loop to process each node
        while (list.asArray().length > 1) {
            E currentNode = list.pop(); // Get the node with the smallest distance.
            double currentDist = distances.item(currentNode); // Current distance.
            HashTable<E, Double> neighbors = graph.get(currentNode); // Get neighbours of current node.

            // Iterate over each neighbour to calculate new distance.
            for (E neighbor : neighbors.getKeys()) {
                double newDist = currentDist + neighbors.item(neighbor); // Calculate new distance to neighbour.
                if (newDist < distances.item(neighbor)) {
                    // If the new distance is shorter, update the distance.
                    distances.changeValue(neighbor, newDist);
                    list.changePriority(neighbor, newDist); // Update priority in the list.
                }
            }
        }
        return distances.item(endVertex); // Return the distance to the endVertex.
    }
}