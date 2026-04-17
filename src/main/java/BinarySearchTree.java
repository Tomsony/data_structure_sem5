/**
 * Реализация Поискового Бинарного Дерева.
 *
 * @param <T> тип элементов в дереве, которые можно сравнить друг с другом через интерфейс Comparable (объекты одного типа).
 */
public class BinarySearchTree<T extends Comparable<T>> {
    private TreeNode<T> root;  // корневой узел

    public BinarySearchTree() {  // конструктор
        root = null;   // при инициализации дерева корневой узел без значения
    }

    /**
     * Поиск элемента по значению. Вызывает рекурсивный метод searchRecursive с алгоритмом поиска.
     *
     * Лучший случай: O(1) - элемент в корне
     * Средний случай: O(log n) - сбалансированное дерево
     * Худший случай: O(n) - вырожденное дерево
     *
     * @param value искомое значение
     * @return true если узел есть в дереве, false если значения нет
     */
    public boolean search(T value) {
        if (value == null) {
            throw new NullPointerException("value не может быть null");
        }
        return searchRecursive(root, value);
    }

    /**
     * Рекурсивный поиск.
     * Основная логика алгоритма - если искомое значение меньше корня, то ищем в левой части поддерева,
     * а иначе в правой.
     * <p>
     * BigO:
     * - Лучший случай: O(1) - значение в корневом узле
     * - Средний случай: O(log n) - сбалансированное дерево
     * - Худший случай: O(n) - вырожденное дерево (линейный список)
     *
     * @param node  текущий узел для проверки (начинается с корня)
     * @param value искомое значение (не должно быть null)
     * @return true если значение найдено в дереве, false если нет
     */
    private boolean searchRecursive(TreeNode<T> node, T value) {
        if (node == null) return false; // Если корневой узел null, не нашли

        if (value.equals(node.getValue())) return true; // Если нашли в корневом узле

        if (value.compareTo(node.getValue()) < 0) {  // Сравниваем значения для определения направления поиска
            return searchRecursive(node.getLeftChild(), value);  // Ищем в левом поддереве
        } else {
            return searchRecursive(node.getRightChild(), value);  // Ищем в правом поддереве
        }
    }

    // ================= SEARCH (static method) =================

    /**
     * Статический поиск элемента по значению для работы с произвольным деревом
     * Используется в TimePlot
     *
     * @param root корень дерева
     * @param value искомое значение
     * @return true если узел есть в дереве, false если значения нет
     */
    public static <T extends Comparable<T>> boolean search(TreeNode<T> root, T value) {
        if (value == null) {
            throw new NullPointerException("value не может быть null");
        }
        return searchRecursiveStatic(root, value);
    }

    /**
     * Рекурсивный поиск (статический)
     */
    private static <T extends Comparable<T>> boolean searchRecursiveStatic(TreeNode<T> node, T value) {
        if (node == null) return false;

        if (value.equals(node.getValue())) return true;

        if (value.compareTo(node.getValue()) < 0) {
            return searchRecursiveStatic(node.getLeftChild(), value);
        } else {
            return searchRecursiveStatic(node.getRightChild(), value);
        }
    }

    // ================= SUCCESSOR =================

    /**
     * Поиск следующего наибольшего узла (successor) для заданного значения
     * Successor - узел с наименьшим значением, большим заданного
     *
     * - Лучший случай: O(1) - значение в корневом узле
     * - Средний случай: O(log n) - сбалансированное дерево
     * - Худший случай: O(n) - вырожденное дерево (линейный список)
     *
     * @param value значение, для которого ищется successor
     * @return значение successor или null, если не найдено
     * @throws NullPointerException если value равен null
     */
    public T successor(T value) {
        TreeNode<T> node = findNodeInBST(root, value);     // Находим узел, который содержит искомое значение
        if (node == null) return null;                    // Узел не найден

        TreeNode<T> successor = findSuccessor(node);
        return successor != null ? successor.getValue() : null;
    }

    /**
     * Оптимизированный поиск узла в Бинарном Дереве Поиска
     * <p>
     * todo Алгоритмическая сложность: O(h), где h - высота дерева
     * Высота дерева - максимальное количество узлов на пути от корневого узла до самого дальнего листового узла.
     *
     * @param <T>   тип элементов в дереве, которые можно сравнить друг с другом через интерфейс Comparable
     * @param root  корневой узел дерева (метод статический, нет доступа к полю класса)
     * @param value значение у узла, который мы ищем
     * @return null если узел не найден, current если нашли
     */
    public static <T extends Comparable<T>> TreeNode<T> findNodeInBST(TreeNode<T> root, T value) {
        if (value == null) throw new NullPointerException("Value не может быть null");

        TreeNode<T> current = root;  // Создаем переменную для временного хранения узла, начиная с корневого
        while (current != null) {
            // Сравниваем значение текущего узла со значением узла, который ищем
            int comparedValue = value.compareTo(current.getValue());
            if (comparedValue == 0) return current;    // Если значения совпадают - возвращаем этот узел

                // Если значение в узле меньше, то ищем в левом поддереве
            else if (comparedValue < 0) current = current.getLeftChild();
                // Иначе в правом поддереве
            else current = current.getRightChild();
        }
        return null;    // Если не нашли
    }

