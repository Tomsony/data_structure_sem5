/**
 * Данный класс представляет реализацию БИНАРНОГО ДЕРЕВА
 */

public class BinaryTree<T> {
    private TreeNode<T> root;          // ссылка на корневой узел
    private boolean goLeft = true;     // переменная для чередования вставки узлов

    public BinaryTree() {   // конструктор
        root = null;        // дерево при инициализации не содержит узлов
    }



    /**
     * Метод для вставки новых узлов в дерево
     */
    public void insert(T value) {
        root = insertRec(root, value); // обращаемся к рекурсивной вставке
        goLeft = !goLeft;   // Меняем направление для следующей вставки
    }

    /**
     * Метод реализует алгоритм вставки значения в бинарное дерево
     *
     * @param node  текущий узел для проверки
     * @param value значение, которое нужно вставить
     * @return возвращает либо новый созданный узел, либо существующий
     */
    private TreeNode<T> insertRec(TreeNode<T> node, T value) {
        if (node == null) {     // Если достигли null - значит нашли место для вставки, создаем узел
            return new TreeNode<>(value);
        }
        // РЕКУРСИВНЫЙ СЛУЧАЙ:
        // чередование вставки (влево/вправо)
        if (goLeft) {   // Случай для перехода в левое поддерево
            node.setLeftChild(insertRec(node.getLeftChild(), value));
        } else {    // Случай для перехода в правое поддерево
            node.setRightChild(insertRec(node.getRightChild(), value));
        }
        return node;
    }

    /**
     * Метод проверки, является ли дерево строгим/полным
     *
     * @return true - является, false - нет
     */
    public boolean isStrict() {
        return isStrictRec(root);
    }

    /**
     * Метод реализует алгоритм проверки строгости
     *
     * @param node текущий узел для проверки
     * @return рекурсивно проверяет все узлы дерева,
     * true - является строгим, false - нет
     */
    private static <T> boolean isStrictRec(TreeNode<T> node) {
        // Если у дерева нет корня - оно строгое
        if (node == null) return true;
        // Строгое дерево: 0 || 2 потомка у каждого узла
        if (((node.getLeftChild() == null && node.getRightChild() != null) ||
                (node.getLeftChild() != null && node.getRightChild() == null))) {
            return false;
        }
        return isStrictRec(node.getLeftChild()) && isStrictRec(node.getRightChild());
    }

    /**
     * Проверка является ли дерево законченным (complete)
     *
     * @return true - является законченным, false - нет
     */
    public boolean isComplete() {
        int nodeCount = countNodes(root); // Создаем счетчик для количества узлов
        return isCompleteRec(root, 0, nodeCount);
    }
//todo копирование дерева
    /**
     * Рекурсивно подсчитывает количество узлов дереве
     *
     * @param node корень поддерева
     * @return количество узлов в дереве
     */
    public static <T> int countNodes(TreeNode<T> node) {
        if (node == null) return 0;
        return 1 + countNodes(node.getLeftChild()) + countNodes(node.getRightChild());
    }

    /**
     * Метод реализует алгоритм проверки законченности
     *
     * @param node      текущий узел для проверки
     * @param index     номер текущего узла (для корня 0, как в массиве)
     * @param nodeCount общее количество узлов
     * @return true - является законченным, false - нет
     */
    private static <T> boolean isCompleteRec(TreeNode<T> node, int index, int nodeCount) {
        if (node == null) return true;

        if (index >= nodeCount) return false;   // Если индекс превышает количество узлов - дерево не полное
        // Рекурсивно проверяем левое и правое поддеревья
        return isCompleteRec(node.getLeftChild(), 2 * index + 1, nodeCount) &&
                isCompleteRec(node.getRightChild(), 2 * index + 2, nodeCount);
    }

    /**
     * Проверка является ли дерево совершенным (perfect)
     *
     * @return true - является, false - нет
     */
    public boolean isPerfect() {
        int depth = getDepth(root);
        return isPerfectRecursive(root, depth, 0);
    }

