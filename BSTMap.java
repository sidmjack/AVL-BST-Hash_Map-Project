// Name: Lawrence Wolf-Sonkin & Sidney Jackson
// JHU Login: lwolfso1  & sjacks85
// Course: Data Structure (600.226.02)
// Project: Project #3A (Basic BST Implementation)
// Due Date: 03-27-2016
// Last Modified: 03-28-2016

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/** 
 * Binary Search Tree Map implementation with inner Node class.
 *  @param <K> the base type of the keys in the entries
 *  @param <V> the base type of the values
 */
public class BSTMap<K extends Comparable<? super K>, V>
    implements MapJHU<K, V>, Iterable<Map.Entry<K, V>> {

    /**
     * Inner node class.  Do not make this static because you want
     * the K to be the same K as in the BSTMap header.
     * @param <K> is Key.
     * @param <V> is Value.  
     */
    public class BNode<K, V> implements Map.Entry<K, V> {

        /** The key of the entry (null if sentinel node). */
        private K key;
        /** The value of the entry (null if sentinel node). */
        private V value;
        /** The left child of this node. */
        private BNode<K, V> left;
        /** The right child of this node. */
        private BNode<K, V> right;

        /** 
         * Create a new node with a particular key and value.
         *  @param k the key for the new node
         *  @param v the value for the new node
         */
        public BNode(K k, V v) {
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
        }

        /**
         * Special "empty" constructor for Empty BNode.
         */
        BNode() {
            this(null, null);
        }

        /** Check whether this node is a leaf sentinel, based on key.
         *  @return true if leaf, false otherwise.
         */
        public boolean isLeaf() {
            return this.getKey() == null;  // sentinel-based implementation
        }

        /**
         * Replaces the value corresponding to this entry with the specified
         * value.
         *
         * @param k new value to be stored in this entry.
         * @return the old value corresponding to the entry.
         */
        public K setKey(K k) {
            K oldKey = this.key;
            this.key = k;
            return oldKey;
        }

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry.
         */
        public K getKey() {
            return this.key;
        }

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value corresponding to this entry.
         */
        public V getValue() {
            return this.value;
        }

        /**
         * Replaces the value corresponding to this entry with the specified
         * value.
         *
         * @param v new value to be stored in this entry
         * @return the old value corresponding to the entry
         */
        public V setValue(V v) {
            V oldValue = this.value;
            this.value = v;
            return oldValue;
        }

        /**
         * Offers a function to transform node into string.
         * @return Returns string node.
         */
        public String toString() {
            return "(" + this.getKey() + ", " + this.getValue() + ")";
        }

        // @Override
        // public boolean equals(Object o) {
        //     System.out.println("Does this equal that??");
        //     BNode<K, V> that = null;
        //     try {
        //         that = (BNode<K, V>) o;
        //     } catch (ClassCastException e) {
        //         System.out.println("Why you give me not BNode????");
        //         return false;
        //     }
        //     System.out.println("This equals that");
        //     return this.key.equals(that.key) 
        //     && this.value.equals(that.value);
        // }

        // public int hashCode() {
        //     return super.hashCode();
        // }


    }



    /** The root of this tree. */
    private BNode<K, V> root;
    /** The number of entries in this map (== non-sentinel nodes). */
    private int size;

    /**
     * A semi-arbitrary counter that is incremented every time one of the
     * methods that makes a change to the hashmap is run. Used exclusively
     * as an indicator of state, and whether or not it changed, to be monitored
     * by the HashMapIterator to check if the HashMap was changed by 
     * non-iterator functions since the iterator was created.
     */
    private int modCounter;

    /** 
     * Create an empty tree with a sentinel root node.
     */
    public BSTMap() {
        // empty tree is a sentinel for the root
        this.modCounter = 0;
        this.clear();
    }

    /**
     * Returns the number of elements in the tree.
     * @return Number of entries (BNodes) in the tree.
     */
    @Override()
    public int size() {
        return this.size;
    }

    /**
     * Clears the tree.
     */
    @Override()
    public void clear() {
        this.root = new BNode<K, V>();
        this.size = 0;
        this.modifyWithoutIterator();
    }

    /**
     * Checks whether the tree is empty.
     * @return REturns boolean confirming whether tree is empty.
     */
    @Override()
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Confirms whether the key exists in the tree structure.
     * @param  key (sought after key.)
     * @return     Returns boolean confirming existence of key.
     */
    @Override()
    public boolean hasKey(K key) {
        return this.get(key) != null;
    }

    /**
     * Returns boolean confirming whether value is in tree.
     * @param  value is what's sought after.
     * @return       returns boolean confirming wheher value is here.
     */
    @Override()
    public boolean hasValue(V value) {
        return this.hasValue(value, this.root).getValue() != null;
    }
    
    /**
     * Returns node containing the value we're searching for.
     * @param  value contained in node.
     * @param  curr  Current BNode.
     * @return       BNode containing desired value.
     */
    public BNode<K, V> hasValue(V value, BNode<K, V> curr) {
        if (curr.isLeaf()) {
            return new BNode<K, V>();
        }
        V tempV = curr.getValue();
        if (tempV != null && tempV.equals(value)) {
            return curr;
        }

        BNode<K, V> l = this.hasValue(value, curr.left);


        tempV = l.getValue();
        if (tempV != null && tempV.equals(value)) {
            return l;
        }

        BNode<K, V> r = this.hasValue(value, curr.right);
        return r;
    }

    /** 
     *  Get the value associated with key from subtree with given root node.
     *  @param key the key of the entry
     *  @return the value associated with the key, or null if not found
     */
    @Override()
    public V get(K key) {
        BNode<K, V> node = this.traverseByKey(key);
        if (node.isLeaf()) {
            return null;
        } else {
            return node.getValue();
        }
        
    }

    /** 
     *  Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return original value associated with the key, or null if not found
     */
    @Override()
    public V put(K key, V val) {
        BNode<K, V> node = this.traverseByKey(key);
        this.modifyWithoutIterator();
        if (node.isLeaf()) {

            node.setKey(key);
            node.setValue(val);

            node.left = new BNode<K, V>();
            node.right = new BNode<K, V>();

            this.size++;

            return null;
        } else {
            return node.setValue(val);
        }
    }

    /** 
     * Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the value associated with the removed key, or null if not found
     */
    @Override()
    public V remove(K key) {
        BNode<K, V> deleteMe = this.traverseByKey(key);
        if (deleteMe.isLeaf()) { // if key not in BSTMap, return null
            return null;
        }
        this.modifyWithoutIterator();

        boolean leftLeaf = deleteMe.left.isLeaf();
        boolean rightLeaf = deleteMe.right.isLeaf();

        V deleteMeVal = deleteMe.getValue();


        // if both children are leaves, cut off that node and make it a leaf.
        if (leftLeaf && rightLeaf) {
            this.leafMeAlone(deleteMe);
        } else {
            // we'll have to traverse to find the next smallest/largest value
            // to replace the removed key's node.
            BNode<K, V> switchy;
            BNode<K, V> switchyMom = deleteMe;
            boolean startedRight = leftLeaf;
            if (!leftLeaf) { //if the left subtree has stuffz in it
                switchy = deleteMe.left; // go one left
                while (!switchy.right.isLeaf()) { //& right as much as possible
                    switchyMom = switchy;
                    switchy = switchy.right;
                    // switchy = next smallest value from deleteMe
                }
            } else { //if the right subtree has stuffz in it
                switchy = deleteMe.right; // go one right
                while (!switchy.left.isLeaf()) { // & left as much as possible
                    switchyMom = switchy;
                    switchy = switchy.left;
                    // switchy = next largest value from deleteMe
                }
            }
            deleteMe.setKey(switchy.getKey()); // deleteMe <-- switchy
            deleteMe.setValue(switchy.getValue());

            // cut switchy off
            this.getRidOfNodeForDeletion(switchy, switchyMom, startedRight);
        }
        this.size--;
        return deleteMeVal; //return deleted value
    }

    /**
     * Store entries in a set.
     * @return Return set of entries.
     */
    @Override()
    public Set<Map.Entry<K, V>> entries() {
        Set<Map.Entry<K, V>> entrySet
            = new HashSet<Map.Entry<K, V>>(this.size());
        for (Map.Entry<K, V> entry : this.inOrder()) {
            entrySet.add(entry);
        }
        return entrySet;
    }

    /**
     * Stores distinct keys of tree in a set.
     * @return Set of keys.
     */
    @Override()
    public Set<K> keys() {
        Set<K> keySet = new HashSet<K>(this.size());
        for (Map.Entry<K, V> entry : this.inOrder()) {
            keySet.add(entry.getKey());
        }
        return keySet;

    }

    /**
     * Store values of tree in a colleciton.
     * @return Collection of values.
     */
    @Override()
    public Collection<V> values() {
        List<V> valueList = new ArrayList<V>(this.size());
        for (Map.Entry<K, V> entry : this.inOrder()) {
            valueList.add(entry.getValue());
        }
        return valueList;
    }

    /* -----   BSTMap-specific functions   ----- */

    /** 
     * Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the min key
     */
    public K firstKey(BNode<K, V> curr) {
        while (!curr.left.isLeaf()) { // & left as much as possible
            curr = curr.left;
        }
        return curr.getKey();
    }

    /** 
     * Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the max key
     */
    public K lastKey(BNode<K, V> curr) {
        while (!curr.right.isLeaf()) { // & right as much as possible
            curr = curr.right;
        }
        return curr.getKey();
    }

    /** 
     * Inorder traversal that produces an iterator over key-value pairs.
     *  @return an iterable list of entries ordered by keys
     */
    public LinkedList<Map.Entry<K, V>> inOrder() {
        return this.inOrder(this.root);
        
    }
    
    /**
     *  Inorder traversal produces an iterator over entries in a subtree.
     *  @param curr     the root of the subtree to iterate over
     *  ordered:  Collection in which to place the inOrder
     *                  traversal of the BSTMap
     *  @return         an iterable list of entries ordered by keys
     */
    private LinkedList<Map.Entry<K, V>> inOrder(BNode<K, V> curr) { //,
        // Collection<Map.Entry<K, V>> ordered) {
        
        if (curr.isLeaf()) {
            return new LinkedList<Map.Entry<K, V>>();
        }

        LinkedList<Map.Entry<K, V>> ordered = this.inOrder(curr.left);

        ordered.add(curr);

        ordered.addAll(this.inOrder(curr.right));

        return ordered;        
    }

    /** 
     * Returns a copy of the portion of this map whose keys are in a range.
     *  @param fromKey the starting key of the range, inclusive if found
     *  @param toKey the ending key of the range, inclusive if found
     *  @return the resulting submap
     */
    public BSTMap<K, V> subMap(K fromKey, K toKey) {
        BSTMap<K, V> tree = new BSTMap<K, V>();
        for (Map.Entry<K, V> entry : this.inOrder()) {
            if (entry.getKey().compareTo(fromKey) >= 0
                && entry.getKey().compareTo(toKey) <= 0) {
                tree.put(entry.getKey(), entry.getValue());
            }
        }
        return tree;
    }

    /* ---------- from Iterable ---------- */

    /**
     * Iterator Constructor.
     * @return Returns iterator.
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new BSTMapIterator();
    }

    /**
     * Nothing Special.
     * @param action -> Nothing Special.
     */
    @Override
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        // you do not have to implement this
    }

    /**
     * Nothing Special.
     * @return Nothing Special.
     */
    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        // you do not have to implement this
        return null;
    }

    /* -----  BSTMapIterator ----- */

    /**
     * BST Iterator Class.
     */
    private class BSTMapIterator implements Iterator<Map.Entry<K, V>> {
        
        /** Internal List of Nodes. */
        private LinkedList<Map.Entry<K, V>> internalList;
        /** Internal List Iterator. */
        private Iterator<Map.Entry<K, V>> internalListIterator;
        /** Iterator Modification Counter. */
        private int iteratorModCounter;
        /** Current Entry. */
        private Map.Entry<K, V> currentEntry;
        /** Current index. */
        private int index;
        /** can be removed. */
        private boolean removable;


        /**
         * Iterator for Binary Search Tree.
         */
        BSTMapIterator() {
            this.iteratorModCounter = BSTMap.this.modCounter;
            this.internalList = BSTMap.this.inOrder();
            this.internalListIterator = this.internalList.iterator();
            this.index = 0;
            this.removable = false;
        }

        /**
         * Allows iterator to traverse/move curr to next node in structure.
         * @return New current node (next node in structure).
         * @throws ConcurrentModificationException : Invalidates the iterator 
         * if outer operation changes the tree structure.
         * @throws NoSuchElementException : Let's us know if there's no such
         * element...
         */
        public Map.Entry<K, V> next() throws ConcurrentModificationException,
            NoSuchElementException {
            if (!this.iteratorStillValid()) {
                throw new ConcurrentModificationException();
            }
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.removable = true;
            this.index++;
            this.currentEntry = this.internalListIterator.next();
            return this.currentEntry;
        }

        /**
         * Checks if there's another node to be traversed in the stucture.
         * @return boolean (informs whether another node follows current.)
         * @throws ConcurrentModificationException : Invalidates interator
         * if outer operation changes structure of tree.
         */
        public boolean hasNext() throws ConcurrentModificationException {
            if (!this.iteratorStillValid()) {
                throw new ConcurrentModificationException();
            }

            return this.internalListIterator.hasNext();
        }

        /**
         * Removes the current node.
         * @throws ConcurrentModificationException Invalidates Iterator.
         * if outer operation changes the structure.
         */
        public void remove() throws ConcurrentModificationException {
            if (!this.iteratorStillValid()) {
                throw new ConcurrentModificationException();
            }

            if (this.removable) {
                // this.internalListIterator.remove();
                
                K keyToRemove = this.currentEntry.getKey();
                BSTMap.this.remove(keyToRemove);


                this.internalList = BSTMap.this.inOrder();
                this.internalListIterator = this.internalList.iterator();
                this.iteratorModCounter = BSTMap.this.modCounter;
                
                int reallignmentSteps = this.index - 1;
                this.index = 0;
                System.out.println("Realligning to inOrder");
                for (int i = 0; i < reallignmentSteps; i++) {
                    this.next();
                }
                this.removable = false;

            }
        }

        /**
         * Returns true is the hashmap hasn't invalidated the iterator by.
         * changing without the iterator.
         * @return true if hashmap hasn't invalidated the iterator.
         */
        private boolean iteratorStillValid() {

            return this.iteratorModCounter == BSTMap.this.modCounter;
        }


    }


    /* ---------- personal additions ---------- */

    /**
     * Traverses through the BSTMap to find the BNode with key.
     * @param  key Key to traverse towards.
     * @return     The node with that Key or null if not in map.
     */
    private BNode<K, V> traverseByKey(K key) {
        BNode<K, V> curr = this.root;
        while (!curr.isLeaf() && !key.equals(curr.getKey())) {
            if (key.compareTo(curr.getKey()) > 0) {
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return curr;
    }
    /**
     * Gets rid of a node for deletion by linking parents to children
     * or turning a childless sad node into a leaf.
     * @param node the node that's operated on.
     * @param nodeParent parent of the node operated on
     * @param startedRight true if traverse started by going right
     */
    private void getRidOfNodeForDeletion(BNode<K, V> node,
        BNode<K, V> nodeParent, boolean startedRight) {

        if (node.left.isLeaf() && node.right.isLeaf()) {
            this.leafMeAlone(node);
        } else {
            BNode<K, V> toLink;
            if (startedRight) {
                toLink = node.right;
            } else {
                toLink = node.left;
            }
            if (nodeParent.right == node) {
                nodeParent.right = toLink;
            } else {
                nodeParent.left = toLink;
            }
        }
    }
    /**
     * Transforms the node that's operated on into a leaf.
     * @param node the node that's operated on.
     */
    private void leafMeAlone(BNode<K, V> node) {
        node.right = null;
        node.left = null;
        node.setKey(null);
        node.setValue(null);
    }
    /**
     * Simply offer's method to print string of list.
     * @return String of Ordered List.
     */
    public String toString() {
        return this.inOrder().toString();
    }

    /**
     * Increases the modification counter for use by the HashMapIterator.
     */
    private void modifyWithoutIterator() {
        this.modCounter++;
    }

}
