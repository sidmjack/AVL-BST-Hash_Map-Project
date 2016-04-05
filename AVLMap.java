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
import java.Math.abs;


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
        super.put(key, val);
        BNode<K, V> b = imBalanced();
        this.root = rotate(b);
    }

    /**
     * Returns the first encountered imbalanced node in an AVL tree
     * @return bNode (first imbalanced node in tree).
     */
    public BNode<K, V> imBalanced() {
        BNode<K, V> b = this.root;
        return imBalanced(b);
    }

    /**
     * Recursively searches for the first incidence of an imbalanced node.
     * @param  bNode : Node checked for imbalance.
     * @return Imbalanced node, or "empty" node if not found.
     */
    public BNode<K, V> imBalanced(BNode<K, V> bNode) {
        BNode<K, V> b;
        // Return an Empty BNode if bottom of AVLTree is reached.
        if (bNode == null || bNode.isLeaf() == true) {
            return new BNode(); //Assuming this has a height of 0.
        // Else, keep searching until bottom of AVLTree is reached,
        // or until an imbalanced node is found.
        } else {             
            //Check to see if Current Node is Imbalanced.
            if (Math.abs(bNode.balanceFactor()) > 1) {
                return bNode;  
            //If current node isn't imbalanced, check children for imbalance.
            } else {
                BNode<K, V> nodeA, nodeB; 
                nodeA = imBalanced(bNode.left);
                nodeB = imBalanced(bNode.right);
                // Not sure if this even needs to be checked???
                if (Math.abs(nodeA.balanceFactor()) > 1 &&
                    Math.abs(nodeB.balanceFactor()) > 1) {
                    if (nodeA.height > nodeB.height) {
                        return nodeA;
                    } else {
                        return nodeB;
                    }
                } else if (Math.abs(nodeA.balanceFactor()) > 1) {
                    return nodeA;
                } else if (Math.abs(nodeB.balanceFactor()) > 1) {
                    return nodeB;
                } else {
                    return new BNode();
                }
            }
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
        return remove(key, this.root).getValue();
    }

    private BNode<K, V> remove(K key, BNode<K, V> node) {
        
        if (node.isLeaf()) {
            return new BNode();
        } else if (this.left.isLeaf() ^ this.right.isLeaf()) {
            
        }
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

    private BNode<K, V> singleL(BNode<K, V> node) {
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

    private BNode<K, V> singleR(BNode<K, V> node) {
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

    private BNode<K, V> doubleLR(BNode<K, V> node) {
        node.left = this.singleL(node.left);
        return this.singleR(node);
    }

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
