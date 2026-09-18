import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import trees.BinarySearchTree;
import trees.TreeNode;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса trees.BinarySearchTree
 */
public class BST_Test {

    private BinarySearchTree<Integer> bst;
    private BinarySearchTree<String> stringBst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
        stringBst = new BinarySearchTree<>();
    }

    @Nested
    @DisplayName("Тесты конструктора и начального состояния")
    class ConstructorTests {

        @Test
        @DisplayName("Создание пустого BST")
        void testEmptyBST() {
            assertDoesNotThrow(() -> new BinarySearchTree<Integer>());
            assertFalse(bst.search(1));
        }

        @Test
        @DisplayName("Поиск в пустом дереве возвращает false")
        void testSearchInEmptyTree() {
            assertFalse(bst.search(10));
        }
    }

    @Nested
    @DisplayName("Тесты вставки элементов")
    class InsertTests {

        @Test
        @DisplayName("Вставка одного элемента")
        void testInsertSingleElement() {
            bst.insert(10);
            assertTrue(bst.search(10));
        }

        @Test
        @DisplayName("Вставка нескольких элементов")
        void testInsertMultipleElements() {
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);

            assertTrue(bst.search(10));
            assertTrue(bst.search(5));
            assertTrue(bst.search(15));
        }

        @Test
        @DisplayName("Вставка элемента, который уже существует")
        void testInsertDuplicateElement() {
            bst.insert(10);
            bst.insert(10); // Дубликат

            // Дерево должно остаться неизменным
            assertTrue(bst.search(10));
        }

        @Test
        @DisplayName("Вставка null вызывает исключение")
        void testInsertNullThrowsException() {
            assertThrows(NullPointerException.class, () -> bst.insert(null));
        }

        @Test
        @DisplayName("Вставка элементов в правильном порядке")
        void testInsertMaintainsOrder() {
            bst.insert(50);
            bst.insert(30);
            bst.insert(70);
            bst.insert(20);
            bst.insert(40);
            bst.insert(60);
            bst.insert(80);

            // Проверяем, что все элементы на месте
            assertTrue(bst.search(20));
            assertTrue(bst.search(30));
            assertTrue(bst.search(40));
            assertTrue(bst.search(50));
            assertTrue(bst.search(60));
            assertTrue(bst.search(70));
            assertTrue(bst.search(80));
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1, 0, 1, 100, Integer.MAX_VALUE, Integer.MIN_VALUE})
        @DisplayName("Вставка различных целочисленных значений")
        void testInsertVariousValues(int value) {
            bst.insert(value);
            assertTrue(bst.search(value));
        }

        @Test
        @DisplayName("Вставка строковых значений")
        void testInsertStringValues() {
            stringBst.insert("Apple");
            stringBst.insert("Banana");
            stringBst.insert("Cherry");

            assertTrue(stringBst.search("Apple"));
            assertTrue(stringBst.search("Banana"));
            assertTrue(stringBst.search("Cherry"));
        }
    }

    @Nested
    @DisplayName("Тесты поиска элементов")
    class SearchTests {

        @Test
        @DisplayName("Поиск существующего элемента")
        void testSearchExistingElement() {
            bst.insert(10);
            bst.insert(20);
            bst.insert(30);

            assertTrue(bst.search(20));
        }

        @Test
        @DisplayName("Поиск несуществующего элемента")
        void testSearchNonExistingElement() {
            bst.insert(10);
            bst.insert(20);

            assertFalse(bst.search(30));
        }

        @Test
        @DisplayName("Поиск null вызывает исключение")
        void testSearchNullThrowsException() {
            assertThrows(NullPointerException.class, () -> bst.search(null));
        }

        @Test
        @DisplayName("Поиск после множественных вставок")
        void testSearchAfterMultipleInserts() {
            for (int i = 0; i < 100; i++) {
                bst.insert(i);
            }

            for (int i = 0; i < 100; i++) {
                assertTrue(bst.search(i), "Should find value: " + i);
            }

            assertFalse(bst.search(100));
            assertFalse(bst.search(-1));
        }

        @Test
        @DisplayName("Поиск в глубоком дереве")
        void testSearchInDeepTree() {
            // Создаем вырожденное дерево (связанный список)
            for (int i = 1; i <= 50; i++) {
                bst.insert(i);
            }

            assertTrue(bst.search(25));
            assertTrue(bst.search(50));
            assertFalse(bst.search(51));
        }
    }

    @Nested
    @DisplayName("Тесты удаления элементов")
    class RemoveTests {

        @Test
        @DisplayName("Удаление из пустого дерева")
        void testRemoveFromEmptyTree() {
            assertDoesNotThrow(() -> bst.remove(10));
        }

        @Test
        @DisplayName("Удаление единственного элемента")
        void testRemoveSingleElement() {
            bst.insert(10);
            assertTrue(bst.search(10));

            bst.remove(10);
            assertFalse(bst.search(10));
        }

        @Test
        @DisplayName("Удаление листа (нет детей)")
        void testRemoveLeafNode() {
            // Дерево:     10
            //           /   \
            //          5     15
            //         / \   / \
            //        3   7 12  20
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);
            bst.insert(3);
            bst.insert(7);
            bst.insert(12);
            bst.insert(20);

            // Удаляем лист 3
            bst.remove(3);
            assertFalse(bst.search(3));
            assertTrue(bst.search(5));  // родитель остается
            assertTrue(bst.search(10)); // корень остается
        }

        @Test
        @DisplayName("Удаление узла с одним ребенком (слева)")
        void testRemoveNodeWithOneChildLeft() {
            // Дерево:     10
            //           /   \
            //          5     15
            //         /
            //        3
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);
            bst.insert(3);

            bst.remove(5); // узел 5 имеет только левого ребенка (3)

            assertFalse(bst.search(5));
            assertTrue(bst.search(3));  // ребенок должен остаться
            assertTrue(bst.search(10));
            assertTrue(bst.search(15));
        }

        @Test
        @DisplayName("Удаление узла с одним ребенком (справа)")
        void testRemoveNodeWithOneChildRight() {
            // Дерево:     10
            //           /   \
            //          5     15
            //           \
            //            7
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);
            bst.insert(7);

            bst.remove(5); // узел 5 имеет только правого ребенка (7)

            assertFalse(bst.search(5));
            assertTrue(bst.search(7));  // ребенок должен остаться
            assertTrue(bst.search(10));
            assertTrue(bst.search(15));
        }

        @Test
        @DisplayName("Удаление узла с двумя детьми")
        void testRemoveNodeWithTwoChildren() {
            // Дерево:     10
            //           /   \
            //          5     15
            //         / \   / \
            //        3   7 12  20
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);
            bst.insert(3);
            bst.insert(7);
            bst.insert(12);
            bst.insert(20);

            // Удаляем узел 10 (корень с двумя детьми)
            // Successor(10) = 12
            bst.remove(10);

            assertFalse(bst.search(10));
            // Проверяем, что successor (12) занял место корня
            assertTrue(bst.search(12));
            // Проверяем, что все остальные элементы на месте
            assertTrue(bst.search(5));
            assertTrue(bst.search(3));
            assertTrue(bst.search(7));
            assertTrue(bst.search(15));
            assertTrue(bst.search(20));
        }

        @Test
        @DisplayName("Удаление корня с двумя детьми и сложным successor")
        void testRemoveRootWithComplexSuccessor() {
            // Дерево:       50
            //             /    \
            //           30      70
            //          /  \    /  \
            //        20   40  60   80
            //               \
            //               45
            bst.insert(50);
            bst.insert(30);
            bst.insert(70);
            bst.insert(20);
            bst.insert(40);
            bst.insert(60);
            bst.insert(80);
            bst.insert(45);

            // Удаляем корень 50
            // Successor(50) = 60
            bst.remove(50);

            assertFalse(bst.search(50));
            assertTrue(bst.search(60)); // новый корень
            // Проверяем, что все элементы на месте
            assertTrue(bst.search(30));
            assertTrue(bst.search(20));
            assertTrue(bst.search(40));
            assertTrue(bst.search(45));
            assertTrue(bst.search(70));
            assertTrue(bst.search(80));
        }

        @Test
        @DisplayName("Удаление несуществующего элемента")
        void testRemoveNonExistingElement() {
            bst.insert(10);
            bst.insert(20);

            assertDoesNotThrow(() -> bst.remove(30));
            // Дерево не должно измениться
            assertTrue(bst.search(10));
            assertTrue(bst.search(20));
        }

        @Test
        @DisplayName("Удаление null вызывает исключение")
        void testRemoveNullThrowsException() {
            assertThrows(NullPointerException.class, () -> bst.remove(null));
        }

        @Test
        @DisplayName("Множественные удаления")
        void testMultipleRemovals() {
            for (int i = 0; i < 10; i++) {
                bst.insert(i);
            }

            // Удаляем четные числа
            for (int i = 0; i < 10; i += 2) {
                bst.remove(i);
            }

            // Проверяем результат
            for (int i = 0; i < 10; i++) {
                if (i % 2 == 0) {
                    assertFalse(bst.search(i), "Even number should be removed: " + i);
                } else {
                    assertTrue(bst.search(i), "Odd number should remain: " + i);
                }
            }
        }

        @Test
        @DisplayName("Удаление всех элементов")
        void testRemoveAllElements() {
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);

            bst.remove(5);
            bst.remove(10);
            bst.remove(15);

            assertFalse(bst.search(5));
            assertFalse(bst.search(10));
            assertFalse(bst.search(15));
        }
    }

    @Nested
    @DisplayName("Тесты поиска successor")
    class SuccessorTests {

        @Test
        @DisplayName("Successor в пустом дереве")
        void testSuccessorInEmptyTree() {
            assertNull(bst.successor(10));
        }

        @Test
        @DisplayName("Successor для несуществующего элемента")
        void testSuccessorForNonExistingElement() {
            bst.insert(10);
            bst.insert(20);
            bst.insert(30);

            assertNull(bst.successor(25));
        }

        @Test
        @DisplayName("Successor для максимального элемента")
        void testSuccessorForMaxElement() {
            bst.insert(10);
            bst.insert(20);
            bst.insert(30);

            assertNull(bst.successor(30));
        }

        @Test
        @DisplayName("Successor для узла с правым ребенком")
        void testSuccessorWithRightChild() {
            // Дерево:     10
            //           /   \
            //          5     15
            //               /  \
            //             12    20
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);
            bst.insert(12);
            bst.insert(20);

            // Successor(10) = 12 (минимальный в правом поддереве)
            assertEquals(12, bst.successor(10));

            // Successor(15) = 20 (минимальный в правом поддереве)
            assertEquals(20, bst.successor(15));
        }

        @Test
        @DisplayName("Successor для узла без правого ребенка")
        void testSuccessorWithoutRightChild() {
            // Дерево:       15
            //             /    \
            //           10      20
            //          /  \    /
            //         5   12  17
            //        / \
            //       3   7
            bst.insert(15);
            bst.insert(10);
            bst.insert(20);
            bst.insert(5);
            bst.insert(12);
            bst.insert(17);
            bst.insert(3);
            bst.insert(7);

            // Successor(12) = 15 (первый предок, у которого 12 в левом поддереве)
            assertEquals(15, bst.successor(12));

            // Successor(7) = 10 (первый предок, у которого 7 в левом поддереве)
            assertEquals(10, bst.successor(7));

            // Successor(17) = 20 (правого ребенка нет, ищем предка)
            assertEquals(20, bst.successor(17));
        }

        @Test
        @DisplayName("Successor для всех элементов дерева")
        void testSuccessorForAllElements() {
            // Дерево:     4
            //           /   \
            //          2     6
            //         / \   / \
            //        1   3 5   7
            bst.insert(4);
            bst.insert(2);
            bst.insert(6);
            bst.insert(1);
            bst.insert(3);
            bst.insert(5);
            bst.insert(7);

            assertEquals(2, bst.successor(1));
            assertEquals(3, bst.successor(2));
            assertEquals(4, bst.successor(3));
            assertEquals(5, bst.successor(4));
            assertEquals(6, bst.successor(5));
            assertEquals(7, bst.successor(6));
            assertNull(bst.successor(7)); // максимальный элемент
        }

        @Test
        @DisplayName("Successor с null вызывает исключение")
        void testSuccessorNullThrowsException() {
            assertThrows(NullPointerException.class, () -> bst.successor(null));
        }

        @Test
        @DisplayName("Successor для вырожденного дерева")
        void testSuccessorInDegenerateTree() {
            // Создаем вырожденное дерево: 1 -> 2 -> 3 -> 4
            for (int i = 1; i <= 4; i++) {
                bst.insert(i);
            }

            assertEquals(2, bst.successor(1));
            assertEquals(3, bst.successor(2));
            assertEquals(4, bst.successor(3));
            assertNull(bst.successor(4));
        }
    }

    @Nested
    @DisplayName("Тесты статических вспомогательных методов")
    class StaticMethodTests {

        @Test
        @DisplayName("findMin для пустого дерева")
        void testFindMinEmpty() {
            assertNull(BinarySearchTree.findMin(null));
        }

        @Test
        @DisplayName("findMin для одного узла")
        void testFindMinSingleNode() {
            TreeNode<Integer> node = new TreeNode<>(10);
            assertEquals(node, BinarySearchTree.findMin(node));
        }

        @Test
        @DisplayName("findMin для сложного дерева")
        void testFindMinComplexTree() {
            TreeNode<Integer> root = new TreeNode<>(10);
            root.setLeftChild(new TreeNode<>(5));
            root.setRightChild(new TreeNode<>(15));
            root.getLeftChild().setLeftChild(new TreeNode<>(3));
            root.getLeftChild().setRightChild(new TreeNode<>(7));

            TreeNode<Integer> min = BinarySearchTree.findMin(root);
            assertNotNull(min);
            assertEquals(3, min.getValue());
        }

        @Test
        @DisplayName("findNodeInBST поиск существующего узла")
        void testFindNodeInBSTExisting() {
            TreeNode<Integer> root = new TreeNode<>(10);
            root.setLeftChild(new TreeNode<>(5));
            root.setRightChild(new TreeNode<>(15));

            TreeNode<Integer> found = BinarySearchTree.findNodeInBST(root, 5);
            assertNotNull(found);
            assertEquals(5, found.getValue());
        }

        @Test
        @DisplayName("findNodeInBST поиск несуществующего узла")
        void testFindNodeInBSTNonExisting() {
            TreeNode<Integer> root = new TreeNode<>(10);
            assertNull(BinarySearchTree.findNodeInBST(root, 20));
        }

        @Test
        @DisplayName("findNodeInBST с null вызывает исключение")
        void testFindNodeInBSTNullThrowsException() {
            TreeNode<Integer> root = new TreeNode<>(10);
            assertThrows(NullPointerException.class,
                    () -> BinarySearchTree.findNodeInBST(root, null));
        }

        @Test
        @DisplayName("deleteMin для одного узла")
        void testDeleteMinSingleNode() {
            TreeNode<Integer> node = new TreeNode<>(10);
            TreeNode<Integer> result = BinarySearchTree.deleteMin(node);
            assertNull(result);
        }

        @Test
        @DisplayName("deleteMin для дерева с левым ребенком")
        void testDeleteMinWithLeftChild() {
            TreeNode<Integer> root = new TreeNode<>(10);
            root.setLeftChild(new TreeNode<>(5));
            root.setRightChild(new TreeNode<>(15));

            TreeNode<Integer> result = BinarySearchTree.deleteMin(root);
            assertNotNull(result);
            assertEquals(10, result.getValue());
            assertNull(result.getLeftChild()); // минимальный узел удален
        }

        @Test
        @DisplayName("deleteMin для глубокого левого поддерева")
        void testDeleteMinDeepLeftSubtree() {
            TreeNode<Integer> root = new TreeNode<>(20);
            root.setLeftChild(new TreeNode<>(10));
            root.getLeftChild().setLeftChild(new TreeNode<>(5));
            root.getLeftChild().getLeftChild().setLeftChild(new TreeNode<>(3));

            TreeNode<Integer> result = BinarySearchTree.deleteMin(root);
            assertNotNull(result);
            // Минимальный узел 3 должен быть удален
            assertEquals(20, result.getValue());
            assertEquals(10, result.getLeftChild().getValue());
            assertEquals(5, result.getLeftChild().getLeftChild().getValue());
            assertNull(result.getLeftChild().getLeftChild().getLeftChild());
        }
    }

    @Nested
    @DisplayName("Тесты итераторов")
    class IteratorTests {

        @Test
        @DisplayName("In-order итератор пустого дерева")
        void testInOrderIteratorEmptyTree() {
            var it = bst.iterator();
            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
        }

        @Test
        @DisplayName("In-order итератор одного элемента")
        void testInOrderIteratorSingleElement() {
            bst.insert(5);
            var it = bst.iterator();
            assertTrue(it.hasNext());
            assertEquals(5, it.next());
            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
        }

        @Test
        @DisplayName("In-order итератор выдаёт элементы в возрастающем порядке")
        void testInOrderIteratorOrdering() {
            int[] values = {4, 2, 6, 1, 3, 5, 7};
            for (int v : values) bst.insert(v);

            var it = bst.iterator();
            int expected = 1;
            while (it.hasNext()) {
                assertEquals(expected, it.next());
                expected++;
            }
            assertEquals(8, expected);
        }

        @Test
        @DisplayName("In-order в for-each цикле")
        void testInOrderIteratorForEach() {
            bst.insert(3);
            bst.insert(1);
            bst.insert(2);
            StringBuilder sb = new StringBuilder();
            for (int val : bst) {
                sb.append(val);
            }
            assertEquals("123", sb.toString());
        }

        @Test
        @DisplayName("Удаление через in-order итератор (remove())")
        void testInOrderIteratorRemove() {
            bst.insert(2);
            bst.insert(1);
            bst.insert(3);
            var it = bst.iterator();

            assertEquals(1, it.next());
            it.remove(); // удаляем наименьший
            assertEquals(2, it.next());
            assertEquals(3, it.next());
            assertFalse(it.hasNext());

            // после удаления 1, дерево должно содержать 2 и 3
            assertFalse(bst.search(1));
            assertTrue(bst.search(2));
            assertTrue(bst.search(3));
        }

        @Test
        @DisplayName("IllegalStateException при повторном remove() без next()")
        void testInOrderIteratorDoubleRemove() {
            bst.insert(1);
            bst.insert(2);
            var it = bst.iterator();
            it.next();
            it.remove();
            assertThrows(IllegalStateException.class, it::remove);
        }

        @Test
        @DisplayName("IllegalStateException при remove() до первого next()")
        void testInOrderIteratorRemoveBeforeNext() {
            bst.insert(1);
            var it = bst.iterator();
            assertThrows(IllegalStateException.class, it::remove);
        }

        @Test
        @DisplayName("Fail-fast при вставке после создания итератора")
        void testInOrderIteratorFailFastOnInsert() {
            bst.insert(2);
            bst.insert(1);
            var it = bst.iterator();
            assertEquals(1, it.next());
            bst.insert(3); // структурная модификация
            assertThrows(ConcurrentModificationException.class, it::next);
        }

        @Test
        @DisplayName("Fail-fast при прямом удалении после создания итератора")
        void testInOrderIteratorFailFastOnDirectRemove() {
            bst.insert(2);
            bst.insert(1);
            var it = bst.iterator();
            assertEquals(1, it.next());
            bst.remove(2); // прямое удаление
            assertThrows(ConcurrentModificationException.class, it::next);
        }

        @Test
        @DisplayName("Descending итератор пустого дерева")
        void testDescendingIteratorEmpty() {
            var it = bst.descendingIterator();
            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
        }

        @Test
        @DisplayName("Descending итератор выдаёт элементы в убывающем порядке")
        void testDescendingIteratorOrdering() {
            int[] values = {4, 2, 6, 1, 3, 5, 7};
            for (int v : values) bst.insert(v);

            var it = bst.descendingIterator();
            int expected = 7;
            while (it.hasNext()) {
                assertEquals(expected, it.next());
                expected--;
            }
            assertEquals(0, expected);
        }

        @Test
        @DisplayName("Удаление через descending итератор")
        void testDescendingIteratorRemove() {
            bst.insert(2);
            bst.insert(1);
            bst.insert(3);
            var it = bst.descendingIterator();
            assertEquals(3, it.next());
            it.remove(); // удаляем максимум
            assertEquals(2, it.next());
            assertEquals(1, it.next());
            assertFalse(it.hasNext());

            assertFalse(bst.search(3));
            assertTrue(bst.search(2));
            assertTrue(bst.search(1));
        }

        @Test
        @DisplayName("Fail-fast descending итератор при вставке")
        void testDescendingIteratorFailFast() {
            bst.insert(2);
            bst.insert(1);
            var it = bst.descendingIterator();
            assertEquals(2, it.next());
            bst.insert(3);
            assertThrows(ConcurrentModificationException.class, it::next);
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Комплексный сценарий: вставка, поиск, удаление, successor")
        void testComplexScenario() {
            // 1. Вставка элементов
            int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 85};
            for (int value : values) {
                bst.insert(value);
            }

            // 2. Проверка поиска всех элементов
            for (int value : values) {
                assertTrue(bst.search(value), "Should find value: " + value);
            }

            // 3. Проверка некоторых successor
            assertEquals(25, bst.successor(20));
            assertEquals(50, bst.successor(45));
            assertEquals(55, bst.successor(50));
            assertEquals(65, bst.successor(60));

            // 4. Удаление некоторых элементов
            bst.remove(20);  // узел с двумя детьми
            bst.remove(40);  // узел с двумя детьми
            bst.remove(10);  // лист
            bst.remove(85);  // лист

            // 5. Проверка, что удаленные элементы не найдены
            assertFalse(bst.search(20));
            assertFalse(bst.search(40));
            assertFalse(bst.search(10));
            assertFalse(bst.search(85));

            // 6. Проверка, что оставшиеся элементы на месте
            assertTrue(bst.search(50));
            assertTrue(bst.search(30));
            assertTrue(bst.search(70));
            assertTrue(bst.search(60));
            assertTrue(bst.search(80));
        }

        @Test
        @DisplayName("Тест с большим количеством элементов")
        void testLargeNumberOfElements() {
            final int COUNT = 1000;

            // Вставляем элементы в случайном порядке
            for (int i = 0; i < COUNT; i++) {
                bst.insert((i * 37) % COUNT); // псевдослучайная последовательность
            }

            // Проверяем поиск
            for (int i = 0; i < COUNT; i++) {
                assertTrue(bst.search(i), "Should find value: " + i);
            }

            // Удаляем половину элементов
            for (int i = 0; i < COUNT; i += 2) {
                bst.remove(i);
            }

            // Проверяем результат
            for (int i = 0; i < COUNT; i++) {
                if (i % 2 == 0) {
                    assertFalse(bst.search(i), "Even number should be removed: " + i);
                } else {
                    assertTrue(bst.search(i), "Odd number should remain: " + i);
                }
            }
        }

        @Test
        @DisplayName("Тест с различными типами данных")
        void testWithDifferentDataTypes() {
            // Тест с Double
            BinarySearchTree<Double> doubleBst = new BinarySearchTree<>();
            doubleBst.insert(3.14);
            doubleBst.insert(2.71);
            doubleBst.insert(1.41);

            assertTrue(doubleBst.search(3.14));
            assertTrue(doubleBst.search(2.71));
            assertFalse(doubleBst.search(0.0));

            // Тест с String
            BinarySearchTree<String> stringBst = new BinarySearchTree<>();
            stringBst.insert("apple");
            stringBst.insert("banana");
            stringBst.insert("cherry");

            assertTrue(stringBst.search("banana"));
            assertFalse(stringBst.search("date"));

            // Тест удаления строк
            stringBst.remove("banana");
            assertFalse(stringBst.search("banana"));
            assertTrue(stringBst.search("apple"));
            assertTrue(stringBst.search("cherry"));
        }

        @Test
        @DisplayName("Тест свойства BST после множественных операций")
        void testBSTPropertyAfterMultipleOperations() {
            // Создаем дерево и выполняем много операций
            for (int i = 0; i < 100; i++) {
                bst.insert(i % 20); // дубликаты
            }

            // Удаляем некоторые элементы
            for (int i = 0; i < 10; i++) {
                bst.remove(i * 2);
            }

            // Добавляем новые элементы
            for (int i = 100; i < 120; i++) {
                bst.insert(i);
            }

            // Проверяем, что дерево все еще работает корректно
            assertTrue(bst.search(1));  // должно остаться
            assertFalse(bst.search(0)); // должно быть удалено
            assertTrue(bst.search(110)); // должно быть добавлено
        }
    }

    @Nested
    @DisplayName("Тесты граничных случаев")
    class EdgeCaseTests {

        @Test
        @DisplayName("Дубликаты при вставке")
        void testDuplicateInsertions() {
            bst.insert(10);
            bst.insert(10);
            bst.insert(10);

            // Дерево должно содержать только один узел со значением 10
            assertTrue(bst.search(10));
            // При удалении должен удалиться полностью
            bst.remove(10);
            assertFalse(bst.search(10));
        }

        @Test
        @DisplayName("Удаление и повторная вставка")
        void testRemoveAndReinsert() {
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);

            bst.remove(10);
            assertFalse(bst.search(10));

            bst.insert(10); // повторная вставка
            assertTrue(bst.search(10));

            // Проверяем, что дерево все еще корректно
            assertTrue(bst.search(5));
            assertTrue(bst.search(15));
        }

        @Test
        @DisplayName("Successor для единственного элемента")
        void testSuccessorSingleElement() {
            bst.insert(42);
            assertNull(bst.successor(42));
        }

        @Test
        @DisplayName("Удаление корня в разных ситуациях")
        void testRemoveRootVariousCases() {
            // Корень без детей
            bst.insert(10);
            bst.remove(10);
            assertFalse(bst.search(10));

            // Корень с одним ребенком (слева)
            bst.insert(10);
            bst.insert(5);
            bst.remove(10);
            assertFalse(bst.search(10));
            assertTrue(bst.search(5));

            bst = new BinarySearchTree<>(); // сброс

            // Корень с одним ребенком (справа)
            bst.insert(10);
            bst.insert(15);
            bst.remove(10);
            assertFalse(bst.search(10));
            assertTrue(bst.search(15));

            bst = new BinarySearchTree<>(); // сброс

            // Корень с двумя детьми
            bst.insert(10);
            bst.insert(5);
            bst.insert(15);
            bst.remove(10);
            assertFalse(bst.search(10));
            assertTrue(bst.search(5));
            assertTrue(bst.search(15));
        }

        @Test
        @DisplayName("Экстремальные значения")
        void testExtremeValues() {
            bst.insert(Integer.MAX_VALUE);
            bst.insert(Integer.MIN_VALUE);
            bst.insert(0);

            assertTrue(bst.search(Integer.MAX_VALUE));
            assertTrue(bst.search(Integer.MIN_VALUE));
            assertTrue(bst.search(0));

            bst.remove(Integer.MAX_VALUE);
            assertFalse(bst.search(Integer.MAX_VALUE));
            assertTrue(bst.search(Integer.MIN_VALUE));
            assertTrue(bst.search(0));
        }

        @Test
        @DisplayName("Цепочка вставок и удалений")
        void testChainOfOperations() {
            // Проверяем, что дерево остается корректным после многих операций
            for (int i = 0; i < 50; i++) {
                bst.insert(i);
                if (i % 3 == 0) {
                    bst.remove(i / 2);
                }
            }

            // Простая проверка - нет исключений
            assertDoesNotThrow(() -> bst.search(25));
            assertDoesNotThrow(() -> bst.remove(30));
            assertDoesNotThrow(() -> bst.successor(40));
        }
    }
}