import java.util.*;
import java.text.DecimalFormat;
import java.io.*;

public class HashTable implements IHashTable {

    //HashTable of LinkedLists. 

    private int nelems;  //Number of element stored in the hash table
    private int expand;  //Number of times that the table has been expanded
    private int collision;  //Number of collisions since last expansion
    private String statsFileName;     //FilePath for the file to write statistics upon every rehash
    private boolean printStats = false;   //Boolean to decide whether to write statistics to file or not after rehashing
    private int buckets; // Number of buckets in the hash table.
    private double loadFactor; //Load factor to tell when to expand.
    private LinkedList<String>[] table; //Array to store elements

    /**
     * Constructor for hash table
     * @param Initial size of the hash table
     */
    public HashTable(int size) throws IllegalArgumentException{
        if (size <= 0 )
            throw new IllegalArgumentException();
        table = new LinkedList[size];
        buckets = size;
        loadFactor = ((double)nelems / buckets);
        nelems = 0;
        expand = 1;
        collision = 0;
        printStats = false;
    }

    /**
     * Constructor for hash table
     * @param Initial size of the hash table
     * @param File path to write statistics
     */
    public HashTable(int size, String fileName)throws IllegalArgumentException{
        if (size <= 0 )
            throw new IllegalArgumentException();
        table = new LinkedList[size];
        buckets = size;
        loadFactor = ((double)nelems / buckets);
        nelems = 0;
        expand = 1;
        collision = 0;
        printStats = true;
        statsFileName = fileName;
    }

    /** Insert the value.
     * @param value to insert
     * @return true if item has been inserted, false if there already exists the same item.
     */
    @Override
    public boolean insert(String value) throws NullPointerException{
        if (value == null)
            throw new NullPointerException();

        // Insert if not duplicate
        else if (!contains(value)){
            // Collision
            if (table[hashFunction(value)] != null){
                table[hashFunction(value)].add(value);
                collision++;
            }
            // First insertion of the bucket
            else{
                table[hashFunction(value)] = new LinkedList<String>();
                table[hashFunction(value)].add(value);
            }
            // Update statistics
            nelems++;
            loadFactor = ((double)nelems / buckets);

            if (loadFactor > ((double)2/3)){
                //				System.out.println("Insert_loadFactor excess. call rehash()");
                rehash();
            }

            return true;
        }
        // Return false if duplicate
        return false;
    }

    /** Delete the value.
     * @param value to delete
     * @return true if item has been deleted, false if there is no such item.
     */
    @Override
    public boolean delete(String value) throws NullPointerException{
        if (value == null)
            throw new NullPointerException();
        else if (contains(value)){
            table[hashFunction(value)].remove(value);
            nelems--;
            loadFactor = ((double)nelems / buckets);
            return true;
        }
        else
            return false;
    }

    /** Determine if element exists in the table.
     * @param value to look for
     * @return true if exists, false if not.
     */
    @Override
    public boolean contains(String value) throws NullPointerException{
        if (value == null)
            throw new NullPointerException();
        if (table[hashFunction(value)]== null)
            return false;
        else{
            ListIterator<String> it = table[hashFunction(value)].listIterator();
            while (it.hasNext()){
                if (it.next().equals(value))
                    return true;
            }
        }
        return false;
    }

    /** Print out the hash table */
    @Override
    public void printTable() {
        for (int i = 0; i < table.length; i++){
            System.out.print(i+": ");
            if (table[i] == null)
                System.out.println();
            else{
                for (int j = 0; j < table[i].size()-1; j++)
                    System.out.print(table[i].get(j)+", ");
                System.out.println(table[i].get(table[i].size()-1));
            }
        }
    }

    /** The number of elements currently store in the table.
     * @return int the number of elements
     */
    @Override
    public int getSize() {
        return nelems;
    }

