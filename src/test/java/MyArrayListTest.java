import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса MyArrayList
 */
class MyArrayListTest {

    private MyArrayList<String> list;
    private MyArrayList<Integer> intList;

    @BeforeEach
    void setUp() {
        list = new MyArrayList<>();
        intList = new MyArrayList<>();
    }

    // ================== ТЕСТЫ КОНСТРУКТОРОВ ==================

    @Test
    void testDefaultConstructor() {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testConstructorWithCapacity() {
        MyArrayList<String> customList = new MyArrayList<>(20);
        assertEquals(0, customList.size());
        assertTrue(customList.isEmpty());
    }

    @Test
    void testConstructorWithZeroCapacity() {
        MyArrayList<String> zeroList = new MyArrayList<>(0);
        assertEquals(0, zeroList.size());
        assertTrue(zeroList.isEmpty());
    }

    @Test
    void testConstructorWithNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MyArrayList<>(-1);
        });
    }

    // ================== ТЕСТЫ ДОБАВЛЕНИЯ ЭЛЕМЕНТОВ ==================

    @Test
    void testAddSingleElement() {
        assertTrue(list.add("Apple"));
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());
        assertEquals("Apple", list.get(0));
    }

    @Test
    void testAddMultipleElements() {
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");

        assertEquals(3, list.size());
        assertEquals("Apple", list.get(0));
        assertEquals("Banana", list.get(1));
        assertEquals("Cherry", list.get(2));
    }

    @Test
    void testAddWithIndexAtBeginning() {
        list.add("Banana");
        list.add(0, "Apple"); // Вставка в начало

        assertEquals(2, list.size());
        assertEquals("Apple", list.get(0));
        assertEquals("Banana", list.get(1));
    }

    @Test
    void testAddWithIndexInMiddle() {
        list.add("Apple");
        list.add("Cherry");
        list.add(1, "Banana"); // Вставка в середину

        assertEquals(3, list.size());
        assertEquals("Apple", list.get(0));
        assertEquals("Banana", list.get(1));
        assertEquals("Cherry", list.get(2));
    }

    @Test
    void testAddWithIndexAtEnd() {
        list.add("Apple");
        list.add("Banana");
        list.add(2, "Cherry"); // Вставка в конец

        assertEquals(3, list.size());
        assertEquals("Apple", list.get(0));
        assertEquals("Banana", list.get(1));
        assertEquals("Cherry", list.get(2));
    }

    @Test
    void testAddWithInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(1, "Invalid"); // Пустой список, индекс 1 невалиден
        });

        list.add("Apple");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(5, "Invalid"); // Индекс больше size
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(-1, "Invalid"); // Отрицательный индекс
        });
    }

    // ================== ТЕСТЫ АВТОМАТИЧЕСКОГО РАСШИРЕНИЯ ==================

    @Test
    void testAutoExpansion() {
        // Создаем маленький массив для теста расширения
        MyArrayList<String> smallList = new MyArrayList<>(3);

        smallList.add("A");
        smallList.add("B");
        smallList.add("C");
        assertEquals(3, smallList.size());

        // Добавляем 4-й элемент - должно произойти расширение
        smallList.add("D");
        assertEquals(4, smallList.size());
        assertEquals("D", smallList.get(3));
    }

    @Test
    void testAutoExpansionWithIndex() {
        MyArrayList<String> smallList = new MyArrayList<>(2);

        smallList.add("A");
        smallList.add("C");
        smallList.add(1, "B"); // Вставка с расширением

        assertEquals(3, smallList.size());
        assertEquals("A", smallList.get(0));
        assertEquals("B", smallList.get(1));
        assertEquals("C", smallList.get(2));
    }

    // ================== ТЕСТЫ GET И SET ==================

    @Test
    void testGetValidIndex() {
        list.add("Apple");
        list.add("Banana");

        assertEquals("Apple", list.get(0));
        assertEquals("Banana", list.get(1));
    }

    @Test
    void testGetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(0); // Пустой список
        });

        list.add("Apple");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(1); // Индекс = size
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(-1); // Отрицательный индекс
        });
    }

    @Test
    void testSetElement() {
        list.add("Apple");
        list.add("Banana");

        String oldValue = list.set(1, "Orange");
        assertEquals("Banana", oldValue);
        assertEquals("Orange", list.get(1));
        assertEquals(2, list.size()); // Размер не должен измениться
    }

    @Test
    void testSetFirstElement() {
        list.add("Apple");
        list.add("Banana");

        String oldValue = list.set(0, "Apricot");
        assertEquals("Apple", oldValue);
        assertEquals("Apricot", list.get(0));
    }

    @Test
    void testSetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.set(0, "Invalid");
        });

        list.add("Apple");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.set(1, "Invalid");
        });
    }

    // ================== ТЕСТЫ РАЗНЫХ ТИПОВ ДАННЫХ ==================

    @Test
    void testIntegerList() {
        intList.add(10);
        intList.add(20);
        intList.add(1, 15);

        assertEquals(3, intList.size());
        assertEquals(10, intList.get(0));
        assertEquals(15, intList.get(1));
        assertEquals(20, intList.get(2));
    }

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

    // ================== ТЕСТЫ ГРАНИЧНЫХ СЛУЧАЕВ ==================

    @Test
    void testEmptyListOperations() {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, "Test"));
    }

    @Test
    void testSingleElementList() {
        list.add("Single");

        assertEquals(1, list.size());
        assertFalse(list.isEmpty());
        assertEquals("Single", list.get(0));

        String old = list.set(0, "Updated");
        assertEquals("Single", old);
        assertEquals("Updated", list.get(0));
    }

    @Test
    void testLargeNumberOfElements() {
        // Тест добавления большого количества элементов
        for (int i = 0; i < 1000; i++) {
            list.add("Element" + i);
        }

        assertEquals(1000, list.size());
        assertEquals("Element0", list.get(0));
        assertEquals("Element999", list.get(999));
    }

    // ================== ТЕСТЫ ПОСЛЕДОВАТЕЛЬНОСТИ ОПЕРАЦИЙ ==================

    @Test
    void testComplexScenario() {
        // Комплексный тест: добавление, вставка, замена
        list.add("A");
        list.add("C");
        list.add(1, "B");
        list.add("D");
        list.set(3, "E");

        assertEquals(4, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        assertEquals("E", list.get(3));
    }

    @Test
    void testMultipleExpansions() {
        // Тест многократного расширения массива
        MyArrayList<Integer> testList = new MyArrayList<>(2);

        for (int i = 0; i < 50; i++) {
            testList.add(i);
        }

        assertEquals(50, testList.size());
        for (int i = 0; i < 50; i++) {
            assertEquals(i, testList.get(i));
        }
    }
}