package babyNaming;

import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		
		String gender; 
		int minNameLen, maxNameLen, numOfNames;
		minNameLen = maxNameLen = numOfNames = 0;
		gender = "";
		
		boolean validAnswer;
		
		Scanner userIn = new Scanner(System.in);
		
		validAnswer = false;
		while(!validAnswer){
			System.out.print("Male or Female?: ");
			String userAns = userIn.nextLine().toLowerCase();
			if(userAns.equals("male") || userAns.equals("female")){
				gender = userAns;
				validAnswer = true;
			}else
				System.out.println("Invalid answer.");
		}
		
		validAnswer = false;
		while(!validAnswer){
			System.out.print("Min name length: ");
			try{
				int userAns = userIn.nextInt();
				minNameLen = userAns;
				validAnswer = true;
			}catch(Exception e){
				System.out.println("Invalid answer.");
				userIn.nextLine();
			}
		}
		
		validAnswer = false;
		while(!validAnswer){
			System.out.print("Max name length: ");
			try{
				int userAns = userIn.nextInt();
				maxNameLen = userAns;
				validAnswer = true;
			}catch(Exception e){
				System.out.println("Invalid answer.");
				userIn.nextLine();
			}
		}
		
		validAnswer = false;
		while(!validAnswer){
			System.out.print("Number of names to generate: ");
			try{
				int userAns = userIn.nextInt();
				numOfNames = userAns;
				validAnswer = true;
			}catch(Exception e){
				System.out.println("Invalid answer.");
				userIn.nextLine();
			}
		}
		
		userIn.close();
	
		NameGenerator p = new NameGenerator(gender, minNameLen, maxNameLen);
		for(int i = 0; i < numOfNames; ++i)
			System.out.println(p.generateName());
		
		
	}
}
