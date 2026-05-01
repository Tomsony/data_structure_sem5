import util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit-тесты для алгоритмических функций класса Algorithm.
 * Тестируются: for_each, any_of, transform, copy_if.
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
    void forEach_printsAllElements() {
        // Проверяем, что действие применяется ко всем элементам,
        // Чтобы изменять значения локальной переменной, создаем её как массив
        //      и в лямбде меняем значение ссылки
        final int[] count = {0};
        final int[] sum = {0};

        Algorithm.for_each(numbers, n -> {
            count[0]++;         // считаем вызовы
            sum[0] += n;        // накапливаем сумму
        });

        assertEquals(10, count[0], "Действие должно быть вызвано 10 раз");
        assertEquals(55, sum[0], "Сумма чисел 1..10 должна быть 55");
    }

    @Test
    void forEach_emptyList_noAction() {
        // Для пустой коллекции действие не должно вызываться
        final int[] callCount = {0};

        Algorithm.for_each(emptyList, n -> callCount[0]++);

        assertEquals(0, callCount[0], "Для пустой коллекции действие не вызывается");
    }

    @Test
    void forEach_withDifferentType() {
        // Проверка работы с другим типом (String)
        final int[] totalLength = {0};

        Algorithm.for_each(words, w -> totalLength[0] += w.length());

        // apple(5) + banana(6) + cherry(6) + date(4) + elderberry(10) = 31
        assertEquals(31, totalLength[0], "Суммарная длина слов должна быть 31");
    }

    @Test
    void forEach_nullAction_throwsNullPointerException() {
        // Передача null вместо Consumer должна вызвать исключение
        assertThrows(NullPointerException.class,
                () -> Algorithm.for_each(numbers, null),
                "Null в качестве Consumer должен вызывать NullPointerException");
    }

    // ==================== ANY_OF ====================

    @Test
    void anyOf_elementExists_returnsTrue() {
        // Проверяем наличие чётного числа
        boolean hasEven = Algorithm.any_of(numbers, n -> n % 2 == 0);
        assertTrue(hasEven, "В массиве 1..10 должно быть чётное число");

        // Проверяем наличие конкретного числа
        boolean hasSeven = Algorithm.any_of(numbers, n -> n == 7);
        assertTrue(hasSeven, "Число 7 должно присутствовать в массиве");
    }

    @Test
    void anyOf_elementDoesNotExist_returnsFalse() {
        // Проверяем отсутствие числа > 100
        boolean hasLarge = Algorithm.any_of(numbers, n -> n > 100);
        assertFalse(hasLarge, "Чисел > 100 не должно быть");

        // Проверяем отсутствие отрицательного числа
        boolean hasNegative = Algorithm.any_of(numbers, n -> n < 0);
        assertFalse(hasNegative, "Отрицательных чисел не должно быть");
    }

    @Test
    void anyOf_emptyList_returnsFalse() {
        // Для пустой коллекции — всегда false
        boolean result = Algorithm.any_of(emptyList, n -> true);
        assertFalse(result, "Пустая коллекция: any_of должно вернуть false");
    }

    @Test
    void anyOf_withStrings() {
        // Есть ли слово, начинающееся с 'c'?
        boolean startsWithC = Algorithm.any_of(words, w -> w.startsWith("c"));
        assertTrue(startsWithC, "Слово 'cherry' начинается с 'c'");

        // Есть ли слово длиной > 20?
        boolean veryLong = Algorithm.any_of(words, w -> w.length() > 20);
        assertFalse(veryLong, "Нет слов длиной > 20");
    }

    @Test
    void anyOf_earlyExit() {
        // Проверка раннего выхода: счётчик вызовов предиката
        final int[] callCount = {0};

        boolean result = Algorithm.any_of(numbers, n -> {
            callCount[0]++;
            return n == 3;          // останавливаемся на 3-м элементе
        });

        assertTrue(result);
        assertEquals(3, callCount[0], "Предикат должен вызваться ровно 3 раза (ранний выход)");
    }

    @Test
    void anyOf_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> Algorithm.any_of(numbers, null),
                "Null в качестве Predicate должен вызывать NullPointerException");
    }

    // ==================== TRANSFORM ====================

    @Test
    void transform_squares() {
        // Возведение в квадрат
        MyArrayList<Integer> squares = Algorithm.transform(numbers, n -> n * n);

        assertEquals(numbers.size(), squares.size(),
                "Размер результата должен совпадать с исходным");

        // Проверяем каждое значение
        for (int i = 0; i < numbers.size(); i++) {
            int original = numbers.get(i);
            int expected = original * original;
            assertEquals(Integer.valueOf(expected), squares.get(i),
                    "Квадрат " + original + " должен быть " + expected);
        }
    }

    @Test
    void transform_typeConversion() {
        // Преобразование Integer → String
        MyArrayList<String> strings = Algorithm.transform(numbers,
                n -> "Number: " + n);

        assertEquals(numbers.size(), strings.size());
        assertEquals("Number: 1", strings.get(0));
        assertEquals("Number: 5", strings.get(4));
        assertEquals("Number: 10", strings.get(9));
    }

    @Test
    void transform_emptyList_returnsEmptyList() {
        MyArrayList<Integer> result = Algorithm.transform(emptyList, n -> n);
        assertTrue(result.isEmpty(), "transform пустой коллекции — пустой результат");
    }

    @Test
    void transform_originalNotModified() {
        // Исходный массив не должен измениться
        MyArrayList<Integer> copy = new MyArrayList<>();
        Algorithm.for_each(numbers, n -> copy.add(n));

        Algorithm.transform(numbers, n -> n * 100);  // выполняем transform

        // Сравниваем исходный массив с копией
        assertEquals(numbers.size(), copy.size());
        for (int i = 0; i < numbers.size(); i++) {
            assertEquals(copy.get(i), numbers.get(i),
                    "Исходный массив не должен измениться после transform");
        }
    }

    @Test
    void transform_toDifferentType() {
        // Integer → Double
        MyArrayList<Double> doubles = Algorithm.transform(numbers, n -> n * 1.5);

        assertEquals(numbers.size(), doubles.size());
        assertEquals(1.5, doubles.get(0), 0.001);
        assertEquals(7.5, doubles.get(4), 0.001);    // 5 * 1.5
        assertEquals(15.0, doubles.get(9), 0.001);    // 10 * 1.5
    }

    @Test
    void transform_withStrings() {
        // Преобразование: длина каждого слова
        MyArrayList<Integer> lengths = Algorithm.transform(words, String::length);

        assertEquals(words.size(), lengths.size());
        assertEquals(Integer.valueOf(5), lengths.get(0));   // "apple"
        assertEquals(Integer.valueOf(6), lengths.get(1));   // "banana"
        assertEquals(Integer.valueOf(4), lengths.get(3));   // "date"
        assertEquals(Integer.valueOf(10), lengths.get(4));  // "elderberry"
    }

    // ==================== COPY_IF ====================

    @Test
    void copyIf_evenNumbers() {
        // Фильтрация: только чётные числа
        MyArrayList<Integer> evens = Algorithm.copy_if(numbers, n -> n % 2 == 0);

        assertEquals(5, evens.size(), "Должно быть 5 чётных чисел: 2,4,6,8,10");
        assertEquals(Integer.valueOf(2), evens.get(0));
        assertEquals(Integer.valueOf(4), evens.get(1));
        assertEquals(Integer.valueOf(6), evens.get(2));
        assertEquals(Integer.valueOf(8), evens.get(3));
        assertEquals(Integer.valueOf(10), evens.get(4));
    }

    @Test
    void copyIf_noElementsMatch_returnsEmptyList() {
        // Фильтрация: числа > 100 (нет таких)
        MyArrayList<Integer> result = Algorithm.copy_if(numbers, n -> n > 100);
        assertTrue(result.isEmpty(), "Если ни один элемент не подходит — пустой результат");
    }

    @Test
    void copyIf_allElementsMatch_returnsAll() {
        // Все числа положительные
        MyArrayList<Integer> result = Algorithm.copy_if(numbers, n -> n > 0);

        assertEquals(numbers.size(), result.size(),
                "Если подходят все элементы, размер результата равен исходному");
        for (int i = 0; i < numbers.size(); i++) {
            assertEquals(numbers.get(i), result.get(i),
                    "Все элементы должны быть скопированы");
        }
    }

    @Test
    void copyIf_emptyList_returnsEmptyList() {
        MyArrayList<Integer> result = Algorithm.copy_if(emptyList, n -> true);
        assertTrue(result.isEmpty(), "copy_if пустой коллекции — пустой результат");
    }

    @Test
    void copyIf_originalNotModified() {
        // Исходный массив не должен измениться
        MyArrayList<Integer> copy = new MyArrayList<>();
        Algorithm.for_each(numbers, n -> copy.add(n));

        Algorithm.copy_if(numbers, n -> n % 2 == 0);  // выполняем copy_if

        // Сравниваем исходный массив с копией
        assertEquals(numbers.size(), copy.size());
        for (int i = 0; i < numbers.size(); i++) {
            assertEquals(copy.get(i), numbers.get(i),
                    "Исходный массив не должен измениться после copy_if");
        }
    }

    @Test
    void copyIf_withStrings() {
        // Фильтрация: слова длиной > 5
        MyArrayList<String> longWords = Algorithm.copy_if(words, w -> w.length() > 5);

        assertEquals(2, longWords.size(), "Должно быть 2 слова длиной > 5: 'banana'(6), 'elderberry'(10)");
        assertEquals("banana", longWords.get(0));
        assertEquals("elderberry", longWords.get(1));

        // Фильтрация: слова, начинающиеся с 'a'
        MyArrayList<String> startsWithA = Algorithm.copy_if(words, w -> w.startsWith("a"));

        assertEquals(1, startsWithA.size(), "Только 'apple' начинается с 'a'");
        assertEquals("apple", startsWithA.get(0));
    }
}