    /**
     * Вычисляет максимальную глубину дерева по левой ветке
     *
     * @param node проверяемый узел
     * @return максимальная глубина дерева (пустое дерево = -1)
     */
    public static <T> int getDepth(TreeNode<T> node) {
        if (node == null) return -1; //todo адаптировал под правило глубина пустого дерева -1
        return 1 + Math.max(getDepth(node.getLeftChild()), getDepth(node.getRightChild()));
    }

    /**
     * @param node  проверяемый узел
     * @param depth максимальная глубина
     * @param level текущий уровень
     * @return true если дерево совершенное, false - если нет
     */
    private static <T> boolean isPerfectRecursive(TreeNode<T> node, int depth, int level) {
        if (node == null) {
            return true;
        }

        // Если это лист(узел без потомков) - проверяем глубину
        if (node.getLeftChild() == null && node.getRightChild() == null) {
            return depth == level;
        }

        // Если не лист - проверяем наличие обоих потомков
        if (node.getLeftChild() == null || node.getRightChild() == null) {
            return false;
        }

        return isPerfectRecursive(node.getLeftChild(), depth, level + 1) &&
                isPerfectRecursive(node.getRightChild(), depth, level + 1);
    }

    /**
     * Метод для поиска узла по значению.
     *
     * @param value искомое значение.
     * @return найденный узел или null если не найден.
     * BigO:
     * O(1) - лучший случай (если корневой узел null; если искомое значение в корневом узле;
     * О(n) - худший случай (полный обход в глубину) посещение каждого узла
     */
    public TreeNode<T> findNode(T value) {
        return findNodeRecursive(this.root, value);
    }

    /**
     * Алгоритм для рекурсивного поиска узла
     *
     * @param node  проверяемый узел
     * @param value искомое значение
     * @return найденный узел или null если не найден
     */
    public static <T> TreeNode<T> findNodeRecursive(TreeNode<T> node, T value) {
        if (node == null) return null;

        // Обрабатываем все случаи
        if (value == null) {          // если ищем "пустой" узел (Защита от NullPointerException)
            if (node.getValue() == null) return node;
        } else {                     // если ищем конкретное значение узла
            if (value.equals(node.getValue())) return node;
        }
        // Если не нашли, ищем в поддеревьях

        // Рекурсивно ищем в левой части
        TreeNode<T> leftResult = findNodeRecursive(node.getLeftChild(), value);
        if (leftResult != null) return leftResult;
        // Рекурсивно ищем в правой части
        return findNodeRecursive(node.getRightChild(), value);
    }

    /// Метод для полного удаления всех узлов дерева
    public void clearTree() {
        System.out.println("clearTree has started...");
        clearTreeRecursive(root);
        root = null;
        System.out.println("All of tree nodes are successfully deleted!");
    }

    /**
     * Алгоритм удаления всех узлов
     *
     * @param node текущий узел
     */
    public static <T> void clearTreeRecursive(TreeNode<T> node) {
        if (node == null) { // Этого достаточно в Java. Garbage Collector автоматически удалит все узлы
            return;
        }
        // ИЗБЫТОЧНО:
        // Сначала рекурсивно удаляем потомков
        clearTreeRecursive(node.getLeftChild());
        clearTreeRecursive(node.getRightChild());
        // Удаляем текущий узел
        System.out.println("Удаляем текущий узел со значением: " + node.getValue());
        // В Java не нужно явно обнулять ссылки - GarbageCollector сделает всё сам
        // node.setLeftChild(null);
        // node.setRightChild(null);
    }

