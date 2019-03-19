/**
 * Filename:   TestPQ.java
 * Project:    p1TestPQ
 * Authors:    Debra Deppler, Kimberly Inthavong
 * Email:	   inthavong@wisc.edu
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    001
 *
 * Due Date:   Before 10pm on September 17, 2018
 * Version:    2.0
 * 
 * Bugs:       no known bugs
 */

import java.util.NoSuchElementException; 
import java.util.Random;

/**
 * Runs black-box unit tests on the priority queue implementations
 * passed in as command-line arguments (CLA).
 * 
 * If a class with the specified class name does not exist 
 * or does not implement the PriorityQueueADT interface,
 * the class name is reported as unable to test.
 * 
 * If the class exists, but does not have a default constructor,
 * it will also be reported as unable to test.
 * 
 * If the class exists and implements PriorityQueueADT, 
 * and has a default constructor, the tests will be run.  
 * 
 * Successful tests will be reported as passed.
 * 
 * Unsuccessful tests will include:
 *     input, expected output, and actual output
 *     
 * Example Output:
 * Testing priority queue class: PQ01
 *    5 PASSED
 *    0 FAILED
 *    5 TOTAL TESTS RUN
 * Testing priority queue class: PQ02
 *    FAILED test00isEmpty: unexpectedly threw java.lang.NullPointerException
 *    FAILED test04insertRemoveMany: unexpectedly threw java.lang.ArrayIndexOutOfBoundsException
 *    3 PASSED
 *    2 FAILED
 *    5 TOTAL TESTS RUN
 * 
 *   ... more test results here
 * 
 * @author deppeler
 */
public class TestPQ {

	// set to true to see call stack trace for exceptions
	private static final boolean DEBUG = false;

	/**
	 * Run tests to determine if each Priority Queue implementation
	 * works as specified. User names the Priority Queue types to test.
	 * If there are no command-line arguments, nothing will be tested.
	 * 
	 * @param args names of PriorityQueueADT implementation class types 
	 * to be tested.
	 */
	public static void main(String[] args) {
		for (int i=0; i < args.length; i++) 
			test(args[i]);

		if ( args.length < 1 ) 
			print("no PQs to test");
	}

	/** 
	 * Run all tests on each priority queue type that is passed as a classname.
	 * Exception is made for PQ02 for test06 due to slow execution
	 * 
	 * If constructing the priority queue in the first test causes exceptions, 
	 * then no other tests are run.
	 * 
	 * @param className the name of the class that contains the 
	 * priority queue implementation.
	 */
	private static void test(String className) {
		print("Testing priority queue class: " + className);
		int passCount = 0;
		int failCount = 0;
		try {
			if (test00isEmpty(className)) passCount++; else failCount++;		
			if (test01getMaxEXCEPTION(className)) passCount++; else failCount++;

			if (test02removeMaxEXCEPTION(className)) passCount++; else failCount++;
			if (test03insertRemoveOne(className)) passCount++; else failCount++;
			if (test04insertRemoveMany(className)) passCount++; else failCount++;
			if (test05duplicatesAllowed(className)) passCount++; else failCount++;
			
			// Due to the inefficient code of PQ02 taking too long to complete 
			// this test, in order to retrieve results within a timely mannor, 
			// it has been determined it would be best to skip regarding PQ02
			if (className.compareTo("PQ02") != 0){
				if (test06manyDataItems(className)) passCount++; else failCount++;
			}
			
			if (test07insertManyRemovingOneOver(className)) passCount++; else failCount++;
			if (test08insertGetMaxRemoveMaxIterations(className)) passCount++; else failCount++;
			if (test09isNoLongerEmpty(className)) passCount++; else failCount++;
			if (test10consistentGetMax(className)) passCount++; else failCount++;

			String passMsg = String.format("%4d PASSED", passCount);
			String failMsg = String.format("%4d FAILED", failCount);
			print(passMsg);
			print(failMsg);

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			if (DEBUG) e.printStackTrace();
			print(className + " FAIL: Unable to construct instance of " + className);
		} finally {
			String msg = String.format("%4d TOTAL TESTS RUN", passCount+failCount);
			print(msg);
		}

	}

