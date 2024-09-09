import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import static java.lang.Math.max;

/**
 * Your implementation of a BST.
 *
 * @author KATE JEONG
 * @version 1.0
 * @userid kjeong40
 * @GTID 903886263
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        for (T dataElement : data) {
            if (dataElement == null) {
                throw new IllegalArgumentException("Data is null.");
            }
            add(dataElement);
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        root = addHelper(data, root);
        size++;
    }

    /** Recursive helper for add method
     *
     * @param data data to be added in
     * @param node node to add into
     * @return node
     */
    private BSTNode<T> addHelper(T data, BSTNode<T> node) {
        if (node == null) {
            return new BSTNode<T>(data);
        }
        if (data.compareTo(node.getData()) < 0) {
            node.setLeft(addHelper(data, node.getLeft()));
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRight(addHelper(data, node.getRight()));
        }
        return node;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        BSTNode<T> removedNode = new BSTNode<>(data);
        root = removeHelper(data, root, removedNode);
        return removedNode.getData();
    }

    /** Recursive helper for remove method
     *
     * @param data data to remove
     * @param node current node
     * @param removedNode dummy node that holds node to remove
     * @return node to remove
     */
    private BSTNode<T> removeHelper(T data, BSTNode<T> node, BSTNode<T> removedNode) {
        if (node == null) {
            throw new NoSuchElementException("Data is not in tree.");
        }
        if (node.getData().compareTo(data) < 0) {
            node.setRight(removeHelper(data, node.getRight(), removedNode));
        } else if (node.getData().compareTo(data) > 0) {
            node.setLeft(removeHelper(data, node.getLeft(), removedNode));
        } else {
            removedNode.setData(node.getData());
            size--;
            // 3 removal methods
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            }
            if (node.getLeft() == null) {
                return node.getRight();
            }
            if (node.getRight() == null) {
                return node.getLeft();
            }
            if (node.getRight() != null && node.getLeft() != null) {
                // next smallest ; one right and then as left as possible
                BSTNode<T> replacedNode = new BSTNode<>(null);
                node.setRight(removeSecondHelper(data, node.getRight(), replacedNode));
                node.setData(replacedNode.getData());
            }
        }
        return node;
    }

    /** Recursive helper for removing successor
     *
     * @param data data to be removed
     * @param node current node
     * @param replacedNode dummy node
     * @return node
     */
    private BSTNode<T> removeSecondHelper(T data, BSTNode<T> node, BSTNode<T> replacedNode) {
        if (node.getLeft() == null) {
            replacedNode.setData(node.getData());
            return node.getRight();
        }
        node.setLeft(removeSecondHelper(data, node.getLeft(), replacedNode));
        return node;
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        T result = containsHelper(data, root);
        if (result == null) {
            throw new NoSuchElementException("Data is not in tree.");
        }
        return result;
    }


    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        return containsHelper(data, root) != null;
    }

    /** Recursive helper for contains method
     *
     * @param data data we looking for
     * @param node current node
     * @return node's data
     */
    private T containsHelper(T data, BSTNode<T> node) {
        if (node == null) {
            return null;
        }
        if (data.compareTo(node.getData()) == 0) {
            return node.getData();
        } else if (data.compareTo(node.getData()) < 0) {
            return containsHelper(data, node.getLeft());
        } else {
            return containsHelper(data, node.getRight());
        }
    }
    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        // left lever
        List<T> list = new ArrayList<>();
        preorderHelper(root, list);
        return list;
    }

    /**
     * Recursive helper method for preorder method
     *
     * @param node passed in node
     * @param list updated list after using preorder method
     */
    private void preorderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            list.add(node.getData());
            preorderHelper(node.getLeft(), list);
            preorderHelper(node.getRight(), list);
        }
    }
    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        inorderHelper(root, list);
        return list;
    }

    /**
     * Recursive helper method for inorder method
     *
     * @param node passed in node
     * @param list updated list after using inorder method
     */
    private void inorderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            inorderHelper(node.getLeft(), list);
            list.add(node.getData());
            inorderHelper(node.getRight(), list);
        }
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        // right lever
        List<T> list = new ArrayList<>();
        postorderHelper(root, list);
        return list;
    }

    /**
     * Recursive helper method for postorder method
     *
     * @param node passed in node
     * @param list updated list after using postorder method
     */
    private void postorderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            postorderHelper(node.getLeft(), list);
            postorderHelper(node.getRight(), list);
            list.add(node.getData());
        }
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        Queue<BSTNode<T>> q = new LinkedList<>();
        q.add(root);
        ArrayList<T> arr = new ArrayList<>(size);
        if (root == null) {
            return arr;
        }
        while (q.size() > 0) {
            BSTNode<T> result = q.remove();
            if (result.getLeft() != null) {
                q.add(result.getLeft());
            }
            if (result.getRight() != null) {
                q.add(result.getRight());
            }
            arr.add(result.getData());
        }
        return arr;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelper(root);
    }

    /** Recursive helper for height method
     *
     * @param node current node
     * @return height
     */
    private int heightHelper(BSTNode<T> node) {
        if (node == null) {
            return -1;
        }
        int left = heightHelper(node.getLeft());
        int right = heightHelper(node.getRight());
        return max(left, right) + 1;
    }
    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Finds and retrieves the k-largest elements from the BST in sorted order,
     * least to greatest.
     *
     * This must be done recursively.
     *
     * In most cases, this method will not need to traverse the entire tree to
     * function properly, so you should only traverse the branches of the tree
     * necessary to get the data and only do so once. Failure to do so will
     * result in an efficiency penalty.
     *
     * EXAMPLE: Given the BST below composed of Integers:
     *
     *                50
     *              /    \
     *            25      75
     *           /  \
     *          12   37
     *         /  \    \
     *        10  15    40
     *           /
     *          13
     *
     * kLargest(5) should return the list [25, 37, 40, 50, 75].
     * kLargest(3) should return the list [40, 50, 75].
     *
     * Should have a running time of O(log(n) + k) for a balanced tree and a
     * worst case of O(n + k), with n being the number of data in the BST
     *
     * @param k the number of largest elements to return
     * @return sorted list consisting of the k largest elements
     * @throws java.lang.IllegalArgumentException if k < 0 or k > size
     */
    public List<T> kLargest(int k) {
        // inorder traversal backwards
        // stop at k
        // add into linkedlist O(1) operation (should sort from smallest -> biggest for me)
        if (k < 0 || k > size) {
            throw new IllegalArgumentException("Invalid value k inputted.");
        }
        LinkedList<T> list = new LinkedList<>();
        kLargestHelper(k, root, list);
        return list;
    }

    /** Recursive helper for kLargest method
     *
     * @param k integer k for num of largest elements to return
     * @param node current node
     * @param list list of sorted largest elements
     */
    private void kLargestHelper(int k, BSTNode<T> node, LinkedList<T> list) {
        if (node != null) {
            if (list.size() < k) {
                kLargestHelper(k, node.getRight(), list);
                if (list.size() < k) {
                    list.addFirst(node.getData());
                }
                kLargestHelper(k, node.getLeft(), list);
            }
        }
    }
    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
