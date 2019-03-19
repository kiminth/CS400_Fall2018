
public class TestGraphImpl {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    /**
	     * Graph object
	     */
	    GraphImpl<String> testGraph = new GraphImpl<String>();
	    
	    testGraph.addVertex("hello");
	    testGraph.addVertex("hey");
	    testGraph.addVertex("sup");
	    testGraph.addVertex("how are you?");
	    testGraph.addVertex("how have you been?");
	    // Now there are four vertices 
	    //testGraph.addVertex("hello"); // if you try to add a duplicate, it just doesnt add it
	    String[] temp = new String[0];
	    temp = testGraph.getAllVertices().toArray(temp);
	    
	    System.out.println("Test getAllVertices: Here are the elements of the graph: ");
	    for(int i = 0; i < temp.length; i++){
	    	System.out.println(temp[i]);
	    }
	    System.out.print("\n");
	    
	    testGraph.addEdge("hello", "how are you?");
	    testGraph.addEdge("hey", "how are you?");
	    testGraph.addEdge("sup", "how are you?");
	    
	    // Now there are two edges
	    
	    System.out.println("Test addEdge and getAdjVerticesOf: " + testGraph.getAdjacentVerticesOf("how are you?").toString());
	    System.out.print("\n");
	    
	    System.out.println("Test hasVertex, should be true: " + testGraph.hasVertex("hello"));
	    System.out.println("Test hasVertex, should be false: " + testGraph.hasVertex("bye"));
	    
	    System.out.println("Number of vertices: " + testGraph.order());
	    System.out.println("Number of edges: " + testGraph.size());
	    
//	    testGraph.removeVertex("hey");
//	    temp = testGraph.getAllVertices().toArray(temp);
//	    
//	    System.out.println("Here are the elements of the graph: ");
//	    for(int i = 0; i < temp.length; i++){
//	    	if (temp[i] != null){
//	    		System.out.println(temp[i]);
//	    	}
//	    }
//	    
//	    testGraph.removeVertex("hello");
//	    temp = testGraph.getAllVertices().toArray(temp);
//	    
//	    System.out.println("Here are the elements of the graph: ");
//	    for(int i = 0; i < temp.length; i++){
//	    	if (temp[i] != null){
//	    		System.out.println(temp[i]);
//	    	}
//	    }
	}

}
