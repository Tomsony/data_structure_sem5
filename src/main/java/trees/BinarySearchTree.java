package trees;

/**
 * Реализация Поискового Бинарного Дерева (BST).
 * Ключи должны реализовывать интерфейс Comparable<T>, причём
 * сравнение через compareTo должно быть согласовано с equals.
 *
 * @param <T> тип элементов, допускающий сравнение.
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private TreeNode<T> root;

    public BinarySearchTree() {
        root = null;
    }

    // ================= SEARCH =================

    /**
     * Поиск элемента по значению.
     *
     * Сложность:
     * - лучший случай: O(1) (элемент в корне)
     * - средний случай: O(log n) (сбалансированное дерево)
     * - худший случай: O(n) (вырожденное дерево)
     *
     * @param value искомое значение (не null)
     * @return true, если значение найдено
     * @throws NullPointerException если value == null
     */
    public boolean search(T value) {
        if (value == null) {
            throw new NullPointerException("value не может быть null");
        }
        return searchRecursive(root, value);
    }

    /**
     * Рекурсивный поиск в поддереве.
     * Использует compareTo == 0 для проверки совпадения,
     * чтобы обеспечить согласованность с навигацией по дереву.
     */
    private boolean searchRecursive(TreeNode<T> node, T value) {
        if (node == null) return false;

        int cmp = value.compareTo(node.getValue());
        if (cmp == 0) return true;

        if (cmp < 0) {
            return searchRecursive(node.getLeftChild(), value);
        } else {
            return searchRecursive(node.getRightChild(), value);
        }
    }

    // ================= STATIC SEARCH =================

    /**
     * Статический поиск по значению для произвольного корня.
     *
     * @param root  корень дерева
     * @param value искомое значение
     * @return true, если найдено
     * @throws NullPointerException если value == null
     */
    public static <T extends Comparable<T>> boolean search(TreeNode<T> root, T value) {
        if (value == null) {
            throw new NullPointerException("value не может быть null");
        }
        return searchRecursiveStatic(root, value);
    }

    private static <T extends Comparable<T>> boolean searchRecursiveStatic(TreeNode<T> node, T value) {
        if (node == null) return false;

        int cmp = value.compareTo(node.getValue());
        if (cmp == 0) return true;

        if (cmp < 0) {
            return searchRecursiveStatic(node.getLeftChild(), value);
        } else {
            return searchRecursiveStatic(node.getRightChild(), value);
        }
    }

    // ================= SUCCESSOR =================

    /**
     * Поиск следующего наибольшего узла (successor) для заданного значения.
     * Successor – узел с наименьшим значением, большим заданного.
     *
     * Лучший случай: сбалансированное дерево (∼log₂ N сравнений)
     * Худший случай: вырожденное дерево (линейный список, ∼N сравнений)
     * Средний случай: O(log N)
     *
     * @param value значение, для которого ищется successor
     * @return значение successor или null, если не найдено
     * @throws NullPointerException если value == null
     */
    public T successor(T value) {
        if (value == null) throw new NullPointerException("value не может быть null");
        TreeNode<T> node = findNodeInBST(root, value);
        if (node == null) return null;

        TreeNode<T> successorNode = findSuccessor(node);
        return successorNode != null ? successorNode.getValue() : null;
    }

    /**
     * Поиск узла в BST по значению (итеративный).
     *
     * @param root  корень дерева
     * @param value искомое значение
     * @return узел или null
     * @throws NullPointerException если value == null
     */
    public static <T extends Comparable<T>> TreeNode<T> findNodeInBST(TreeNode<T> root, T value) {
        if (value == null) throw new NullPointerException("Value не может быть null");

        TreeNode<T> current = root;
        while (current != null) {
            int cmp = value.compareTo(current.getValue());
            if (cmp == 0) return current;
            else if (cmp < 0) current = current.getLeftChild();
            else current = current.getRightChild();
        }
        return null;
    }

    /**
     * Внутренний поиск successor для заданного узла.
     *
     * @param node узел, для которого ищем successor
     * @return successor или null
     */
    private TreeNode<T> findSuccessor(TreeNode<T> node) {
        if (node == null) return null;
        // Случай 1: есть правое поддерево -> минимальный элемент в нём
        if (node.getRightChild() != null) {
            return findMin(node.getRightChild());
        }

        // Случай 2: нет правого поддерева -> ищем предка, для которого
        // текущий узел находится в левом поддереве.
        TreeNode<T> successor = null;
        TreeNode<T> current = root;
        T targetValue = node.getValue();

        while (current != null) {
            int cmp = targetValue.compareTo(current.getValue());
            if (cmp < 0) {
                successor = current;               // текущий может быть наследником
                current = current.getLeftChild();
            } else if (cmp > 0) {
                current = current.getRightChild();
            } else {
                break; // нашли исходный узел
            }
        }
        return successor;
    }

    /**
     * Поиск узла с минимальным значением в поддереве.
     *
     * @param node корень поддерева
     * @return узел с минимальным значением или null, если дерево пусто
     */
    public static <T> TreeNode<T> findMin(TreeNode<T> node) {
        if (node == null) return null;
        while (node.getLeftChild() != null) {
            node = node.getLeftChild();
        }
        return node;
    }

    // ================= REMOVE =================

    /**
     * Удаление узла по значению.
     *
     * Сложность:
     * - лист / один ребёнок: O(h)
     * - два ребёнка: O(h) + O(h) = O(h)
     * где h – высота дерева.
     *
     * @param value значение для удаления
     * @throws NullPointerException если value == null
     */
    public void remove(T value) {
        if (value == null) throw new NullPointerException("Value не может быть null");
        root = removeRecursive(root, value);
    }

    /**
     * Рекурсивное удаление узла.
     *
     * O(n) – для несбалансированного, O(log n) – для сбалансированного.
     *
     * @param node  текущий корень поддерева
     * @param value удаляемое значение
     * @return новое поддерево
     */
    public static <T extends Comparable<T>> TreeNode<T> removeRecursive(TreeNode<T> node, T value) {
        if (node == null) return null;

        int cmp = value.compareTo(node.getValue());
        if (cmp < 0) {
            node.setLeftChild(removeRecursive(node.getLeftChild(), value));
        } else if (cmp > 0) {
            node.setRightChild(removeRecursive(node.getRightChild(), value));
        } else {
            // Нашли узел для удаления
            // Случай 1: нет детей
            if (node.getLeftChild() == null && node.getRightChild() == null) {
                return null;
            }
            // Случай 2: один ребёнок
            if (node.getLeftChild() == null) {
                return node.getRightChild();
            } else if (node.getRightChild() == null) {
                return node.getLeftChild();
            }
            // Случай 3: два ребёнка
            TreeNode<T> successor = findMin(node.getRightChild());
            node.setValue(successor.getValue());
            node.setRightChild(deleteMin(node.getRightChild()));
        }
        return node;
    }

    /**
     * Удаление минимального узла в поддереве.
     *
     * @param node корень поддерева
     * @return поддерево без минимального узла
     */
    public static <T> TreeNode<T> deleteMin(TreeNode<T> node) {
        if (node.getLeftChild() == null) {
            return node.getRightChild();
        }
        node.setLeftChild(deleteMin(node.getLeftChild()));
        return node;
    }

    // ================= INSERT =================

    /**
     * Вставка значения в дерево.
     * Дубликаты игнорируются.
     *
     * Средний случай: O(log n), худший: O(n).
     *
     * @param value вставляемое значение (не null)
     * @throws NullPointerException если value == null
     */
    public void insert(T value) {
        if (value == null) throw new NullPointerException("Value не может быть null!");
        root = insertRecursive(root, value);
    }

    /**
     * Рекурсивная вставка.
     *
     * @param node  корень поддерева
     * @param value значение
     * @return обновлённое поддерево
     * @throws NullPointerException если value == null
     */
    public static <T extends Comparable<T>> TreeNode<T> insertRecursive(TreeNode<T> node, T value) {
        if (value == null) throw new NullPointerException("Value не может быть null!");
        if (node == null) {
            return new TreeNode<>(value);
        }
        int cmp = value.compareTo(node.getValue());
        if (cmp < 0) {
            node.setLeftChild(insertRecursive(node.getLeftChild(), value));
        } else if (cmp > 0) {
            node.setRightChild(insertRecursive(node.getRightChild(), value));
        }
        // cmp == 0: дубликат не вставляем
        return node;
    }


    // ================= COPY =================

    /**
     * Создаёт глубокую копию дерева.
     *
     * Сложность: O(n), где n — количество узлов в дереве.
     * Каждый узел посещается ровно один раз.
     *
     * @return новое BST, содержащее все элементы исходного дерева
     */
    public BinarySearchTree<T> copy() {
        BinarySearchTree<T> newTree = new BinarySearchTree<>();
        newTree.root = copyRecursive(this.root);
        return newTree;
    }

    /**
     * Рекурсивное копирование поддерева.
     * Выполняет preorder-обход (NLR): сначала копируем корень,
     * затем левое поддерево, затем правое.
     *
     * @param node корень копируемого поддерева
     * @return глубокая копия поддерева с корнем в node
     */
    private TreeNode<T> copyRecursive(TreeNode<T> node) {
        if (node == null) {
            return null;
        }

        // Создаём новый узел с тем же значением
        TreeNode<T> newNode = new TreeNode<>(node.getValue());

        // Рекурсивно копируем левое и правое поддеревья
        newNode.setLeftChild(copyRecursive(node.getLeftChild()));
        newNode.setRightChild(copyRecursive(node.getRightChild()));

        return newNode;
    }
}