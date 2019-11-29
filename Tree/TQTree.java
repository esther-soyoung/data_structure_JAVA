 /* A Java class that supports a Binary Tree that plays the game of twenty questions */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Scanner;
import java.util.LinkedList;

public class TQTree {
    private TQNode root;

    /**
     * Inner class that supports a node for a twenty questions tree. You should not
     * need to change this class.
     */
    class TQNode {
        /* You SHOULD NOT add any instance variables to this class */
        TQNode yesChild; // The node's right child
        TQNode noChild; // The node's left child
        String data; // A question (for non-leaf nodes)
                     // or an item (for leaf nodes)

        int idx; // index used for printing

        /**
         * Make a new TWNode
         * 
         * @param data
         *            The question or answer to store in the node.
         */
        public TQNode(String data) {
            this.data = data;
        }

        /**
         * Setter for the noChild
         * 
         * @param noChild
         *            The new left (no) child
         */
        public void setNoChild(TQNode noChild) {
            this.noChild = noChild;
        }

        /**
         * Setter for the yesChild
         * 
         * @param yesChild
         *            The new right (yes) child
         */
        public void setYesChild(TQNode yesChild) {
            this.yesChild = yesChild;
        }

        /**
         * Getter for the yesChild
         * 
         * @return The node's yes (right) child
         */
        public TQNode getYesChild() {
            return this.yesChild;
        }

        /**
         * Getter for the noChild
         * 
         * @return The node's no (left) child
         */
        public TQNode getNoChild() {
            return this.noChild;
        }

        /**
         * Getter for the data
         * 
         * @return The data stored in this node
         */
        public String getData() {
            return this.data;
        }

        /**
         * Setter for the index
         * 
         * @param idx
         *            index of this for printing
         */
        public void setIndex(int idx) {
            this.idx = idx;
        }

        /**
         * get the index
         * 
         * @return idx index of this for printing
         */
        public int getIndex() {
            return this.idx;
        }
    } // End TQNode

    /**
     * Builds a new TQTree by reading from a file
     * 
     * @param filename
     *            The file containing the tree
     * @throws FileNotFoundException
     *             if the file cannot be found or read.
     */
    public TQTree(String filename) {
        File f = new File(filename);
        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file " + filename);
            System.err.println("Building default Question Tree.");
            buildDefaultTree();
            return;
        }

        buildTreeFromFile(reader);
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("An error occured while closing file " + filename);
        }
    }

    // Build the tree from the file that reader is reading from.
    private void buildTreeFromFile(LineNumberReader reader) {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            errorBuildTree("File contains no tree.");
            return;
        }

        if (line == null) {
            errorBuildTree("File contains no tree.");
            return;
        }
        String[] lineSplit = line.split(":", 2);
        if (lineSplit.length < 2) {
            errorBuildTree("Incorrect file format: line 1.");
            return;
        }
        String qOrA = lineSplit[0];
        String data = lineSplit[1];

        if (!qOrA.equals("Q")) {
            errorBuildTree("Incorrect file format: line 1.");
            return;
        }
        root = new TQNode(data);
        try {
            root.setNoChild(buildSubtree(reader));
            root.setYesChild(buildSubtree(reader));
        } catch (ParseException e) {
            errorBuildTree(e.getMessage() + ": line " + +e.getErrorOffset());
        }
    }

    // Print an error message and then build the default tree
    private void errorBuildTree(String message) {
        System.err.println(message);
        System.err.println("Buidling default Question Tree");
        buildDefaultTree();
    }

    /**
     * Recursive method to build a TQTree by reading from a file.
     * 
     * @param reader
     *            A LineNumberReader that reads from the file
     * @return The TQNode at the root of the created tree
     * @throws ParseException
     *             If the file format is incorrect
     */
    private TQNode buildSubtree(LineNumberReader reader) throws ParseException {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new ParseException("Error reading tree from file: " + e.getMessage(), reader.getLineNumber());
        }

        if (line == null) {
            // We should never be calling this method if we don't have any more input
            throw new ParseException("End of file reached unexpectedly", reader.getLineNumber());
        }

        String[] lineSplit = line.split(":", 2);
        String qOrA = lineSplit[0];
        String data = lineSplit[1];

        TQNode subRoot = new TQNode(data);
        if (qOrA.equals("Q")) {
            subRoot.setNoChild(buildSubtree(reader));
            subRoot.setYesChild(buildSubtree(reader));
        }
        return subRoot;
    }

    /** Builds a starter TQ tree with 1 question and 2 answers */
    public TQTree() {
        buildDefaultTree();
    }

    private void buildDefaultTree() {
        root = new TQNode("Is it bigger than a breadbox?");
        root.setNoChild(new TQNode("spam"));
        root.setYesChild(new TQNode("a computer scientist"));
    }

    /**
     * Play one round of the game of Twenty Questions using this game tree 
     * Augments the tree if the computer does not guess the right answer
     */
    public void play(Scanner input) {
    	boolean done = false;
        String response;
        String store_response;
        TQNode curr = root;
        TQNode parent;
        
        // Play
    	while (!done) {
            System.out.println(curr.getData());
            response = input.nextLine();
        	store_response = response;
//            goDown(parent, curr, response);
            parent = curr;
        	if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
        		curr = curr.getYesChild();
        	}
        	else {
        		curr = curr.getNoChild();
        	}
            
            // Reach the leaf
            if (leaf(curr)) {
//            	System.out.println("leaf");
            	done = true;
            	System.out.println("Is it " + curr.getData() + "?");
                response = input.nextLine();
                // Computer wins
                if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                	System.out.println("Got it!");
                }
                // Augment tree
                else {
                	augment(parent, curr, store_response, input);
//                	System.out.println("called augment");
                }
            }
    	}
    }

    // PRIVATE HELPER METHODS
