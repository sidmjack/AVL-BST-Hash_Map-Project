import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/** Binary Search Tree Map implementation with inner Node class.
 *  @param <K> the base type of the keys in the entries
 *  @param <V> the base type of the values
 */
public class BSTMap<K extends Comparable<? super K>, V>
    implements MapJHU<K, V>, Iterable<Map.Entry<K, V>> {

    /** Inner node class.  Do not make this static because you want
        the K to be the same K as in the BSTMap header.
    */
    public class BNode<K extends Comparable<? super K>, V> implements Map.Entry<K, V> {

        /** The key of the entry (null if sentinel node). */
        private K key;
        /** The value of the entry (null if sentinel node). */
        private V value;
        /** The left child of this node. */
        private BNode<K, V> left;
        /** The right child of this node. */
        private BNode<K, V> right;

        /** Create a new node with a particular key and value.
         *  @param k the key for the new node
         *  @param v the value for the new node
         */
        public BNode(K k, V v) {
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
        }

        BNode() {
            this(null, null);
        }

        /** Check whether this node is a leaf sentinel, based on key.
         *  @return true if leaf, false otherwise
         */
        public boolean isLeaf() {
            return this.getKey() == null;  // sentinel-based implementation
        }

        /**
         * Replaces the value corresponding to this entry with the specified
         * value.
         *
         * @param k new value to be stored in this entry
         * @return the old value corresponding to the entry
         */
        public K setKey(K k) {
            K oldKey = this.key;
            this.key = k;
            return oldKey;
        }

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         */
        public K getKey() {
            return this.key;
        }

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value corresponding to this entry
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

        public String toString() {
            return "(" + this.getKey() + ", " + this.getValue() + ")";
        }

        public boolean equals(BNode<K, V> that) {
            return this.key.equals(that.key) && this.value.equals(that.value);
        }

        public int compareTo(BNode<K, V> that) {
            return this.key.compareTo(that.key);
        }

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

    /** Create an empty tree with a sentinel root node.
     */
    public BSTMap() {
        // empty tree is a sentinel for the root
        this.modCounter = 0;
        this.clear();
    }

    @Override()
    public int size() {
        return this.size;
    }

    @Override()
    public void clear() {
        this.root = new BNode<K, V>();
        this.size = 0;
        this.modifyWithoutIterator();
    }

    @Override()
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override()
    public boolean hasKey(K key) {
        return this.get(key) != null;
    }

    @Override()
    public boolean hasValue(V value) {
        return this.hasValue(value, this.root).getValue() != null;
    }
    
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

    /** Get the value associated with key from subtree with given root node.
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

    /** Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return the original value associated with the key, or null if not found
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

    /** Remove entry with specified key from subtree with given root node.
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
            if (!leftLeaf) { //if the left subtree has stuffz in it
                switchy = deleteMe.left; // go one left
                while (!switchy.right.isLeaf()) { // & right as much as possible
                    switchy = switchy.right;
                    // switchy = next smallest value from deleteMe
                }
            } else { //if the right subtree has stuffz in it
                switchy = deleteMe.right; // go one right
                while (!switchy.left.isLeaf()) { // & left as much as possible
                    switchy = switchy.left;
                    // switchy = next largest value from deleteMe
                }
            }
            deleteMe.setKey(switchy.getKey()); // deleteMe <-- switchy
            deleteMe.setValue(switchy.getValue());
            this.leafMeAlone(switchy); // cut switchy off
        }
        this.size--;
        return deleteMeVal; //return deleted value
    }

    
    @Override()
    public Set<Map.Entry<K, V>> entries() {
        Set<Map.Entry<K, V>> entrySet
            = new HashSet<Map.Entry<K, V>>(this.size());
        for (Map.Entry<K, V> entry : this.inOrder()) {
            entrySet.add(entry);
        }
        return entrySet;
    }

    @Override()
    public Set<K> keys() {
        Set<K> keySet = new HashSet<K>(this.size());
        for (Map.Entry<K, V> entry : this.inOrder()) {
            keySet.add(entry.getKey());
        }
        return keySet;

    }

    @Override()
    public Collection<V> values() {
        List<V> valueList = new ArrayList<V>(this.size());
        for (Map.Entry<K, V> entry : this.inOrder()) {
            valueList.add(entry.getValue());
        }
        return valueList;
    }

    /* -----   BSTMap-specific functions   ----- */

    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the min key
     */
    public K firstKey(BNode<K, V> curr) {
        while (!curr.left.isLeaf()) { // & left as much as possible
            curr = curr.left;
        }
        return curr.getKey();
    }

    /** Get the smallest key in a subtree.
     *  @param curr the root of the subtree to search
     *  @return the max key
     */
    public K lastKey(BNode<K, V> curr) {
        while (!curr.right.isLeaf()) { // & right as much as possible
            curr = curr.right;
        }
        return curr.getKey();
    }

    /** Inorder traversal that produces an iterator over key-value pairs.
     *  @return an iterable list of entries ordered by keys
     */
    public Iterable<Map.Entry<K, V>> inOrder() {
        LinkedList<Map.Entry<K, V>> ordered = new LinkedList<Map.Entry<K, V>>();
        this.inOrder(this.root, ordered);
        return ordered;
    }
    
    /**
     *  Inorder traversal produces an iterator over entries in a subtree.
     *  @param curr     the root of the subtree to iterate over
     *  @param ordered  Collection in which to place the inOrder
     *                  traversal of the BSTMap
     *  @return         an iterable list of entries ordered by keys
     */
    private void inOrder(BNode<K, V> curr,
        Collection<Map.Entry<K, V>> ordered) {


        // that is, an empty tree should not fill up it's in order list
        // if it tried to, it would pull null pointer exceptions on the
        // babies it doesn't have
        if (curr.isLeaf()) { 
            return;
        }

        if (!curr.left.isLeaf()) {
            this.inOrder(curr.left, ordered);
        }
        ordered.add(curr);
        if (!curr.right.isLeaf()) {
            this.inOrder(curr.right, ordered);
        }
        
    }

    /** Returns a copy of the portion of this map whose keys are in a range.
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

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new BSTMapIterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        // you do not have to implement this
    }

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        // you do not have to implement this
        return null;
    }

    /* -----  BSTMapIterator ----- */

    private class BSTMapIterator implements Iterator<Map.Entry<K, V>> {
        
        private Iterable<Map.Entry<K, V>> internalList;
        private Iterator<Map.Entry<K, V>> internalListIterator;
        private int iteratorModCounter;
        private Map.Entry<K, V> currentEntry;

        BSTMapIterator() {
            this.iteratorModCounter = BSTMap.this.modCounter;
            this.internalList = BSTMap.this.inOrder();
            this.internalListIterator = this.internalList.iterator();
        }

        public Map.Entry<K, V> next() throws ConcurrentModificationException {
            if (!this.iteratorStillValid()) {
                throw new ConcurrentModificationException();
            }
            this.currentEntry = this.internalListIterator.next();
            return this.currentEntry;
        }

        public boolean hasNext() throws ConcurrentModificationException {
            if (!this.iteratorStillValid()) {
                throw new ConcurrentModificationException();
            }

            return this.internalListIterator.hasNext();
        }

        public void remove() throws ConcurrentModificationException {
            if (!this.iteratorStillValid()) {
                throw new ConcurrentModificationException();
            }
            this.internalListIterator.remove();
            K keyToRemove = this.currentEntry.getKey();
            BSTMap.this.remove(keyToRemove);

            this.iteratorModCounter = BSTMap.this.modCounter;
        }

        /**
         * Returns true is the hashmap hasn't invalidated the iterator by
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
     * @param  key Key to traverse towards
     * @return     THe node with that Key or null if not in map
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

    private void leafMeAlone(BNode<K, V> node) {
        node.right = null;
        node.left = null;
        node.setKey(null);
        node.setValue(null);
    }

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
