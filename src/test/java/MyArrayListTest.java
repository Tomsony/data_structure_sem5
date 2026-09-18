import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.MyArrayList;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса util.MyArrayList
 */
class MyArrayListTest {

    private MyArrayList<String> list;
    private MyArrayList<Integer> intList;

    /**
     * Инициализация тестовых списков перед каждым тестом.
     * Обеспечивает изоляцию тестов друг от друга.
     */
    @BeforeEach
    void setUp() {
        list = new MyArrayList<>();
        intList = new MyArrayList<>();
    }

    // ================== ТЕСТЫ КОНСТРУКТОРОВ ==================

    /**
     * Проверка конструктора по умолчанию:
     * - список должен быть пустым
     * - размер должен быть 0
     */
    @Test
    void testDefaultConstructor() {
        assertEquals(0, list.size(), "Размер нового списка должен быть 0");
        assertTrue(list.isEmpty(), "Новый список должен быть пустым");
    }

    /**
     * Проверка конструктора с указанием ёмкости:
     * - список должен быть пустым независимо от начальной ёмкости
     */
    @Test
    void testConstructorWithCapacity() {
        MyArrayList<String> customList = new MyArrayList<>(20);
        assertEquals(0, customList.size(), "Размер списка с кастомной ёмкостью должен быть 0");
        assertTrue(customList.isEmpty(), "Список с кастомной ёмкостью должен быть пустым");
    }

    /**
     * Проверка конструктора с нулевой начальной ёмкостью:
     * - граничный случай, должно работать без ошибок
     */
    @Test
    void testConstructorWithZeroCapacity() {
        MyArrayList<String> zeroList = new MyArrayList<>(0);
        assertEquals(0, zeroList.size(), "Размер списка с нулевой ёмкостью должен быть 0");
        assertTrue(zeroList.isEmpty(), "Список с нулевой ёмкостью должен быть пустым");
    }

