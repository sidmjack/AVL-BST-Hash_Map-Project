import java.util.LinkedList;
import java.util.Collection;

/** Binary search tree implementation, recursive, passes BSTTest.java.
    @param <T> the type of objects in the nodes
 */

public class BSTree<T extends Comparable<? super T>> {

    private int size;
    private T data;
    private BSTree<T> left;
    private BSTree<T> right;
    private BSTree<T> parent;

    /** Create empty placeholder nodes for root and children.
     */
    public BSTree() {
        this.size = 0;
        this.data = null;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    /** Create a root node with a particular value and empty children.
        @param value set the data element with value
     */
    public BSTree(T value) {
        this.size = 1;
        this.data = value;
        this.left = new BSTree<T>();
        this.right = new BSTree<T>();
        this.parent = null;
    }

    /** Get the root value of this (sub)tree.
     *  @return the data in the root
     */
    public T root() {
        return this.data;
    }

    /** Create an string representation using an inorder traversal so
     *  that the data values are ordered properly.
     *  @return the string
     */
    public String toString() {
        return this.inOrder().toString();
    }

    /** Create a collection of the data values ordered.
     *  @return the collection
     */
    public Collection<T> inOrder() {
        LinkedList<T> nodes = new LinkedList<T>();
        if (this.left != null) {
            nodes.addAll(this.left.inOrder());
        }
        if (this.data != null) {
            nodes.addLast(this.data);
        }
        if (this.right != null) {
            nodes.addAll(this.right.inOrder());
        }

        return nodes;
    }

    /** Get the number of nodes in this tree.
     *  @return the number
     */
    public int size() {
        return this.size;
    }

    /** Check if the tree is empty.
     *  @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return this.data == null;
    }

    /** Insert a value into the tree, allowing duplicates.
     *  @param value the value to insert
     */
    public void insert(T value) {
        this.size++;
        if (this.data == null) {
            this.data = value;
            this.left = new BSTree<T>();
            this.right = new BSTree<T>();
            this.left.parent = this;
            this.right.parent = this;
        } else {
            int diff = this.data.compareTo(value);
            if (diff <= 0) {  // allow dups
                this.right.insert(value);
            } else {  // diff > 0
                this.left.insert(value);
            }
        }
    }

    /** Check if a value is already in the tree.
     *  @param value the value for which to search
     *  @return true if there, false otherwise
     */
    public boolean contains(T value) {
        return this.find(value) != null;
    }

    /** Find the (first) node containing this value.
     *  @param value the value for which to search
     *  @return the tree whose root has the value, or null if not there
     */
    public BSTree<T> find(T value) {
        if (this.isEmpty()) {
            return null;
        } else {
            int diff = this.data.compareTo(value);
            if (diff > 0) {  // look left
                return this.left.find(value);
            } else if (diff < 0) {  // look right
                return this.right.find(value);
            } else {  // diff == 0, found
                return this;
            }
        }
    }

    /** Delete (one occurrence of) a value from the tree if it's there.
     *  @param value the value to be deleted
     */
    public void delete(T value) {
        BSTree<T> match = this.find(value);
        if (match != null) { // found
            T temp = match.deleteNode();
            if (!temp.equals(value)) {
                throw new RuntimeException("delete: " 
                    + temp + " should equal " + value);
            }
        }
    }


    /** Delete this node from the tree.  When there is a choice,
     *  replace the deleted node with the minimum value in its right
     *  subtree.
     *  @return the value in the delete node
     */
    private T deleteNode() {
        if (this.isEmpty()) {
            // shouldn't happen, but just to be safe
            return null;
        }

        int kids = this.numKids();
        BSTree<T> rent = this.parent;
        T value = this.data;

        if (kids == 2) { 
            BSTree<T> min = this.right.findMin();
            this.data = min.data;
            min.deleteNode();  // will adjust size here
        } else {
            if (kids == 0) { // deleting a leaf
                // convert leaf node to a sentinel
                this.data = null;
                this.left = null;
                this.right = null;
            } else if (this.left.isEmpty()) {  // promote right child fields
                this.data = this.right.data;
                this.left = this.right.left;
                this.right = this.right.right;
                this.left.parent = this;
                this.right.parent = this;
            } else if (this.right.isEmpty()) {  // promote left child fields
                this.data = this.left.data;
                this.right = this.left.right;
                this.left = this.left.left;
                this.right.parent = this;
                this.left.parent = this;
            }

            // adjust size up to root, only place parent is really needed
            BSTree<T> curr = this;
            while (curr != null) {
                curr.size--;
                curr = curr.parent;
            }
        }
        return value;
    }

    /** Helper method to get the number of children for this root.
     *  @return the number (0, 1, 2)
     */
    private int numKids() {
        int count = 0;
        if (!this.left.isEmpty()) {
            count++;
        }
        if (!this.right.isEmpty()) {
            count++;
        }
        return count;
    }

    /** Helper method finds the maximum value in this tree.
     *  @return the root node with the maximum value
     */
    private BSTree<T> findMax() {
        if (this == null || this.isEmpty()) {
            return null;
        }
        BSTree<T> max = this;
        while (!max.right.isEmpty()) {
            max = max.right;
        }
        return max;
    }

    /** Helper method finds the minimum value in this tree.
     *  @return the root node with the minimum value
     */
    private BSTree<T> findMin() {
        if (this == null || this.isEmpty()) {
            return null;
        }
        BSTree<T> min = this;
        while (!min.left.isEmpty()) {
            min = min.left;
        }
        return min;
    }


}


