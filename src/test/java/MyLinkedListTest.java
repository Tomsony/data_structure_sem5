import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.MyLinkedList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    private MyLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList<>();
    }

    // ================== ПУСТОЙ СПИСОК ==================

    // size=0, isEmpty=true
    @Test
    void testEmptyList() {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        assertEquals("[]", list.toString());
    }

    // ================== ДОБАВЛЕНИЕ ==================

    // addLast / add / addFirst
    @Test
    void testAdd() {
        list.add("B");
        list.addFirst("A");
        list.addLast("C");

        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    // вставка по индексу в начало/середину/конец
    @Test
    void testAddByIndex() {
        list.add("A");
        list.add("C");
        list.add(1, "B");
        list.add(0, "X");
        list.add(list.size(), "Z");

        assertEquals(5, list.size());
        assertEquals("X", list.get(0));
        assertEquals("A", list.get(1));
        assertEquals("B", list.get(2));
        assertEquals("C", list.get(3));
        assertEquals("Z", list.get(4));
    }

    // невалидные индексы add
    @Test
    void testAddInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "X"));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "X"));
    }

    // null запрещён
    @Test
    void testAddNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> list.add(null));
    }

    // ================== GET / SET ==================

    @Test
    void testGetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    // set возвращает старое значение
    @Test
    void testSet() {
        list.add("A");
        list.add("B");
        String old = list.set(1, "C");
        assertEquals("B", old);
        assertEquals("C", list.get(1));
    }

    // ================== УДАЛЕНИЕ ==================

    // removeFirst / removeLast
    @Test
    void testRemoveFirstLast() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals("A", list.removeFirst());
        assertEquals("C", list.removeLast());
        assertEquals(1, list.size());
        assertEquals("B", list.get(0));
    }

    // remove(int)
    @Test
    void testRemoveByIndex() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals("B", list.remove(1));
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    // remove(Object)
    @Test
    void testRemoveByValue() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertTrue(list.remove("B"));
        assertEquals(2, list.size());
        assertFalse(list.remove("X"));
        assertEquals(2, list.size());
    }

    // невалидные индексы remove
    @Test
    void testRemoveInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    // пустой список — исключения
    @Test
    void testRemoveFromEmpty() {
        assertThrows(NoSuchElementException.class, list::removeFirst);
        assertThrows(NoSuchElementException.class, list::removeLast);
    }

    // clear
    @Test
    void testClear() {
        list.add("A");
        list.add("B");
        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    // ================== ПОИСК ==================

    @Test
    void testIndexOfContains() {
        list.add("A");
        list.add("B");
        list.add("A");

        assertEquals(0, list.indexOf("A"));
        assertEquals(1, list.indexOf("B"));
        assertEquals(2, list.lastIndexOf("A"));
        assertEquals(-1, list.indexOf("X"));
        assertTrue(list.contains("A"));
        assertFalse(list.contains("X"));
    }

    // ================== ПРЯМОЙ ИТЕРАТОР ==================

    // базовый обход
    @Test
    void testIteratorBasic() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
    }

    // пустой итератор
    @Test
    void testIteratorEmpty() {
        Iterator<String> it = list.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    // for-each
    @Test
    void testIteratorForEach() {
        list.add("X");
        list.add("Y");
        list.add("Z");

        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s);
        assertEquals("XYZ", sb.toString());
    }

    // remove() во время обхода
    @Test
    void testIteratorRemove() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String v = it.next();
            if ("B".equals(v) || "C".equals(v)) it.remove();
        }

        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("D", list.get(1));
    }

    // remove до next → IllegalStateException
    @Test
    void testIteratorRemoveBeforeNext() {
        list.add("A");
        assertThrows(IllegalStateException.class, list.iterator()::remove);
    }

    // повторный remove → IllegalStateException
    @Test
    void testIteratorRemoveTwice() {
        list.add("A");
        Iterator<String> it = list.iterator();
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, it::remove);
    }

    // ================== ОБРАТНЫЙ ИТЕРАТОР ==================

    @Test
    void testDescendingIterator() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.descendingIterator();
        assertEquals("C", it.next());
        assertEquals("B", it.next());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());
    }

    // remove в обратном итераторе
    @Test
    void testDescendingIteratorRemove() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.descendingIterator();
        it.next();     // C
        it.remove();   // удаляем C

        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    // ================== FAIL-FAST ==================

    @Test
    void testFailFastOnAdd() {
        list.add("A");
        list.add("B");
        Iterator<String> it = list.iterator();
        it.next();
        list.add("C");
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void testFailFastOnRemove() {
        list.add("A");
        list.add("B");
        Iterator<String> it = list.iterator();
        it.next();
        list.remove(0);
        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    // set НЕ ломает итератор (не структурное изменение)
    @Test
    void testSetDoesNotBreakIterator() {
        list.add("A");
        list.add("B");
        Iterator<String> it = list.iterator();
        it.next();
        list.set(0, "X");
        assertDoesNotThrow(() -> assertEquals("B", it.next()));
    }

    // ================== ПРОЧЕЕ ==================

    // addAll из коллекции
    @Test
    void testAddAll() {
        list.addAll(java.util.List.of("A", "B", "C"));
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(2));
    }

    // toArray
    @Test
    void testToArray() {
        list.add("A");
        list.add("B");
        Object[] arr = list.toArray();
        assertArrayEquals(new Object[]{"A", "B"}, arr);
    }

    // toString
    @Test
    void testToString() {
        assertEquals("[]", list.toString());
        list.add("A");
        list.add("B");
        assertEquals("[A, B]", list.toString());
    }

    // большой список
    @Test
    void testLargeList() {
        for (int i = 0; i < 1000; i++) list.add("E" + i);
        assertEquals(1000, list.size());
        assertEquals("E0", list.get(0));
        assertEquals("E999", list.get(999));
    }
}