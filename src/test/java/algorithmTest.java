import util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit-тесты для алгоритмических функций класса Algorithm.
 * Тестируются: for_each, any_of, transform, copy_if, all_of, none_of.
 *
 * @author Артём Томских, ИВТ-23
 */
class algorithmTest {

    private MyArrayList<Integer> numbers;
    private MyArrayList<Integer> emptyList;
    private MyArrayList<String> words;

    @BeforeEach
    void setUp() {
        // Тестовые данные: числа от 1 до 10
        numbers = new MyArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }

        // Пустой массив для граничных тестов
        emptyList = new MyArrayList<>();

        // Массив строк для тестов с другим типом
        words = new MyArrayList<>();
        words.add("apple");
        words.add("banana");
        words.add("cherry");
        words.add("date");
        words.add("elderberry");
    }

    // ==================== FOR_EACH ====================

    @Test
    void forEach_appliesActionToAllElements() {
        // Проверяем, что действие применяется ко всем 10 элементам, и считаем сумму
        final int[] count = {0};
        final int[] sum = {0};

        Algorithm.for_each(numbers, n -> {
            count[0]++;
            sum[0] += n;
        });

        assertEquals(10, count[0], "Действие должно быть вызвано 10 раз");
        assertEquals(55, sum[0], "Сумма чисел 1..10 должна быть 55");
    }

    @Test
    void forEach_emptyList_noAction() {
        // Для пустой коллекции действие не должно вызываться ни разу
        final int[] callCount = {0};

        Algorithm.for_each(emptyList, n -> callCount[0]++);

        assertEquals(0, callCount[0], "Для пустой коллекции действие не вызывается");
    }

    @Test
    void forEach_nullAction_throwsNullPointerException() {
        // Передача null вместо Consumer должна вызывать NullPointerException
        assertThrows(NullPointerException.class,
                () -> Algorithm.for_each(numbers, null));
    }

    // ==================== ANY_OF ====================

    @Test
    void anyOf_elementExists_returnsTrue() {
        // В массиве 1..10 есть чётное число — ожидаем true
        assertTrue(Algorithm.any_of(numbers, n -> n % 2 == 0));
    }

    @Test
    void anyOf_elementDoesNotExist_returnsFalse() {
        // Чисел > 100 нет — ожидаем false
        assertFalse(Algorithm.any_of(numbers, n -> n > 100));
    }

    @Test
    void anyOf_emptyList_returnsFalse() {
        // Для пустой коллекции — всегда false
        assertFalse(Algorithm.any_of(emptyList, n -> true));
    }

    @Test
    void anyOf_earlyExit() {
        // Проверяем ранний выход: предикат должен остановиться на числе 3
        final int[] callCount = {0};

        boolean result = Algorithm.any_of(numbers, n -> {
            callCount[0]++;
            return n == 3;
        });

        assertTrue(result);
        assertEquals(3, callCount[0], "Предикат должен вызваться ровно 3 раза");
    }

    @Test
    void anyOf_nullPredicate_throwsNullPointerException() {
        // Передача null вместо Predicate должна вызывать NullPointerException
        assertThrows(NullPointerException.class,
                () -> Algorithm.any_of(numbers, null));
    }

    // ==================== TRANSFORM ====================

    @Test
    void transform_squares() {
        // Возведение в квадрат: размер сохраняется, значения — квадраты исходных
        MyArrayList<Integer> squares = Algorithm.transform(numbers, n -> n * n);

        assertEquals(numbers.size(), squares.size());
        assertEquals(Integer.valueOf(1), squares.get(0));
        assertEquals(Integer.valueOf(25), squares.get(4));
        assertEquals(Integer.valueOf(100), squares.get(9));
    }

    @Test
    void transform_typeConversion() {
        // Смена типа: Integer -> String
        MyArrayList<String> strings = Algorithm.transform(numbers, n -> "Number: " + n);

        assertEquals(numbers.size(), strings.size());
        assertEquals("Number: 1", strings.get(0));
        assertEquals("Number: 10", strings.get(9));
    }

    @Test
    void transform_emptyList_returnsEmptyList() {
        // Для пустой коллекции — пустой результат
        MyArrayList<Integer> result = Algorithm.transform(emptyList, n -> n);
        assertTrue(result.isEmpty());
    }

    @Test
    void transform_originalNotModified() {
        // Исходный массив не должен меняться после transform
        Algorithm.transform(numbers, n -> n * 100);

        assertEquals(Integer.valueOf(1), numbers.get(0));
        assertEquals(Integer.valueOf(10), numbers.get(9));
    }

    // ==================== COPY_IF ====================

    @Test
    void copyIf_evenNumbers() {
        // Фильтрация чётных чисел: 2, 4, 6, 8, 10. Размер должен быть 5
        MyArrayList<Integer> evens = Algorithm.copy_if(numbers, n -> n % 2 == 0);

        assertEquals(5, evens.size());
        assertEquals(Integer.valueOf(2), evens.get(0));
        assertEquals(Integer.valueOf(10), evens.get(4));
    }

    @Test
    void copyIf_noElementsMatch_returnsEmptyList() {
        // Ни один элемент не подходит — пустой результат
        MyArrayList<Integer> result = Algorithm.copy_if(numbers, n -> n > 100);
        assertTrue(result.isEmpty());
    }

    @Test
    void copyIf_allElementsMatch_returnsAll() {
        // Подходят все элементы — результат совпадает с исходным
        MyArrayList<Integer> result = Algorithm.copy_if(numbers, n -> n > 0);

        assertEquals(numbers.size(), result.size());
        assertEquals(numbers.get(0), result.get(0));
        assertEquals(numbers.get(9), result.get(9));
    }

    @Test
    void copyIf_originalNotModified() {
        // Исходный массив не должен меняться после copy_if
        Algorithm.copy_if(numbers, n -> n % 2 == 0);

        assertEquals(10, numbers.size());
        assertEquals(Integer.valueOf(1), numbers.get(0));
    }

    // ==================== ALL_OF ====================

    @Test
    void allOf_allPositive_returnsTrue() {
        // Все числа 1..10 положительные — ожидаем true
        assertTrue(Algorithm.all_of(numbers, n -> n > 0));
    }

    @Test
    void allOf_notAllEven_returnsFalse() {
        // Не все числа чётные — ожидаем false
        assertFalse(Algorithm.all_of(numbers, n -> n % 2 == 0));
    }

    @Test
    void allOf_emptyList_returnsTrue() {
        // Для пустой коллекции условие выполняется вакуумно (все элементы "прошли" проверку)
        assertTrue(Algorithm.all_of(emptyList, n -> n > 0));
    }

    @Test
    void allOf_nullPredicate_throwsNullPointerException() {
        // Передача null вместо Predicate должна вызывать NullPointerException
        assertThrows(NullPointerException.class,
                () -> Algorithm.all_of(numbers, null));
    }

    // ==================== NONE_OF ====================

    @Test
    void noneOf_noNegative_returnsTrue() {
        // Отрицательных чисел нет — ожидаем true
        assertTrue(Algorithm.none_of(numbers, n -> n < 0));
    }

    @Test
    void noneOf_hasEven_returnsFalse() {
        // Чётные числа есть — ожидаем false
        assertFalse(Algorithm.none_of(numbers, n -> n % 2 == 0));
    }

    @Test
    void noneOf_emptyList_returnsTrue() {
        // Для пустой коллекции ни один элемент не нарушает условие — ожидаем true
        assertTrue(Algorithm.none_of(emptyList, n -> true));
    }

    @Test
    void noneOf_nullPredicate_throwsNullPointerException() {
        // Передача null вместо Predicate должна вызывать NullPointerException
        assertThrows(NullPointerException.class,
                () -> Algorithm.none_of(numbers, null));
    }
}