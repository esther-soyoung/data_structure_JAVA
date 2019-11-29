/** 
 * Class RockPaperScissors. Plays repeated games of Rock Paper Scissors Lizard Spock with a user.
 * Rule:
 * Rock wins Scissors and Lizard.
 * Paper wins Rock and Spock.
 * Scissors win Paper and Lizard.
 * Lizard wins Paper and Spock.
 * Spock wins Rock and Scissors.
 * Tactic:
 * 1. If loses: Switch to the thing that beats the thing user just played.
 * 2. If wins: Switch to the thing that would beat the thing that system just played.
 * @author Soyoung Kang
 * @date Oct. 5th, 2017
 */

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;

public class RockPaperScissors
{
  /**
   * Helper method to get valid user input.
   * @return one letter String that represents among rock, paper, or scissors.
   */
  private static String getInput() {
	  String userInput = null;
	  boolean flag = false;
	  while (flag == false) {
		  try {
			  System.out.println("Let's play! What's your move? (r=rock, p=paper, s=scissors, li=lizard, sp=spock or q to quit)");     
			  Scanner sc = new Scanner(System.in);
			  userInput = sc.next();
			  if (!(userInput.equals("r") || userInput.equals("p") || userInput.equals("s") 
					  || userInput.equals("li") || userInput.equals("sp") || userInput.equals("q")))
				  throw new Exception();
			  flag = true;
		  }
		  // Re-prompt when invalid user input
		  catch (Exception e) {
			  System.out.println("That is not a valid move. Please try again.");
		  }
	  }
	  return userInput;
  }

  /**
   * Expands the capacity of string array that holds user's move history.
   * @param array String array that stores what user throws
   * @return expanded String array 
   */
  private static String[] expandArray(String[] array) {
	  int n = array.length;
	  String[] newArray = new String[2*n];
	  for (int i = 0; i < n; i++) {
		  newArray[i] = array[i];
	  }
	  return newArray;
  }
  
  /**
   * Randomly decides what the system will throw.
   * @return one letter String that represents rock, paper or scissors.
   */
  private static String randomMove() {
	  Random tmp = new Random();
	  int symbol = tmp.nextInt(5);
	  switch (symbol) {
	  case 0: return "r";
	  case 1: return "p";
	  case 2: return "s";
	  case 3: return "li";
	  case 4: return "sp";
	  }
	  return "";
  }
  
/**
 * Switch to the thing that beats the thing user just played.
 * @param userInput the previous move by user
 * @return one letter String that represents among rock, paper or scissors.
 */
  private static String afterLose(String userInput) {
	  Random tmp = new Random();
	  int symbol = tmp.nextInt(2);

	  if (userInput.equals("rock"))
		  switch (symbol) {
		  case 0: return "p";
		  case 1: return "sp";
		  }
	  else if (userInput.equals("paper"))
		  switch (symbol) {
		  case 0: return "s";
		  case 1: return "li";
		  }
	  else if (userInput.equals("scissors"))
		  switch (symbol) {
		  case 0: return "r";
		  case 1: return "sp";
		  }
	  else if (userInput.equals("lizard"))
		  switch (symbol) {
		  case 0: return "r";
		  case 1: return "s";
		  }
	  else if (userInput.equals("spock"))
		  switch (symbol) {
		  case 0: return "p";
		  case 1: return "li";
		  }
	  return "foo";
  }
  
  /**
   * Switch to the thing that would beat the thing that system just played.
   * @param userInput the previous move by user
   * @return one letter String that represents among rock, paper or scissors.
   */
  private static String afterWin(LinkedList<String> systemMoves) {
	  Random tmp = new Random();
	  int symbol = tmp.nextInt(2);
	  
	  if (systemMoves.getLast().equals("rock      "))
		  switch (symbol) {
		  case 0: return "p";
		  case 1: return "sp";
		  }
	  else if (systemMoves.getLast().equals("paper     "))
		  switch (symbol) {
		  case 0: return "s";
		  case 1: return "li";
		  }
	  else if (systemMoves.getLast().equals("scissors  "))
		  switch (symbol) {
		  case 0: return "r";
		  case 1: return "sp";
		  }
	  else if (systemMoves.getLast().equals("lizard    "))
		  switch (symbol) {
		  case 0: return "r";
		  case 1: return "s";
		  }
	  else if (systemMoves.getLast().equals("spock     "))
		  switch (symbol) {
		  case 0: return "p";
		  case 1: return "li";
		  }
	  return "foo";
  }