    /**
     * Проверка конструктора с отрицательной ёмкостью:
     * - должно выбрасываться IllegalArgumentException
     */
    @Test
    void testConstructorWithNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MyArrayList<>(-1);
        }, "Отрицательная начальная ёмкость должна вызывать исключение");
    }

    // ================== ТЕСТЫ ДОБАВЛЕНИЯ ЭЛЕМЕНТОВ ==================

    /**
     * Проверка добавления одного элемента в конец списка:
     * - размер должен увеличиться на 1
     * - список не должен быть пустым
     * - добавленный элемент должен быть доступен по индексу 0
     */
    @Test
    void testAddSingleElement() {
        assertTrue(list.add("Apple"), "Метод add должен возвращать true");
        assertEquals(1, list.size(), "Размер списка должен быть 1 после добавления элемента");
        assertFalse(list.isEmpty(), "Список не должен быть пустым после добавления");
        assertEquals("Apple", list.get(0), "Первый элемент должен быть 'Apple'");
    }

    /**
     * Проверка добавления нескольких элементов в конец списка:
     * - элементы должны сохраняться в порядке добавления
     * - размер должен соответствовать количеству добавленных элементов
     */
    @Test
    void testAddMultipleElements() {
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");

        assertEquals(3, list.size(), "Размер списка должен быть 3");
        assertEquals("Apple", list.get(0), "Первый элемент должен быть 'Apple'");
        assertEquals("Banana", list.get(1), "Второй элемент должен быть 'Banana'");
        assertEquals("Cherry", list.get(2), "Третий элемент должен быть 'Cherry'");
    }

    /**
     * Проверка вставки элемента в начало списка:
     * - существующие элементы должны сдвинуться вправо
     * - размер должен увеличиться на 1
     */
    @Test
    void testAddWithIndexAtBeginning() {
        list.add("Banana");
        list.add(0, "Apple"); // Вставка в начало

        assertEquals(2, list.size(), "Размер списка должен быть 2");
        assertEquals("Apple", list.get(0), "Первый элемент должен быть 'Apple'");
        assertEquals("Banana", list.get(1), "Второй элемент должен быть 'Banana'");
    }

    /**
     * Проверка вставки элемента в середину списка:
     * - элементы справа от индекса должны сдвинуться
     * - порядок остальных элементов должен сохраниться
     */
    @Test
    void testAddWithIndexInMiddle() {
        list.add("Apple");
        list.add("Cherry");
        list.add(1, "Banana"); // Вставка в середину

        assertEquals(3, list.size(), "Размер списка должен быть 3");
        assertEquals("Apple", list.get(0), "Первый элемент должен быть 'Apple'");
        assertEquals("Banana", list.get(1), "Второй элемент должен быть 'Banana'");
        assertEquals("Cherry", list.get(2), "Третий элемент должен быть 'Cherry'");
    }

    /**
     * Проверка вставки элемента в конец списка по индексу:
     * - поведение должно быть аналогично обычному add()
     */
    @Test
    void testAddWithIndexAtEnd() {
        list.add("Apple");
        list.add("Banana");
        list.add(2, "Cherry"); // Вставка в конец

        assertEquals(3, list.size(), "Размер списка должен быть 3");
        assertEquals("Apple", list.get(0), "Первый элемент должен быть 'Apple'");
        assertEquals("Banana", list.get(1), "Второй элемент должен быть 'Banana'");
        assertEquals("Cherry", list.get(2), "Третий элемент должен быть 'Cherry'");
    }

    /**
     * Проверка вставки с невалидным индексом:
     * - индекс вне диапазона [0, size] должен вызывать IndexOutOfBoundsException
     * - проверяем пустой список, слишком большой индекс, отрицательный индекс
     */
    @Test
    void testAddWithInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(1, "Invalid"); // Пустой список, индекс 1 невалиден
        }, "Вставка в пустой список по индексу 1 должна вызвать исключение");

        list.add("Apple");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(5, "Invalid"); // Индекс больше size
        }, "Вставка по индексу больше size должна вызвать исключение");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(-1, "Invalid"); // Отрицательный индекс
        }, "Вставка по отрицательному индексу должна вызвать исключение");
    }

    // ================== ТЕСТЫ АВТОМАТИЧЕСКОГО РАСШИРЕНИЯ ==================

    /**
     * Проверка автоматического расширения массива при добавлении в конец:
     * - при превышении начальной ёмкости массив должен расширяться
     * - все элементы должны сохраняться
     */
    @Test
    void testAutoExpansion() {
        // Создаем маленький массив для теста расширения
        MyArrayList<String> smallList = new MyArrayList<>(3);

        smallList.add("A");
        smallList.add("B");
        smallList.add("C");
        assertEquals(3, smallList.size(), "Размер должен быть 3 после заполнения начальной ёмкости");

        // Добавляем 4-й элемент - должно произойти расширение
        smallList.add("D");
        assertEquals(4, smallList.size(), "Размер должен быть 4 после расширения");
        assertEquals("D", smallList.get(3), "Четвёртый элемент должен быть 'D'");
    }

    /**
     * Проверка расширения массива при вставке по индексу:
     * - расширение должно работать и при вставке в середину
     * - сдвиг элементов должен корректно работать с новым массивом
     */
    @Test
    void testAutoExpansionWithIndex() {
        MyArrayList<String> smallList = new MyArrayList<>(2);

        smallList.add("A");
        smallList.add("C");
        smallList.add(1, "B"); // Вставка с расширением

        assertEquals(3, smallList.size(), "Размер должен быть 3 после вставки с расширением");
        assertEquals("A", smallList.get(0), "Первый элемент должен быть 'A'");
        assertEquals("B", smallList.get(1), "Второй элемент должен быть 'B'");
        assertEquals("C", smallList.get(2), "Третий элемент должен быть 'C'");
    }

    // ================== ТЕСТЫ GET И SET ==================

    /**
     * Проверка получения элементов по валидному индексу:
     * - должны возвращаться корректные элементы
     */
    @Test
    void testGetValidIndex() {
        list.add("Apple");
        list.add("Banana");

        assertEquals("Apple", list.get(0), "Элемент по индексу 0 должен быть 'Apple'");
        assertEquals("Banana", list.get(1), "Элемент по индексу 1 должен быть 'Banana'");
    }

    /**
     * Проверка получения элемента по невалидному индексу:
     * - пустой список: любой индекс невалиден
     * - индекс равен size: выход за границы
     * - отрицательный индекс: невалидный
     */
    @Test
    void testGetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(0); // Пустой список
        }, "Получение элемента из пустого списка должно вызвать исключение");

        list.add("Apple");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(1); // Индекс = size
        }, "Получение элемента по индексу равному size должно вызвать исключение");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(-1); // Отрицательный индекс
        }, "Получение элемента по отрицательному индексу должно вызвать исключение");
    }

    /**
     * Проверка замены элемента по индексу:
     * - должно возвращаться старое значение
     * - новое значение должно быть установлено
     * - размер списка не должен измениться
     */
    @Test
    void testSetElement() {
        list.add("Apple");
        list.add("Banana");

        String oldValue = list.set(1, "Orange");
        assertEquals("Banana", oldValue, "Старое значение должно быть 'Banana'");
        assertEquals("Orange", list.get(1), "Новое значение должно быть 'Orange'");
        assertEquals(2, list.size(), "Размер списка не должен измениться после замены");
    }

    /**
     * Проверка замены первого элемента списка:
     * - частный случай замены элемента
     */
    @Test
    void testSetFirstElement() {
        list.add("Apple");
        list.add("Banana");

        String oldValue = list.set(0, "Apricot");
        assertEquals("Apple", oldValue, "Старое значение должно быть 'Apple'");
        assertEquals("Apricot", list.get(0), "Новое значение должно быть 'Apricot'");
    }

    /**
     * Проверка замены по невалидному индексу:
     * - пустой список
     * - индекс за пределами списка
     */
    @Test
    void testSetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.set(0, "Invalid");
        }, "Замена элемента в пустом списке должна вызвать исключение");

        list.add("Apple");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.set(1, "Invalid");
        }, "Замена элемента по индексу равному size должна вызвать исключение");
    }

    // ================== ТЕСТЫ ИТЕРАТОРА ==================

    /**
     * Проверка базовой работы итератора:
     * - hasNext() должен возвращать true пока есть элементы
     * - next() должен возвращать элементы в правильном порядке
     * - после перебора всех элементов hasNext() должен вернуть false
     */
    @Test
    void testIteratorBasic() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> iterator = list.iterator();

        assertTrue(iterator.hasNext(), "Итератор должен иметь следующий элемент");
        assertEquals("A", iterator.next(), "Первый элемент должен быть 'A'");

        assertTrue(iterator.hasNext(), "Итератор должен иметь следующий элемент");
        assertEquals("B", iterator.next(), "Второй элемент должен быть 'B'");

        assertTrue(iterator.hasNext(), "Итератор должен иметь следующий элемент");
        assertEquals("C", iterator.next(), "Третий элемент должен быть 'C'");

        assertFalse(iterator.hasNext(), "Итератор не должен иметь элементы после перебора всех");
    }

    /**
     * Проверка итератора на пустом списке:
     * - hasNext() должен сразу вернуть false
     * - вызов next() должен выбросить NoSuchElementException
     */
    @Test
    void testIteratorEmptyList() {
        Iterator<String> iterator = list.iterator();

        assertFalse(iterator.hasNext(), "Итератор пустого списка не должен иметь элементы");

        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        }, "Вызов next() на пустом итераторе должен вызвать исключение");
    }

    /**
     * Проверка итератора со списком из одного элемента:
     * - hasNext() должен вернуть true один раз
     * - next() должен вернуть единственный элемент
     * - последующий вызов next() должен выбросить исключение
     */
    @Test
    void testIteratorSingleElement() {
        list.add("Single");

        Iterator<String> iterator = list.iterator();

        assertTrue(iterator.hasNext(), "Итератор должен иметь следующий элемент");
        assertEquals("Single", iterator.next(), "Элемент должен быть 'Single'");

        assertFalse(iterator.hasNext(), "После извлечения единственного элемента hasNext() должен вернуть false");

        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        }, "Повторный вызов next() должен вызвать исключение");
    }

    /**
     * Проверка использования итератора в цикле for-each:
     * - for-each должен корректно перебирать все элементы
     * - порядок элементов должен сохраняться
     */
    @Test
    void testIteratorForEach() {
        list.add("X");
        list.add("Y");
        list.add("Z");

        StringBuilder result = new StringBuilder();
        for (String item : list) {
            result.append(item);
        }

        assertEquals("XYZ", result.toString(), "For-each должен перебрать элементы в правильном порядке");
    }

    /**
     * Проверка итератора после изменения списка:
     * - после добавления новых элементов старый итератор может стать невалидным
     * - новый итератор должен видеть все добавленные элементы
     */
    @Test
    void testIteratorAfterModification() {
        list.add("A");
        list.add("B");

        // Получаем итератор, но не используем
        Iterator<String> iterator1 = list.iterator();
        assertEquals("A", iterator1.next(), "Первый элемент должен быть 'A'");

        // Изменяем список
        list.add("C");
        list.add("D");

        // Создаем новый итератор
        Iterator<String> iterator2 = list.iterator();

        StringBuilder result = new StringBuilder();
        while (iterator2.hasNext()) {
            result.append(iterator2.next());
        }

        assertEquals("ABCD", result.toString(), "Новый итератор должен видеть все 4 элемента");
    }

    /**
     * Проверка итератора с null-элементами:
     * - итератор должен корректно обрабатывать null значения
     * - hasNext() и next() должны работать как обычно
     */
    @Test
    void testIteratorWithNullElements() {
        list.add("A");
        list.add(null);
        list.add("C");

        Iterator<String> iterator = list.iterator();

        assertTrue(iterator.hasNext(), "Должен быть первый элемент");
        assertEquals("A", iterator.next(), "Первый элемент должен быть 'A'");

        assertTrue(iterator.hasNext(), "Должен быть второй элемент (null)");
        assertNull(iterator.next(), "Второй элемент должен быть null");

        assertTrue(iterator.hasNext(), "Должен быть третий элемент");
        assertEquals("C", iterator.next(), "Третий элемент должен быть 'C'");

        assertFalse(iterator.hasNext(), "Элементов больше не должно быть");
    }

    /**
     * Проверка итератора с целочисленным списком:
     * - проверка работы с другим типом данных
     * - for-each с вычислением суммы
     */
    @Test
    void testIteratorIntegerList() {
        intList.add(10);
        intList.add(20);
        intList.add(30);

        int sum = 0;
        for (Integer num : intList) {
            sum += num;
        }

        assertEquals(60, sum, "Сумма элементов должна быть 60");
    }

    /**
     * Проверка множественного вызова hasNext():
     * - hasNext() не должен изменять состояние итератора
     * - множественные вызовы hasNext() без next() должны возвращать одинаковый результат
     */
    @Test
    void testIteratorMultipleHasNext() {
        list.add("A");
        list.add("B");

        Iterator<String> iterator = list.iterator();

        // Множественные вызовы hasNext() без next()
        assertTrue(iterator.hasNext(), "Первый вызов hasNext()");
        assertTrue(iterator.hasNext(), "Второй вызов hasNext()");
        assertTrue(iterator.hasNext(), "Третий вызов hasNext()");

        assertEquals("A", iterator.next(), "Первый элемент должен быть 'A'");

        assertTrue(iterator.hasNext(), "hasNext() должен вернуть true для второго элемента");
        assertTrue(iterator.hasNext(), "Повторный вызов hasNext()");

        assertEquals("B", iterator.next(), "Второй элемент должен быть 'B'");

        assertFalse(iterator.hasNext(), "hasNext() должен вернуть false после перебора");
        assertFalse(iterator.hasNext(), "Повторный вызов hasNext() должен вернуть false");
    }

    /**
     * Проверка итератора при большом количестве элементов:
     * - итератор должен корректно работать с большими списками
     * - проверка количества перебранных элементов
     */
    @Test
    void testIteratorLargeList() {
        // Заполняем список 1000 элементов
        for (int i = 0; i < 1000; i++) {
            list.add("Item" + i);
        }

        int count = 0;
        for (String item : list) {
            assertEquals("Item" + count, item, "Элемент " + count + " должен быть 'Item" + count + "'");
            count++;
        }

        assertEquals(1000, count, "Должно быть перебрано 1000 элементов");
    }

    // ================== ТЕСТЫ РАЗНЫХ ТИПОВ ДАННЫХ ==================

    /**
     * Проверка работы с целочисленным списком:
     * - базовые операции должны работать с Integer
     * - вставка по индексу
     */
    @Test
    void testIntegerList() {
        intList.add(10);
        intList.add(20);
        intList.add(1, 15);

        assertEquals(3, intList.size(), "Размер списка Integer должен быть 3");
        assertEquals(10, intList.get(0), "Первый элемент должен быть 10");
        assertEquals(15, intList.get(1), "Второй элемент должен быть 15");
        assertEquals(20, intList.get(2), "Третий элемент должен быть 20");
    }

    /**
     * Проверка работы с null элементами:
     * - список должен поддерживать null значения
     * - null элементы должны корректно обрабатываться всеми операциями
     */
    @Test
    void testNullElements() {
        list.add(null);
        list.add("Apple");
        list.add(1, null);

        assertEquals(3, list.size(), "Размер списка с null элементами должен быть 3");
        assertNull(list.get(0), "Первый элемент должен быть null");
        assertNull(list.get(1), "Второй элемент должен быть null");
        assertEquals("Apple", list.get(2), "Третий элемент должен быть 'Apple'");
    }

    // ================== ТЕСТЫ ГРАНИЧНЫХ СЛУЧАЕВ ==================

    /**
     * Проверка операций с пустым списком:
     * - size() должен возвращать 0
     * - isEmpty() должен возвращать true
     * - get() и set() должны выбрасывать исключения
     */
    @Test
    void testEmptyListOperations() {
        assertEquals(0, list.size(), "Размер пустого списка должен быть 0");
        assertTrue(list.isEmpty(), "Пустой список должен возвращать true на isEmpty()");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0),
                "get() на пустом списке должен вызвать исключение");
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, "Test"),
                "set() на пустом списке должен вызвать исключение");
    }

    /**
     * Проверка операций со списком из одного элемента:
     * - базовые операции должны работать корректно
     * - замена элемента
     */
    @Test
    void testSingleElementList() {
        list.add("Single");

        assertEquals(1, list.size(), "Размер списка с одним элементом должен быть 1");
        assertFalse(list.isEmpty(), "Список с элементом не должен быть пустым");
        assertEquals("Single", list.get(0), "Элемент должен быть 'Single'");

        String old = list.set(0, "Updated");
        assertEquals("Single", old, "Старое значение должно быть 'Single'");
        assertEquals("Updated", list.get(0), "Новое значение должно быть 'Updated'");
    }

    /**
     * Проверка работы с большим количеством элементов:
     * - стресс-тест добавления 1000 элементов
     * - проверка сохранности всех элементов
     */
    @Test
    void testLargeNumberOfElements() {
        // Тест добавления большого количества элементов
        for (int i = 0; i < 1000; i++) {
            list.add("Element" + i);
        }

        assertEquals(1000, list.size(), "Размер списка должен быть 1000");
        assertEquals("Element0", list.get(0), "Первый элемент должен быть 'Element0'");
        assertEquals("Element999", list.get(999), "Последний элемент должен быть 'Element999'");
    }

    // ================== ТЕСТЫ ПОСЛЕДОВАТЕЛЬНОСТИ ОПЕРАЦИЙ ==================

    /**
     * Комплексный тест последовательности операций:
     * - добавление в конец
     * - вставка в середину
     * - замена элемента
     * - проверка всех элементов после операций
     */
    @Test
    void testComplexScenario() {
        // Комплексный тест: добавление, вставка, замена
        list.add("A");
        list.add("C");
        list.add(1, "B");
        list.add("D");
        list.set(3, "E");

        assertEquals(4, list.size(), "Размер списка должен быть 4");
        assertEquals("A", list.get(0), "Первый элемент должен быть 'A'");
        assertEquals("B", list.get(1), "Второй элемент должен быть 'B'");
        assertEquals("C", list.get(2), "Третий элемент должен быть 'C'");
        assertEquals("E", list.get(3), "Четвёртый элемент должен быть 'E'");
    }

    /**
     * Тест многократного расширения массива:
     * - начальная ёмкость 2, добавляем 50 элементов
     * - должно произойти несколько расширений
     * - все элементы должны сохраниться в правильном порядке
     */
    @Test
    void testMultipleExpansions() {
        // Тест многократного расширения массива
        MyArrayList<Integer> testList = new MyArrayList<>(2);

        for (int i = 0; i < 50; i++) {
            testList.add(i);
        }

        assertEquals(50, testList.size(), "Размер списка должен быть 50");
        for (int i = 0; i < 50; i++) {
            assertEquals(i, testList.get(i), "Элемент " + i + " должен быть равен " + i);
        }
    }
}