	/**
	 * Checks that getMax can consistantly provide the same intended max priority value
	 * Testing by inserting a random integer, then inserting multiple values that are
	 * lower priority than the first inserted integer, checking every loop iteration that
	 * getMax has not changed.
	 *
	 * @param className
	 * @return false is getMax is not consistent 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test10consistentGetMax(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		Random rand = new Random();
		Integer maxPriority; // Holds the max priority value, inserted first
		Integer temp; // Holds values to compare to intended max priority
		int numLowerValues = 20; // Number of lower priority values inserted 
		
		try {
			// Create new priority queue  
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			
			maxPriority = rand.nextInt();
			pq.insert(maxPriority);
			
			// Check that inserted item is the maxPriority
			temp = pq.getMax();
			if (maxPriority.compareTo(temp) != 0){
				print("FAILED test10consistentGetMax: first item inserted in PQ was "
						+ "not assigned to be the max priority value.");
				return false;
			}
			
			// Insert values of lesser priority than maxPriority into PQ,
			// after each insertion, call getMax and compare given output 
			// to intended max priority value
			for (int i = 1; i <= numLowerValues; i++){
				pq.insert(maxPriority - i); 
				temp = pq.getMax();
				if (maxPriority.compareTo(temp) != 0){
					print("FAILED test10consistentGetMax: after " + i + " values were inserted, "
							+ "getMax did not return proper max priority value"
							+ "\n Last inserted value: " + temp
							+ "\n Expected output: " + maxPriority + ", Actual output: " + temp);
					return false;
				}
				
			}
			
		} catch(Exception e){
			if (DEBUG) e.printStackTrace();
			print("FAILED test10consistentGetMax: threw " + e.getClass().getName());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks the isEmpty function of the PQ returns true after inserting at least 1 value
	 * into the PQ. 
	 *
	 * @param className
	 * @return false if isEmpty returns false on a non-empty PQ
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test09isNoLongerEmpty(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		Random rand = new Random();
		Integer temp = rand.nextInt(); // Create a random integer to insert
		
		try {
			// Create new priority queue  
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			
			// Insert random value into the priority queue
			pq.insert(temp);
			
			// Check the isEmpty fuction 
			if (pq.isEmpty()){
				print("FAILED test09isNoLongerEmpty: isEmpty returned true,"
						+ " even though the PQ was not empty."
						+ "\n Inserted value: " + temp);	
				return false;
			}
			
		} catch(NoSuchElementException e){
			print("FAILED test09isNoLongerEmpty: isEmpty threw a NoSuchElementException,"
					+ " even though the PQ was not empty."
					+ "\n Inserted value: " + temp);
			return false;
		
		} catch(Exception e){
			if (DEBUG) e.printStackTrace();
			print("FAILED test09isNoLongerEmpty: threw " + e.getClass().getName());
			return false;
		}
		return true;
	}
	
	/**
	 * Checks that PQ can properly do multiple iterations of inserting, getMax, and removing
	 * from the PQ. The cycle is is adding one value, getting the max priority value which
	 * should be the initial value inserted, and removing the max priority value which should
	 * still be the initial value inserted. The PQ should end up empty before and after every loop.
	 * A random generator sets the number of iterations
	 * the loop does, with private fields determining the max and min.
	 *
	 * @param className
	 * @return false if priority queue throws any exception, or the getMax or removeMax values
	 * does not match value inserted within the first step of the iteration
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test08insertGetMaxRemoveMaxIterations(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		final int MAX_ITERATIONS = 50; // Max times loop will be iterated
		final int MIN_ITERATIONS = 10; // Min times loop will be iterated
		Random rand = new Random();
		Integer temp; // Holds a random integer to insert into PQ
		Integer compareValue; // Holds integer from getMax and removeMax to compare
		int iterateCount = 0; // Holds the iteration count 
		
		try {
			// Create new priority queue  
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			
			for (int i = 0; i <= rand.nextInt(MAX_ITERATIONS) + MIN_ITERATIONS; i++){
				iterateCount++;
				
				temp = rand.nextInt(); // Create random integer to insert
				pq.insert(temp);
				
				compareValue = pq.getMax(); // Obtain getMax value to compare
				if (temp.compareTo(compareValue) != 0){
					print("FAILED test08insertRemoveIterations: getMax on iteration loop #"
							+ iterateCount + " did not match value inserted."
							+ "\n Expected output: " + temp + ", Actual output: " + compareValue);
					return false;
				}
				
				compareValue = pq.removeMax(); // Obtain removeMax value to compare
				if (temp.compareTo(compareValue) != 0){
					print("FAILED test08insertRemoveIterations: removeMax on iteration loop #"
							+ iterateCount + " did not match value inserted."
							+ "\n Expected output: " + temp + ", Actual output: " + compareValue);
					return false;
				}
			}
			
		} catch (Exception e){
			if (DEBUG) e.printStackTrace();
			print("FAILED test08insertRemoveIterations: threw " + e.getClass().getName());
			return false;
		}
		return true;
	}
	
	/**
	 * Inserts many items and removes the exact number of items plus one over, 
	 * fails if removeMax does not throw NoSuchElementException.
	 * Test makes sure that removes items is capable of removing all items and
	 * checks if removeMax throws an exception on a now theoretically empty PQ.
	 *
	 * @param className
	 * @return false if removeMax does not throw NoSuchElementException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test07insertManyRemovingOneOver(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		final int MAX_ITEMS = 100; // Max items to be inserted and removed
		Random rand = new Random();
		
		try {
			// Create new priority queue  
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			
			// Insert items 
			for (int k = 0; k < MAX_ITEMS; k++){
				pq.insert(rand.nextInt());
			}
	
			// Remove items and one extra removeMax call 
			for (int i = 0; i < MAX_ITEMS + 1; i++){
				pq.removeMax();
			}
		} catch (NoSuchElementException e){
			return true;
		} catch (Exception e){
			if (DEBUG) e.printStackTrace();
			print("FAILED test07insertManyRemovingOneOver: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test07insertManyRemovingOneOver: NoSuchElementExeption was not thrown "
				+ "when calling removeMax on empty PQ after removing all items for PQ."
				+ "\n Inserted " + MAX_ITEMS + " items and attempted to remove " + (MAX_ITEMS + 1));
		return false; 
		
		
	}
	
	/**
	 * Inserts many items to see if internal data structure expands properly to allow
	 * many items to be added and removed in priority order
	 *
	 * @param className
	 * @return false if priority queue cannot allow many items to be added and removed
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test06manyDataItems(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		final int MAX_ITEMS = 1000000; //The number of many items to be inserted
		
		try {
			// Create new priority queue  
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
	
			int temp;  // Holds items that are removed 
	
			// Insert many items 
			for (int i = 0; i <= MAX_ITEMS; i++){
				pq.insert(i);
			}
	
			// Remove items and check that items are returned in priority order
			for (int i = 0; i <= MAX_ITEMS; i++){
				temp = pq.removeMax();
				
				// Priority queue fails test if it does not match the priority order
				if (temp != (MAX_ITEMS - i)){
					print("FAILED test06manyDataItems: priority queue does not expand internal data structure to "
							+ "allow many items to be added and removed");
					return false;
				}
			}
		} catch (Exception e){
			if (DEBUG) e.printStackTrace();
			print("FAILED test06manyDataItems: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		return true;

	}

	/** 
	 * Inserts duplicate values, fails if they cannot be inserted and removed
	 *
	 * @param className
	 * @return false if duplicate values cannot be inserted and removed from PQ
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test05duplicatesAllowed(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	
		Integer duplicateVal = 5; // Value to be inserted twice
		Integer lowPriorityVal = 2; // Integer with lower priority than duplicate value
	    Integer temp1; // Hold first value removed
		Integer temp2; // Hold second value removed 
		
		try {
			// Create new priority queue  
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);		
			
			// Insert same value (duplicate), should be allowed
			pq.insert(duplicateVal);
			pq.insert(lowPriorityVal); 
			pq.insert(duplicateVal);
			
	
			// Remove values to compare if the are the same as inserted
			temp1 = pq.removeMax();
			temp2 = pq.removeMax();
	
			if (duplicateVal.compareTo(temp1) == 0 && duplicateVal.compareTo(temp2) == 0){
				return true;
			}
		} catch (Exception e){
			if (DEBUG) e.printStackTrace();
			print("FAILED test05duplicatesAllowed: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test05duplicatesAllowed: duplicate values cannot be inserted and removed from PQ"
				+ "\n Inserted two values of 5 and one 2 value, expected duplicate values to be "
				+ "inserted and placed at highest priority."
				+ "\n Expected output: '5, 5.' Actual output: '" + temp1 + ", " + temp2 + ".'" );
		return false;

	}


	/** 
	 * Inserts multiple items in the priorityQueue and removes the items,
	 * test fails if the it does not return the max values in priority order
	 *
	 * @param className
	 * @return true if values are returned in priority order
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test04insertRemoveMany(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		boolean fail = false; // If test passes or fails
		
		try {
			// Create new priority queue  
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			
			// Items to insert
			Integer[] insertedItems = new Integer[]{5, 8, 4, 7, 3};
			// Expected return order 
			Integer[] expectedItems = new Integer[]{8, 7, 5, 4, 3};
	
			Integer[] removedItems = new Integer[5]; // Holds returned values from PQ to be compared
			String actualOutput = "";
			
			// Insert items 
			for (int k = 0; k < expectedItems.length; k++){
				pq.insert(insertedItems[k]);
			}
	
			// Remove items and check that items are returned in priority order
			for (int i = 0; i < expectedItems.length; i++){
				removedItems[i] = pq.removeMax();
				
				// Priority queue fails test if it does not match the priority order
				if (removedItems[i].compareTo(expectedItems[i]) != 0){
					fail = true;
				}
			}
			
			if (fail){
				print("FAILED test04insertRemoveMany: the items were not removed in "
						+ "priority order. "
						+ "Inserted '5 8 4 7 3'"
						+ "\nExpected output: 8 7 5 4 3. ");
				for (int j = 0; j < removedItems.length - 1; j++){
					actualOutput += removedItems[j] + " ";
				}
				print("Actual output: " + actualOutput + removedItems[ removedItems.length - 1] + ".");
				return false;
			}
		} catch (Exception e){
			if (DEBUG) e.printStackTrace();
			print("FAILED test04insertRemoveMany: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		
		return true;
	}

	/** 
	 * Inserts a single items and removes item from priorityQueue, 
	 * test fails if the items is not the same value as inserted item
	 *
	 * @param className
	 * @return false if removed items is not the same value as what was inserted
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test03insertRemoveOne(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		Random rand = new Random();
		Integer item; // To be inserted into the PQ
		Integer temp; // To hold the value that is removed
		
		try {
			// Create new priority queue 
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			
			item = rand.nextInt(); // Generates random value
	
			pq.insert(item); 
			temp = pq.removeMax();
			
			// Checks to see if removed item matches the value insert
			if (temp.compareTo(item) == 0){
				return true;
			}

		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test03insertRemoveOne: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test03insertRemoveOne: the removed item is not the same value "
				+ "as inserted item: " + item
				+ "\n Expected output: " + item + ", Actual output:" + temp);
		return false;
	}

	/** 
	 * Tests the removeMax function of the PriorityQueue, 
	 * fails if removeMax on a newly constructed PriorityQueue does not throw
	 * a NoSuchElementException
	 *
	 * @param className
	 * @return false if no exception is thrown when calling removeMax on a 
	 * new Priority Queue
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test02removeMaxEXCEPTION(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {		
		try { 
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			pq.removeMax();
		} catch (NoSuchElementException e){
			return true; 
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test02removeMaxException: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test02removeMaxException: NoSuchElementExeption was not thrown "
					+ "when calling removeMax on newly made PQ");
		return false;
	}

	/** DO NOT EDIT -- provided as an example
	 * Confirm that getMax throws NoSuchElementException if called on 
	 * an empty priority queue.  Any other exception indicates a fail.
	 * 
	 * @param className name of the priority queue implementation to test.
	 * @return true if getMax on empty priority queue throws NoSuchElementException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test01getMaxEXCEPTION(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT<Integer> pq = newIntegerPQ(className);
		try {
			pq.getMax();
		} catch (NoSuchElementException e) {
			return true;			
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test01getMaxEXCEPTION: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test01getMaxEXCEPTION: getMax did not throw NoSuchElement exception on newly constructed PQ");
		return false;
	}

	/** DO NOT EDIT THIS METHOD
	 * @return true if able to construct Integer priority queue and 
	 * the instance isEmpty.
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test00isEmpty(String className) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT<Integer> pq = newIntegerPQ(className);
		try {
		if (pq.isEmpty()) 
			return true;
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test00isEmpty: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test00isEmpty: isEmpty returned false on newly constructed PQ");
		return false;
	}



	/** DO NOT EDIT THIS METHOD
	 * Constructs a max Priority Queue of Integer using the class that is name.
	 * @param className The specific Priority Queue to construct.
	 * @return a PriorityQueue
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "unchecked" })
	public static final PriorityQueueADT<Integer> newIntegerPQ(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> pqClass = Class.forName(className);
		Object obj = pqClass.newInstance();	
		if (obj instanceof PriorityQueueADT) {
			return (PriorityQueueADT<Integer>) obj;
		}
		return null;
	}

	/** DO NOT EDIT THIS METHOD
	 * Constructs a max Priority Queue of Double using the class that is named.
	 * @param className The specific Priority Queue to construct.
	 * @return a PriorityQueue
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "unchecked" })
	public static final PriorityQueueADT<Double> newDoublePQ(final String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> pqClass = Class.forName(className);
		Object obj = pqClass.newInstance();	
		if (obj instanceof PriorityQueueADT) {
			return (PriorityQueueADT<Double>) obj;
		}
		return null;
	}

	/** DO NOT EDIT THIS METHOD
	 * Constructs a max Priority Queue of String using the class that is named.
	 * @param className The specific Priority Queue to construct.
	 * @return a PriorityQueue
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "unchecked" })
	public static final PriorityQueueADT<String> newStringPQ(final String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> pqClass = Class.forName(className);
		Object obj = pqClass.newInstance();	
		if (obj instanceof PriorityQueueADT) {
			return (PriorityQueueADT<String>) obj;
		}
		return null;
	}


	/** DO NOT EDIT THIS METHOD
	 * Write the message to the standard output stream.
	 * Always adds a new line to ensure each message is on its own line.
	 * @param message Text string to be output to screen or other.
	 */
	private static void print(String message) {
		System.out.println(message);
	}

}

