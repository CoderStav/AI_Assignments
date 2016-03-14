package babyNaming;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class NameGenerator {
	
	private Hashtable<String, Integer> dataset;
	private Hashtable<String, Hashtable<Character, Double>> markovModel;
	
	private Random rng;

	private int minNameLen;
	private int maxNameLen;
	private int order;
	
	/**
	 * fillDataset
	 * Reads data from file and inserts it line-by-line into the
	 * dataset Hashtable
	 * @param gender The gendered file of names to load: 0 - male, 1 - female
	 */
	private void fillDataset(int gender){
		
		this.dataset = new Hashtable<String, Integer>();
		
		MyFileReader nameFile = null;
		try {
			if(gender == 0)
				nameFile = new MyFileReader("src/babyNaming/namesBoys.txt");
			else
				nameFile = new MyFileReader("src/babyNaming/namesGirls.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		StringBuilder name;
		int readStatus = 0;
		int i = 0;
		while(readStatus != -1){
			name = new StringBuilder();
			try {
				readStatus = nameFile.readLine(name);
			} catch (IOException e) { 
				readStatus = -1;
			}
			this.dataset.put(name.toString().toLowerCase(), i++);
		}
		
		try {
			nameFile.close();
		} catch (IOException e) { }
	}
	
	/**
	 * buildMarkovModel
	 * Builds a Order-N Markov model in the markovModel Hashtable based on data
	 * in the dataset Hashtable
	 * @param gender
	 */
	private void buildMarkovModel(int gender){
		String chars = "abcdefghijklmnopqrstuvwxyz_";
		
		/*
		 * markovModel Hashtable
		 * 
		 * Structure:
		 * 	key - substrings of length two discovered in the dataset
		 * 	value - sub Hashtable
		 * 		key - character that follows the substring somewhere in the dataset
		 * 			- special key '#' (more on this below)
		 * 		value - number of times this character has followed the substring in the dataset
		 * 			  - in '#'s case, total number of iterations of the substring in the dataset
		 */
		this.markovModel = new Hashtable<String, Hashtable<Character, Double>>();
		
		String nameString;
		for(String name : this.dataset.keySet()){
			
			nameString = "";
			
			for(int i = 0; i < this.order; ++i)
				nameString += '_';
			
			nameString += name;
			
			for(int i = 0; i < this.order; ++i)
				nameString += '_';
			
			/*
			 * This loop reads all the two character substrings inside of a name
			 */
			for(int i = 0; i < nameString.length(); ++i){
				if(i >= nameString.length()-(this.order-1)) break;
				
				/*
				 * Getting the next character substring
				 */
				String characterSequence = new String();
				for(int j = 0; j < this.order; ++j)
					characterSequence += nameString.charAt(i+j);
				
				char followingChar = '_';
				try{
					followingChar = nameString.charAt(i+this.order);
				}catch(IndexOutOfBoundsException e){
					break;
				}
				
				/*
				 * If the sequence being tested isn't inside markovModel,
				 * add the key with an empty character probabilities table
				 * to the markovModel Hashtable. Else, add/iterate the
				 * count for the character in the existing probability table
				 * entry.
				 */
				if(!this.markovModel.containsKey(characterSequence)){
					Hashtable<Character, Double> probabilityTable = new Hashtable<Character, Double>();
					probabilityTable.put('#', 1.0);
					probabilityTable.put(followingChar, 1.0);
					this.markovModel.put(characterSequence, probabilityTable);
				}else{
					
					Hashtable<Character, Double> probabilityTable = this.markovModel.get(characterSequence);
					
					if(!probabilityTable.containsKey(followingChar))
						probabilityTable.put(followingChar, 1.0);
					else
						probabilityTable.put(followingChar, probabilityTable.get(followingChar) + 1);
					
					probabilityTable.put('#', probabilityTable.get('#') + 1);
				}
			}
		}
	}
	
	private Hashtable<Character, Double[]> getProbabilities(String charSequence){
		
		/*
		 * probabilities Hashtable
		 * 
		 * Structure:
		 * 	key - character (excluding '#') found in the probability table for charSequence within markovModel
		 * 	value - double array of length two representing a probability range for the key ([lo, hi])
		 */
		Hashtable<Character, Double[]> probabilities = new Hashtable<Character, Double[]>();
		
		if(!this.markovModel.containsKey(charSequence))
			return probabilities;
		
		Hashtable<Character, Double> probTable = this.markovModel.get(charSequence);
		Set<Character> keys = probTable.keySet();
		Double sum = probTable.get('#');
		Double total = 0.0;
		
		for(char key : keys){
			if(key != '#'){
				Double[] probabilityRange = new Double[2];
				probabilityRange[0] = total;
				probabilityRange[1] = (total += (probTable.get(key)/sum)*100);
				probabilities.put(key, probabilityRange);
			}
		}
		
		return probabilities;
	}
	
	private char nextCharacter(String charSequence){
		
		Hashtable<Character, Double[]> probabilities = this.getProbabilities(charSequence);
		
		/*
		 * Here a random double between 0.00 and 100.00 is generated
		 * this random value is then checked against the generated
		 * probability ranges for charSequence. The key of whichever range
		 * randRoll falls within is returned as the next character
		 */
		double randRoll = this.rng.nextDouble()*100;
		for(char key : probabilities.keySet()){
			Double[] range = probabilities.get(key);
			if(randRoll > (double) range[0] && randRoll < (double) range[1])
				return key;
		}
		
		return '_';
	}
	
	/**
	 * Constructor
	 * @param gender Gender of the names to be generated
	 * @param minNameLen Minimum length of the names to be generated
	 * @param maxNameLen Maximum length of the names to be generated
	 * @param markovOrder Order of the Markov chain
	 */
	public NameGenerator(String gender, int minNameLen, int maxNameLen, int markovOrder){
		
		this.rng = new Random(System.currentTimeMillis());
		
		this.order = markovOrder;
		this.minNameLen = minNameLen;
		this.maxNameLen = maxNameLen;
		
		if(gender.equals("male")){
			this.fillDataset(0);
			this.buildMarkovModel(0);
		}else if(gender.equals("female")){
			this.fillDataset(1);
			this.buildMarkovModel(1);
		}
		
	}
	
	/**
	 * generateName
	 * private helper method
	 * generates name according to NameGenerator's settings
	 * @param name Initial "seed" string
	 * @return Generated name
	 */
	private String generateName(String name){
		String charSequence = name;
		char nextChar;
		int i = 0;
		
		do{
			nextChar = this.nextCharacter(charSequence);
			name += nextChar;
			charSequence = "";
			++i;
			for(int j = 0; j < this.order; ++j)
				charSequence += name.charAt(i+j);
		}while(nextChar != '_');
		
		return name.substring((this.order-1), name.length()-1);
	}
	
	/**
	 * generateName
	 * generates name according to NameGenerator's settings
	 * @return Generated name
	 */
	public String generateName(){
		
		String chars = "abcdefghijklmnopqrstuvwxyz";
		String startString, generatedName;
		
		generatedName = "";
		while(generatedName.length() < this.minNameLen || generatedName.length() > this.maxNameLen || this.dataset.containsKey(generatedName)){
			try{
				
				startString = "";
				for(int i = 0; i < this.order-1; ++i)
					startString += '_';
				startString += chars.charAt(this.rng.nextInt(26));
				
				generatedName = this.generateName(startString);
				
			}catch(NullPointerException e){ 
				e.printStackTrace();
			}
		}
		
		return generatedName.toString();
	}
	
}
