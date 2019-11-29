import java.util.ArrayList;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** MyHeap class that implements an unbounded array-backed heap structure and is
 * an extension of the Java Collections AbstractQueue class 
 * <p>
 * The elements of the heap are ordered according to their natural 
 * ordering,  MyHeap does not permit null elements. 
 * The top of this heap is the minimal or maximal element (called min/max)  
 * with respect to the specified natural ordering.  
 * If multiple elements are tied for min/max value, the top is one of 
 * those elements -- ties are broken arbitrarily. 
 * The queue retrieval operations poll and  peek 
 * access the element at the top of the heap.
 * <p>
 * A MyHeap is unbounded, but has an internal capacity governing the size of 
 * an array used to store the elements on the queue. It is always at least as 
 * large as the queue size. As elements are added to a MyHeap, its capacity 
 * grows automatically. The details of the growth policy are not specified.
 * <p>
 * This class and its iterator implements the optional methods of the 
 * Iterator interface (including remove()). The Iterator provided in method 
 * iterator() is not guaranteed to traverse the elements of the MyHeap in 
 * any particular order. 
 * <p>
 * Note that this implementation is not synchronized. Multiple threads 
 * should not access a MyHeap instance concurrently if any of the 
 * threads modifies the MyHeap. 
 */
public class MyHeap<E extends Comparable <? super E>> extends AbstractQueue<E> 
{
    /* -- Define any private member variables here -- */
    /* In particular, you will need an ArrayList<E> to hold the elements
     * in the heap.  You will also want many more variables */
	private ArrayList<E> heap;
	private boolean max;
    private int nelem; 
    private int capacity;
    
    /** O-argument constructor. Creates an empty MyHeap with unspecified
     *  initial capacity, and is a min-heap 
     */ 
    public MyHeap(){
    	heap = new ArrayList<E>(5);
    	heap.add(null);
    	max = false;
    	nelem = 0;
    	capacity = 5;
    }

    /** 
     * Constructor to build a min or max heap
     * @param isMaxHeap   if true, this is a max-heap, else a min-heap 
     */ 
    public MyHeap(boolean isMaxHeap){
    	heap = new ArrayList<E>(5);
        heap.add(null);
    	max = isMaxHeap;
    	nelem = 0;
    	capacity = 5;
    }

    /** 
     * Constructor to build a with specified initial capacity 
     *  min or max heap
     * @param capacity initial capacity of the heap.   
     * @param isMaxHeap if true, this is a max-heap, else a min-heap 
     */ 
    public MyHeap(int capacity, boolean isMaxHeap) throws IllegalArgumentException{
    	if (capacity < 1)
    		throw new IllegalArgumentException();
    	heap = new ArrayList<E>(capacity);
        heap.add(null);
    	max = isMaxHeap;
    	nelem = 0;
    	this.capacity = capacity;
    }
    
    /** Copy constructor. Creates MyHeap with a deep copy of the argument
     * @param toCopy      the heap that should be copied
     */
    public MyHeap (MyHeap<E> toCopy){
    	heap = new ArrayList<E>(toCopy.getCapacity());
    	heap.add(null);
    	max = toCopy.max;
    	Iterator<E> iter = toCopy.iterator();
    	while (iter.hasNext()){
    	        heap.add(iter.next());
    	}
    	nelem = toCopy.size();
    	capacity = toCopy.getCapacity();
    }

    /* The following are defined "stub" methods that provide degenerate
     * implementations of methods defined as abstract in parent classes.
     * These need to be coded properly for MyHeap to function correctly
     */

    /** Override remove() from Abstractqueue 
     * Retrieves and removes the head of this queue
     * @return the head of this queue
     * @throws NoSuchElementException if the queue is empty
     */
    public E remove() throws NoSuchElementException{
        if (nelem == 0)
            throw new NoSuchElementException();
        else{
            return this.poll();
        }
    }
    
    /** Size of the heap
     * @return the number of elements stored in the heap
    */
    public int size()
    {
        return nelem; 
    }
    
    /** get Capacity of the heap
     * @return the capacity
     */
    public int getCapacity(){
        return capacity;
    }

    /** 
     * @return an Iterator for the heap 
    */
    public Iterator<E> iterator()
    {
        Iterator<E> it = new MyHeapIterator();
        return it;
    }

    /** peek - see AbstractQueue for details 
     * @return Element at top of heap. Do not remove 
    */
    public E peek(){
    	if (nelem == 0)
    		return null;
        return heap.get(1);
    }
    
    /** poll - see AbstractQueue for details 
     * @return Element at top of heap. And remove it from the heap. 
     *  return <tt>null</tt> if the heap is empty
    */
    public E poll(){
        // Remove
    	if (nelem == 0)
    		return null;
    	E store = heap.remove(1);
        nelem--;
        
        // Re-order
        if (size() != 0){
            heap.add(1, heap.remove(nelem));
            trickleDown(1);
        }
    	return store;
    }
    
