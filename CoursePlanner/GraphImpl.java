import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Filename:   GraphImpl.java
 * Project:    p4
 * Course:     cs400 
 * Authors:    Kimberly Inthavong, Sahana Iyer
 * Due Date:   11/19/2018
 * 
 * T is the label of a vertex, and List<T> is a list of
 * adjacent vertices for that vertex.
 *
 * Additional credits: 
 *
 * Bugs or other notes: 
 *
 * @param <T> type of a vertex
 */
public class GraphImpl<T> implements GraphADT<T> {

    // YOU MAY ADD ADDITIONAL private members
    // YOU MAY NOT ADD ADDITIONAL public members

    /**
     * Store the vertices and the vertice's adjacent vertices
     */
    private Map<T, List<T>> verticesMap; 
    
    private int edgeNumber;
    
    /**
     * Construct and initialize and empty Graph
     */ 
    public GraphImpl() {
        verticesMap = new HashMap<T, List<T>>();
        // you may initialize additional data members here
        edgeNumber = 0;
    }

    /**
     * This method adds a new vertex to the graph, 
     * 
     * @param vertex T
     */
    public void addVertex(T vertex) {
    	// Create a new array list for the vertex 
    	// to store its neighbors 
    	try {
    	List<T> neighbors = new ArrayList<T>();
    	verticesMap.put(vertex, neighbors);
    	} catch (Exception e){
    		throw e;
    	}
    }

    /**
     * This method removes a vertex from the graph,
     * as well as any edges connecting to the vertex.
     * 
     * @param vertex T
     */
    public void removeVertex(T vertex) {
    	// If the vertex is the only one in the graph
    	if (verticesMap.size() == 1 && verticesMap.containsKey(vertex)){
    		verticesMap.remove(vertex);
    	}
    	
    	// Create a set of all the vertices (keys) in the hashmap
    	Set<T> tempKeySet = getAllVertices();
    	
    	// Create an array of T elements to store vertices
    	T[] tempKeyArray;
    	// TODO DELETE Object[] array = new Object[0];
    	// Convert the set of vertices to an accessable array 
    	tempKeyArray = (T[]) tempKeySet.toArray();
    	T tempVertex; 
    	
    	// Checks every vertex in the graph for their out-degree 
    	// list of neighbors for vertex-to-be-remove. 
    	// If it is, remove from list, removing the edge from graph
    	for (int i = 0; i < tempKeySet.size(); i++){
    		if (tempKeyArray[i] != null) {
	    		tempVertex = tempKeyArray[i];
	    		if (verticesMap.get(tempVertex).contains(vertex)) {
	    			// Iterate through the neighbor list to find and remove 
	    			// the vertex to be removed from graph
	    			for (int j = 0; j < verticesMap.get(tempVertex).size(); j++) {
	    				if (verticesMap.get(tempVertex).get(j).equals(vertex)) {
	    					verticesMap.get(tempVertex).remove(j);
	    				}
	    			}
	    		}
    		}
    		
    	}
    	
    	// Remove vertex from Hashmap
    	verticesMap.remove(vertex);
    	
    	
    }

    /**
     * This method adds an edge between two vertices 
     * 
     * @param vertex1, start vertex
     * @param vertex2, end vertex
     */
    public void addEdge(T vertex1, T vertex2) {
    	// Obtain the vertex in the hashmap, adding 
    	// the outgoing vertex to it's list of neighbors 
    	try {
    		if (verticesMap.get(vertex1).add(vertex2)){
    			edgeNumber++;
    		}
    	} catch(Exception e){
    		throw e;
    	}
    }
    
    /**
     * This method removes an edge between two vertices.
     * 
     * @param vertex1, start vertex
     * @param vertex2, end vertex
     */
    public void removeEdge(T vertex1, T vertex2) {
    	try {
    		if (verticesMap.get(vertex1).remove(vertex2)){
        		edgeNumber--;
        	}
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    }    
    
    /**
     * This method returns a set representation 
     * of all the vertices in the graph
     * 
     * @return Set<T>
     */
    public Set<T> getAllVertices() {
        return verticesMap.keySet();
    }

    /**
     * This method returns a list representation
     * of all the adjacent vertices of the given vertex,
     * which vertices pointing to the given vertex
     * 
     * @return List<T>
     */
    public List<T> getAdjacentVerticesOf(T vertex) {
    	return verticesMap.get(vertex);
    }
    
    /**
     * This method determines if the given vertex
     * is in the graph
     * 
     * @param vertex
     * @return boolean if it exists
     */
    public boolean hasVertex(T vertex) {
        return verticesMap.containsKey(vertex);
    }

    /**
     * This method returns the number of vertices in the graph
     * 
     * @return int number of vertices 
     */
    public int order() { 	
    	return verticesMap.size();
    }

    /**
     * This method returns the number of EDGES
     * 
     * @return int number of edges
     */
    public int size() {
    	return edgeNumber;
    }
    
    
    /**
     * Prints the graph for the reference
     * DO NOT EDIT THIS FUNCTION
     * DO ENSURE THAT YOUR verticesMap is being used 
     * to represent the vertices and edges of this graph.
     */
    public void printGraph() {

        for ( T vertex : verticesMap.keySet() ) {
            if ( verticesMap.get(vertex).size() != 0) {
                for (T edges : verticesMap.get(vertex)) {
                    System.out.println(vertex + " -> " + edges + " ");
                }
            } else {
                System.out.println(vertex + " -> " + " " );
            }
        }
    }
}
