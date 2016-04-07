// Name: Lawrence Wolf-Sonkin & Sidney Jackson
// JHU Login: lwolfso1  & sjacks85
// Course: Data Structure (600.226.02)
// Project: Project #3A (Basic AVL Implementation)
// Due Date: 03-27-2016
// Last Modified: 03-28-2016


import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Iterator;
// import java.Math.abs;


/** 
 * Binary Search Tree Map implementation with inner Node class.
 *  @param <K> the base type of the keys in the entries
 *  @param <V> the base type of the values
 */
public class AVLMap<K extends Comparable<? super K>, V> extends BSTMap<K, V> {


    public AVLMap() {
        super();
    }


    /**
     * the last node removed from the remove function.
     */
    private V lastValueRemoved;

    /**
     * the last node removed from the put function.
     */
    private V lastValuePutted;

    /** 
     *  Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return original value associated with the key, or null if not found
     */
    @Override()
    public V put(K key, V val) {
        this.modifyWithoutIterator();
        this.root = put(this.root ,key, val);
        return this.lastValuePutted;
    }

    /** 
     *  Put <key,value> entry into subtree with given root node.
     *  @param node the root of the subtree into which to put the entry
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @return original value associated with the key, or null if not found
     */
    private BNode<K, V> put(BNode<K,V> node, K key, V val) {
        if (node.isLeaf()) {
            this.lastValuePutted = null;
            this.putLeaf(node, key, val);
            return node;
        } else if (key.compareTo(node.getKey()) > 0) {
            node.right = this.put(node.right, key, val);
        } else if (key.compareTo(node.getKey()) < 0) {
            node.left = this.put(node.left, key, val);
        } else {
            
            this.lastValuePutted = node.getValue();
            node.setValue(val);

        }
        
        node.updateHeight();
        
        return this.rotate(node);
    }

    public void putLeaf(BNode<K,V> currNode, K key, V val) {
        currNode.setKey(key);
        currNode.setValue(val);
        currNode.left = new BNode<K,V>();
        currNode.right = new BNode<K,V>();
        currNode.updateHeight();
        size++;
    }

    /** 
     * Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the value associated with the removed key, or null if not found
     */
    @Override()
    public V remove(K key) {
        this.modifyWithoutIterator();
        this.root = remove(key, this.root);
        return this.lastValueRemoved;

    }

    /**
     * Removes the node with the given key from the AVLTree
     * @param  key  key to remove
     * @param  node part of subtree to look through
     * @return      resultant subtree after the removal
     */
    private BNode<K, V> remove(K key, BNode<K, V> node) {
        if (node.isLeaf()) {
            this.lastValueRemoved = null;
            return node;
        } else if (key.compareTo(node.getKey()) > 0) {
            node.right = this.remove(key, node.right);
        } else if (key.compareTo(node.getKey()) < 0) {
            node.left = this.remove(key, node.left);
        } else {
            
            boolean leftLeaf = node.left.isLeaf();
            boolean rightLeaf = node.right.isLeaf();


            boolean leftStart = rightLeaf;
            this.lastValueRemoved = node.getValue();
            node = removeHelperSwitch(node, true, leftStart, node);

            this.size--;

        }

        node.updateHeight();

        return this.rotate(node);

    }

    /**
     * Traverses down the tree to find the next smallest or largest key and
     * swaps the values betweent the node to delete and the next smallest or
     * largest key
     * @param  node      node that's currently being worked on
     * @param  firstIt   true if the first iteration of removeHelperSwitch
     *                   false otherwise
     * @param  leftStart true if traverse starts going left, false else
     * @param  deleteMe  node that's going to be deleted from the AVLMap
     * @return           Returns the subtree following this removal
     */
    private BNode<K, V> removeHelperSwitch(BNode<K, V> node, boolean firstIt, boolean leftStart, BNode<K, V> deleteMe) {
        boolean goLeft = (firstIt == leftStart);
        BNode<K, V> placeToGo;
        BNode<K, V> placeNextDoor;
        if (goLeft) {
            placeToGo = node.left;
            placeNextDoor = node.right;
        } else {
            placeToGo = node.right;
            placeNextDoor = node.left;
        }

        if (!placeToGo.isLeaf()) {
            BNode<K, V> child = removeHelperSwitch(placeToGo, false, leftStart, deleteMe);
            
            if (goLeft) {
                node.left = child;
            } else {
                node.right = child;
            }

            node.updateHeight();
            return this.rotate(node);

        } else {
            deleteMe.setKey(node.getKey());
            deleteMe.setValue(node.getValue());
            return placeNextDoor;
        }
    }

