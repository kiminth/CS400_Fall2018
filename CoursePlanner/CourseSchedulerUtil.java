
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Filename:   CourseSchedulerUtil.java
 * Project:    p4
 * Authors:    Debra Deppeler, Kimberly Inthavong, Sahana Iyer
 * 
 * Use this class for implementing Course Planner
 * @param <T> represents type
 */

public class CourseSchedulerUtil<T> {
    
    // can add private but not public members
	
    /**
     * Graph object
     */
    private GraphImpl<T> graphImpl;
    
    
    /**
     * constructor to initialize a graph object
     */
    public CourseSchedulerUtil() {
        this.graphImpl = new GraphImpl<T>();
    }
    
    /**
    * createEntity method is for parsing the input json file 
    * @return array of Entity object which stores information 
    * about a single course including its name and its prerequisites
    * @throws Exception like FileNotFound, JsonParseException
    */
    @SuppressWarnings("rawtypes")
    public Entity[] createEntity(String fileName) throws Exception {
       
    	JSONParser parser = new JSONParser();
    	
    	try {
    		Object obj = parser.parse(new FileReader(fileName + ".json"));
    		// typecasting obj to JSONObject 
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray courses = (JSONArray) jsonObj.get("courses");
            	
        	Iterator <Map.Entry>itr1 = courses.iterator();
        	Iterator itr2 = courses.iterator();

        	ArrayList<String> tempPreqArray = new ArrayList<String>();
        	Entity[] entity = new Entity[10];
        	int count = 0;
        	
            while (itr2.hasNext()){
            	// Create a temporary entity object to motify
            	// (and later store)
            	Entity temp = new Entity();
            	
            	itr1 = ((Map) itr2.next()).entrySet().iterator();
   
            	while (itr1.hasNext()){
            		
            		Map.Entry pair = itr1.next();
            		
            		// Create a JSONArray to store the prequisites 
            		JSONArray preq = (JSONArray) pair.getValue();
            		// Iterator to traverse the prequisites 
            		Iterator itrPreq = preq.iterator();
	
            		// Check if there are prequisites 
            		if (pair.getValue().toString().length() > 3){
            			// Traverse the list of prerequisites
	            		while (itrPreq.hasNext()){
	            			Object tempPreq = itrPreq.next();
	            			tempPreqArray.add(tempPreq.toString());
	            		}
            		} else {
            			tempPreqArray = new ArrayList<String>();
            		}
            			
            		temp.setPrerequisites(tempPreqArray.toArray());
        		
            		pair = itr1.next();
            		
            		temp.setName(pair.getValue());
            		
            		
            	}
            	
            	// Adds object to entity array 
                entity[count] = temp;
                // Remove all the values in the temp array
                for (int r = 0; r < tempPreqArray.size(); r++){
                	tempPreqArray.remove(r);
                }
                               
                // Increment count 
                count++;
        	}
    		 
            return entity;
            
    	} catch (Exception e){
    		e.printStackTrace();
    		throw e;
    	}

        
    }
    
    
    /**
     * Construct a directed graph from the created entity object 
     * @param entities which has information about a single course 
     * including its name and its prerequisites
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void constructGraph(Entity[] entities) {
    	try {
    		Object[] temp;
	
	    	for (int i = 0; i < entities.length; i++){
	    		// Confirm that entity exists
	    		if (entities[i] != null){
	    			// Adds the name as a vertex of the graph
	    			graphImpl.addVertex((T) entities[i].getName());
	    			temp = entities[i].getPrerequisites();
	    			
	    			// Adds the prequisites into the list of adj vertices for the graph
	    			for (int j = 0; j < entities[i].getPrerequisites().length ; j++){
	    				if (entities[i].getPrerequisites()[j] != null){    				
		    				graphImpl.addEdge((T) entities[i].getName(), 
		    						(T) entities[i].getPrerequisites()[j]);

		    				if (!graphImpl.hasVertex((T) entities[i].getPrerequisites()[j])){
		    					graphImpl.addVertex((T) entities[i].getPrerequisites()[j]);
		    				}

	    				}
	    			}
	    			
	    		}
	    		
	    	}
	    	   	
    	} catch (Exception e){
    		e.printStackTrace();
    		throw e;
    	}

    			
    }
    
    
    /**
     * Returns all the unique available courses
     * @return the sorted list of all available courses
     */
    public Set<T> getAllCourses() {
        return graphImpl.getAllVertices();
    }
    
    
    /**
     * To check whether all given courses can be completed or not
     * @return boolean true if all given courses can be completed,
     * otherwise false
     * @throws Exception
     */
    public boolean canCoursesBeCompleted() throws Exception {
    	// Declare feilds
    	Set<T> tempSet = graphImpl.getAllVertices();
    	T[] tempArray = (T[]) tempSet.toArray();
    	List<T> tempList; 
    	Boolean canComplete = true;
    	
    	try{
	    	// For each vertice in the graph, check if there is a path back to 
	    	// to it (looking for cycle)
	    	for (int i = 0; i < tempArray.length; i++){
	    		if (tempArray[i] != null){
	    		
	    			// Get all the prerequisites of the temp list
	    			tempList = graphImpl.getAdjacentVerticesOf(tempArray[i]);
	    			
	    			// Call recurssive method each avalible prerequisite
	    			if (!tempList.isEmpty()){
	    				for (int j = 0; j < tempList.size(); j++){
	    					canComplete = completeHelper(tempArray[i], tempList.get(j));
	    					
	    					// If there ever is a case that a self loop is found, return the false
	    					// value 
	    					if(!canComplete){
	    						return canComplete;
	    					}
	    				}
	    			}
	    		}
	    	}
	    	
	    	return canComplete;
	    	
    	} catch (Exception e){
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    /**
     * This is a recurssive helper for the "canCoursesBeCompleted method.
     * 
     * @param start is the vertex to keep comparing to, does not change
     * @param compare changes every recurssive call
     * @return boolean will be false if the passed in vertices match
     */
    private boolean completeHelper(T start, T compare){	
    	List<T> tempList = graphImpl.getAdjacentVerticesOf(compare);
    	Boolean temp = true;
    	
    	// If there are no prequisites, return true
    	if (tempList.isEmpty()){
    		return temp;
    	}
    	
    	// If the start vertice matches the compare vertice, return false
    	if (start.equals(compare)){
    		return false;
    	}

    	// Recurrsively call each prerequisite chain to check if 
    	// the starting vertex is within the 
    	for (int i = 0; i < tempList.size(); i++){
    		temp = completeHelper(start, tempList.get(i));
    		if (!temp){
    			return temp; // This indidcates a self loop was found as a prerequisite 
    		}
    	}
    	
    	return temp;
    }
    
    
    /**
     * The order of courses in which the courses has to be taken
     * @return the list of courses in the order it has to be taken
     * @throws Exception when courses can't be completed in any order
     */
    public List<T> getSubjectOrder() throws Exception {
        // Declare feilds
    	Set<T> tempSet = graphImpl.getAllVertices();
    	T[] tempArray = (T[]) tempSet.toArray();
        Stack stack = new Stack(); 
        List<T> listOrder = new ArrayList<T>();
        List<T> correctOrder = new ArrayList<T>();
        
        try {
	        // Initialize the visited array to all not visited 
	        boolean visited[] = new boolean[graphImpl.order()]; 
	        for (int i = 0; i < graphImpl.order(); i++) 
	            visited[i] = false; 
	  
	        // Use a recursive helper to store the vertices
	        // in Topological order
	        for (int i = 0; i < graphImpl.order(); i++) 
	            if (visited[i] == false) 
	                orderHelper(tempArray[i], i, visited, stack, tempArray); 
	  
	        // Add contents of the stack to the array
	        while (!stack.empty()) {
	        	listOrder.add(tempArray[(int) stack.pop()]);
	        }
	        
	        // Reverse the order: needs to be first to last
	        for (int c = graphImpl.order() - 1; c >= 0; c--){
	        	correctOrder.add(listOrder.remove(c));
	        }
	        
	    	// List in topological order, from first to last
	        return correctOrder;
	        
        } catch (Exception e){
        	e.printStackTrace();
    		throw e;
        }

    }

    private void orderHelper(T vertice, int v, boolean visited[], Stack stack, T[] vertices){
   	 // Mark the current node as visited. 
       visited[v] = true; 
       T newVertice; 
       int index = v;
       
       // Create an iterator to go through the list of adjacent vertices 
       Iterator<T> it = graphImpl.getAdjacentVerticesOf(vertices[v]).iterator(); 
       while (it.hasNext()) 
       { 
           newVertice = it.next(); 
           // Find the location of this new vertice in the 
           // corresponding vertices array
           for (int f = 0; f < vertices.length; f++){
           	if(newVertice.equals(vertices[f])){
           		index = f;
           	}
           }
           
           if (!visited[index]) 
               orderHelper(newVertice, index, visited, stack, vertices); 
       } 
 
       // Push current vertex to stack which stores result 
       stack.push(new Integer(v)); 
   }
        
    /**
     * The minimum course required to be taken for a given course
     * @param courseName 
     * @return the number of minimum courses needed for a given course
     */
    public int getMinimalCourseCompletion(T courseName) throws Exception {
    	// Declare feilds
    	Set<T> tempSet = graphImpl.getAllVertices();
    	T[] tempArray = (T[]) tempSet.toArray();
        Stack stack = new Stack(); 
        List<T> listOrder = new ArrayList<T>();
        List<T> correctOrder = new ArrayList<T>();
        int courseCount = 0;
        
        try {
	        // Initialize the visited array to all not visited 
	        boolean visited[] = new boolean[graphImpl.order()]; 
	        for (int i = 0; i < graphImpl.order(); i++) 
	            visited[i] = false; 
	  
	        // Use a recursive helper to store the vertices
	        // in Topological order
	        for (int i = 0; i < graphImpl.order(); i++){ 
	            if (visited[i] == false) {
	                orderHelper(courseName, i, visited, stack, tempArray); 
	            }
	        }
	
	        // Add contents of the stack to the array
	        while (!stack.empty()) {
	        	listOrder.add(tempArray[(int) stack.pop()]);
	        }
	        
	        // Reverse the order: needs to be first to last
	        for (int c = graphImpl.order() - 1; c >= 0; c--){
	        	correctOrder.add(listOrder.remove(c));
	        }
	        
	        //TODO UPADTE: NOW IT WORKS
	        // Count the number of courses to the goal
	        for (int a = 0; a < graphImpl.order(); a++){
	        	if (correctOrder.toArray()[a].equals(courseName)){
	        		return courseCount;
	        	}
	        	courseCount++;
	        }
	        
	        return courseCount;
        } catch (Exception e){
        	throw e;
        }
        
    }
   
    
}