    /**
     * Универсальный метод для применения обработчика к узлам дерева
     * @param processor функциональный интерфейс для обработки узлов (TreeProcessor)
     * @param traversalType тип обхода: "NLr", "rNL", "Bfs"
     */
    public void apply(TreeProcessor<T> processor, String traversalType){
        if (processor == null){     // проверка на валидность функционального интерфейса
            throw new IllegalArgumentException("processor не может быть null");
        }
        // обработка выбранного типа обхода
        switch (traversalType.toLowerCase()){     // .toLowerCase() - улучшенная обработка
            case "nlr" -> applyNLR(root,processor);   // NODE-LEFT-RIGHT обход
            case "lnr" -> applyLNR(root,processor);   // LEFT-NODE-RIGHT обход
            case "lrn" -> applyLRN(root,processor);   // LEFT-RIGHT-NODE обход
            case "rnl" -> applyRNL(root,processor);   // RIGHT-NODE-LEFT обход
            case "rln" -> applyRLN(root,processor);   // RIGHT-LEFT-NODE обход
            case "bfs" -> applyBFS(root,processor);   // обход в ширину
            case "dfs" -> applyDFS(root,processor);   // обход в глубину
            case "drfs" -> applyDRFS(root,processor); // рекурсивный обход в глубину
        }
    }

    /**
     * NODE-LEFT-RIGHT обход
     * @param node обрабатываемый узел
     * @param processor функциональный интерфейс для обработки
     */
    public static <T> void applyNLR(TreeNode<T> node, TreeProcessor<T> processor){
        if(node != null){
            processor.process(node);                                    // Обработать корень через интерфейс
            applyNLR(node.getLeftChild(), processor);       // Обойти левое поддерево
            applyNLR(node.getRightChild(), processor);      // Обойти правое поддерево
        }
    }

    /**
     * LEFT-NODE-RIGHT обход
     * @param node обрабатываемый узел
     * @param processor функциональный интерфейс для обработки
     */
    public static <T>  void applyLNR(TreeNode<T> node, TreeProcessor<T> processor){
        if(node != null){
            applyLNR(node.getLeftChild(), processor);       // Обойти левое поддерево
            processor.process(node);                                    // Обработать корень
            applyLNR(node.getRightChild(), processor);      // Обойти правое поддерево
        }
    }

    /**
     * LEFT-RIGHT-NODE обход
     * @param node обрабатываемый узел
     * @param processor функциональный интерфейс для обработки
     */
    public static <T>  void applyLRN(TreeNode<T> node, TreeProcessor<T> processor){
        if(node != null){
            applyLRN(node.getLeftChild(), processor);       // Обойти левое поддерево
            applyLRN(node.getRightChild(), processor);      // Обойти правое поддерево
            processor.process(node);                                    // Обработать корень
        }
    }

    /**
     * RIGHT-NODE-LEFT обход
     * @param node обрабатываемый узел
     * @param processor функциональный интерфейс для обработки
     */
    public static <T>  void applyRNL(TreeNode<T> node, TreeProcessor<T> processor){
        if(node != null){
            applyRNL(node.getRightChild(), processor);      // Обойти правое поддерево
            processor.process(node);                                    // Обработать корень
            applyRNL(node.getLeftChild(), processor);       // Обойти левое поддерево
        }
    }

    /**
     * RIGHT-LEFT-NODE обход
     * @param node обрабатываемый узел
     * @param processor функциональный интерфейс для обработки
     */
    public static <T>  void applyRLN(TreeNode<T> node, TreeProcessor<T> processor){
        if(node != null){
            applyRLN(node.getRightChild(), processor);     // Обойти правое поддерево
            applyRLN(node.getLeftChild(), processor);       // Обойти левое поддерево
            processor.process(node);                                   // Обработать корень
        }
    }

