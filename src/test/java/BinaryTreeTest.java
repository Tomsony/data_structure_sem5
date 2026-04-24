import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trees.BinarySearchTree;
import trees.TreeNode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для BinarySearchTree.
 * Покрывают базовые случаи: пустое дерево, вставка, поиск,
 * удаление (лист, один ребёнок, два ребёнка), successor.
 */
class BinarySearchTreeTest {

    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    // ---------- ВСТАВКА И ПОИСК ----------

    @Test
    void insertAndSearchSingleElement() {
        // Вставка одного элемента и его поиск
        bst.insert(42);
        assertTrue(bst.search(42), "Элемент должен быть найден");
    }

    @Test
    void searchInEmptyTree() {
        // Поиск в пустом дереве возвращает false
        assertFalse(bst.search(10));
    }

    @Test
    void insertMultipleAndSearch() {
        // Вставка нескольких элементов и проверка их наличия
        bst.insert(8);
        bst.insert(3);
        bst.insert(10);
        bst.insert(1);
        bst.insert(6);
        bst.insert(14);
        bst.insert(4);
        bst.insert(7);
        bst.insert(13);

        // Присутствующие значения
        assertTrue(bst.search(8));
        assertTrue(bst.search(3));
        assertTrue(bst.search(10));
        assertTrue(bst.search(1));
        assertTrue(bst.search(6));
        assertTrue(bst.search(14));
        assertTrue(bst.search(4));
        assertTrue(bst.search(7));
        assertTrue(bst.search(13));

        // Отсутствующее значение
        assertFalse(bst.search(100));
    }

    @Test
    void insertDuplicate() {
        // При попытке вставить дубликат дерево не меняется
        bst.insert(5);
        bst.insert(5);
        // Удаляем 5 – если дубликатов не было, дерево опустеет
        bst.remove(5);
        assertFalse(bst.search(5), "После удаления единственного элемента его не должно быть");
    }

    // ---------- УДАЛЕНИЕ ----------

    @Test
    void removeLeaf() {
        // Удаление листа (узла без детей)
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.remove(5);  // 5 – лист
        assertFalse(bst.search(5), "Удалённый лист не должен находиться");
        assertTrue(bst.search(10));
        assertTrue(bst.search(15));
    }

    @Test
    void removeNodeWithOneChild() {
        // Удаление узла с одним ребёнком
        bst.insert(10);
        bst.insert(5);
        bst.insert(12);
        bst.insert(11);  // 12 – левый ребёнок 11
        // Удаляем 12, у которого есть левый ребёнок 11
        bst.remove(12);
        assertFalse(bst.search(12));
        assertTrue(bst.search(11), "Ребёнок удалённого узла должен остаться в дереве");
        assertTrue(bst.search(10));
        assertTrue(bst.search(5));
    }

    @Test
    void removeNodeWithTwoChildren() {
        // Удаление узла с двумя детьми
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.insert(5);
        bst.insert(15);
        bst.insert(25);
        bst.insert(35);
        bst.insert(12);
        bst.insert(17);

        // Удаляем 30 – правого потомка, у него оба ребёнка (25 и 35)
        bst.remove(30);
        assertFalse(bst.search(30), "Удалённый узел не найден");
        assertTrue(bst.search(35), "Потомки удалённого узла должны остаться");
        assertTrue(bst.search(25));
        assertTrue(bst.search(20));
        assertTrue(bst.search(10));
        assertTrue(bst.search(5));
        assertTrue(bst.search(15));
        assertTrue(bst.search(12));
        assertTrue(bst.search(17));
    }

    @Test
    void removeRootWithTwoChildren() {
        // Удаление корневого узла, имеющего оба поддерева
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        bst.insert(60);
        bst.insert(80);

        bst.remove(50);
        assertFalse(bst.search(50));
        // Все остальные узлы на месте
        assertTrue(bst.search(30));
        assertTrue(bst.search(70));
        assertTrue(bst.search(20));
        assertTrue(bst.search(40));
        assertTrue(bst.search(60));
        assertTrue(bst.search(80));
    }

    @Test
    void removeNonExistent() {
        // Удаление отсутствующего элемента не ломает дерево
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.remove(99);
        // Дерево должно остаться неизменным
        assertTrue(bst.search(5));
        assertTrue(bst.search(3));
        assertTrue(bst.search(7));
    }

    // ---------- SUCCESSOR ----------

    @Test
    void successorWhenRightSubtreeExists() {
        // У узла есть правое поддерево: successor — минимальный в правом
        bst.insert(8);
        bst.insert(3);
        bst.insert(10);
        bst.insert(1);
        bst.insert(5);
        bst.insert(9);
        bst.insert(12);

        assertEquals(Integer.valueOf(9), bst.successor(8));
    }

    @Test
    void successorWhenNoRightSubtree() {
        // У узла нет правого поддерева: successor — первый предок, чей левый потомок на пути
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.insert(5);
        bst.insert(15);
        bst.insert(12);
        bst.insert(17);

        // Узел 17: нет правого, successor — предок 20 (так как 15 — левый потомок 20?)
        // Путь: 20 -> 10 -> 15 -> 17. Предок 15 — левый потомок 20? Нет, 15 — правый потомок 10.
        // 10 — левый потомок 20. Значит successor(17) = 20.
        assertEquals(Integer.valueOf(20), bst.successor(17));
    }

