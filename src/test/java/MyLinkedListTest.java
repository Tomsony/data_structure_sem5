import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.MyLinkedList;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса util.MyLinkedList
 */
class MyLinkedListTest {

    private MyLinkedList<String> list;
    private MyLinkedList<Integer> intList;

    /**
     * Инициализация тестовых списков перед каждым тестом.
     * Обеспечивает изолированное состояние для каждого теста.
     */
    @BeforeEach
    void setUp() {
        list = new MyLinkedList<>();
        intList = new MyLinkedList<>();
    }

    // ================== ТЕСТЫ КОНСТРУКТОРОВ ==================

    /**
     * Проверка конструктора по умолчанию:
     * - список должен быть пустым
     * - size == 0, isEmpty == true
     */
    @Test
    void testDefaultConstructor() {
        assertEquals(0, list.size(), "Размер нового списка должен быть 0");
        assertTrue(list.isEmpty(), "Новый список должен быть пустым");
    }

    /**
     * Проверка конструктора, принимающего коллекцию:
     * - элементы из коллекции должны быть добавлены в порядке обхода коллекции
     * - размер списка должен совпадать с размером коллекции
     * - передача null должна вызывать NullPointerException
     */
    @Test
    void testConstructorFromCollection() {
        List<String> source = Arrays.asList("A", "B", "C");
        MyLinkedList<String> fromCollection = new MyLinkedList<>(source);
        assertEquals(3, fromCollection.size(), "Размер должен быть 3");
        assertEquals("A", fromCollection.get(0), "Первый элемент должен быть 'A'");
        assertEquals("B", fromCollection.get(1), "Второй элемент должен быть 'B'");
        assertEquals("C", fromCollection.get(2), "Третий элемент должен быть 'C'");

        assertThrows(NullPointerException.class,
                () -> new MyLinkedList<>(null),
                "Конструктор от null коллекции должен выбрасывать исключение");
    }