    /**
     * Реализация алгоритма поиска successor.
     * Алгоритм поиска successor для узла:
     * 1. Если есть правое поддерево - successor это минимальный элемент в правом поддереве
     * 2. Если нет правого поддерева - ищем первого предка, для которого текущий узел находится в левом поддереве
     *
     * @param node узел для которого ищется successor
     * @return successor узел или null если не найден
     */
    private TreeNode<T> findSuccessor(TreeNode<T> node) {
        if (node == null) return null;
        // Случай 1: Есть правое поддерево
        if (node.getRightChild() != null) {
            return findMin(node.getRightChild());
        }

        // Случай 2: Нет правого поддерева - ищем в предках, начиная от корня вниз
        TreeNode<T> successor = null;   // Переменная для хранения successor
        TreeNode<T> current = root;     // Обрабатываемый узел
        TreeNode<T> target = node;      // Узел по которому сравниваем другие

        while (current != null) {
            int cmp = target.getValue().compareTo(current.getValue());  // Сравниваем потенциальный узел с текущим

            if (cmp < 0) {
                // Текущий узел больше target - потенциальный successor
                successor = current;
                current = current.getLeftChild();   // меньшие значения в левом поддереве
            } else if (cmp > 0) {
                // Текущий узел меньше target - продолжаем поиск
                current = current.getRightChild();  // большие значения в правом поддереве
            } else {
                // Нашли target - выходим из цикла
                break;
            }
        }
        return successor;
    }

    /**
     * Поиск узла с минимальным значением в поддереве
     *
     * @param node корень поддерева для поиска
     * @return узел с минимальным значением в поддереве
     */
    public static <T> TreeNode<T> findMin(TreeNode<T> node) {
        if (node == null) return null;

        TreeNode<T> current = node;
        while (current.getLeftChild() != null) {
            current = current.getLeftChild();
        }
        return current;
    }

    // ================= REMOVE =================

    /**
     * Удаление узла по значению в BST
     *
     * Лист (нет детей): O(log n) - только поиск
     * Один ребенок: O(log n) - поиск + замена ссылки
     * Два ребенка: O(log n) + O(log n) = O(log n) - поиск + поиск successor
     *
     * @param value значение узла, который нужно удалить
     */
    public void remove(T value) {
        if (value == null) throw new NullPointerException("Value не может быть null");
        root = removeRecursive(root, value);
    }

    /**
     * Алгоритм реализации рекурсивного удаления узла по значению
     * 1 случай: У узла нет детей - удаляем узел.
     * 2 cлучай: Один дочерний элемент - присоединяем дочерний к родителю, текущий удаляем.
     * 3 случай: Два дочерних элемента - находим successor, обмениваемся с ним значениями,
     * удаляем узел с которым произошел обмен.
     *
     * BigO:
     * O(n) - для несбалансированного дерева
     * 0(log n) - для сбалансированного дерева
     *
     * @param node обрабатываемый узел, начиная с корневого
     * @param value значение узла, который нужно удалить
     */
    public static <T extends Comparable <T>> TreeNode<T> removeRecursive(TreeNode<T> node, T value) {
        if (node == null) return null;                  // узел не найден

        int cmp = value.compareTo(node.getValue());     // определяем, в каком поддереве искать

        // Рекурсивный поиск - идем влево или вправо в зависимости от сравнения.
        // Обновляем ссылки на детей после рекурсивных вызовов.
        if (cmp < 0) {
            node.setLeftChild(removeRecursive(node.getLeftChild(), value));
        } else if (cmp > 0) {
            node.setRightChild(removeRecursive(node.getRightChild(), value));
        } else {
            // Нашли узел для удаления

            // СЛУЧАЙ 1: НЕТ ДЕТЕЙ
            if (node.getLeftChild() == null && node.getRightChild() == null) {
                return null;  // просто удаляем узел
            }

            // 2 СЛУЧАЙ: 1 РЕБЕНОК
            if (node.getLeftChild() == null) {
                return node.getRightChild();     // Заменяем удаляемый узел на правого ребенка
            } else if (node.getRightChild() == null) {
                return node.getLeftChild();       // Заменяем удаляемый узел на левого ребенка
            }

            // 3 СЛУЧАЙ: 2 РЕБЕНКА
            // Находим минимальный узел в правом поддереве (successor)
            TreeNode<T> successor = findMin(node.getRightChild());

            // Копируем значение successor в удаляемый узел
            node.setValue(successor.getValue());

            // Удаляем successor через прямой вызов
            node.setRightChild(deleteMin(node.getRightChild()));
        }

        return node;
    }

    /**
     * Удаляет минимальный узел в поддереве
     *
     * BigO: O(h), где h - высота поддерева
     * - Лучший случай: O(1) - минимальный узел слева
     * - Худший случай: O(h) - если левый потомок глубокий
     *
     * @param node корень поддерева
     * @return обновленное поддерево без минимального узла
     */
    public static <T> TreeNode<T> deleteMin(TreeNode<T> node){
        if(node.getLeftChild() == null){    // Нашли минимальный узел
            return node.getRightChild();    // Заменяем его на правого ребенка
        }
        node.setLeftChild(deleteMin(node.getLeftChild()));  // Рекурсивно идем влево
        return node;
    }

    // ================= INSERT =================

    /**
     * Вставка элементов в бинарное дерево поиска
     *
     * Средний: O(log n)
     * Худший: O(n)
     *
     * @param value вставляемое значение
     */
    public  void insert(T value){
        if (value == null) throw new NullPointerException("Value не может быть null!");
        root = insertRecursive(root, value);
    }

    public static <T extends Comparable<T>> TreeNode<T> insertRecursive(TreeNode<T> node, T value){
        // Базовый случай: достигли места для вставки
        if (node == null) {
            return new TreeNode<>(value);
        }

        // Сравниваем значения
        int cmp = value.compareTo(node.getValue());

        if (cmp < 0) {
            // Вставляем в левое поддерево
            node.setLeftChild(insertRecursive(node.getLeftChild(), value));
        } else if (cmp > 0) {
            // Вставляем в правое поддерево
            node.setRightChild(insertRecursive(node.getRightChild(), value));
        }
        // Если cmp == 0, значение уже существует - ничего не делаем

        return node;
    }
}