    public AVLMap<K, V> subMap(K fromKey, K toKey) {
        AVLMap<K, V> tree = new AVLMap<K, V>();
        for (Map.Entry<K, V> entry : this.inOrder()) {
            if (entry.getKey().compareTo(fromKey) >= 0
                && entry.getKey().compareTo(toKey) <= 0) {
                tree.put(entry.getKey(), entry.getValue());
            }
        }
        return tree;
    }



    /**
     * Function to choose and operate the proper rotation to rebalance a node
     * @param  node node to rotate in order to fix balace of that node
     * @return      the new root of this subtree
     */
    private BNode<K, V> rotate(BNode<K, V> node) {
        final int bf = node.balanceFactor();
        if (node.isLeaf() || node.isBalanced()) {
            return node;
        } else if (bf <= -2) { // right heavy
            final int rightBf = node.right.balanceFactor();
            if (rightBf >= 2) { // right subtree left-heavy
                return this.doubleLR(node);
            } else { // right subtree right-heavy
                return this.singleL(node);
            }
        } else { // left heavy
            final int leftBf = node.left.balanceFactor();
            if (leftBf <= -2) { //left subtree right-heavy
                return this.doubleRL(node);
            } else { // left subtree left-heavy
                return this.singleR(node);
            }
        }
    }

    /**
     * Single Right Left Rotation.
     * @param  node.
     * @return      node.
     */
    private BNode<K, V> singleR(BNode<K, V> node) {
        BNode<K, V> s = node;
        BNode<K, V> c = node.right;
        BNode<K, V> x = node.left;
        BNode<K, V> a = x.left;
        BNode<K, V> b = x.right;
        s.left = b;
        s.right = c;
        x.left = a;
        x.right = s;

        s.updateHeight();
        x.updateHeight();

        return x;
    }

    /**
     * Single Left Rotation.
     * @param node.
     * @return node.
     */
    private BNode<K, V> singleL(BNode<K, V> node) {
        BNode<K, V> s = node;
        BNode<K, V> a = node.left;
        BNode<K, V> x = node.right;
        BNode<K, V> b = x.left;
        BNode<K, V> c = x.right;

        s.left = a;
        s.right = b;
        x.left = s;
        x.right = c;

        s.updateHeight();
        x.updateHeight();
        
        return x;
    }

    /**
     * Double Left Right Rotation.
     * @param node.
     * @return node.
     */
    private BNode<K, V> doubleLR(BNode<K, V> node) {
        node.left = this.singleL(node.left);
        return this.singleR(node);
    }
    
    /**
     * Double Right Left Rotation.
     * @param  node [description]
     * @return      [description]
     */
    private BNode<K, V> doubleRL(BNode<K, V> node) {
        node.right = this.singleR(node.right);
        return this.singleL(node);
        
    }


    /* ---------- from Iterable ---------- */

    /**
     * Iterator Constructor.
     * @return Returns iterator.
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new AVLMapIterator();
    }


    /* -----  AVLMapIterator ----- */

    /**
     * AVL Iterator Class.
     */
    private class AVLMapIterator extends BSTMap<K, V>.BSTMapIterator {
        
        /**
         * Iterator for AVL Tree.
         */
        AVLMapIterator() {
            super();
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

            return super.next();
        }


        /**
         * Checks if there's another node to be traversed in the stucture.
         * @return boolean (informs whether another node follows current.)
         * @throws ConcurrentModificationException : Invalidates interator
         * if outer operation changes structure of tree.
         */
        public boolean hasNext() throws ConcurrentModificationException {
            return super.hasNext();
        }


        /**
         * Removes the current node.
         * @throws ConcurrentModificationException Invalidates Iterator.
         * if outer operation changes the structure.
         */
        public void remove() throws ConcurrentModificationException {
            super.remove();

        }

    }


    /* ---------- personal additions ---------- */

    /**
     * Traverses through the AVLMap to find the BNode with key.
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
     * Print Functions.
     */
    public void print() {
        this.print(this.root);
    }

    /**
     * Prints.
     * @param node [description].
     */
    public void print(BNode<K, V> node) {
        if (node.isLeaf()) {
            return;
        } else {
            System.out.print("{");
            this.print(node.left);
            System.out.print(node);
            this.print(node.right);
            System.out.print("}");
        }
    }

    /**
     *  Gets Height.
     * @return height.
     */
    public int getHeight() {
        return this.root.getHeight();
    }

    /**
     * Get Balance.
     * @return integer.
     */
    public int getBalance() {
        return this.root.balanceFactor();
    }

    /**
     * Gets Balanve.
     * @param  node .
     * @return      integer.
     */
    public int getBalance(BNode<K, V> node) {
        return node.balanceFactor();
    }


}
