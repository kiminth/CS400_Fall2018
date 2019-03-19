/**
 * Filename:   AVLTree.java
 * Project:    p2
 * Authors:    Debra Deppeler, Kimberly Inthavong
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    001
 * 
 * Due Date:   10/01/18
 * Version:    1.0
 * 
 * Credits:    None
 * 
 * Bugs:       no known bugs, but not complete either
 */

import java.lang.IllegalArgumentException;

/**
 * This class creates a balanced AVL tree that can insert, delete,
 * search, and create a printable string of all tree nodes.
 * Implements AVLTreeADT, using BTSNode
 * 
 * @param <K>
 */
public class AVLTree<K extends Comparable<K>> implements AVLTreeADT<K> {

	// Declare fields
	private BSTNode<K> root; // root of the BST
	
	/** 
	 * Represents a tree node.
	 * Creates an node that stores the key and 
	 * the left and right child of the node.
	 * Used for AVLTree.
	 * 
	 * @param <K>
	 */
	class BSTNode<K> {
		/* fields */
		private K key;	// Data stored in the node
		private int height;	// Height of the tree
		private BSTNode<K> left, right;	// Right and left child of current node
		
		/**
		 * Constructor for a BST node.
		 * @param key
		 */
		BSTNode(K key) {
			this.key = key;
			this.left = null;
			this.right = null;
			this.height = 1;
		}

		/* accessors */
	
		/**
	     * Gets left child of node
	     * 
	     * @return left
	     */
		public BSTNode<K> getLeftChild() {
			return this.left;
		}
		
		/**
	     * Gets right child of node
	     * 
	     * @return right
	     */
		public BSTNode<K> getRightChild() {
			return this.right;
		}
		
		/**
		 * Gets the key of the node
		 * 
		 * @param key
		 */
		public K getKey(){
			return this.key;
		}
		
		/**
		 * Gets the height of the node
		 * 
		 * @param height
		 */
		public int getHeight(){
			return this.height;
		}
		
		/* mutators */
		
		/**
		 * Sets the right child of node
		 * 
		 * @param right
		 */
		public void setRightChild(BSTNode<K> right) {
			this.right = right;
		}
		
		/**
		 * Sets the left child of node
		 * 
		 * @param left
		 */
		public void setLeftChild(BSTNode<K> left) {
			this.left = left;
		}
		
		/**
		 * Sets the key of the node
		 * 
		 * @param key
		 */
		public void setKey(K key){
			this.key = key;
		}
		
		/**
		 * Sets the height of the node
		 * 
		 * @param height
		 */
		public void setHeight(int height){
			this.height = height;
		}
		
	}
	
	/**
	 * Checks if the tree is empty
	 * 
	 * @return true if the tree if empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		if (root == null){
			return true;
		}
		return false;
	}

	/**
	 * Adds key to the AVL tree
	 * 
	 * @param key
	 * 
	 * @throws DuplicateKeyException if key is already in the AVL tree
	 * @throws IllegalArgumentException if null value inserted
	 */
	@Override
	public void insert(K key) throws DuplicateKeyException, IllegalArgumentException {
		
		// If the tree is empty, initialize the root
		if (isEmpty()){
			root = new BSTNode<K>(key);
			return;
		}
		
		try {
			insertHelper(root, key);
		} catch (DuplicateKeyException e){
			throw new DuplicateKeyException();
		} catch (IllegalArgumentException e){
			throw new IllegalArgumentException();
		} 
	}
	
