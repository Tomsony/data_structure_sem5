import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.MyArrayList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для util.MyArrayList.
 *
 * Покрывают:
 * - конструкторы и базовые операции;
 * - добавление / удаление / поиск / очистку;
 * - итератор: базовый обход, remove() в итераторе, fail-fast;
 * - граничные случаи (пустой список, null, большой объём).
 */
class MyArrayListTest {

    private MyArrayList<String> list;
    private MyArrayList<Integer> intList;

    @BeforeEach
    void setUp() {
        list = new MyArrayList<>();
        intList = new MyArrayList<>();
    }

    // ================== КОНСТРУКТОРЫ ==================

    // пустой список по умолчанию: size=0, isEmpty=true
    @Test
    void testDefaultConstructor() {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    // заданная ёмкость: список всё равно пустой
    @Test
    void testConstructorWithCapacity() {
        MyArrayList<String> custom = new MyArrayList<>(20);
        assertEquals(0, custom.size());
        assertTrue(custom.isEmpty());
    }

    // нулевая ёмкость + add → массив расширяется, элемент доступен
    @Test
    void testConstructorWithZeroCapacity() {
        MyArrayList<String> zero = new MyArrayList<>(0);
        zero.add("A");
        assertEquals(1, zero.size());
        assertEquals("A", zero.get(0));
    }

    // отрицательная ёмкость → IllegalArgumentException
    @Test
    void testConstructorWithNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new MyArrayList<>(-1));
    }

    // ================== ADD ==================

    // добавление в конец: элементы идут в порядке вставки
    @Test
    void testAddToEnd() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    // вставка по индексу: хвост сдвигается вправо
    @Test
    void testAddByIndex() {
        list.add("A");
        list.add("C");
        list.add(1, "B");

        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    // невалидные индексы add → IndexOutOfBoundsException
    @Test
    void testAddInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "X"));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "X"));
    }

    // авторасширение: при превышении capacity массив растёт
    @Test
    void testAutoExpansion() {
        MyArrayList<String> small = new MyArrayList<>(2);
        small.add("A");
        small.add("B");
        small.add("C");
        small.add("D");

        assertEquals(4, small.size());
        assertEquals("D", small.get(3));
    }

    // ================== GET / SET ==================

    // get по невалидному индексу → IndexOutOfBoundsException
    @Test
    void testGetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    // set возвращает старое значение и устанавливает новое
    @Test
    void testSet() {
        list.add("A");
        list.add("B");

        String old = list.set(1, "C");
        assertEquals("B", old);
        assertEquals("C", list.get(1));
        assertEquals(2, list.size());
    }

    // set по невалидному индексу → IndexOutOfBoundsException
    @Test
    void testSetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, "X"));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "X"));
    }

    // ================== REMOVE ==================

    // remove по индексу возвращает элемент и сдвигает хвост влево
    @Test
    void testRemoveByIndex() {
        list.add("A");
        list.add("B");
        list.add("C");

        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    // невалидные индексы remove → IndexOutOfBoundsException
    @Test
    void testRemoveInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    // remove по значению: true если найден, false если нет
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

    // remove(null) корректно ищет и удаляет null-элемент
    @Test
    void testRemoveNull() {
        list.add("A");
        list.add(null);
        assertTrue(list.remove(null));
        assertEquals(1, list.size());
    }

    // ================== indexOf / contains / clear ==================

    // indexOf находит первое вхождение, contains проверяет наличие
    @Test
    void testIndexOfContains() {
        list.add("A");
        list.add("B");
        list.add("A");

        assertEquals(0, list.indexOf("A"));
        assertEquals(1, list.indexOf("B"));
        assertEquals(-1, list.indexOf("X"));
        assertTrue(list.contains("A"));
        assertFalse(list.contains("X"));
    }

    // clear очищает список: size=0, isEmpty=true
    @Test
    void testClear() {
        list.add("A");
        list.add("B");
        list.clear();

        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    // ================== ИТЕРАТОР ==================

    // базовый обход: hasNext/next возвращают элементы по порядку
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

    // пустой итератор: hasNext=false, next() бросает NoSuchElementException
    @Test
    void testIteratorEmpty() {
        Iterator<String> it = list.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    // for-each использует итератор и перебирает элементы по порядку
    @Test
    void testIteratorForEach() {
        list.add("X");
        list.add("Y");
        list.add("Z");

        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s);
        assertEquals("XYZ", sb.toString());
    }

    // null-элементы корректно перебираются итератором
    @Test
    void testIteratorWithNull() {
        list.add("A");
        list.add(null);
        list.add("C");

        Iterator<String> it = list.iterator();
        assertEquals("A", it.next());
        assertNull(it.next());
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
    }

    // ================== REMOVE В ИТЕРАТОРЕ ==================

    // remove() в итераторе удаляет последний возвращённый элемент
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

    // remove() до next() → IllegalStateException
    @Test
    void testIteratorRemoveBeforeNext() {
        list.add("A");
        Iterator<String> it = list.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    // повторный remove() без next() → IllegalStateException
    @Test
    void testIteratorRemoveTwice() {
        list.add("A");
        Iterator<String> it = list.iterator();
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, it::remove);
    }

    // ================== FAIL-FAST ==================

    // add во время обхода ломает итератор
    @Test
    void testFailFastOnAdd() {
        list.add("A");
        list.add("B");

        Iterator<String> it = list.iterator();
        it.next();
        list.add("C");

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    // remove во время обхода (через внешний метод) ломает итератор
    @Test
    void testFailFastOnRemove() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.iterator();
        it.next();
        list.remove(0);

        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    // clear во время обхода ломает итератор
    @Test
    void testFailFastOnClear() {
        list.add("A");
        list.add("B");

        Iterator<String> it = list.iterator();
        it.next();
        list.clear();

        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    // set() НЕ ломает итератор (не структурное изменение)
    @Test
    void testSetDoesNotBreakIterator() {
        list.add("A");
        list.add("B");

        Iterator<String> it = list.iterator();
        it.next();
        list.set(0, "X");

        assertDoesNotThrow(() -> assertEquals("B", it.next()));
    }

    // remove() через сам итератор НЕ ломает его
    @Test
    void testIteratorRemoveDoesNotBreakIterator() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.iterator();
        it.next();
        it.remove();

        assertDoesNotThrow(() -> assertEquals("B", it.next()));
    }

    // ================== РАЗНЫЕ ТИПЫ / БОЛЬШИЕ ОБЪЁМЫ ==================

    // работа с Integer-списком
    @Test
    void testIntegerList() {
        intList.add(10);
        intList.add(20);
        intList.add(1, 15);

        assertEquals(10, intList.get(0));
        assertEquals(15, intList.get(1));
        assertEquals(20, intList.get(2));
    }

    // null-элементы поддерживаются всеми операциями
    @Test
    void testNullElements() {
        list.add(null);
        list.add("Apple");
        list.add(1, null);

        assertEquals(3, list.size());
        assertNull(list.get(0));
        assertNull(list.get(1));
        assertEquals("Apple", list.get(2));
    }

    // большой список: 1000 элементов + стресс авторасширения
    @Test
    void testLargeList() {
        for (int i = 0; i < 1000; i++) list.add("E" + i);
        assertEquals(1000, list.size());
        assertEquals("E0", list.get(0));
        assertEquals("E999", list.get(999));
    }

    // toString: пустой → [], с элементами → [A, B]
    @Test
    void testToString() {
        assertEquals("[]", list.toString());
        list.add("A");
        list.add("B");
        assertEquals("[A, B]", list.toString());
    }
}