  /*
   * main
   */
  public static void main( String[] args )
  {	  
    int initialCapacity = 5;
    int round = 0;
    int s = 0;
    int u = 0;
    int t = 0;
    String sysInput = null;
    
    // Store the user's move history
    String[] userMoves = new String[initialCapacity];  
    
    // Store the System's move history
    LinkedList<String> systemMoves = new LinkedList<String>();
    
    // Store the results of each round
    LinkedList<String> results = new LinkedList<String>();
    
    // Run the game
    while (true) {
    	// Get user input
    	String userInput = getInput();
    	//System.out.println(userInput);
 
    	// Quit the game
    	if (userInput.equals("q")) {
    		System.out.println("Thanks for playing!");
    		
    		// Records of recent games in reverse
    		System.out.println("Our most recent games (in reverse order) were:");
    		if (round == 0)
    			System.out.println("We haven't played a single game.");
    		else {
    			if (round < 10) {
    				for (int i = 0; i < round; i++) {
    					System.out.println("Me: "+systemMoves.pollLast()+"  You: "+userMoves[round-1-i]);
    				}
    			}
    			else{
    				for (int i = 0; i < 10; i++) {
    						System.out.println("Me: "+systemMoves.pollLast()+"  You: "+userMoves[round-1-i]);
    				}	
    			}
    		}
    		
    		// Overall statistics
    		System.out.println("Our overall stats are:");
    		try {
    			System.out.println("I won: " + (s*100/round) + "%");
    			System.out.println("You won: " + (u*100/round) + "%");
    			System.out.println("We tied: " + (t*100/round) + "%");
    			System.exit(0); 
    		}
    		catch (Exception e) {
    			System.out.println("I won: " + (0) + "%");
    			System.out.println("You won: " + (0) + "%");
    			System.out.println("We tied: " + (0) + "%");
    			System.exit(0); 
    		}
    	}
    	
    	// Play the game
    	else {
    		round++;
    		
    		// Make a random move at the first round and the round after tie
    		if (results.isEmpty() || results.getLast().equals("t")) {
    			sysInput = randomMove();
    		}
    		
    		// Deploy tactic move based on the previous round
    		else if (results.getLast().equals("s")) {
    			sysInput = afterWin(systemMoves);
    		}
    		
    		else if (results.getLast().equals("u")) {
    			sysInput = afterLose(userMoves[round-2]);
    		}

    		// Record each move
    		try{
    			switch (userInput) {
        		case "r":
        			userMoves[round-1] = "rock";
        			break;
        		case "p":
        			userMoves[round-1] = "paper";
        			break;
        		case "s":
        			userMoves[round-1] = "scissors";
        			break;
        		case "li":
        			userMoves[round-1] = "lizard";
        			break;
        		case "sp":
        			userMoves[round-1] = "spock";
        			break;
        		}
    		}
    		catch (ArrayIndexOutOfBoundsException e) {
    			userMoves = expandArray(userMoves);
    			switch (userInput) {
        		case "r":
        			userMoves[round-1] = "rock";
        			break;
        		case "p":
        			userMoves[round-1] = "paper";
        			break;
        		case "s":
        			userMoves[round-1] = "scissors";
        			break;
        		case "li":
        			userMoves[round-1] = "lizard";
        			break;
        		case "sp":
        			userMoves[round-1] = "spock";
        			break;
        		}
    		}

    		switch (sysInput) {
    		case "r":
    			systemMoves.add("rock      ");
    			break;
    		case "p":
    			systemMoves.add("paper     ");
    			break;
    		case "s":
    			systemMoves.add("scissors  ");
    			break;
    		case "li":
    			systemMoves.add("lizard    ");
    			break;
    		case "sp":
    			systemMoves.add("spock     ");
    			break;
    			
    		}
    		
    		// Record who won
    		if (sysInput.equals("r") && userInput.equals("r")) {
    			t++;
    			results.addLast("t");
    			System.out.println("I choose rock. It's a tie.");
    		}	
    		else if (sysInput.equals("r") && userInput.equals("p")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose rock. You win.");
			}	
    		else if (sysInput.equals("r") && userInput.equals("s")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose rock. I win!");
			}
    		else if (sysInput.equals("r") && userInput.equals("li")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose rock. I win!");
			}
    		else if (sysInput.equals("r") && userInput.equals("sp")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose rock. You win.");
			}
    		
    		else if (sysInput.equals("p") && userInput.equals("r")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose paper. I win!");
			}	
    		else if (sysInput.equals("p") && userInput.equals("p")) {
    			t++;
    			results.addLast("t");
    			System.out.println("I choose paper. It's a tie.");
    		}
    		else if (sysInput.equals("p") && userInput.equals("s")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose paper. You win.");
    		}
    		else if (sysInput.equals("p") && userInput.equals("li")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose paper. You win.");
			}	
    		else if (sysInput.equals("p") && userInput.equals("sp")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose paper. I win!");
			}	
    		
    		else if (sysInput.equals("s") && userInput.equals("r")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose scissors. You win.");
    		}	
    		else if (sysInput.equals("s") && userInput.equals("p")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose scissors. I win!");
			}	
    		else if (sysInput.equals("s") && userInput.equals("s")) {
    			t++;
    			results.addLast("t");
    			System.out.println("I choose scissors. It's a tie.");
    		}
    		else if (sysInput.equals("s") && userInput.equals("li")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose scissors. I win!");
    		}	
    		else if (sysInput.equals("s") && userInput.equals("sp")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose scissors. You win.");
    		}
    		
    		else if (sysInput.equals("li") && userInput.equals("r")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose lizard. You win.");
    		}
    		else if (sysInput.equals("li") && userInput.equals("p")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose lizard. I win!");
    		}
    		else if (sysInput.equals("li") && userInput.equals("s")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose lizard. You win.");
    		}
    		else if (sysInput.equals("li") && userInput.equals("li")) {
    			t++;
    			results.addLast("t");
    			System.out.println("I choose lizard. It's a tie.");
    		}
    		else if (sysInput.equals("li") && userInput.equals("sp")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose lizard. I win!");
    		}
    		
    		else if (sysInput.equals("sp") && userInput.equals("r")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose spock. I win!");
    		}
    		else if (sysInput.equals("sp") && userInput.equals("p")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose spock. You win.");
    		}
    		else if (sysInput.equals("sp") && userInput.equals("s")) {
    			s++;
    			results.addLast("s");
    			System.out.println("I choose spock. I win!");
    		}
    		else if (sysInput.equals("sp") && userInput.equals("li")) {
    			u++;
    			results.addLast("u");
    			System.out.println("I choose spock. You win.");
    		}
    		else if (sysInput.equals("sp") && userInput.equals("sp")) {
    			t++;
    			results.addLast("t");
    			System.out.println("I choose spock. It's a tie.");
    		}
    	}	
    } 
  }
}
