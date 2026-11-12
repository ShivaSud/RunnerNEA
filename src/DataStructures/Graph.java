package DataStructures;

import java.util.ArrayList;

public class Graph<E> {

    ArrayList<E> nodes = new ArrayList<>(); // A list to store all nodes in the graph.

    public final HashTable<E, HashTable<E, Double>> graph = new HashTable<>(); // Represents the graph with nodes and their connections.

    // Adds a directed edge from startNode to endNode with a given weight.
    public void add(E startNode, E endNode, double weight){
        if(!nodes.contains(startNode)){
            nodes.add(startNode); // Adds the startNode to the nodes list if not already present.
        }
        if(!nodes.contains(endNode)){
            nodes.add(endNode); // Adds the endNode to the nodes list if not already present.
        }
        if(graph.contains(startNode)){
            if(!graph.item(startNode).contains(endNode)){
                graph.item(startNode).add(endNode, weight); // Adds the connection if it doesn't already exist.
            }else{
                throw new IllegalArgumentException("Already added to graph"); // Throws an error if the connection already exists.
            }
        }else{
            HashTable<E, Double> newConnection = new HashTable<>(); // Creates a new connection table for the startNode.
            newConnection.add(endNode, weight);
            graph.add(startNode, newConnection); // Adds the new connection to the graph.
        }
    }

    // Adds a two-way (bi-directional) connection between startNode and endNode with the specified weight.
    public void addTwoWayConnection(E startNode, E endNode, int weight){
        add(startNode, endNode, weight); // Adds a connection from startNode to endNode.
        add(endNode, startNode, weight); // Adds a connection from endNode to startNode.
    }

    // Retrieves the list of all nodes in the graph.
    public ArrayList<E> getNodes(){
        return nodes;
    }

    // Converts the graph to its string representation.
    public String toString(){
        return graph.toString();
    }

    // Retrieves the connections (edges and weights) of a specific node.
    public HashTable<E, Double> get(E node){
        if(graph.contains(node)) {
            return graph.item(node);
        }else{
            throw new IllegalArgumentException(); // Throws an error if the node does not exist in the graph.
        }
    }
}