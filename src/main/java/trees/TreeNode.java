package trees;

/**
 * Класс представляет собой реализацию узла для бинарного дерева.
 * Класс использует ссылочный тип T.
 * Наследуется от основного класса trees.BaseNode.
 *
 * @author Артём Томских, ИВТ-23
 */
public class TreeNode<T> extends BaseNode<T> {
    private TreeNode<T> leftChild; // ссылка на левого потомка
    private TreeNode<T> rightChild; // ссылка на правого потомка

    /**
     * Конструктор узла дерева
     * @param value значение узла
     * @throws IllegalArgumentException если значение равно null
     */
    public TreeNode(T value) {
        super(value); // передаем значение в родительский конструктор (суперкласс)
    }

    /**
     * Получить левого потомка
     * @return левый потомок
     */
    public TreeNode<T> getLeftChild() {
        return this.leftChild;
    }

    /**
     * Установить значение для левого потомка
     * @param leftChild левый потомок
     */
    public void setLeftChild(final TreeNode<T> leftChild) {     // final - параметр не меняется
        this.leftChild = leftChild;
    }

    /**
     * Получить правого потомка
     * @return правый потомок
     */
    public TreeNode<T> getRightChild() {
        return this.rightChild;
    }

    /**
     * Установить значение для правого потомка
     * @param rightChild правый потомок
     */
    public void setRightChild(final TreeNode<T> rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public String toString() {
        String leftInfo = (leftChild != null) ? leftChild.getValue().toString() : "null";
        String rightInfo = (rightChild != null) ? rightChild.getValue().toString() : "null";
        return "trees.TreeNode{ " +
                "key-value: = " + getValue() +
                ", left child: = " + leftInfo +
                ", right child: = " + rightInfo + " }";
    }
}