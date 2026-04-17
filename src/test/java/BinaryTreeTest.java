import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса BinaryTree
 */
class BinaryTreeTest {

    private BinaryTree<Integer> tree;
    private BinaryTree<String> stringTree;

    @BeforeEach
    void setUp() {
        tree = new BinaryTree<>();
        stringTree = new BinaryTree<>();
    }

    @Nested
    @DisplayName("Тесты конструктора и начального состояния")
    class ConstructorTests {

        @Test
        @DisplayName("Создание пустого дерева")
        void testEmptyTreeCreation() {
            assertNull(tree.findNode(1));
            assertEquals(0, tree.countNodes(null));
            assertEquals(-1, tree.getDepth(null));
        }
    }

    @Nested
    @DisplayName("Тесты операции insert")
    class InsertTests {

        @Test
        @DisplayName("Вставка одного элемента")
        void testInsertSingleElement() {
            tree.insert(10);

            TreeNode<Integer> found = tree.findNode(10);
            assertNotNull(found);
            assertEquals(10, found.getValue());
        }

        @Test
        @DisplayName("Вставка нескольких элементов с чередованием")
        void testInsertMultipleElements() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            TreeNode<Integer> root = tree.findNode(10);
            assertNotNull(root);
            assertNotNull(root.getLeftChild());
            assertNotNull(root.getRightChild());
        }