    /** Expand and rehash the items into the table 
     * when load factor goes over threshold. */
    private void rehash(){
        if (printStats){
            //			System.out.println("Rehash()_got into rehash(); printStats");
            printStatistics();
        }

        // Resize
        LinkedList<String>[] newTable = new LinkedList[buckets*2];
        buckets = 2 * buckets;
        expand++;

        // Rehash
        for (int i = 0; i < table.length; i++){
            // If the bucket has items
            if (table[i] != null){
                ListIterator<String> it = table[i].listIterator();
                while (it.hasNext()){
                    String item = it.next();
                    // Insert to new table with collision
                    if (newTable[hashFunction(item)] != null)
                        newTable[hashFunction(item)].add(item);
                    // Insert for the first time
                    else{
                        newTable[hashFunction(item)] = new LinkedList<String>();
                        newTable[hashFunction(item)].add(item);
                    }
                }
            }
        }
        //		System.out.println("Rehash()_Completed rehashing");

        loadFactor = ((double)nelems / buckets);
        collision = 0;
        table = newTable;
    }

    /** Trim decimals 2 decimal places.
     * @param number Number to get trimmed.
     * @return Trimmed number as a string.
     */
    private static String trimDecimal(double number){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }

    /** Print the statistics after each expansion.
     * Called from insert/rehash only if printStats = true.
     */
    private void printStatistics(){
        //		System.out.println("printStatistics()_entered successfully.");
        // Find longest chain
        int longest_chain = 0;
        for (int i = 0; i < table.length; i++){
            //			System.out.println("Entered for loop");
            if (table[i] != null){
                //				System.out.println("look for every table");
                if (longest_chain <= table[i].size()){
                    //					System.out.println("Update longest chain");
                    longest_chain = table[i].size();
                }
            }
        }
        //		System.out.print("longestchain: ");
        //		System.out.println(Integer.toString(longest_chain));

        //Set up
        PrintWriter pw;
        String formatted_loadFactor = trimDecimal(loadFactor);

        // Write
        try{
            //			System.out.println("got into try block");
            pw = new PrintWriter(new FileOutputStream(statsFileName, true));
            //			System.out.println("Created PrintWriter object");
            pw.print(expand);
            pw.print(" resizes, load factor ");
            pw.print(formatted_loadFactor + ", ");
            pw.print(collision);
            pw.print(" collisions, ");
            pw.print(longest_chain);
            pw.println(" longest chain");
            pw.close();
            //			System.out.println("Write successful");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /** Hash function to assign key to each element.
     * @param value The input String value
     * @return Integer key that stores the value
     */
    private int hashFunction(String value){
        int hashKey = value.charAt(0);
        for (int i = 1; i < value.length(); i++){
            int letter = value.charAt(i);
            hashKey = (hashKey * 53	+ letter) % buckets;
        }
        return hashKey % buckets;
    }

    /** Search for the given value.
     * @param value value to search.
     * @return the index in the LinkedList if the value exists. Returns -1 if no such value exists.
     */
    private int search(String value){
        if (contains(value)){
            ListIterator<String> it = table[hashFunction(value)].listIterator();
            while (it.hasNext()){
                if (it.next().equals(value))
                    return it.previousIndex();
            }
            return -1;
        }
        else
            return -1;
    }

    /** Getter method for testing
     * @return number of buckets
     */
    public int getBuckets(){
        return buckets;
    }
    /** Getter method for testing
     * @return boolean printStats
     */
    public boolean getPrintStats(){
        return printStats;
    }

    public static void main(String[] args){
        HashTable tester = new HashTable(1, "testerWrite.txt");
        System.out.println("This is for a");
        tester.insert("a");
        System.out.println("This is for b");
        tester.insert("b");
        System.out.println("This is for c");
        tester.insert("c");
        System.out.println("This is for d");
        tester.insert("d");
        System.out.println("This is for e");
        tester.insert("e");
        tester.printTable();
    }
}
