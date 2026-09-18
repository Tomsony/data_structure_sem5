package trees;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Итератор, обходящий дерево в порядке убывания (reverse in-order).
 * Начинает с максимального элемента и движется к минимальному.
 * Использует явный стек, идя сначала по правым веткам.
 */
private class DescendingBSTIterator<T> implements Iterator<T> {
    private final java.util.Stack<TreeNode<T>> stack = new java.util.Stack<>();
    private int expectedModCount;
    private TreeNode<T> lastReturned = null;

    public DescendingBSTIterator() {
        expectedModCount = modCount;
        pushRight(root);
    }

    /**
     * Помещает на стек все узлы, идя по правым потомкам.
     * Это даст максимальный элемент на вершине стека.
     */
    private void pushRight(TreeNode<T> node) {
        while (node != null) {
            stack.push(node);
            node = node.getRightChild();
        }
    }

    @Override
    public boolean hasNext() {
        checkForComodification();
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        checkForComodification();

        TreeNode<T> node = stack.pop();
        // запоминаем для возможного удаления
        lastReturned = node;
        // Сохраняем значение которое вернем пользователю
        T result = node.getValue();

        // Если у узла есть левое поддерево, уходим в него и идём по правым веткам
        if (node.getLeftChild() != null) {
            pushRight(node.getLeftChild());
        }
        return result;
    }

    @Override
    public void remove() {
        if (lastReturned == null) {
            throw new IllegalStateException("next() не был вызван");
        }
        checkForComodification();
        BinarySearchTree.this.remove(lastReturned.getValue());
        expectedModCount = modCount;
        lastReturned = null;
    }

    private void checkForComodification() {
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
}