    /**
     * Обход в ширину (Breadth-First Search) с использованием очереди
     * @param node начальный узел для обхода
     * @param processor функциональный интерфейс для обработки узлов
     */
    public static <T>  void applyBFS(TreeNode<T> node, TreeProcessor<T> processor) {
        if (node == null) return;

        // Используем собственную очередь MyQueue
        MyQueue<TreeNode<T>> queue = new MyQueue<>();
        queue.add(node);    // Добавляем узел

        while (!queue.isEmpty()) {
            TreeNode<T> current = queue.dequeue();  // Удаляем и возвращаем первый элемент очереди в current
            processor.process(current);             // Обрабатываем узел

            // Добавляем потомков в очередь
            if (current.getLeftChild() != null) {
                queue.add(current.getLeftChild());
            }
            if (current.getRightChild() != null) {
                queue.add(current.getRightChild());
            }
        }
    }

    /**
     * Нерекурсивный обход в глубину (Depth-First Search) с использованием стека
     * @param node начальный узел для обхода
     * @param processor функциональный интерфейс для обработки узлов
     */
    public static <T>  void applyDFS(TreeNode<T> node, TreeProcessor<T> processor) {
        if (node == null) return;

        // Используем стек для хранения узлов
        MyStack<TreeNode<T>> stack = new MyStack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            TreeNode<T> current = stack.pop();       // Удаляем и возвращаем элемент с вершины стека в current
            processor.process(current);              // Обрабатываем узел

            // Сначала добавляем правого потомка, потом левого,
            // чтобы левый обрабатывался первым
            if (current.getRightChild() != null) {
                stack.push(current.getRightChild());
            }
            if (current.getLeftChild() != null) {
                stack.push(current.getLeftChild());
            }
        }
    }

    /**
     * Рекурсивный обход в глубину (Depth-First Recursive Search)
     * @param node начальный узел для обхода
     * @param processor функциональный интерфейс для обработки узлов
     */
    public static <T>  void applyDRFS(TreeNode<T> node, TreeProcessor<T> processor) {
        if (node != null) {
            processor.process(node);                    // Обработать текущий узел
            applyDRFS(node.getLeftChild(), processor);  // Рекурсивно обойти левое поддерево
            applyDRFS(node.getRightChild(), processor); // Рекурсивно обойти правое поддерево
        }
    }

    /**
     * Метод для сбора значений в коллекцию
     * @param traversalType тип обхода
     * @param collection коллекция для сбора значений
     */
    public void collectToArray(String traversalType, MyArrayList<T> collection){
        if (collection == null){
            throw new IllegalArgumentException("Collection не может быть null");
        }

        // TreeProcessor добавляет значения в коллекцию
        TreeProcessor<T> collector = node -> collection.add(node.getValue());
        apply(collector,traversalType);
    }

    /**
     * Перегруженная версия с возвратом новой коллекции
     *
     * @param traversalType тип обхода
     * @return новая коллекция со значениями узлов
     */
    public MyArrayList<T> collectToArray(String traversalType){
        MyArrayList<T> result  = new MyArrayList<>();
        collectToArray(traversalType, result);
        return result;
    }

    /**
     * Создает глубокую копию дерева
     * O(n) - где n количество узлов в дереве, так как нужно посетить каждый узел ровно один раз.
     *
     * @return новая копия дерева
     */
    public BinaryTree<T> copy(){
        BinaryTree<T> copiedTree = new BinaryTree<>();
        copiedTree.root = copyRecursive(this.root);

        copiedTree.goLeft = this.goLeft; // сбрасываем к начальному значению
        return copiedTree;
    }

    /**
     * Рекурсивный метод для копирования узлов дерева
     * @param node исходный узел для копирования
     * @return копия узла со всеми потомками
     */
    private TreeNode<T> copyRecursive(TreeNode<T> node){
        if (node == null){
            return null;
        }

        // Создаем новый узел с тем же значением
        TreeNode<T> newNode = new TreeNode<>(node.getValue());

        // Рекурсивно копируем левое и правое поддеревья
        newNode.setLeftChild(copyRecursive(node.getLeftChild()));
        newNode.setRightChild(copyRecursive(node.getRightChild()));

        return newNode;
    }
}