	/**
	 * This method traverses (recurrsively) down the tree till
	 * the end it reaches the end (depending on right/left, comparing 
	 * key)
	 * 
	 * @param BSTNode<k> node
	 * @param K key
	 * @param int heightCount
	 * 
	 * @throws DuplicateKeyException if key is already in the AVL tree
	 */
	public BSTNode<K> insertHelper(BSTNode<K> node, K key) throws DuplicateKeyException {
		
		// Once current node is null, reaching the end of the tree
		if (node == null){
			node = new BSTNode<K>(key);
			return node;
		}
		
		// Checks for a duplicate key while traversing down the tree
		// Throws an exception if duplicate is found
		if (node.getKey().compareTo(key) == 0){
			throw new DuplicateKeyException();
		}
		
		// Traverse left
		if (node.getKey().compareTo(key) > 0){
			node.setLeftChild(insertHelper(node.getLeftChild(), key));
		} 
		// Traverse right
		else if (node.getKey().compareTo(key) < 0){
			node.setRightChild(insertHelper(node.getRightChild(), key));
		} 
		else {
			return node;
		}
		
        // Update height of the given node
        node.setHeight(1 + getMax(node.getLeftChild().height, node.getRightChild().height));
  
        // Check balance factor of node
        int balance = getBalance(node); 
  
        // If it is unbalanced, rebalance: 
      
        // Left Left rotation
        if (balance > 1 && key.compareTo(node.getLeftChild().getKey()) < 0) 
            return rightRotate(node); 
  
        // Right Right rotation
        if (balance < -1 && key.compareTo(node.getRightChild().getKey()) > 0) 
            return leftRotate(node); 
  
        // Left Right rotation
        if (balance > 1 && key.compareTo(node.getLeftChild().getKey()) > 0) { 
            node.left = leftRotate(node.left); 
            return rightRotate(node); 
        } 
  
        // Right Left rotation 
        if (balance < -1 && key.compareTo(node.getRightChild().getKey()) < 0) { 
            node.right = rightRotate(node.right); 
            return leftRotate(node); 
        } 
  
        return node; 
	}

	/**
	 * Deletes key from the AVL tree
	 * 
	 * @param key
	 * @throws IllegalArgumentException if try to delete null
	 */
	@Override
	public void delete(K key) throws IllegalArgumentException {
		
		if (key == null){
			throw new IllegalArgumentException();
		}
		
		root = deleteHelper(root, key); // Call helper method
		
	}

	/**
	 * This method is a helper method that recursively traverses the 
	 * AVL tree to delete the node containing the key to be deleted
	 * 
	 * @param node
	 * @param key
	 * @return node
	 */
	private BSTNode<K> deleteHelper(BSTNode<K> node, K key) {
	
		if (node == null) {
	        return node; // Key to delete was not found
	    }
		
		 // Traverse left
	    if (node.getKey().compareTo(key) > 0) {
	        node.setLeftChild( deleteHelper(node.getLeftChild(), key) );
	        return node;
	    }
	    // Traverse right
	    else if (node.getKey().compareTo(key) < 0) {
	        node.setRightChild( deleteHelper(node.getRightChild(), key) );
	        return node;
	    }
	    
	    else if (node.getKey().equals(key)) {
	        // node is the node to be removed
	        if (node.getLeftChild() == null && node.getRightChild() == null) {
	            return null;
	        }
	        
	        if (node.getLeftChild() == null) {
	        	node.getRightChild().setHeight(node.getHeight());
	            return node.getRightChild();
	        }
	        if (node.getRightChild() == null) {
	        	node.getLeftChild().setHeight(node.getHeight());
	            return node.getLeftChild();
	        }
	       
	        // if we get here, then node has 2 children
	        // Calls helper method to find smallest node in the right subtree
	        // to replace node 
	        K smallVal = smallestHelper(node.getRightChild()); 
	        node.setKey(smallVal);
	        node.setRightChild( deleteHelper(node.getRightChild(), smallVal) );
	        return node; 
	    }
	    
        // Update height of the given node
        node.setHeight(1 + getMax(node.getLeftChild().height, node.getRightChild().height));
  
        // Check balance factor of node
        int balance = getBalance(node); 
  
        // If it is unbalanced, rebalance: 
        
        // Left Left rotation  
        if (balance > 1 && getBalance(node.getLeftChild()) >= 0)  
            return rightRotate(node);   
  
        // Right Right rotation  
        if (balance < -1 && getBalance(node.getRightChild()) <= 0)  
            return leftRotate(node);  
  
        // Left Right rotation 
        if (balance > 1 && getBalance(node.getLeftChild()) < 0)  
        {  
            node.setLeftChild(leftRotate(node.getLeftChild()));  
            return rightRotate(node);  
        } 
        
        // Right Left rotation
        if (balance < -1 && getBalance(node.getRightChild()) > 0)  
        {  
            node.setRightChild(rightRotate(node.getRightChild()));  
            return leftRotate(node);  
        }  
  
        return node;  
	    
	}

