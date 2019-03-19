/**
 * Filename:   TestAVLTree.java
 * Project:    p2
 * Authors:    Debra Deppeler, Kimberly Inthavong
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    001
 * 
 * Due Date:   as specified in Canvas
 * Version:    1.0
 * 
 * Credits:    none
 * 
 * Bugs:       no known bugs
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.lang.IllegalArgumentException;
import org.junit.Test;

/** Tests the various functionalities of an AVL Tree */
public class TestAVLTree {

	/**
	 * Tests that an AVLTree is empty upon initialization.
	 */
	@Test
	public void test01isEmpty() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		assertTrue(tree.isEmpty());
	}

	/**
	 * Tests that an AVLTree is not empty after adding a node.
	 */
	@Test
	public void test02isNotEmpty() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
			tree.insert(1);
			assertFalse(tree.isEmpty());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Tests functionality of a single delete following several inserts.
	 */
	@Test
	public void test03insertManyDeleteOne() {
		// Declare fields
		final int NUM_ITEMS_INSERT = 1000000; // the number of inserts 
		
		AVLTree<Integer> tree = new AVLTree<Integer>();
		int deleteItem = 0;
		boolean noError = true; 
		
		try{
			// Insert several items
			for (int i = 0; i <= NUM_ITEMS_INSERT; i++){
					tree.insert(i);
			}
				
			// Single delete, item to delete should exist in tree
			tree.delete(deleteItem);
			
			assertTrue(noError);
			
		} catch (IllegalArgumentException e){
			System.out.println(e.getMessage());
			noError = false;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			noError = false;
		}
		
		assertTrue(noError);
	}
	
	/**
	 * Tests functionality of many deletes following several inserts.
	 */
	@Test
	public void test04insertManyDeleteMany() {
		// Declare fields
		final int NUM_ITEMS_INSERT = 1000000; // the number of inserts
		final int NUM_ITEMS_DELETE = 5000; // the number of deletes, less insert number
		
		AVLTree<Integer> tree = new AVLTree<Integer>();
		boolean noError = true; 
		
		try{
			// Insert several items
			for (int i = 0; i <= NUM_ITEMS_INSERT; i++){
					tree.insert(i);
			}
				
			// Several deletes, items should exist in tree
			for (int j = 0; j <= NUM_ITEMS_DELETE; j++){
					tree.delete(j);
			}
			
			assertTrue(noError);
			
		} catch (IllegalArgumentException e){
			noError = false;
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			noError = false;
		}
		
		assertTrue(noError);
	}
	
	/**
	 * Tests if isEmpty works after inserting and then deleting a single element.
	 * isEmpty should return true, else test fails.
	 */
	@Test
	public void test05isEmptyAfterInsertRemoveSingle(){
		// Declare fields
		final int ITEM_INSERT = 1; // the number to insert 
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
			tree.insert(ITEM_INSERT); // Inserts a single element
			tree.delete(ITEM_INSERT); // Removes the same element 
			
			assertTrue(tree.isEmpty());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Tests if isEmpty works after inserting and then deleting multiple elements. 
	 * isEmpty should return true, else test fails.
	 */
	@Test
	public void test06isEmptyAfterInsertRemoveMany(){
		// Declare fields
		final int NUM_ITEMS_TO_TEST = 10; // the number of many elements to test
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
		
			for (int i = 0; i < NUM_ITEMS_TO_TEST; i++){
					tree.insert(i); // Inserts an element 
			}
			 
			for (int j = 0; j < NUM_ITEMS_TO_TEST; j++){
					tree.delete(j); // Removes element (should exist) 
			}
			
			assertTrue(tree.isEmpty());
		}	catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 *	Tests the tree's search function after one insert, 
	 *	searched for items should exist 
	 */
	@Test
	public void test07searchIsTrueAfterSingleInsert(){
		// Declare fields 
		final int ITEM_INSERT = 1; // the number to insert and search for
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try{
			tree.insert(ITEM_INSERT);
			assertTrue(tree.search(ITEM_INSERT));
		}	catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
			 
		
	}
	
	/**
	 *	Tests the tree's search function after multiple inserts, 
	 *	searched for item should exist 
	 */
	@Test
	public void test08searchIsTrueAfterMultipleInsert(){
		// Declare fields
		final int NUM_ITEMS_TO_TEST = 10; // the number of many elements to test
		final int NUM_TO_FIND = 1; // element to search for, must exist in insert range
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
		
			for (int i = 0; i < NUM_ITEMS_TO_TEST; i++){
				tree.insert(i); // Inserts an element 
			}
			
			assertTrue(tree.search(NUM_TO_FIND));
		}	catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 *	Tests the tree's search function after one insert, 
	 *	searched for items should not exist 
	 */
	@Test
	public void test09searchIsFalseAfterSingleInsert(){
		// Declare fields 
		final int ITEM_TO_INSERT= 1; // the number to insert, should not exist 
		final int ITEM_TO_SEARCH = 2; // the number to search, should not exist 
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try{
			tree.insert(ITEM_TO_INSERT);
			assertFalse(tree.search(ITEM_TO_SEARCH));
		}	catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
			 
		
	}
	
	/**
	 *	Tests the tree's search function after multiple inserts, 
	 *	searched for item should not exist. 
	 */
	@Test
	public void test10searchIsFalseAfterMultipleInsert(){
		// Declare fields
		final int NUM_ITEMS_TO_TEST = 10; // the number of many elements to test
		final int NUM_TO_FIND = 20; // element to search for, must not exist in insert range
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
		
			for (int i = 0; i < NUM_ITEMS_TO_TEST; i++){
				tree.insert(i); // Inserts an element 
			}
			
			assertFalse(tree.search(NUM_TO_FIND));
		}	catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Tests the tree's search function on an empty tree.
	 * Search function should return false.
	 */
	@Test 
	public void test11searchEmptyTree(){
		// Declare fields
		final int NUM_TO_FIND = 20; // element to search for
		AVLTree<Integer> tree = new AVLTree<Integer>();
		try {
			assertFalse(tree.search(NUM_TO_FIND));	
		}	catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Tests the tree's delete function on an empty tree,
	 * Exception should not be thrown.
	 */
	@Test
	public void test12deleteEmptyTree(){
		// Declare fields
		final int DELETE_ITEM = 0;
		AVLTree<Integer> tree = new AVLTree<Integer>();
		boolean noError = true; 
		
		try{
			tree.delete(DELETE_ITEM);
			
		}	catch (IllegalArgumentException e) {
			noError = false;
			System.out.println(e.getMessage());
		}
		
		assertTrue(noError);
	}
	
	/**
	 * Test multiple iterations of adding and removing elements from the tree
	 */
	@Test
	public void test13insertDeleteIterations(){
		// Declare fields
		final int ITERATION_MAX = 10; // number of times to iterate 
		final int ELEMENT_TO_TEST = 1; // element to insert and delete 
		int iterationCount = 0; // to keep track of iterations
		AVLTree<Integer> tree = new AVLTree<Integer>();
		boolean noError = true;
		
		try{
			while(iterationCount < ITERATION_MAX){
				tree.insert(ELEMENT_TO_TEST);
				tree.delete(ELEMENT_TO_TEST);
				iterationCount++;
			}
	
		}	catch (IllegalArgumentException e) {
			noError = false;
			System.out.println(e.getMessage());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			noError = false;
		}
		
		assertTrue(noError);
	}
	
}