    @Test
    void successorOfMaximum() {
        // Successor максимального элемента в дереве — null
        bst.insert(5);
        bst.insert(2);
        bst.insert(8);
        bst.insert(10);
        assertNull(bst.successor(10));
    }

    @Test
    void successorOfNonExistentValue() {
        // Значение отсутствует в дереве — возвращается null
        bst.insert(1);
        bst.insert(2);
        assertNull(bst.successor(99));
    }

    @Test
    void successorWithNullThrows() {
        // Передача null вызывает исключение
        bst.insert(1);
        assertThrows(NullPointerException.class, () -> bst.successor(null));
    }

    // ---------- КОМБИНИРОВАННЫЕ СЦЕНАРИИ ----------

    @Test
    void insertRemoveSequenceMaintainsBST() {
        // Последовательность вставок и удалений, дерево сохраняет BST-свойство
        bst.insert(50);
        bst.insert(25);
        bst.insert(75);
        bst.insert(10);
        bst.insert(30);
        bst.insert(60);
        bst.insert(90);

        assertTrue(bst.search(30));
        bst.remove(25);
        assertFalse(bst.search(25));
        assertTrue(bst.search(30)); // потомок остался
        assertTrue(bst.search(10));
        // Проверка структуры через successor
        assertEquals(Integer.valueOf(30), bst.successor(10));
    }

    @Test
    void staticSearchWorks() {
        // Тестирование статического метода поиска
        TreeNode<Integer> root = null;
        root = BinarySearchTree.insertRecursive(root, 100);
        root = BinarySearchTree.insertRecursive(root, 50);
        root = BinarySearchTree.insertRecursive(root, 150);

        assertTrue(BinarySearchTree.search(root, 100));
        assertFalse(BinarySearchTree.search(root, 200));
    }

    @Test
    void staticInsertAndDeleteMaintainIntegrity() {
        // Статические вставка и удаление корректно обновляют дерево
        TreeNode<Integer> root = null;
        root = BinarySearchTree.insertRecursive(root, 40);
        root = BinarySearchTree.insertRecursive(root, 20);
        root = BinarySearchTree.insertRecursive(root, 60);
        root = BinarySearchTree.insertRecursive(root, 10);
        root = BinarySearchTree.insertRecursive(root, 30);

        root = BinarySearchTree.removeRecursive(root, 20);
        assertFalse(BinarySearchTree.search(root, 20));
        assertTrue(BinarySearchTree.search(root, 10));
        assertTrue(BinarySearchTree.search(root, 30));
        assertTrue(BinarySearchTree.search(root, 40));
        assertTrue(BinarySearchTree.search(root, 60));
    }

    @Test
    void copyEmptyTree() {
        // Копия пустого дерева — тоже пустое дерево
        BinarySearchTree<Integer> original = new BinarySearchTree<>();
        BinarySearchTree<Integer> copy = original.copy();

        assertFalse(copy.search(1), "Копия пустого дерева не должна содержать элементов");
    }

    @Test
    void copySingleNode() {
        // Копия дерева с одним узлом
        BinarySearchTree<Integer> original = new BinarySearchTree<>();
        original.insert(42);

        BinarySearchTree<Integer> copy = original.copy();

        assertTrue(copy.search(42), "Копия должна содержать тот же элемент");
        assertFalse(copy.search(100), "Посторонних элементов быть не должно");
    }

    @Test
    void copyPreservesAllValues() {
        // Все значения из оригинала присутствуют в копии
        BinarySearchTree<Integer> original = new BinarySearchTree<>();
        int[] values = {8, 3, 10, 1, 6, 14, 4, 7, 13};

        for (int v : values) {
            original.insert(v);
        }

        BinarySearchTree<Integer> copy = original.copy();

        for (int v : values) {
            assertTrue(copy.search(v), "Копия должна содержать значение " + v);
        }
    }

    @Test
    void copyIsIndependent() {
        // Изменения в копии не влияют на оригинал, и наоборот
        BinarySearchTree<Integer> original = new BinarySearchTree<>();
        original.insert(10);
        original.insert(5);
        original.insert(15);
        original.insert(3);
        original.insert(7);

        BinarySearchTree<Integer> copy = original.copy();

        // Изменяем копию: добавляем новый элемент
        copy.insert(20);

        // Оригинал не должен содержать новый элемент
        assertFalse(original.search(20),
                "Оригинал не должен содержать элемент, добавленный в копию");
        assertTrue(copy.search(20),
                "Копия должна содержать новый элемент");

        // Изменяем копию: удаляем элемент
        copy.remove(5);

        // Оригинал должен сохранить удалённый элемент
        assertTrue(original.search(5),
                "Оригинал должен сохранить элемент, удалённый из копии");
        assertFalse(copy.search(5),
                "Копия не должна содержать удалённый элемент");
    }

    @Test
    void copyPreservesBSTProperty() {
        // Копия сохраняет BST-свойство (проверяем через inorder)
        BinarySearchTree<Integer> original = new BinarySearchTree<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 35, 45, 55, 65, 90};

        for (int v : values) {
            original.insert(v);
        }

        BinarySearchTree<Integer> copy = original.copy();

        // Проверяем, что successor работает одинаково в оригинале и копии
        for (int v : values) {
            assertEquals(original.successor(v), copy.successor(v),
                    "Successor для " + v + " должен совпадать в оригинале и копии");
        }
    }
}