	/**
	 * Search for a key in AVL tree
	 * @param key
	 * @return true if AVL tree contains that key
	 * @throws IllegalArgumentException if searching for a null value
	 */
	@Override
	public boolean search(K key) throws IllegalArgumentException {
		return searchHelper(root, key); // Call helper method
	}
	
	/**
	 * This helper method recursively traverses the tree to search for
	 * the given key to find
	 * 
	 * @param node
	 * @param K key being searched for
	 * @return boolean is true if key is found, else false
	 */
	public boolean searchHelper(BSTNode<K> node, K key){
		// If the key is found, return true
		if (node.getKey().compareTo(key) == 0){
			return true;
		}
		
		// Traverse left 
		if (node.getKey().compareTo(key) > 0){
			if (node.getLeftChild() != null){
				return searchHelper(node.getLeftChild(), key);
			}
		} 
		
		// Traverse right
		if (node.getKey().compareTo(key) < 0){
			if (node.getRightChild() != null){
				return searchHelper(node.getRightChild(), key);
			}	
		}
		
		return false;
	}

	/**
	 * Performs in-order traversal of AVL Tree, with the assistance of helper method
	 * 
	 * @return a String with all the keys, in order, with exactly one space between keys
	 */
	@Override
	public String print() {
		// Declare variables
		String list = "";
		list = printHelper(root, list);
		
		return list;
	}
	
	/**
	 * This helper method traverses the node tree, storing all the 
	 * elements in a string, in order
	 * 
	 * @param node
	 * @param list
	 * @return list is String with all keys in order, with one space between keys
	 */
	public String printHelper(BSTNode<K> node, String list) {
		// Once current node is null, reaching end of tree
		if (node == null){
			return list.trim();
		}
		
		// Travel down left branches
		if (node.getLeftChild() != null){
			printHelper(node.getLeftChild(), list);
		}
		
		// Visit the node
		list += node.getKey() + " ";
		
		// Travel down right branches
		if (node.getRightChild() != null){
			printHelper(node.getRightChild(), list);
		}
		
		return list;
		
	}


	/**
	 * Searches for the smallest value node in the subtree of the given node
	 * 
	 * @param node
	 * @return the smallest value in the subtree rooted at n
	 */
	private K smallestHelper(BSTNode<K> node){ 
		 if (node.getLeftChild() == null) {
		        return node.getKey();
		 } else {
		        return smallestHelper(node.getLeftChild());
		 }
	}
	
	/**
	 * Searches for the largest value node in the subtree of the given node
	 * 
	 * @param node
	 * @return the largest value in the subtree rooted at n
	 */
	private K largestHelper(BSTNode<K> node){ 
		 if (node.getRightChild() == null) {
		        return node.getKey();
		 } else {
		        return smallestHelper(node.getRightChild());
		 }
	}
	/**
	 * Calculates balance factor of node n
	 * If it is > 1, then it is left heavy, but it is < -1, it is right heavy 
	 * 
	 * @param node
	 * @return
	 */
    private int getBalance(BSTNode<K> node) { 
        if (node == null) 
            return 0; 
  
        return node.getLeftChild().getHeight() - node.getRightChild().getHeight(); 
    } 
	
