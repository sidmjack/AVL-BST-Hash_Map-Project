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


/** 
 * Binary Search Tree Map implementation with inner Node class.
 *  @param <K> the base type of the keys in the entries
 *  @param <V> the base type of the values
 */
public class AVLMap<K extends Comparable<? super K>, V> extends BSTMap<K, V> {



    /** 
     *  Put <key,value> entry into subtree with given root node.
     *  @param key the key of the entry
     *  @param val the value of the entry
     *  @param curr the root of the subtree into which to put the entry
     *  @return original value associated with the key, or null if not found
     */
    @Override()
    public V put(K key, V val) {


    }


    /** 
     * Remove entry with specified key from subtree with given root node.
     *  @param key the key of the entry to remove, if there
     *  @param curr the root of the subtree from which to remove the entry
     *  @return the value associated with the removed key, or null if not found
     */
    @Override()
    public V remove(K key) {
        return remove(key, this.root).getValue();
    }

    private BNode<K, V> remove(K key, BNode<K, V> node) {
        
        if (node.isLeaf()) {
            return new BNode();
        } else if (this.left.isLeaf() ^ this.right.isLeaf()) {
            
        }
    }


    private BNode<K, V> rotate(BNode<K, V> node) {

    }

    private BNode<K, V> singleL(BNode<K, V> node) {
        
    }

    private BNode<K, V> singleR(BNode<K, V> node) {
        
    }

    private BNode<K, V> doubleLR(BNode<K, V> node) {
        
    }

    private BNode<K, V> doubleRL(BNode<K, V> node) {
        
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

            super.next();
        }


        /**
         * Checks if there's another node to be traversed in the stucture.
         * @return boolean (informs whether another node follows current.)
         * @throws ConcurrentModificationException : Invalidates interator
         * if outer operation changes structure of tree.
         */
        public boolean hasNext() throws ConcurrentModificationException {
            super.hasNext();
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

}
