import java.util.*;

public class MyLinkedList<E> extends AbstractList<E> {
  
  private int nelems;
  private Node head;
  private Node tail;
  
  protected class Node {
    E data;
    Node next;
    Node prev;
    
    /** Constructor to create singleton Node 
     * @param element data to store in the Node
     */
    public Node(E element)
    {
		this.data = element;
		this.next = null;
		this.prev = null;
    }
    
    /** Constructor to create singleton link it between previous and next 
      *   @param element Element to add, can be null
      *   @param prevNode predecessor Node, can be null
      *   @param nextNode successor Node, can be null 
      */
    public Node(E element, Node prevNode, Node nextNode)
    {
		this.data = element;
		this.next = nextNode;
		this.prev = prevNode;
    }
    
    /** Remove this node from the list. Update previous and next nodes */
    public void remove()
    {
		this.next.prev = this.prev;
		this.prev.next = this.next;
    }
    
    /** Set the previous node in the list
      *  @param p new previous node
      */
    public void setPrev(Node p)
    {
		this.prev = p;
    }
	
    /** Set the next node in the list
      *  @param n new next node
      */
    public void setNext(Node n)
    {
		this.next = n;
    }
    
    /** Set the element 
      *  @param e new element 
      */
    public void setElement(E e)
    {
		this.data = e;
    }
    
    /** Accessor to get the next Node in the list 
     * @return Node next node
     */
    public Node getNext()
    {
    	return this.next;
    }
    
    /** Accessor to get the prev Node in the list 
     * @return Node previous node
     */
    public Node getPrev()
    {
      return this.prev;
    } 
    
    /** Accessor to get the Nodes Element 
     * @return E element
     */
    public E getElement()
    {
      return this.data;
    } 
  }
  
  /** ListIterator implementation */ 
  protected class MyListIterator implements ListIterator<E> {
    
    private boolean forward;
    private boolean canRemove;
    private Node left,right; // Cursor sits between these two nodes
    private int idx;        // Tracks current position. what next() would return
    private Node recent; // Stores the recently returned Node
 
    /** Constructor that is used to initialize the iterator */
    public MyListIterator()
    {
		left = head;
		right = head.getNext();
    	idx = 0;
    	forward = true;
    	canRemove = false;
    	recent = null;
    }
    
    /** Insert the given item into the list immediately before 
     * whatever would have been returned by a call to next(),
     * @param e data to add
     * @throws IllegalArgumentException if e is null
     */
    @Override
    public void add(E e) throws  IllegalArgumentException
    {
    	if (e == null)
    		throw new IllegalArgumentException();
    	// create a new Node
    	Node newNode = new Node(e, this.left, this.right);
    	// link the new Node
    	this.left = newNode;
    	this.left.setNext(newNode);
    	this.right.setPrev(newNode);
    	// move the cursor of iterator
    	this.idx++;
    	canRemove = false;
    	nelems++;
    }
    
    /** Return true if there are more elements 
     * when going in the forward direction
     * @return boolean true if there is next element
     */
    @Override
    public boolean hasNext(){
    	if (this.right == tail)
    		return false;
    	return true;
    }
    
    /** Return true if there are more elements
     * when going in the reverse direction
     * @return boolean
     */
    @Override
    public boolean hasPrevious()
    {
    	if (this.left == head)
    		return false;
    	return true;
    }
    
    /** Return the next element in the list when going forward,
     * then advance the cursor forward
     * @return E next element
     * @throws NoSuchElementException if there is no next element
     */
    @Override
    public E next() throws NoSuchElementException
    {
    	if (!this.hasNext())
    		throw new NoSuchElementException();
    	this.idx++;
    	this.left = this.right;
    	this.right = this.right.next;
    	this.recent = this.left;
    	canRemove = true;
    	forward = true;
    	return this.recent.getElement();
    }
    
    /** Return the index of the element 
     * that would be returned by a call to next()
     * @return int index of the next element
     */
    @Override
    public int nextIndex()
    {
    	return this.idx;
    }
    
    /** Return the next element in the list when going backwards,
     * then advance the cursor backward
     * @return E element
     * @throws NoSuchElementException if there is no previous element
     */
    @Override
    public E previous() throws NoSuchElementException
    {
    	if (!this.hasPrevious())
    		throw new NoSuchElementException();
    	this.idx--;
    	this.right = this.left;
    	this.left = this.left.prev;
    	this.recent = this.right;
    	canRemove = true;
    	forward = false;
    	return this.recent.getElement();
    }
    
    /** Return the index of the element 
     * that would be returned by a call to previous()
     * Return -1 if at the start of the list
     * @return int index of the previous element
     */
    @Override
    public int previousIndex()
    {
    	if (this.idx == 0)
    		return -1;
    	return (this.idx)-1;
    }
    
    /** Remove the last element returned by the most recent call
     * to either next/previous
     * @throws IllegalStateException if there was no recent call for next() or previous()
     * @throws IllegalStateException if add() or remove() has been called just before
     */
    @Override
    public void remove() throws IllegalStateException
    {
    	if (! canRemove)
    		throw new IllegalStateException();
    	// disconnect the link on recently returned Node
    	recent.prev.setNext(recent.next); 
    	recent.next.setPrev(recent.prev);
    	
    	// when the most recent call was next
    	if (forward == true){
			this.left = recent.prev;
    		this.idx--;
    	}
    	// when the most recent call was previous
    	else
    		this.right = recent.next;
    	canRemove = false;
    	nelems -= 1;
    }
    