	/**
	 * Finds the maxiumum of two integers
	 * 
	 * @param a
	 * @param b
	 * @return the larger of the two passed in integers
	 */
	private int getMax(int a, int b){
		if (a >= b){
			return a;
		} else {
			return b;
		}
	}
	
	/** Rotates node a to the left, making its right child into its parent
	 * 
	 * @param grandparent node to rotate
	 * @return parent
	 */
	private BSTNode<K> leftRotate(BSTNode<K> node){
		 // Declare variables 
		BSTNode<K> g = node;
		BSTNode<K> p = g.getRightChild();
		BSTNode<K> temp = p.getLeftChild();
		
		// Rotate left
		g.setRightChild(temp);
		p.setRightChild(g);
		
		// Update heights
        g.height = getMax(g.left.getHeight(), g.right.getHeight()) + 1; 
        p.height = getMax(p.left.getHeight(), p.right.getHeight()) + 1; 
        
		return p;
	}
	
	/** Rotates node a to the right, making its left child into its parent
	 * 
	 * @param grandparent node to rotate
	 * @return parent
	 */
	private BSTNode<K> rightRotate(BSTNode<K> node){
		 // Declare variables 
		BSTNode<K> g = node;
		BSTNode<K> p = g.getLeftChild();
		BSTNode<K> temp = p.getRightChild();
		
		// Rotate right
		g.setLeftChild(temp);
		p.setLeftChild(g);
		
		// Update heights
        g.height = getMax(p.left.getHeight(), p.right.getHeight()) + 1; 
        p.height = getMax(g.left.getHeight(), g.right.getHeight()) + 1; 
        
		return p;
	}
	
	/**
	 * Checks for the Balanced Search Tree.
	 * 
	 * @return true if AVL tree is balanced tree
	 */
	@Override
	public boolean checkForBalancedTree() {
		return checkForBalancedTreeHelper(root); //Call helper method
	}
	
	/**
	 * Recursively check that each node of the tree is balanced
	 * 
	 * @param node
	 * @return boolean true if tree is all balanced, else false
	 */
	public boolean checkForBalancedTreeHelper(BSTNode<K> node) {
		if (node == null){
			return true;
		}
		
		// Travel down left branches
		if (node.getLeftChild() != null){
			checkForBalancedTreeHelper(node.getLeftChild());
		}
		
		// Visit the node
		int balanced = getBalance(node);
		if (balanced < -1 || balanced > 1){
			return false;
		}
		
		// Travel down right branches
		if (node.getRightChild() != null){
			checkForBalancedTreeHelper(node.getRightChild());
		}
		
		return true;
	}

	/**
	 * Checks for the Binary Search Tree.
	 * @return true if AVL tree is balanced tree
	 */
	@Override
	public boolean checkForBinarySearchTree() {
		// Call helper function
		return checkForBinarySearchTreeHelper(root);
	}
	
	/**
	 * Checks for the Binary Search Tree by recursively preorder traversal,
	 * checking each node's smallest value in the right subtree and largest
	 * value in the left subtree to the passed in node
	 * 
	 * @return true if AVL tree is balanced tree
	 */
	private boolean checkForBinarySearchTreeHelper(BSTNode<K> node) {
		if (node == null){
			return true;
		}
		
		// Checks if the smallest value in the right subtree is greater than the node,
		// and if the largest value in the left subtree is less than the node,
		// if so, then false
		if (node.getRightChild() != null && node.getLeftChild() != null){
			if ((smallestHelper(node.getRightChild()).compareTo(node.getKey()) < 0)
					|| (largestHelper(node.getLeftChild()).compareTo(node.getKey()) > 0)){
				return false;
			}
		}
			
		// Travel down left branches
		if (node.getLeftChild() != null){
			checkForBinarySearchTreeHelper(node.getLeftChild());
		}
	
		// Travel down right branches
		if (node.getRightChild() != null){
			checkForBinarySearchTreeHelper(node.getRightChild());
		}
		
		return true;
	}
}