    /** offer -- see AbstractQueue for details
     * insert an element in the heap
     * @return true
     * @throws NullPointerException 
     *  if the specified element is null    
     */
    public boolean offer (E e) throws NullPointerException{
    	if (e == null)
    		throw new NullPointerException();
    	
    	// Double the capacity of heap
    	if (this.size() == this.getCapacity()-1){
    		ArrayList<E> newheap = new ArrayList<E>(2 * this.getCapacity());
    		newheap.add(null);
    		
    		Iterator<E> iter = this.iterator();
    		while (iter.hasNext()){
    		    newheap.add(iter.next());
    		}
    		heap = newheap;
    		capacity = 2 * capacity;
    	}
    	
    	// Insert element
    	heap.add(e);
    	nelem ++;
    	
    	// Order the heap
    	bubbleUp(nelem);
    	
        return true;
    }

    /* ------ Private Helper Methods ----
     *  DEFINE YOUR HELPER METHODS HERE
     */
    
    /** Swap two nodes */
    private void swap(int idx1, int idx2){
    	E item1 = heap.get(idx1);
    	E item2 = heap.get(idx2);
    	heap.remove(idx1);
    	heap.add(idx1, item2);
    	heap.remove(idx2);
    	heap.add(idx2, item1);
    }
    
    /** Bubble up the node depending on the order
     * @param index of node to bubble up
     * @throws IndexOutOfBoudnsException
     */
    private void bubbleUp(int child) throws IndexOutOfBoundsException{
        if (child < 1 || child > this.size())
            throw new IndexOutOfBoundsException();
        else if (child == 1)
            return;
        int parent = child/2;
        
        // When max heap
        if (max){
            int compare_max = heap.get(child).compareTo(heap.get(parent));
            // If child is greater than parent
            if (compare_max > 0){
                swap(child, parent);
                bubbleUp(parent);
            }
            return;
        }
        
        // When min heap
        else if (!max){
            int compare_min = heap.get(child).compareTo(heap.get(parent));
            // If child is less than parent
            if (compare_min < 0){
                swap(child, parent);
                bubbleUp(parent);
            }               
            return;
        }
    }
    
    /** Trickle down the node depending on the order
     * @param index of node to trickle down
     * @throws IndexOutOfBoudnsException if the parameter is the last node
     */
    private void trickleDown(int parent) throws IndexOutOfBoundsException{
        if (parent < 1 || parent > this.size())
            throw new IndexOutOfBoundsException();

        int child1 = parent*2;
        int child2 = parent*2 + 1;
        if (child1 > this.size())
            return;
               
        // When max heap
        if (max){
            int max_child;
            // Only one child
            if (child1 == nelem)
                max_child = child1;
            // Two child nodes
            else{
                int compare_child = heap.get(child1).compareTo(heap.get(child2));
                if (compare_child < 0)
                    max_child = child2;
                else
                    max_child = child1;
            }
            int compare_max = heap.get(parent).compareTo(heap.get(max_child));
            // If parent is less than max_child
            if (compare_max < 0){
                swap(max_child, parent);
                trickleDown(max_child);
            } 
            return;
        }
        
        // When min heap
        else if (!max){
            int min_child;
            // Only one child
            if (child2 == nelem+1)
                min_child = child1;
            // Two child nodes
            else{
                int compare_child = heap.get(child1).compareTo(heap.get(child2));
                if (compare_child < 0)
                    min_child = child1;
                else
                    min_child = child2;
            }
            int compare_min = heap.get(parent).compareTo(heap.get(min_child));
            // If parent is greater than min_child
            if (compare_min > 0){
                swap(min_child, parent);
                trickleDown(min_child);
            }               
            return;
        }
    }


    /** Inner Class for an Iterator **/
    /* stub routines */

    private class MyHeapIterator implements Iterator<E>
    {
        private int index;
        private boolean canRemove;
        
        private MyHeapIterator()
        {
            // Heap starts with index 1
            index = 1;
            canRemove = false;
        }

        /* hasNext() to implement the Iterator<E> interface */
        /** Returns true if the iteration has more elements. */
        public boolean hasNext()
        {
            if (index <= size())
                return true;
            return false;
        }

        /* next() to implement the Iterator<E> interface */
        /** Returns the next element in the iteration.
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public E next() throws NoSuchElementException
        {
            if (! this.hasNext())
                throw new NoSuchElementException();
            E store = heap.get(index);
            index++;
            canRemove = true;
            return store; 
        }
        
        /* remove() to implement the Iterator<E> interface */
        /** Removes last element returned by this iterator.
         * This method can be called only once per call to next().
         * @throws IllegalStateException  if the next method has not yet been called, 
         * or the remove method has already been called after the last call to the next method
         */
        public void remove() throws IllegalStateException
        {
            // Remove
            if (!canRemove){
                throw new IllegalStateException();
            }
            else{
                heap.remove(index-1);
                nelem--;
                canRemove = false;
            }

            index--;
        }   
    }
} 
