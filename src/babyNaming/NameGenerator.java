package babyNaming;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class NameGenerator {
	
	private Random rng;
	private Hashtable<String, Hashtable<Character, Double>> markovModel;
	private int minNameLen;
	private int maxNameLen;
	
	/*
	 * buildMarkovModel
	 * builds a Markov model from a dataset
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
		
		StringBuilder name = new StringBuilder();
		String nameString = new String();
		int readStatus;
		
		try{
			readStatus = nameFile.readLine(name);
		}catch(IOException e){
			readStatus = -1;
		}
		
		while(readStatus != -1){
			nameString = "__" + name.toString().toLowerCase() + "__";
			
			/*
			 * This loop reads all the two character substrings inside of a name
			 */
			for(int i = 0; i < nameString.length(); ++i){
				if(i >= nameString.length()-1) break;
				
				/*
				 * Getting the next two character substring
				 */
				String characterSequence = new String();
				characterSequence += nameString.charAt(i);
				characterSequence += nameString.charAt(i+1);
				
				/*
				 * If the sequence being tested isn't inside markovModel,
				 * add the key with an empty character probabilities table
				 * to the markovModel Hashtable. Else, add/iterate the
				 * count for the character in the existing probability table
				 * entry.
				 */
				if(!this.markovModel.containsKey(characterSequence)){
					Hashtable<Character, Double> probabilities = new Hashtable<Character, Double>();
					probabilities.put('#', 1.0); // since we know something was found for this substring we can set the counter entry to 1
					this.markovModel.put(characterSequence, probabilities);
				}else{
					char followingChar = nameString.charAt(i+2);
					
					Hashtable<Character, Double> probabilityTable = this.markovModel.get(characterSequence);
					
					if(!probabilityTable.containsKey(followingChar))
						probabilityTable.put(followingChar, 1.0);
					else
						probabilityTable.put(followingChar, probabilityTable.get(followingChar) + 1);
					
					probabilityTable.put('#', probabilityTable.get('#') + 1);
				}
				
			}
			
			/*
			 * After reading all the substrings in the current name,
			 * we read the next line from file and loop.
			 */
			name = new StringBuilder();
			try{
				readStatus = nameFile.readLine(name);
			}catch(IOException e){
				readStatus = -1;
			}			
		}
		
		try{
			nameFile.close();
		}catch(IOException e){ }
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
	
	public NameGenerator(String gender, int minNameLen, int maxNameLen){
		
		this.rng = new Random(System.currentTimeMillis());
		
		this.minNameLen = minNameLen;
		this.maxNameLen = maxNameLen;
		
		if(gender.equals("male"))
			this.buildMarkovModel(0);
		else if(gender.equals("female"))
			this.buildMarkovModel(1);
	}
	
	private String generateName(String name){
		String charSequence = name;
		char nextChar;
		int i = 0;
		do{
			nextChar = this.nextCharacter(charSequence);
			name += nextChar;
			charSequence = "";
			charSequence += name.charAt(++i);
			charSequence += name.charAt(i+1);
		}while(nextChar != '_');
		
		return name.substring(1, name.length()-1);
	}
	
	public String generateName(){
		String chars = "abcdefghijklmnopqrstuvwxyz";
		String name, generatedName;
		
		generatedName = "";
		while(generatedName.length() < this.minNameLen || generatedName.length() > this.maxNameLen){
			try{
				generatedName = this.generateName("_" + chars.charAt(this.rng.nextInt(26)));
			}catch(NullPointerException e){ }
		}
		
		return generatedName;
	}
	
}