    /**
     * Проверка конструктора с коллекцией, содержащей null:
     * так как список не разрешает null, ожидается IllegalArgumentException.
     */
    @Test
    void testConstructorFromCollectionWithNull() {
        List<String> withNull = new ArrayList<>();
        withNull.add("A");
        withNull.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> new MyLinkedList<>(withNull),
                "Коллекция с null элементом должна приводить к исключению");
    }

    // ================== ТЕСТЫ ДОБАВЛЕНИЯ ЭЛЕМЕНТОВ ==================

    /**
     * Тест add (в конец) для одного элемента:
     * - возвращает true, размер становится 1,
     * - можно получить первый и последний элемент через getFirst/getLast.
     */
    @Test
    void testAddSingleElement() {
        assertTrue(list.add("Apple"), "Метод add должен вернуть true");
        assertEquals(1, list.size(), "Размер после добавления должен быть 1");
        assertFalse(list.isEmpty(), "Список не должен быть пустым");
        assertEquals("Apple", list.getFirst(), "Первый элемент должен быть 'Apple'");
        assertEquals("Apple", list.getLast(), "Последний элемент должен быть 'Apple'");
    }

    /**
     * Тест добавления нескольких элементов в конец списка:
     * - порядок должен сохраняться
     * - get по индексам возвращает соответствующие элементы.
     */
    @Test
    void testAddMultipleElements() {
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");

        assertEquals(3, list.size(), "Размер списка должен быть 3");
        assertEquals("Apple", list.get(0), "Индекс 0 -> Apple");
        assertEquals("Banana", list.get(1), "Индекс 1 -> Banana");
        assertEquals("Cherry", list.get(2), "Индекс 2 -> Cherry");
    }

    /**
     * Тест addFirst:
     * - элементы добавляются в начало, сдвигая уже существующие вправо
     * - размер увеличивается
     */
    @Test
    void testAddFirst() {
        list.add("B");
        list.addFirst("A");

        assertEquals(2, list.size(), "Размер после addFirst должен быть 2");
        assertEquals("A", list.getFirst(), "Первый элемент после addFirst должен быть 'A'");
        assertEquals("B", list.getLast(), "Последний элемент остаётся 'B'");

        // добавление в пустой список через addFirst
        MyLinkedList<String> empty = new MyLinkedList<>();
        empty.addFirst("First");
        assertEquals(1, empty.size());
        assertEquals("First", empty.getFirst());
    }

    /**
     * Тест addLast (аналогично add, но проверяем отдельно):
     * - элемент идёт в конец, хвост обновляется.
     */
    @Test
    void testAddLast() {
        list.addLast("A");
        list.addLast("B");
        assertEquals(2, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("B", list.getLast());

        // добавление в пустой список
        MyLinkedList<String> empty = new MyLinkedList<>();
        empty.addLast("X");
        assertEquals("X", empty.getLast());
    }

    /**
     * Тест добавления по индексу:
     * - вставка в середину, начало, конец
     * - проверка сдвига элементов
     */
    @Test
    void testAddAtIndex() {
        list.add("A");   // index 0
        list.add("C");   // index 1
        list.add(1, "B"); // вставка между A и C

        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));

        // вставка в начало по индексу 0
        list.add(0, "Start");
        assertEquals("Start", list.getFirst());
        assertEquals("A", list.get(1));

        // вставка в конец по индексу size
        list.add(list.size(), "End");
        assertEquals("End", list.getLast());
    }

    /**
     * Тест невалидных индексов при добавлении:
     * - отрицательный индекс, индекс больше size вызывает IndexOutOfBoundsException
     */
    @Test
    void testAddAtIndexInvalid() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(-1, "A"),
                "Отрицательный индекс должен вызывать исключение");
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(1, "A"), // size=0, допустим только 0
                "Индекс больше size должен вызывать исключение");
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(2, "B"), // size=1, допустимы 0 и 1
                "Индекс > size после добавления элемента должен вызывать исключение");
    }

    /**
     * Тест addAll:
     * - добавление всех элементов из коллекции в конец
     * - возвращает true, если список изменился
     * - пустая коллекция не изменяет список
     */
    @Test
    void testAddAll() {
        List<String> items = Arrays.asList("A", "B", "C");
        boolean changed = list.addAll(items);
        assertTrue(changed, "addAll должен вернуть true при добавлении элементов");
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));

        // добавление пустой коллекции
        boolean changedEmpty = list.addAll(Collections.emptyList());
        assertFalse(changedEmpty, "Пустая коллекция не должна изменять список");
        assertEquals(3, list.size());
    }

    // ================== ТЕСТЫ ПОЛУЧЕНИЯ ЭЛЕМЕНТОВ ==================

    /**
     * Тест getFirst / getLast на пустом списке:
     * должны выбрасывать NoSuchElementException
     */
    @Test
    void testGetFirstLastEmpty() {
        assertThrows(NoSuchElementException.class, () -> list.getFirst(),
                "getFirst на пустом списке должно выбросить исключение");
        assertThrows(NoSuchElementException.class, () -> list.getLast(),
                "getLast на пустом списке должно выбросить исключение");
    }

    /**
     * Тест get по индексу: корректное получение и исключения при невалидных индексах.
     */
    @Test
    void testGetByIndex() {
        list.add("A");
        list.add("B");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1),
                "Отрицательный индекс");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2),
                "Индекс равен size");
    }

    /**
     * Тест set: замена элемента по индексу.
     * - возвращает старое значение
     * - размер не меняется
     */
    @Test
    void testSet() {
        list.add("A");
        list.add("B");
        String old = list.set(1, "X");
        assertEquals("B", old, "Старое значение должно быть 'B'");
        assertEquals("X", list.get(1), "Новое значение должно быть 'X'");
        assertEquals(2, list.size());
    }

    /**
     * Тест set с невалидными индексами.
     */
    @Test
    void testSetInvalidIndex() {
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "X"));
    }

    /**
     * Тест set на null: так как null запрещён, ожидается IllegalArgumentException.
     */
    @Test
    void testSetNull() {
        list.add("A");
        assertThrows(IllegalArgumentException.class, () -> list.set(0, null),
                "Установка null должна вызывать исключение");
    }

    // ================== ТЕСТЫ УДАЛЕНИЯ ==================

    /**
     * Тест removeFirst: удаление из начала, на пустом списке исключение.
     */
    @Test
    void testRemoveFirst() {
        list.add("A");
        list.add("B");
        String removed = list.removeFirst();
        assertEquals("A", removed);
        assertEquals(1, list.size());
        assertEquals("B", list.getFirst());

        removed = list.removeFirst();
        assertEquals("B", removed);
        assertTrue(list.isEmpty());

        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    /**
     * Тест removeLast: аналогично.
     */
    @Test
    void testRemoveLast() {
        list.add("A");
        list.add("B");
        String removed = list.removeLast();
        assertEquals("B", removed);
        assertEquals("A", list.getLast());

        removed = list.removeLast();
        assertEquals("A", removed);
        assertTrue(list.isEmpty());
    }

    /**
     * Тест remove по индексу: удаление из середины, начала, конца.
     */
    @Test
    void testRemoveByIndex() {
        list.addAll(Arrays.asList("A", "B", "C"));
        // удаление середины
        String removedMid = list.remove(1);
        assertEquals("B", removedMid);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));

        // удаление начала через индекс 0
        String removedFirst = list.remove(0);
        assertEquals("A", removedFirst);
        assertEquals("C", list.getFirst());

        // удаление последнего
        String removedLast = list.remove(list.size() - 1);
        assertEquals("C", removedLast);
        assertTrue(list.isEmpty());
    }

    /**
     * Тест remove по невалидному индексу.
     */
    @Test
    void testRemoveInvalidIndex() {
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1)); // size=1
    }

    /**
     * Тест clear: список становится пустым, все элементы удалены.
     */
    @Test
    void testClear() {
        list.addAll(Arrays.asList("A", "B", "C"));
        assertFalse(list.isEmpty());
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        // после очистки можно снова добавлять
        list.add("new");
        assertEquals(1, list.size());
    }

    // ================== ТЕСТЫ ПОИСКА ==================

    /**
     * Тест indexOf / lastIndexOf:
     * - поиск существующих и отсутствующих элементов
     * - обработка null (null не разрешён, но сам метод ищет)
     */
    @Test
    void testIndexOf() {
        list.addAll(Arrays.asList("A", "B", "A", "C"));
        assertEquals(0, list.indexOf("A"), "Первое вхождение A должно быть 0");
        assertEquals(2, list.lastIndexOf("A"), "Последнее вхождение A должно быть 2");
        assertEquals(-1, list.indexOf("X"), "Отсутствующий элемент -> -1");
        assertEquals(-1, list.lastIndexOf("X"), "Отсутствующий -> -1");

        // проверка, что indexOf(null) не падает, но возвращает -1 (так как null в списке нет)
        assertEquals(-1, list.indexOf(null), "null не хранится, должно быть -1");
        assertEquals(-1, list.lastIndexOf(null));
    }
