/**
 * Filename:   Profile.java
 * Project:    p3
 * Authors:    Kimberly Inthavong, Lecture 001
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   10/29 at 10pm
 * Version:    1.0
 * 
 * Credits:    none
 * 
 * Bugs:       No known bugs
 */
import java.util.TreeMap;

public class Profile<K extends Comparable<K>, V> {

	HashTableADT<K, V> hashtable;
	TreeMap<K, V> treemap;
	
	public Profile() {
		// Instantiate hashtable and treemap
		hashtable = new HashTable();
		treemap = new TreeMap();
	}
	
	public void insert(K key, V value) {
		// Insert K, V into both hashtable and treemap
		
		hashtable.put(key, value);
		treemap.put(key, value);
		
	}
	
	public void retrieve(K key) {
		// get value V for key K from both hashtable and treemap
		
		hashtable.get(key);
		treemap.get(key);
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Expected 1 argument: <num_elements>");
			System.exit(1);
		}
		int numElements = Integer.parseInt(args[0]);
		
		/* 
		 * Create a profile object. 
		 * For example, Profile<Integer, Integer> profile = new Profile<Integer, Integer>();
		 * execute the insert method of profile as many times as numElements
		 * execute the retrieve method of profile as many times as numElements
		 * See, ProfileSample.java for example.
		 */
		Profile<Integer, Integer> profile = new Profile<Integer, Integer>();
		for (int i = 0; i < numElements; i++){
			profile.insert(i, i);
		}
		
		for (int j = 0; j < numElements; j++){
			profile.retrieve(j);
		}
		
		String msg = String.format("Successfully inserted and retreived %d elements into the hash table and treemap", numElements);
		System.out.println(msg);
	}
}
