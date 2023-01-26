/**
 * 
 */
package jme;

/**
 * * Manages a double linked lists to perform undo and/or redo operations.
 * 
 * 
 * @author bruno based on
 *         http://www.algosome.com/articles/implementing-undo-redo-java.html
 * 
 */
public class ChangeManager<T> {

	// the current index node
	protected Node<T> currentIndex = null;

	// the parent node far left node.
	protected Node<T> parentNode = new Node<T>();

	/**
	 * 
	 * Creates a new ChangeManager object which is initially empty.
	 */

	public ChangeManager() {

		currentIndex = parentNode;

	}

	/**
	 * 
	 * Creates a new ChangeManager which is a duplicate of the parameter in both
	 * contents and current index.
	 * 
	 * @param manager
	 */

	public ChangeManager(ChangeManager<T> manager) {

		this();

		currentIndex = manager.currentIndex;

	}

	/**
	 * 
	 * Clears all items contained in this manager.
	 */

	public void clear() {

		currentIndex = parentNode;
		parentNode.right = null;
	}

	/**
	 * 
	 * insert an item to manage.
	 * 
	 * @param item
	 */

	public void insertItem(T item) {
		

		if(item == null) {
			System.err.println("Null item");
		}
		Node<T> node = new Node<T>(item);
		assert(item != null);
		
		Node<T> savedRight = currentIndex.right;
		currentIndex.right = node;

		node.left = currentIndex;
		
		if(savedRight != null) {
			node.right = savedRight;
			savedRight.left = node;
		}
		
		currentIndex = node;

	}

	public T removeLast() {
		if(!canUndo()) {
			return null;
		}
		
		Node<T> savedRight = currentIndex.right;
		T lastItem = currentIndex.item; //the item to be returned
		this.undo();;
		currentIndex.right = savedRight;

		return lastItem;
	}
	/**
	 * 
	 * Determines if an undo can be performed.
	 * 
	 * @return
	 */

	public boolean canUndo() {

		//return currentIndex != parentNode;
		return currentIndex != parentNode && currentIndex.left != parentNode;

	}

	/**
	 * 
	 * Determines if a redo can be performed.
	 * 
	 * @return
	 */

	public boolean canRedo() {

		return currentIndex.right != null;

	}

	/**
	 * 
	 * Undoes the Item at the current index.
	 * 
	 * @throws IllegalStateException
	 *             if canUndo returns false.
	 */

	/*
	 * Retrieve the last item
	 */
	public T undo() {

		// validate

		if (!canUndo()) {

			throw new IllegalStateException(
					"Cannot undo. Index is out of range.");

		}

		moveLeft();
		T item = currentIndex.item;

		return item;

	}

	/**
	 * 
	 * Moves the internal pointer of the backed linked list to the left.
	 * 
	 * @throws IllegalStateException
	 *             If the left index is null.
	 */

	private void moveLeft() {

		if (currentIndex.left == null) {

			throw new IllegalStateException("Internal index set to null.");

		}

		currentIndex = currentIndex.left;

	}

	/**
	 * 
	 * Moves the internal pointer of the backed linked list to the right.
	 * 
	 * @throws IllegalStateException
	 *             If the right index is null.
	 */

	private void moveRight() {

		if (currentIndex.right == null) {

			throw new IllegalStateException("Internal index set to null.");

		}

		currentIndex = currentIndex.right;

	}

	/**
	 * 
	 * Redoes the Changable at the current index.
	 * 
	 * @throws IllegalStateException
	 *             if canRedo returns false.
	 */

	public T redo() {

		// validate
		if (!canRedo()) {

			throw new IllegalStateException(
					"Cannot redo. Index is out of range.");
		}

		moveRight();
		T item = currentIndex.item;

		return item;

	}

	/**
	 * 
	 * Inner class to implement a doubly linked list for our queue of Items.
	 * 
	 * @author Greg Cope
	 * 
	 * 
	 */

	private class Node<N> {

		private Node<N> left = null;

		private Node<N> right = null;

		private final N item;

		public Node(N c) {

			item = c;

		}

		public Node() {

			item = null;

		}

	}

}