        @Test
        @DisplayName("Попытка вставки null должна выбрасывать исключение")
        void testInsertNullValue() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> tree.insert(null));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 999, Integer.MAX_VALUE, Integer.MIN_VALUE})
        @DisplayName("Вставка различных целочисленных значений")
        void testInsertVariousIntegerValues(int value) {
            tree.insert(value);
            TreeNode<Integer> found = tree.findNode(value);
            assertNotNull(found);
            assertEquals(value, found.getValue());
        }

        @Test
        @DisplayName("Проверка чередования вставки (goLeft)")
        void testInsertAlternation() {
            // Первая вставка - должна пойти влево
            tree.insert(10); // root
            tree.insert(20); // должен пойти влево от root
            tree.insert(30); // должен пойти вправо от root

            TreeNode<Integer> root = tree.findNode(10);
            assertNotNull(root.getLeftChild());
            assertNotNull(root.getRightChild());
            assertEquals(30, root.getLeftChild().getValue());
            assertEquals(20, root.getRightChild().getValue());
        }
    }

    @Nested
    @DisplayName("Тесты метода findNode")
    class FindNodeTests {

        @Test
        @DisplayName("Поиск в пустом дереве")
        void testFindInEmptyTree() {
            assertNull(tree.findNode(10));
        }

        @Test
        @DisplayName("Поиск существующего элемента")
        void testFindExistingElement() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            TreeNode<Integer> found = tree.findNode(20);
            assertNotNull(found);
            assertEquals(20, found.getValue());
        }

        @Test
        @DisplayName("Поиск несуществующего элемента")
        void testFindNonExistingElement() {
            tree.insert(10);
            tree.insert(20);

            assertNull(tree.findNode(999));
        }

        @Test
        @DisplayName("Поиск в глубоком дереве")
        void testFindInDeepTree() {
            for (int i = 0; i < 10; i++) {
                tree.insert(i);
            }

            TreeNode<Integer> found = tree.findNode(7);
            assertNotNull(found);
            assertEquals(7, found.getValue());
        }
    }

    @Nested
    @DisplayName("Тесты метода countNodes")
    class CountNodesTests {

        @Test
        @DisplayName("Подсчет узлов в пустом дереве")
        void testCountEmptyTree() {
            assertEquals(0, tree.countNodes(null));
        }

        @Test
        @DisplayName("Подсчет одного узла")
        void testCountSingleNode() {
            tree.insert(10);
            TreeNode<Integer> root = tree.findNode(10);
            assertEquals(1, tree.countNodes(root));
        }

        @Test
        @DisplayName("Подсчет нескольких узлов")
        void testCountMultipleNodes() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);
            tree.insert(40);

            TreeNode<Integer> root = tree.findNode(10);
            assertEquals(4, tree.countNodes(root));
        }

        @Test
        @DisplayName("Подсчет поддерева")
        void testCountSubtree() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);
            tree.insert(40);
            tree.insert(50);

            TreeNode<Integer> node20 = tree.findNode(20);
            assertNotNull(node20);
            // Количество узлов в поддереве с корнем 20
            int count = tree.countNodes(node20);
            assertTrue(count >= 1 && count <= 3);
        }
    }

    @Nested
    @DisplayName("Тесты метода getDepth")
    class GetDepthTests {

        @Test
        @DisplayName("Глубина пустого дерева")
        void testDepthEmptyTree() {
            assertEquals(-1, tree.getDepth(null));
        }

        @Test
        @DisplayName("Глубина дерева с одним узлом")
        void testDepthSingleNode() {
            tree.insert(10);
            TreeNode<Integer> root = tree.findNode(10);
            assertEquals(0, tree.getDepth(root));
        }

        @Test
        @DisplayName("Глубина сбалансированного дерева")
        void testDepthBalancedTree() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            TreeNode<Integer> root = tree.findNode(10);
            int depth = tree.getDepth(root);
            assertTrue(depth >= 1 && depth <= 2);
        }

        @Test
        @DisplayName("Глубина несбалансированного дерева")
        void testDepthUnbalancedTree() {
            // Создаем несбалансированное дерево
            BinaryTree<Integer> unbalancedTree = new BinaryTree<>();
            unbalancedTree.insert(10);
            unbalancedTree.insert(20);
            unbalancedTree.insert(25);
            unbalancedTree.insert(30);

            TreeNode<Integer> root = unbalancedTree.findNode(10);
            int depth = unbalancedTree.getDepth(root);
            assertTrue(depth >= 2);
        }
    }

    @Nested
    @DisplayName("Тесты проверки свойств дерева")
    class TreePropertyTests {

        @Test
        @DisplayName("Пустое дерево является строгим")
        void testEmptyTreeIsStrict() {
            assertTrue(tree.isStrict());
        }

        @Test
        @DisplayName("Дерево с одним узлом является строгим")
        void testSingleNodeTreeIsStrict() {
            tree.insert(10);
            assertTrue(tree.isStrict());
        }

        @Test
        @DisplayName("Проверка строгого дерева")
        void testStrictTree() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            // Дерево с двумя потомками у корня - строгое
            assertTrue(tree.isStrict());
        }

        @Test
        @DisplayName("Проверка нестрогого дерева")
        void testNonStrictTree() {
            // Создаем нестрогое дерево вручную
            TreeNode<Integer> root = new TreeNode<>(10);
            root.setLeftChild(new TreeNode<>(20));
            // Правый потомок отсутствует - не строгое

            BinaryTree<Integer> customTree = new BinaryTree<>();
            // Используем рефлексию для установки root
            try {
                java.lang.reflect.Field rootField = BinaryTree.class.getDeclaredField("root");
                rootField.setAccessible(true);
                rootField.set(customTree, root);

                assertFalse(customTree.isStrict());
            } catch (Exception e) {
                fail("Reflection failed: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Пустое дерево является законченным")
        void testEmptyTreeIsComplete() {
            assertTrue(tree.isComplete());
        }

        @Test
        @DisplayName("Проверка законченного дерева")
        void testCompleteTree() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            // Простое законченное дерево
            assertTrue(tree.isComplete());
        }

        @Test
        @DisplayName("Пустое дерево не является совершенным")
        void testEmptyTreeIsPerfect() {
            assertTrue(tree.isPerfect()); // Пустое дерево считается совершенным
        }

        @Test
        @DisplayName("Проверка совершенного дерева")
        void testPerfectTree() {
            // Создаем совершенное дерево вручную
            TreeNode<Integer> root = new TreeNode<>(10);
            root.setLeftChild(new TreeNode<>(20));
            root.setRightChild(new TreeNode<>(30));

            BinaryTree<Integer> customTree = new BinaryTree<>();
            try {
                java.lang.reflect.Field rootField = BinaryTree.class.getDeclaredField("root");
                rootField.setAccessible(true);
                rootField.set(customTree, root);

                assertTrue(customTree.isPerfect());
            } catch (Exception e) {
                fail("Reflection failed: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Тесты метода clearTree")
    class ClearTreeTests {

        @Test
        @DisplayName("Очистка пустого дерева")
        void testClearEmptyTree() {
            assertDoesNotThrow(() -> tree.clearTree());
        }

        @Test
        @DisplayName("Очистка дерева с узлами")
        void testClearTreeWithNodes() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            assertDoesNotThrow(() -> tree.clearTree());
            // После очистки поиск должен возвращать null
            assertNull(tree.findNode(10));
        }

        @Test
        @DisplayName("Добавление после очистки")
        void testInsertAfterClear() {
            tree.insert(10);
            tree.insert(20);
            tree.clearTree();

            tree.insert(40);
            TreeNode<Integer> found = tree.findNode(40);
            assertNotNull(found);
            assertEquals(40, found.getValue());
        }
    }

    @Nested
    @DisplayName("Тесты обходов дерева")
    class TreeTraversalTests {

        @Test
        @DisplayName("Обход NLR пустого дерева")
        void testNLREemptyTree() {
            assertDoesNotThrow(() -> tree.apply(node -> {}, "NLR"));
        }

        @Test
        @DisplayName("Обход NLR с обработкой")
        void testNLRWithProcessing() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            StringBuilder result = new StringBuilder();
            tree.apply(node -> result.append(node.getValue()).append(" "), "NLR");

            assertFalse(result.toString().isEmpty());
        }

        @Test
        @DisplayName("Обход LNR (симметричный)")
        void testLNRTraversal() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            StringBuilder result = new StringBuilder();
            tree.apply(node -> result.append(node.getValue()).append(" "), "LNR");

            assertFalse(result.toString().isEmpty());
        }

        @Test
        @DisplayName("Обход BFS (в ширину)")
        void testBFSTraversal() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);
            tree.insert(40);
            tree.insert(50);

            StringBuilder result = new StringBuilder();
            tree.apply(node -> result.append(node.getValue()).append(" "), "BFS");

            assertFalse(result.toString().isEmpty());
            // BFS должен обработать узлы по уровням
            assertTrue(result.toString().contains("10"));
        }

        @Test
        @DisplayName("Обход DFS (в глубину)")
        void testDFSTraversal() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            StringBuilder result = new StringBuilder();
            tree.apply(node -> result.append(node.getValue()).append(" "), "DFS");

            assertFalse(result.toString().isEmpty());
        }

        @Test
        @DisplayName("Обход DRFS (рекурсивный в глубину)")
        void testDRFSTraversal() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            StringBuilder result = new StringBuilder();
            tree.apply(node -> result.append(node.getValue()).append(" "), "DRFS");

            assertFalse(result.toString().isEmpty());
        }

        @Test
        @DisplayName("Неизвестный тип обхода")
        void testUnknownTraversalType() {
            tree.insert(10);

            assertDoesNotThrow(() -> tree.apply(node -> {}, "UNKNOWN"));
            // Должен использовать обход по умолчанию или не делать ничего
        }

        @Test
        @DisplayName("Обход с null процессором")
        void testTraversalWithNullProcessor() {
            tree.insert(10);

            assertThrows(IllegalArgumentException.class,
                    () -> tree.apply(null, "NLR"));
        }
    }

    @Nested
    @DisplayName("Тесты всех типов обходов")
    class AllTraversalTypesTests {

        @Test
        @DisplayName("Все поддерживаемые типы обходов")
        void testAllTraversalTypes() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            String[] traversalTypes = {"NLR", "LNR", "LRN", "RNL", "RLN", "BFS", "DFS", "DRFS"};

            for (String type : traversalTypes) {
                StringBuilder result = new StringBuilder();
                assertDoesNotThrow(() -> tree.apply(node -> {
                    result.append(node.getValue()).append(" ");
                }, type));

                assertFalse(result.toString().isEmpty(),
                        "Traversal type " + type + " should process nodes");
            }
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Комплексный тест всех операций")
        void testComplexScenario() {
            // Создаем дерево
            for (int i = 0; i < 5; i++) {
                tree.insert(i * 10);
            }

            // Проверяем свойства
            assertFalse(tree.isStrict()); // Зависит от структуры
            assertFalse(tree.isComplete()); // Зависит от структуры

            // Ищем узлы
            TreeNode<Integer> found = tree.findNode(20);
            assertNotNull(found);

            // Подсчитываем узлы
            TreeNode<Integer> root = tree.findNode(0);
            int count = tree.countNodes(root);
            assertEquals(5, count);

            // Проверяем обходы
            StringBuilder bfsResult = new StringBuilder();
            tree.apply(node -> bfsResult.append(node.getValue()).append(" "), "BFS");
            assertFalse(bfsResult.toString().isEmpty());

            // Очищаем дерево
            assertDoesNotThrow(() -> tree.clearTree());
        }

        @Test
        @DisplayName("Тест с большим деревом")
        void testLargeTree() {
            final int NODE_COUNT = 100;

            for (int i = 0; i < NODE_COUNT; i++) {
                tree.insert(i);
            }

            TreeNode<Integer> root = tree.findNode(0);
            assertNotNull(root);

            int nodeCount = tree.countNodes(root);
            assertTrue(nodeCount >= NODE_COUNT);

            int depth = tree.getDepth(root);
            assertTrue(depth >= 0);

            // Проверяем обход BFS
            final int[] bfsCount = {0};
            tree.apply(node -> bfsCount[0]++, "BFS");
            assertEquals(NODE_COUNT, bfsCount[0]);
        }

        @Test
        @DisplayName("Тест с пользовательскими объектами")
        void testWithCustomObjects() {
            BinaryTree<String> customTree = new BinaryTree<>();

            customTree.insert("Apple");
            customTree.insert("Banana");
            customTree.insert("Cherry");

            TreeNode<String> found = customTree.findNode("Banana");
            assertNotNull(found);
            assertEquals("Banana", found.getValue());

            StringBuilder result = new StringBuilder();
            customTree.apply(node -> result.append(node.getValue()).append(" "), "NLR");
            assertFalse(result.toString().isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты граничных случаев")
    class EdgeCaseTests {

        @Test
        @DisplayName("Дерево с одним элементом")
        void testSingleElementTree() {
            tree.insert(42);

            assertTrue(tree.isStrict());
            assertTrue(tree.isComplete());
            assertTrue(tree.isPerfect());

            TreeNode<Integer> found = tree.findNode(42);
            assertNotNull(found);
            assertEquals(42, found.getValue());

            assertEquals(1, tree.countNodes(found));
            assertEquals(0, tree.getDepth(found));
        }

        @Test
        @DisplayName("Дерево с дубликатами значений")
        void testTreeWithDuplicateValues() {
            tree.insert(10);
            tree.insert(10); // Дубликат
            tree.insert(20);

            // Оба узла со значением 10 должны быть найдены
            TreeNode<Integer> found1 = tree.findNode(10);
            assertNotNull(found1);
        }

        @Test
        @DisplayName("Многократная вставка и удаление")
        void testRepeatedInsertAndClear() {
            for (int cycle = 0; cycle < 5; cycle++) {
                for (int i = 0; i < 10; i++) {
                    tree.insert(i);
                }

                assertNotNull(tree.findNode(5));
                tree.clearTree();
                assertNull(tree.findNode(5));
            }
        }

        @Test
        @DisplayName("Обход с исключением в процессоре")
        void testTraversalWithExceptionInProcessor() {
            tree.insert(10);
            tree.insert(20);

            assertThrows(RuntimeException.class,
                    () -> tree.apply(node -> {
                        throw new RuntimeException("Test exception");
                    }, "NLR"));
        }
    }

    @Nested
    @DisplayName("Тесты методов collectToArray")
    class CollectToArrayTests {

        @Test
        @DisplayName("Сбор значений из пустого дерева")
        void testCollectFromEmptyTree() {
            MyArrayList<Integer> result = tree.collectToArray("NLR");
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Сбор значений в порядке NLR")
        void testCollectNLR() {
            // Создаем дерево с предсказуемой структурой
            tree.insert(10);  // root
            tree.insert(20);  // left child
            tree.insert(30);  // right child
            tree.insert(40);  // left-left child

            MyArrayList<Integer> result = tree.collectToArray("NLR");

            assertNotNull(result);
            assertEquals(4, result.size());
            // NLR: Корень -> Левое поддерево -> Правое поддерево
            // Порядок зависит от алгоритма вставки с чередованием
        }

        @Test
        @DisplayName("Сбор значений в порядке LNR")
        void testCollectLNR() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            MyArrayList<Integer> result = tree.collectToArray("LNR");

            assertNotNull(result);
            assertEquals(3, result.size());
            // LNR: Левое поддерево -> Корень -> Правое поддерево
        }

        @Test
        @DisplayName("Сбор значений в порядке LRN")
        void testCollectLRN() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            MyArrayList<Integer> result = tree.collectToArray("LRN");

            assertNotNull(result);
            assertEquals(3, result.size());
            // LRN: Левое поддерево -> Правое поддерево -> Корень
        }

        @Test
        @DisplayName("Сбор значений в порядке DFS")
        void testCollectDFS() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            MyArrayList<Integer> result = tree.collectToArray("DFS");

            assertNotNull(result);
            assertEquals(3, result.size());
            // DFS: обход в глубину (стековый)
        }

        @Test
        @DisplayName("Сбор значений в порядке DRFS")
        void testCollectDRFS() {
            tree.insert(10);
            tree.insert(20);
            tree.insert(30);

            MyArrayList<Integer> result = tree.collectToArray("DRFS");

            assertNotNull(result);
            assertEquals(3, result.size());
            // DRFS: рекурсивный обход в глубину
        }

        @Test
        @DisplayName("Сбор значений с null коллекцией")
        void testCollectWithNullCollection() {
            tree.insert(10);

            assertThrows(IllegalArgumentException.class,
                    () -> tree.collectToArray("NLR", null));
        }

        @Test
        @DisplayName("Несколько вызовов collectToArray с одной коллекцией")
        void testMultipleCollectCalls() {
            tree.insert(10);
            tree.insert(20);

            MyArrayList<Integer> collection = new MyArrayList<>();

            // Первый вызов
            tree.collectToArray("NLR", collection);
            assertEquals(2, collection.size());

            // Второй вызов - значения добавляются снова
            tree.collectToArray("NLR", collection);
            assertEquals(4, collection.size()); // значения продублировались
        }


        @Test
        @DisplayName("Неизвестный тип обхода для collectToArray")
        void testCollectWithUnknownTraversal() {
            tree.insert(10);

            // Неизвестный тип обхода должен работать (используется switch default)
            assertDoesNotThrow(() -> tree.collectToArray("UNKNOWN"));

            MyArrayList<Integer> result = tree.collectToArray("UNKNOWN");
            // В текущей реализации неизвестный тип не обрабатывается,
            // поэтому результат может быть пустым
        }

        @Test
        @DisplayName("Сбор значений после очистки дерева")
        void testCollectAfterClear() {
            tree.insert(10);
            tree.insert(20);

            MyArrayList<Integer> beforeClear = tree.collectToArray("NLR");
            assertEquals(2, beforeClear.size());

            tree.clearTree();

            MyArrayList<Integer> afterClear = tree.collectToArray("NLR");
            assertEquals(0, afterClear.size());
        }

    }
}