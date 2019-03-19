/**
 * Filename:   HashTable.java
 * Project:    p3
 * Authors:    Kimberly Inthavong, Lecture 001
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   10/29 at 10pm
 * Version:    1.0
 * 
 * Credits:    None
 * 
 * Bugs:       None
 */

import java.util.NoSuchElementException;
import java.util.ArrayList;


// DO NOT ADD PUBLIC MEMBERS TO YOUR CLASS
//
// Collision resolution scheme:
// In this program, collisions are handled by using Buckets,
// specificilly ArrayList as the data structure of the buckets
//
// Hashing Algorithm: 
// All elements are stored in a Hash table that consists 
// of an array of array lists that store a hash node. 
//
// NOTE: you are not required to design your own algorithm for hashing,
//       you may use the hashCode provided by the <K key> object
//       
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
	// Declare fields
	private static final int INITIAL_CAPACITY = 11; // Initial capacity for default constructor
	private static final double INITIAL_LOAD_FACTOR = 1; // Initial load factor for default constructor 
	private int capacity; // Current capacity of the hashTable
	private double loadFactor;	// Load factor of the hashTable
	private int numItems; // Number of items in the hashTable
	private ArrayList<HashNode>[] hashTable; // hashTable (array of array lists)
	
	/**
	 * This class is to create a node that will be stored in the hashTable.
	 * Node contains the key and value of element.
	 * 
	 */
	private class HashNode{
		private V value;
		private K key;
		
		HashNode (V value, K key){
			this.value = value;
			this.key = key;
		}
		
		public V getValue(){
			return this.value;
		}
		
		public K getKey(){
			return this.key;
		}
	}
	
	// Default no-arg constructor
	public HashTable() {
		this(INITIAL_CAPACITY, INITIAL_LOAD_FACTOR);
	}
	
	// Constructor that accepts initial capacity and load factor
	public HashTable(int initialCapacity, double loadFactor) {
		this.capacity = initialCapacity;
		this.loadFactor = loadFactor;
		this.numItems = 0;
		this.hashTable = new ArrayList[initialCapacity];
	}

	/**
	 * This method generates a hash code based on the key and
	 * capacity of hashTable 
	 * 
	 * @param K key
	 * @return
	 */
	private int hash(K key) {
		return capacity % key.hashCode();
	}
	
	/**
	 * Calculates the current load factor of the hash table
	 * 
	 * @return int loadFactor 
	 */
	private int calculateLoadFactor(){
		return numItems / capacity;
	}
	
	/**
	 * Resizes the hashTable 
	 * 
	 */
	private void resize(){
		// Declare fields
		HashNode temp;
		int tempHashCode;
		
		// Calculate next prime number for larger capacity 
		int newCapacity = capacity * 2;
		capacity = newCapacity;
		// Nearest prime greater than double the current capacity
		while (!isPrimeNumber(newCapacity)){
			newCapacity++; 
		}
		
		// Create new hashTable with new capacity
		ArrayList<HashNode>[] newHashTable = new ArrayList[newCapacity];
		
		// Rehash all items items in old hashTable to new hashTable
		for (int i = 0; i < hashTable.length; i++){
			for (int j = 0; j < hashTable[i].size(); j++){
				if (hashTable[i].get(j) != null){
					// Insert item into new hashTable after rehashing
					temp = hashTable[i].get(j);
					tempHashCode = hash(temp.getKey());
					newHashTable[tempHashCode].add(temp);
					
				}
			}
		}
		
		// Reassign to new hash table
		hashTable = newHashTable;
	}
	
	/**
	 * Checks if the passed in value is a prime number
	 * 
	 * @param n
	 * @return boolean
	 */
	private boolean isPrimeNumber(int n)  { 
        if (n <= 1) 
            return false; 
       
        // Check from 2 to n-1 
        for (int i = 2; i < n; i++) 
            if (n % i == 0) 
                return false; 
       
        return true; 
    } 
	
	/**
	 * This method inserts a value into the hashTable
	 * 
	 * @param K key
	 * @param V value
	 */
	@Override
	public void put(K key, V value) throws IllegalArgumentException {
		// Check if parameters are valid
		if (key == null|| value == null){
			throw new IllegalArgumentException();
		}
		
		// Create new hashNode to insert into table
		HashNode node = new HashNode(value, key);
		hashTable[hash(key)].add(node); 
		numItems++;
		
		// Check load factor to determine if resizing is needed
		if (calculateLoadFactor() >= loadFactor){
			resize();
		}
	
	}
	
	/**
	 * This method retrieves the value of the requested key. 
	 * Returns null if the requested key is not found.
	 * 
	 * @param K key
	 * @return V value
	 */
	@Override
	public V get(K key) throws IllegalArgumentException, NoSuchElementException {
		// Declare fields
		HashNode temp;
		
		for (int i = 0; i < hashTable[hash(key)].size(); i++){
			temp = hashTable[hash(key)].get(i);	
			if (temp.getKey().compareTo(key) == 0){
				return temp.getValue();
			}
		}
		
		return null;
	}

	/**
	 * Removes requested key from hash table
	 * 
	 * @param k key 
	 */
	@Override
	public void remove(K key) throws IllegalArgumentException, NoSuchElementException {
		HashNode temp;
		Boolean exists = false;
		
		for (int i = 0; i < hashTable[hash(key)].size(); i++){
			temp = hashTable[hash(key)].get(i);	
			if (temp.getKey().compareTo(key) == 0){
				hashTable[hash(key)].remove(i);
				numItems--;
				exists = true; // confirms the removal of the object
			}
		}
		
		if (exists){
			throw new NoSuchElementException();
		}
		
	}
	
	/**
	 * This method returns the size of the hashTabe
	 * 
	 * @return int size
	 */
	@Override
	public int size() {
		return this.capacity;
	}
		
}