LinkedList
    /**
     * Тест contains.
     */
    @Test
    void testContains() {
        list.addAll(Arrays.asList("Apple", "Banana"));
        assertTrue(list.contains("Apple"));
        assertTrue(list.contains("Banana"));
        assertFalse(list.contains("Cherry"));
        // contains(null) должно вернуть false, так как null не может быть в списке
        assertFalse(list.contains(null));
    }

    // ================== ТЕСТЫ ИТЕРАТОРА (ПРЯМОЙ) ==================

    /**
     * Базовый тест прямого итератора: обход всех элементов в правильном порядке.
     */
    @Test
    void testIteratorBasic() {
        list.addAll(Arrays.asList("A", "B", "C"));
        Iterator<String> it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertTrue(it.hasNext());
        assertEquals("C", it.next());
        assertFalse(it.hasNext(), "После перебора всех элементов hasNext = false");

        assertThrows(NoSuchElementException.class, () -> it.next(),
                "Повторный вызов next после окончания должен вызывать исключение");
    }

    /**
     * Тест итератора на пустом списке.
     */
    @Test
    void testIteratorEmptyList() {
        Iterator<String> it = list.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    /**
     * Тест for-each цикла.
     */
    @Test
    void testIteratorForEach() {
        list.addAll(Arrays.asList("X", "Y", "Z"));
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        assertEquals("XYZ", sb.toString());
    }

    /**
     * Тест итератора с большим количеством элементов.
     */
    @Test
    void testIteratorLargeList() {
        for (int i = 0; i < 1000; i++) {
            list.add("Item" + i);
        }
        int count = 0;
        for (String s : list) {
            assertEquals("Item" + count, s);
            count++;
        }
        assertEquals(1000, count);
    }

    /**
     * Тест fail-fast поведения итератора:
     * изменение списка после создания итератора (add) должно вызвать
     * ConcurrentModificationException при следующем вызове next.
     */
    @Test
    void testIteratorFailFastOnAdd() {
        list.addAll(Arrays.asList("A", "B"));
        Iterator<String> it = list.iterator();
        assertEquals("A", it.next());
        list.add("C"); // изменение структуры списка
        assertThrows(ConcurrentModificationException.class, it::next,
                "Добавление элемента после создания итератора должно вызвать ConcurrentModificationException");
    }

    /**
     * Тест fail-fast при удалении элемента напрямую (не через итератор).
     */
    @Test
    void testIteratorFailFastOnRemove() {
        list.addAll(Arrays.asList("A", "B", "C"));
        Iterator<String> it = list.iterator();
        assertEquals("A", it.next());
        list.removeFirst(); // прямое изменение
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    /**
     * Тест fail-fast при clear.
     */
    @Test
    void testIteratorFailFastOnClear() {
        list.addAll(Arrays.asList("A", "B"));
        Iterator<String> it = list.iterator();
        assertEquals("A", it.next());
        list.clear();
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    /**
     * Тест метода remove() итератора:
     * - удаляется последний возвращённый next() элемент
     * - повторный вызов remove без next вызывает IllegalStateException
     * - после удаления размер уменьшается, элемент отсутствует в списке.
     */
    @Test
    void testIteratorRemove() {
        list.addAll(Arrays.asList("A", "B", "C"));
        Iterator<String> it = list.iterator();
        assertEquals("A", it.next());
        it.remove(); // удаляем A
        assertEquals(2, list.size());
        assertEquals("B", list.getFirst());

        // проверяем, что повторный remove без next вызывает исключение
        assertThrows(IllegalStateException.class, it::remove,
                "Повторный вызов remove должен бросать IllegalStateException");

        // удаление элемента из середины через итератор
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        it.remove(); // удаляем C
        assertEquals(1, list.size());
        assertEquals("B", list.getFirst());
    }

    /**
     * Тест удаления последнего элемента через итератор.
     */
    @Test
    void testIteratorRemoveLast() {
        list.add("A");
        Iterator<String> it = list.iterator();
        it.next();
        it.remove();
        assertTrue(list.isEmpty());
        assertThrows(IllegalStateException.class, it::remove);
    }

    // ================== ТЕСТЫ ОБРАТНОГО ИТЕРАТОРА ==================

    /**
     * Базовый тест descendingIterator: обход от конца к началу.
     */
    @Test
    void testDescendingIteratorBasic() {
        list.addAll(Arrays.asList("A", "B", "C"));
        Iterator<String> it = list.descendingIterator();
        assertTrue(it.hasNext());
        assertEquals("C", it.next());
        assertEquals("B", it.next());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());
    }

    /**
     * Обратный итератор на пустом списке.
     */
    @Test
    void testDescendingIteratorEmpty() {
        Iterator<String> it = list.descendingIterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    /**
     * Тест удаления через обратный итератор.
     */
    @Test
    void testDescendingIteratorRemove() {
        list.addAll(Arrays.asList("A", "B", "C"));
        Iterator<String> it = list.descendingIterator();
        assertEquals("C", it.next());
        it.remove(); // удаляем C
        assertEquals(2, list.size());
        assertEquals("B", list.getLast());

        it.next(); // B
        it.next(); // A
        it.remove(); // удаляем A
        assertEquals("B", list.getFirst());
        assertEquals("B", list.getLast());
    }

    /**
     * Проверка fail-fast для обратного итератора.
     */
    @Test
    void testDescendingIteratorFailFast() {
        list.addAll(Arrays.asList("A", "B"));
        Iterator<String> it = list.descendingIterator();
        assertEquals("B", it.next());
        list.add("C"); // прямое изменение
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    // ================== ТЕСТЫ С ДРУГИМИ ТИПАМИ ДАННЫХ ==================

    /**
     * Тест работы с Integer (проверка генериков).
     */
    @Test
    void testIntegerList() {
        intList.add(10);
        intList.add(20);
        intList.add(1, 15);
        assertEquals(3, intList.size());
        assertEquals(Integer.valueOf(10), intList.get(0));
        assertEquals(Integer.valueOf(15), intList.get(1));
        assertEquals(Integer.valueOf(20), intList.get(2));

        // поиск
        assertEquals(1, intList.indexOf(15));
        assertTrue(intList.contains(20));
        assertFalse(intList.contains(99));
    }

    /**
     * Тест запрета null-элементов: все методы add
     * set должны бросать исключение.
     */
    @Test
    void testNullElementsForbidden() {
        assertThrows(IllegalArgumentException.class, () -> list.add(null));
        assertThrows(IllegalArgumentException.class, () -> list.addFirst(null));
        assertThrows(IllegalArgumentException.class, () -> list.addLast(null));
        assertThrows(IllegalArgumentException.class, () -> list.add(0, null));
        list.add("A");
        assertThrows(IllegalArgumentException.class, () -> list.set(0, null));
    }

    // ================== ТЕСТЫ ПРЕОБРАЗОВАНИЯ В МАССИВ И TOSTRING ==================

    /**
     * Тест toArray: возвращаемый массив содержит все элементы в правильном порядке.
     */
    @Test
    void testToArray() {
        list.addAll(Arrays.asList("A", "B", "C"));
        Object[] arr = list.toArray();
        assertEquals(3, arr.length);
        assertEquals("A", arr[0]);
        assertEquals("B", arr[1]);
        assertEquals("C", arr[2]);

        // пустой список
        MyLinkedList<String> empty = new MyLinkedList<>();
        Object[] emptyArr = empty.toArray();
        assertEquals(0, emptyArr.length);
    }

    /**
     * Тест toString: формат должен быть [element1, element2, ...] или [] для пустого.
     */
    @Test
    void testToString() {
        assertEquals("[]", list.toString(), "Пустой список -> []");
        list.add("Hello");
        list.add("World");
        assertEquals("[Hello, World]", list.toString());
    }

    // ================== ТЕСТЫ ГРАНИЧНЫХ СЛУЧАЕВ ==================

    /**
     * Тест последовательности операций, имитирующий реальное использование.
     */
    @Test
    void testComplexScenario() {
        list.addLast("A");
        list.addFirst("B");
        list.add(1, "C"); // [B, C, A]
        assertEquals(3, list.size());
        assertEquals("B", list.get(0));
        assertEquals("C", list.get(1));
        assertEquals("A", list.get(2));

        list.set(1, "X"); // [B, X, A]
        assertEquals("X", list.get(1));

        list.removeFirst(); // [X, A]
        list.removeLast();  // [X]
        assertEquals("X", list.getFirst());

        list.clear();
        assertTrue(list.isEmpty());
    }

    /**
     * Тест с большим количеством элементов для проверки производительности
     * и корректности при многократном добавлении/удалении.
     */
    @Test
    void testStressAddRemove() {
        int count = 2000;
        for (int i = 0; i < count; i++) {
            list.add("Elem" + i);
        }
        assertEquals(count, list.size());

        for (int i = 0; i < count; i++) {
            assertEquals("Elem" + i, list.get(i));
        }

        // удаляем все через removeFirst
        for (int i = 0; i < count; i++) {
            list.removeFirst();
        }
        assertTrue(list.isEmpty());
    }
}