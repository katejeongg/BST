import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import static java.lang.Math.max;

public class BST<T extends Comparable<? super T>> {

    private BSTNode<T> root;
    private int size;

    public BST() {
    }

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

    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        root = addHelper(data, root);
        size++;
    }

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

    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        BSTNode<T> removedNode = new BSTNode<>(data);
        root = removeHelper(data, root, removedNode);
        return removedNode.getData();
    }

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

    private BSTNode<T> removeSecondHelper(T data, BSTNode<T> node, BSTNode<T> replacedNode) {
        if (node.getLeft() == null) {
            replacedNode.setData(node.getData());
            return node.getRight();
        }
        node.setLeft(removeSecondHelper(data, node.getLeft(), replacedNode));
        return node;
    }

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

    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        return containsHelper(data, root) != null;
    }

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

    public List<T> preorder() {
        // left lever
        List<T> list = new ArrayList<>();
        preorderHelper(root, list);
        return list;
    }

    private void preorderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            list.add(node.getData());
            preorderHelper(node.getLeft(), list);
            preorderHelper(node.getRight(), list);
        }
    }

    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        inorderHelper(root, list);
        return list;
    }

    private void inorderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            inorderHelper(node.getLeft(), list);
            list.add(node.getData());
            inorderHelper(node.getRight(), list);
        }
    }

    public List<T> postorder() {
        // right lever
        List<T> list = new ArrayList<>();
        postorderHelper(root, list);
        return list;
    }

    private void postorderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            postorderHelper(node.getLeft(), list);
            postorderHelper(node.getRight(), list);
            list.add(node.getData());
        }
    }

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

    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(BSTNode<T> node) {
        if (node == null) {
            return -1;
        }
        int left = heightHelper(node.getLeft());
        int right = heightHelper(node.getRight());
        return max(left, right) + 1;
    }

    public void clear() {
        root = null;
        size = 0;
    }

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
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * @return the size of the tree
     */
    public int size() {
        return size;
    }
}
