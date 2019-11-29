import java.io.*;
import java.nio.*;
import java.util.*;

public class SpellChecker {
    private HashTable dictionary;

    /** Constructor with no argument 
     * Initializes HashTable instance
     */
    public SpellChecker(){
        dictionary = new HashTable(1);
    }

    /** Loads the words in the file into the hash table dictionary of the SpellChecker.
     * This method can do anything reasonable for improperly formatted files.
     * @param dictFileReader Reader object for a dictionary file
     */
    public void readDictionary(Reader dictFileReader){
        // Read words from dictFileReader into dictionary
        Scanner sc;
        try{
            sc = new Scanner(dictFileReader);
            while (sc.hasNext()){
                String word = sc.next().toLowerCase();
                dictionary.insert(word);
            }
            sc.close();
        }
        catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
        catch (IllegalStateException e){
            System.out.println(e.getMessage());
        }
    }

    /** Check the input word.
     * If input word exists in the dictionary, returns null
     * @param word to check
     * @return array of possible suggestions for the misspelled word
     */
    public String[] checkWord(String word){
        //		System.out.println(word +" got into checkWord method");

        // Set up
        word.toLowerCase().trim();
        //		System.out.println("succesfully setup");

        // Correct input word
        if (dictionary.contains(word)){
            //			System.out.println("correct word");
            return null;
        }

        // Give suggestions for incorrect input
        else{
            //			System.out.println(word + "not a correct word. look for suggestions.");
            LinkedList<String> suggestions = new LinkedList<String>();
            String suggest_word = null;
            //			System.out.println("successfully setup linkedlist.");

            /* A wrong letter error*/
            for (int i = 0 ; i < word.length(); i++){
                //				System.out.println("looking for a wrong letter error");
                String part1;
                String part2;
                if (i == 0){
                    part1 = "";
                    //					System.out.println("Substring 1 setup");

                    if ((i+1) >= word.length()){
                        part2 = "";
                        //						System.out.println("Substring 2 setup");
                    }
                    else{
                        part2 = word.substring(i+1);
                        //						System.out.println("Substring 2 setup");
                    }
                }
                else{
                    part1 = word.substring(0, i);
                    //					System.out.println("Substring 1 setup");

                    if ((i+1) >= word.length()){
                        part2 = "";
                        //						System.out.println("Substring 2 setup");
                    }
                    else{
                        part2 = word.substring(i+1);
                        //						System.out.println("Substring 2 setup");
                    }
                }

                for (char j = 'a'; j <= 'z'; j++){
                    //					System.out.println("trying all possible alphabets");
                    suggest_word = part1.concat(Character.toString(j)).concat(part2);
                    //					System.out.println(suggest_word);

                    //					System.out.println(dictionary.contains(suggest_word));
                    if (dictionary.contains(suggest_word)){
                        //						System.out.println("Found suggestion. Now adding to the suggestions list");
                        if (!suggestions.contains(suggest_word))
                            suggestions.add(suggest_word);
                        //						System.out.println("A wrong letter suggestion for "+word+": "+suggest_word);
                    }	
                }
            }

            /* An inserted letter error*/
            if (word.length() > 1){
                for (int i = 0 ; i < word.length(); i++){
                    //					System.out.println("looking for an inserted letter error");
                    String part1, part2;
                    if (i == 0){
                        part1 = "";
                        //						System.out.println("Setup substring 1");

                        if ((i+1) >= word.length()){
                            part2 = "";
                            //							System.out.println("Setup substring 2");
                        }
                        else{
                            part2 = word.substring(i+1);
                            //							System.out.println("Setup substring 2");
                        }
                    }
                    else{
                        part1 = word.substring(0, i);
                        //						System.out.println("Setup substring 1");

                        if ((i+1) >= word.length()){
                            part2 = "";
                            //							System.out.println("Setup substring 2");
                        }
                        else{
                            part2 = word.substring(i+1);
                            //							System.out.println("Setup substring 2");
                        }
                    }

                    suggest_word = part1.concat(part2);
                    if (dictionary.contains(suggest_word)){
                        //					System.out.println("Found suggestion. Now adding to the suggestions list");
                        if (!suggestions.contains(suggest_word))
                            suggestions.add(suggest_word);
                        //					System.out.println("An inserted letter suggestion for "+word+": "+suggest_word);
                    }
                }
            }

            /* A deleted letter error*/
            // Missing first letter
            for (char ch = 'a'; ch <= 'z'; ch ++){
                //				System.out.println("looking for a deleted letter error, first letter missing");
                suggest_word = Character.toString(ch).concat(word);
                //				System.out.println("suggest_word formed successfully.");

                //				System.out.println(dictionary.contains(suggest_word));
                if (dictionary.contains(suggest_word)){
                    //					System.out.println("Found suggestion. Now adding to the suggestions list");
                    if (!suggestions.contains(suggest_word))
                        suggestions.add(suggest_word);
                    //					System.out.println("A deleted letter suggestion 1.missing first for "+word+": "+suggest_word);
                }
            }

            // Missing middle letter
            for (int i = 1; i < word.length(); i++){
                //				System.out.println("looking for a deleted letter error, middle letter missing");
                String part1 = word.substring(0, i);
                String part2 = word.substring(i);
                //				System.out.println("Substrings all set");

                for (char ch = 'a'; ch <= 'z'; ch++){
                    suggest_word = part1.concat(String.valueOf(ch)).concat(part2);
                    //					System.out.println(suggest_word);
                    //					System.out.println(dictionary.contains(suggest_word));
                    if (dictionary.contains(suggest_word)){
                        //						System.out.println("Found suggestion. Now adding to the suggestions list");
                        //						System.out.println(suggest_word);
                        if (!suggestions.contains(suggest_word))
                            suggestions.add(suggest_word);
                        //						System.out.println("A deleted letter suggestion 3.missing middle for "+word+": "+suggest_word);
                    }
                }
            }

            // Missing last letter
            for (char ch = 'a'; ch <= 'z'; ch ++){
                //				System.out.println("looking for a deleted letter error, last letter missing");
                suggest_word = word.concat(Character.toString(ch));
                //				System.out.println("suggest_word formed successfully.");

                //				System.out.println(dictionary.contains(suggest_word));
                if (dictionary.contains(suggest_word)){
                    //					System.out.println("Found suggestion. Now adding to the suggestions list");
                    if (!suggestions.contains(suggest_word))
                        suggestions.add(suggest_word);
                    //					System.out.println("A deleted letter suggestion 2.missing last for "+word+": "+suggest_word);
                }
            }

            /* A pair of adjacent transposed letters*/
            for (int i = 0 ; i < word.length()-1; i++){
                //				System.out.println("looking for transposed letter error");
                String part1, part2;
                if (i == 0){
                    part1 = "";

                    if ((i+2) >= word.length()){
                        part2 = "";
                    }
                    else{
                        part2 = word.substring(i+2);
                    }
                }
                else{
                    part1 = word.substring(0, i);

                    if ((i+2) >= word.length()){
                        part2 = "";
                    }
                    else{
                        part2 = word.substring(i+2);
                    }
                }
                //				System.out.println("Substrings all set");

                suggest_word = part1.concat(String.valueOf(word.charAt(i+1))).concat(String.valueOf(word.charAt(i))).concat(part2);

                //				System.out.println(dictionary.contains(suggest_word));
                if (dictionary.contains(suggest_word)){
                    //					System.out.println("Found suggestion. Now adding to the suggestions list");
                    if (!suggestions.contains(suggest_word))
                        suggestions.add(suggest_word);
                    //					System.out.println("A pair of adjacent transposed letters suggestion for "+word+": "+suggest_word);
                }
            }

            /* Every pair of strings that can be made by inserting a space into the word*/
            for (int i = 1 ; i < word.length(); i++){
                //				System.out.println("looking for a space omission error");
                String part1 = word.substring(0, i);
                String part2 = word.substring(i);
                //				System.out.println("Substrings all set");

                if (dictionary.contains(part1) && dictionary.contains(part2)){
                    if (!suggestions.contains(part1+" "+part2))
                        suggestions.add(part1+" "+part2);
                    //					System.out.println("Found suggestion. Now adding to the suggestions list");
                }
            }

            // Generate String[] from LinkedList of suggestions
            String[] final_suggestions = null;
            try{
                final_suggestions = suggestions.toArray(new String[suggestions.size()]);
                //				System.out.println("Successfully converted to array");
            }
            catch (ArrayStoreException e){
                System.out.println("Error in converting to array");
            }
            catch (NullPointerException e){
                System.out.println("Error in converting to array");
            }

            //			System.out.print("Final suggestions for "+word+": ");
            //			for (int i = 0; i < final_suggestions.length; i++)
            //				System.out.print(final_suggestions[i]+", ");
            //			System.out.println();

            return final_suggestions;
        }
    }

    public static void main(String[] args){
        SpellChecker spellChecker = new SpellChecker();
//		String path = "../resource/asnlib/";
        File dictionary = new File("longdict.txt");
        try {
            Reader reader = new FileReader( dictionary );
            spellChecker.readDictionary( reader ); // Loads the dictionary.
//	        spellChecker.dictionary.printTable();
        } catch ( FileNotFoundException e ) {
            System.err.println( "Failed to open " + dictionary );
            e.printStackTrace(); // Error getting the dictionary.
            System.exit( 1 );
        }
        File inputFile = new File("input.txt");
        try {
            Scanner input = new Scanner( inputFile ); // Reads list of words
            while ( input.hasNext() ) {               // from input file.
                // Converts to lowercase.
                String word = input.next().toLowerCase(); // Gets suggestions.
//	            System.out.println("Checking word");
                String[] suggestion = spellChecker.checkWord( word );
                if (suggestion != null){
                    for (int i = 0; i < suggestion.length; i++){
                        System.out.print(suggestion[i]+" ");
                        System.out.println();
                    }
                }
            }
        }
        catch (IOException e){
        }
    }
}