//    /**
//     * Go down the tree
//     * @param parent store parent node
//     * @param curr move current node to the child node
//     * @param direction no or yes
//     */
//    private void goDown(TQNode parent, TQNode curr, String direction) {
//		parent = curr;
//    	if (direction.equalsIgnoreCase("y") || direction.equalsIgnoreCase("yes")) {
//    		curr = curr.getYesChild();
//    	}
//    	else {
//    		curr = curr.getNoChild();
//    	}
//    	return;
//    }
    
    /**
     * Check if the node is leaf
     * @param curr node to check
     * @return true if the node is leaf
     */
    private boolean leaf(TQNode curr) {
    	if (curr.getNoChild() == null)
    		return true;
    	return false;
    }
    
    /**
     * Augment the tree
     * @param parent node to which connect new question node
     * @param curr node currently pointed at
     * @param sc scanner object
     */
    private void augment(TQNode parent, TQNode curr, String response, Scanner sc) {
    	// Collect data
    	System.out.println("OK, what was it?");
    	String answer = sc.nextLine();

    	System.out.println("Give me a question that would distinguish "
    			+ answer + " from " + curr.getData());
    	String question = sc.nextLine();
    	
    	System.out.println("And would the answer to the question for "
    			+ answer + " be yes or no?");
    	String yn = sc.nextLine();
    	
		TQNode questionNode = new TQNode(question);
		TQNode answerNode = new TQNode(answer);
    	
    	// Augment
		if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes"))
			parent.setYesChild(questionNode);
		else
			parent.setNoChild(questionNode);

    	if (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes")) {
    		questionNode.setYesChild(answerNode);
    		questionNode.setNoChild(curr);
    	}
    	else {
    		questionNode.setNoChild(answerNode);
    		questionNode.setYesChild(curr);
    	}
    	return;
    }
    
    /**
     * Stores each node and set the index
     * @param storage linkedlist to store nodes
     * @param curr current node
     */
    private void storeTree(LinkedList<TQNode> storage, TQNode root){
     	TQNode curr = root;
    	TQNode no;
    	TQNode yes;
    	int i = 0;
    	
    	storage.add(root);
    	root.setIndex(i);
    	
    	while (true) {
    		if (leaf(curr)) {
    			try {
    				curr = storage.get(curr.getIndex()+1);
//    				System.out.println("leaf: horizontal move");
    			}
    			catch(IndexOutOfBoundsException e) {
    				break;
    			}
    		}
    		else{
    			no = curr.getNoChild();
//            	System.out.println("got no Child");
            	storage.add(no);
//            	System.out.println(no.getData());
            	no.setIndex(++i);
            	
            	yes = curr.getYesChild();
            	storage.add(yes);
            	yes.setIndex(++i);
            	
            	if (storage.get(curr.getIndex()+1) != null)
            		curr = storage.get(curr.getIndex()+1);
    		}
    	}
//		System.out.println("stored all the nodes");
    }

    public void print() {
        PrintWriter writer = new PrintWriter(System.out);
        printTree(writer, root);
        writer.flush();
    }

    /**
     * method for breadth-first traversal of the tree.
     * 
     * @param The
     *            print writer to write to (usually stdout)
     * @param The
     *            current root from which to write
     */
    private void printTree(PrintWriter writer, TQNode root) {
        //First bread-first search: Mark the nodes
    	LinkedList<TQNode> storage = new LinkedList<TQNode>();
    	storeTree(storage, root);
    	
    	// Second bread-first search: Print the nodes
    	for (int j = 0; j < storage.size(); j++) {
    		if (storage.get(j).getNoChild() != null){
            	writer.print(j);
            	writer.print(": '" + storage.get(j).getData() + "' no:(");
            	writer.print(storage.get(j).getNoChild().getIndex());
            	writer.print(") yes: (");
            	writer.print(storage.get(j).getYesChild().getIndex());
            	writer.println(")");
    		}
    		else {
            	writer.print(j);
            	writer.println(": '" + storage.get(j).getData() + "' no:(null) yes:(null)");
    		}
    	}
    }

    /**
     * Save this Twenty Questions tree to the file with the given name
     * 
     * @param filename
     *            The name of the file to save to
     * @throws FileNotFoundException
     *             If the file cannot be used to save the tree
     */
    public void save(String filename) throws FileNotFoundException {
        File f = new File(filename);
        PrintWriter writer = new PrintWriter(f);
        saveTree(writer, root);
        writer.close();
    }

    /**
     * Recursive helper method to do the preorder traversal of the tree.
     * 
     * @param The
     *            print writer to write to
     * @param The
     *            current root from which to write
     */
    private void saveTree(PrintWriter writer, TQNode currentRoot) {
        if (currentRoot == null) {
            return;
        }
        String toWrite = "";
        if (currentRoot.getNoChild() == null && currentRoot.getYesChild() == null) {
            toWrite = "A:" + currentRoot.getData();
        } else {
            toWrite = "Q:" + currentRoot.getData();
        }
        writer.println(toWrite);
        saveTree(writer, currentRoot.getNoChild());
        saveTree(writer, currentRoot.getYesChild());
    }

}
