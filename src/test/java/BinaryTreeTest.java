import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trees.BinarySearchTree;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для trees.BinarySearchTree.
 *
 * Покрывают:
 * - вставку, поиск, дубликаты;
 * - удаление (лист / один ребёнок / два ребёнка / корень / отсутствующий);
 * - successor;
 * - size / isEmpty;
 * - copy (глубокая копия, независимость).
 *
 * Секция итератора появится после реализации BSTIterator.
 */
class BinarySearchTreeTest {

    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    // ================== ПУСТОЕ ДЕРЕВО ==================

    // новое дерево пустое
    @Test
    void testEmptyTree() {
        assertEquals(0, bst.size());
        assertTrue(bst.isEmpty());
        assertFalse(bst.search(10));
    }

    // ================== ВСТАВКА / ПОИСК ==================

    // вставка одного элемента + поиск
    @Test
    void testInsertAndSearch() {
        bst.insert(42);
        assertEquals(1, bst.size());
        assertTrue(bst.search(42));
    }

    // вставка нескольких элементов + поиск присутствующих/отсутствующих
    @Test
    void testInsertMultiple() {
        for (int x : new int[]{8, 3, 10, 1, 6, 14, 4, 7, 13}) bst.insert(x);

        assertEquals(9, bst.size());
        assertTrue(bst.search(8));
        assertTrue(bst.search(1));
        assertTrue(bst.search(14));
        assertFalse(bst.search(100));
    }

    // дубликат не увеличивает size
    @Test
    void testInsertDuplicate() {
        bst.insert(5);
        bst.insert(5);
        assertEquals(1, bst.size());
        bst.remove(5);
        assertFalse(bst.search(5));
    }

    // ================== УДАЛЕНИЕ ==================

    // удаление листа
    @Test
    void testRemoveLeaf() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.remove(5);

        assertEquals(2, bst.size());
        assertFalse(bst.search(5));
        assertTrue(bst.search(10));
        assertTrue(bst.search(15));
    }

    // удаление узла с одним ребёнком
    @Test
    void testRemoveOneChild() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(12);
        bst.insert(11);
        bst.remove(12);

        assertFalse(bst.search(12));
        assertTrue(bst.search(11));
        assertEquals(3, bst.size());
    }

    // удаление узла с двумя детьми
    @Test
    void testRemoveTwoChildren() {
        for (int x : new int[]{20, 10, 30, 5, 15, 25, 35, 12, 17}) bst.insert(x);
        bst.remove(30);

        assertFalse(bst.search(30));
        assertTrue(bst.search(25));
        assertTrue(bst.search(35));
        assertEquals(8, bst.size());
    }

    // удаление корня с двумя детьми
    @Test
    void testRemoveRoot() {
        for (int x : new int[]{50, 30, 70, 20, 40, 60, 80}) bst.insert(x);
        bst.remove(50);

        assertFalse(bst.search(50));
        assertTrue(bst.search(30));
        assertTrue(bst.search(70));
        assertEquals(6, bst.size());
    }

    // удаление отсутствующего элемента не меняет size
    @Test
    void testRemoveAbsent() {
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.remove(99);

        assertEquals(3, bst.size());
        assertTrue(bst.search(5));
        assertTrue(bst.search(3));
        assertTrue(bst.search(7));
    }

    // ================== SUCCESSOR ==================

    // successor: минимум в правом поддереве
    @Test
    void testSuccessorWithRightSubtree() {
        for (int x : new int[]{8, 3, 10, 1, 5, 9, 12}) bst.insert(x);
        assertEquals(Integer.valueOf(9), bst.successor(8));
    }

    // successor: поднимаемся к предку
    @Test
    void testSuccessorWithoutRightSubtree() {
        for (int x : new int[]{20, 10, 30, 5, 15, 12, 17}) bst.insert(x);
        assertEquals(Integer.valueOf(20), bst.successor(17));
    }

    // successor максимального → null
    @Test
    void testSuccessorOfMax() {
        for (int x : new int[]{5, 2, 8, 10}) bst.insert(x);
        assertNull(bst.successor(10));
    }

    // successor отсутствующего → null
    @Test
    void testSuccessorOfAbsent() {
        bst.insert(1);
        bst.insert(2);
        assertNull(bst.successor(99));
    }

    // ================== COPY ==================

    // копия содержит те же значения
    @Test
    void testCopyPreservesValues() {
        int[] values = {8, 3, 10, 1, 6, 14, 4, 7, 13};
        for (int v : values) bst.insert(v);

        BinarySearchTree<Integer> copy = bst.copy();
        assertEquals(bst.size(), copy.size());
        for (int v : values) assertTrue(copy.search(v));
    }

    // изменения в копии не влияют на оригинал
    @Test
    void testCopyIsIndependent() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);

        BinarySearchTree<Integer> copy = bst.copy();
        copy.insert(20);
        copy.remove(5);

        assertFalse(bst.search(20));
        assertTrue(bst.search(5));
    }

    // копия сохраняет BST-свойство (successor совпадает)
    @Test
    void testCopyPreservesBSTProperty() {
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 35, 45, 55, 65, 90};
        for (int v : values) bst.insert(v);

        BinarySearchTree<Integer> copy = bst.copy();
        for (int v : values) {
            assertEquals(bst.successor(v), copy.successor(v));
        }
    }

    // ================== ГРАНИЧНЫЕ ==================

    // поиск null → NullPointerException
    @Test
    void testSearchNullThrows() {
        assertThrows(NullPointerException.class, () -> bst.search(null));
    }

    // ================== ИТЕРАТОР ==================
    //
    // TODO: добавить после реализации BSTIterator:
    // - testIteratorInOrder: for-each даёт возрастание;
    // - testIteratorEmpty: hasNext=false, next() → NoSuchElementException;
    // - testFailFastOnInsert: insert во время обхода → ConcurrentModificationException;
    // - testFailFastOnRemove: remove во время обхода → ConcurrentModificationException;
    // - testDescendingIterator: элементы идут от максимума к минимуму.
}