    /** Change the value in the node returned by the most recent
     * next/previous with the new value
     * @param e element
     * @throws IllegalArgumentException if data is null
     * @throws IllegalStateException if there was no next() or previous() called
     * @throws IllegalStateException if add() or remove() has been called just before
     */
    @Override
    public void set(E e) 
      throws IllegalArgumentException,IllegalStateException
    {
		if (e == null)
			throw new IllegalArgumentException();
		if (recent == null)
			throw new IllegalStateException();
		if (! canRemove)
			throw new IllegalStateException();		
    	recent.setElement(e);
    }
  }
  
  //  Implementation of the MyLinkedList Class
  /** Only 0-argument constructor is defined */
  public MyLinkedList()
  {
	  head = new Node(null, null, null);
	  tail = new Node(null, head, null);
	  head.setNext(tail);
	  nelems = 0;
  }
  
  /**Helper Method 
   * Returns the Node at a specified index, not the content.
   * @param index which node to return
   * @return Node at the specified index
   * @throws IndexOutOfBoundsException if index get out of the bound
   */
  private Node getNth(int index) throws IndexOutOfBoundsException{
	  if (index < 0 || index >= nelems)
		  throw new IndexOutOfBoundsException();
	  Node curr = head;
	  for (int i = 0; i <= index; i++)
		  curr = curr.getNext();
	  return curr;
  }
  
  /** Return the number of elements being stored 
   * @return integer
   */
  @Override
  public int size()
  {
    return this.nelems;
  }
  
  /** Get contents at the specified index.
   * @param index position to retrieve content
   * @return E content of the linked list
   * @throws IndexOutOfBoudnsException if index get out of the bound
   */
  @Override
  public E get(int index) throws IndexOutOfBoundsException
  {
	  if (index < 0 || index >= nelems)
		  throw new IndexOutOfBoundsException();
	  Node curr = head;
	  for (int i = 0; i <= index; i++)
		  curr = curr.getNext();
	  return curr.getElement();
  }
  
  @Override
  /** Add an element to the list. "null" objects not allowed. 
    * @param index  where in the list to add
    * @param data data to add
    * @throws IndexOutOfBoundsException if index get out of the bound
    * @throws IllegalArgumentException if data is null
    */ 
    public void add(int index, E data) 
    throws IndexOutOfBoundsException,IllegalArgumentException
  {
	  if (index < 0 || index > nelems)
		  throw new IndexOutOfBoundsException();
	  if (data == null)
		  throw new IllegalArgumentException();
	  // move the cursor
	  Node curr = head;
	  for (int i = 0; i <= index; i++)
		  curr = curr.getNext();
	  // create a new Node
	  Node newNode = new Node(data, curr.getPrev(), curr);
	  // link the new Node
	  curr.getPrev().setNext(newNode);
	  curr.setPrev(newNode);
	  // adjust the size
	  this.nelems++;
  }
  
  /** Add an element to the end of the list 
    * @param data data to add
    * @throws IllegalArgumentException if data is null
    */ 
  public boolean add(E data) throws IllegalArgumentException
  {
    if (data == null)
    	throw new IllegalArgumentException();
    // create a new Node
    Node newNode = new Node(data);
    // linked the new Node
    newNode.setNext(tail);
    newNode.setPrev(tail.getPrev());
    this.tail.getPrev().setNext(newNode);
    this.tail.setPrev(newNode);
    // adjust the size
    this.nelems++;
    return true;
  }
  
  /** Set the element at an index in the list 
    * @param index  where in the list to add
    * @param data data to add
    * @return element that was previously at this index.
    * @throws IndexOutOfBoundsException if index get out of the bound
    * @throws IllegalArgumentException if data is null
    */ 
  public E set(int index, E data) 
    throws IndexOutOfBoundsException,IllegalArgumentException
  {
	  if (data == null)
		  throw new IllegalArgumentException();
	  if (index < 0 || index >= nelems)
		  throw new IndexOutOfBoundsException();
	  Node curr = head;
	  for (int i = 0; i <= index; i++)
		  curr = curr.getNext();
	  E orig = curr.getElement();
	  curr.setElement(data);
	  return orig;
  }
  
  /** Remove the element at an index in the list 
    * @param index  where in the list to add
    * @return element the data found
    * @throws IndexOutOfBoundsException if data get out of the bound
    */ 
  public E remove(int index) throws IndexOutOfBoundsException
  {
	  if (index < 0 || index >= nelems)
		  throw new IndexOutOfBoundsException();
	  Node curr = head;
	  for (int i = 0; i <= index; i++)
		  curr = curr.getNext();
	  E orig = curr.getElement();
	  curr.remove();
	  this.nelems--;
	  return orig;
  }
  
  /** Clear the linked list */
  public void clear()
  {
	  head.setNext(tail);
	  tail.setPrev(head);
	  nelems = 0;
  }
  
  /** Determine if the list empty 
    *  @return true if empty, false otherwise */
  public boolean isEmpty()
  {
    if (nelems == 0)
    	return true;
    return false;
  }
 
  public Iterator<E> iterator()
  {
	  return new MyListIterator();
  }
  public ListIterator<E> listIterator()
  {
	  return new MyListIterator();
  }
}
