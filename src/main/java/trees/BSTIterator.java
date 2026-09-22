//// BSTIterator.java
//package trees;  // Тот же пакет
//
//import java.util.Iterator;
//import java.util.NoSuchElementException;
//import java.util.ConcurrentModificationException;
//import java.util.Stack;
//
//public class BSTIterator<T extends Comparable<T>> implements Iterator<T> {
//    private final Stack<TreeNode<T>> stack = new Stack<>();
//    private final BinarySearchTree<T> tree;  // ссылка на дерево
//    private int expectedModCount;
//    private TreeNode<T> lastReturned = null;
//
//    public BSTIterator(BinarySearchTree<T> tree) {
//        this.tree = tree;
//        this.expectedModCount = tree.modCount;  // прямой доступ к package-private полю
//        pushLeft(tree.getRoot());
//    }
//
//    private void pushLeft(TreeNode<T> node) {
//        while (node != null) {
//            stack.push(node);
//            node = node.getLeftChild();
//        }
//    }
//
//    @Override
//    public boolean hasNext() {
//        checkForComodification();
//        return !stack.isEmpty();
//    }
//
//    @Override
//    public T next() {
//        if (!hasNext()) {
//            throw new NoSuchElementException();
//        }
//        checkForComodification();
//
//        TreeNode<T> node = stack.pop();
//        lastReturned = node;
//        T result = node.getValue();
//
//        if (node.getRightChild() != null) {
//            pushLeft(node.getRightChild());
//        }
//        return result;
//    }
//
//    @Override
//    public void remove() {
//        if (lastReturned == null) {
//            throw new IllegalStateException("next() не был вызван");
//        }
//        checkForComodification();
//
//        tree.remove(lastReturned.getValue());  // удаляем через ссылку на дерево
//        expectedModCount = tree.modCount;  // обновляем счетчик
//        lastReturned = null;  // сбрасываем после удаления
//    }
//
//    private void checkForComodification() {
//        if (tree.modCount != expectedModCount) {
//            throw new ConcurrentModificationException();
//        }
//    }
//}