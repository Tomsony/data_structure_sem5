import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.MyStack;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса util.MyStack
 */
class MyStackTest {

    private MyStack<String> stack;
    private MyStack<Integer> intStack;

    @BeforeEach
    void setUp() {
        stack = new MyStack<>();
        intStack = new MyStack<>();
    }

    @Nested
    @DisplayName("Тесты конструктора и начального состояния")
    class ConstructorTests {

        @Test
        @DisplayName("Создание пустого стека")
        void testEmptyStackCreation() {
            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
            assertNull(stack.peek());
            assertNull(stack.pop());
        }
    }

    @Nested
    @DisplayName("Тесты операции push")
    class PushTests {

        @Test
        @DisplayName("Добавление одного элемента в стек")
        void testPushSingleElement() {
            stack.push("First");

            assertFalse(stack.isEmpty());
            assertEquals(1, stack.size());
            assertEquals("First", stack.peek());
        }

        @Test
        @DisplayName("Добавление нескольких элементов в стек")
        void testPushMultipleElements() {
            stack.push("First");
            stack.push("Second");
            stack.push("Third");

            assertEquals(3, stack.size());
            assertEquals("Third", stack.peek());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "Hello World", "Special@Chars!#$"})
        @DisplayName("Добавление различных строковых значений")
        void testPushVariousStringValues(String value) {
            stack.push(value);
            assertEquals(value, stack.peek());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 999, Integer.MAX_VALUE, Integer.MIN_VALUE})
        @DisplayName("Добавление различных целочисленных значений")
        void testPushVariousIntegerValues(int value) {
            intStack.push(value);
            assertEquals(value, intStack.peek());
        }
    }

    @Nested
    @DisplayName("Тесты операции pop")
    class PopTests {

        @Test
        @DisplayName("Извлечение из пустого стека")
        void testPopFromEmptyStack() {
            assertNull(stack.pop());
            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
        }

        @Test
        @DisplayName("Извлечение одного элемента")
        void testPopSingleElement() {
            stack.push("Only");

            String result = stack.pop();

            assertEquals("Only", result);
            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
        }

        @Test
        @DisplayName("Извлечение нескольких элементов в порядке LIFO")
        void testPopMultipleElementsLIFO() {
            stack.push("First");
            stack.push("Second");
            stack.push("Third");

            assertEquals("Third", stack.pop());
            assertEquals("Second", stack.pop());
            assertEquals("First", stack.pop());
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("Попытка добавления null должна выбрасывать исключение")
        void testPushNullThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> stack.push(null));
        }

        @Test
        @DisplayName("Чередование операций push и pop")
        void testPushPopInterleaved() {
            stack.push("A");
            stack.push("B");
            assertEquals("B", stack.pop());

            stack.push("C");
            assertEquals("C", stack.pop());
            assertEquals("A", stack.pop());

            assertTrue(stack.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты операции peek")
    class PeekTests {

        @Test
        @DisplayName("Просмотр вершины пустого стека")
        void testPeekEmptyStack() {
            assertNull(stack.peek());
        }

        @Test
        @DisplayName("Просмотр вершины стека без удаления")
        void testPeekWithoutRemoval() {
            stack.push("Top");

            String result = stack.peek();

            assertEquals("Top", result);
            assertFalse(stack.isEmpty());
            assertEquals(1, stack.size());

            // Повторный peek должен вернуть тот же элемент
            assertEquals("Top", stack.peek());
            assertEquals(1, stack.size());
        }

        @Test
        @DisplayName("Просмотр после нескольких операций push")
        void testPeekAfterMultiplePushes() {
            stack.push("First");
            assertEquals("First", stack.peek());

            stack.push("Second");
            assertEquals("Second", stack.peek());

            stack.push("Third");
            assertEquals("Third", stack.peek());
        }

        @Test
        @DisplayName("Просмотр после операции pop")
        void testPeekAfterPop() {
            stack.push("A");
            stack.push("B");

            stack.pop();
            assertEquals("A", stack.peek());
        }
    }

    @Nested
    @DisplayName("Тесты метода isEmpty")
    class IsEmptyTests {

        @Test
        @DisplayName("Проверка пустого стека")
        void testIsEmptyOnEmptyStack() {
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("Проверка непустого стека")
        void testIsEmptyOnNonEmptyStack() {
            stack.push("Element");
            assertFalse(stack.isEmpty());
        }

        @Test
        @DisplayName("Проверка после очистки стека")
        void testIsEmptyAfterClear() {
            stack.push("A");
            stack.push("B");
            stack.clear();

            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("Проверка после добавления и удаления всех элементов")
        void testIsEmptyAfterPushAndPop() {
            stack.push("Element");
            stack.pop();

            assertTrue(stack.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты метода size")
    class SizeTests {

        @Test
        @DisplayName("Размер пустого стека")
        void testSizeOfEmptyStack() {
            assertEquals(0, stack.size());
        }

        @Test
        @DisplayName("Размер после добавления элементов")
        void testSizeAfterPush() {
            stack.push("A");
            assertEquals(1, stack.size());

            stack.push("B");
            assertEquals(2, stack.size());

            stack.push("C");
            assertEquals(3, stack.size());
        }

        @Test
        @DisplayName("Размер после удаления элементов")
        void testSizeAfterPop() {
            stack.push("A");
            stack.push("B");
            stack.push("C");

            stack.pop();
            assertEquals(2, stack.size());

            stack.pop();
            assertEquals(1, stack.size());

            stack.pop();
            assertEquals(0, stack.size());
        }

        @Test
        @DisplayName("Размер после очистки")
        void testSizeAfterClear() {
            stack.push("A");
            stack.push("B");
            stack.push("C");

            stack.clear();
            assertEquals(0, stack.size());
        }
    }

    @Nested
    @DisplayName("Тесты метода clear")
    class ClearTests {

        @Test
        @DisplayName("Очистка пустого стека")
        void testClearEmptyStack() {
            stack.clear();

            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
        }

        @Test
        @DisplayName("Очистка стека с одним элементом")
        void testClearSingleElementStack() {
            stack.push("Single");
            stack.clear();

            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
            assertNull(stack.peek());
        }

        @Test
        @DisplayName("Очистка стека с несколькими элементами")
        void testClearMultiElementStack() {
            stack.push("A");
            stack.push("B");
            stack.push("C");

            stack.clear();

            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
            assertNull(stack.peek());
            assertNull(stack.pop());
        }

        @Test
        @DisplayName("Добавление элементов после очистки")
        void testPushAfterClear() {
            stack.push("A");
            stack.push("B");
            stack.clear();

            stack.push("New");

            assertFalse(stack.isEmpty());
            assertEquals(1, stack.size());
            assertEquals("New", stack.peek());
        }
    }

    @Nested
    @DisplayName("Тесты метода toString")
    class ToStringTests {

        @Test
        @DisplayName("Строковое представление пустого стека")
        void testToStringEmptyStack() {
            assertEquals("Stack: []", stack.toString());
        }

        @Test
        @DisplayName("Строковое представление стека с одним элементом")
        void testToStringSingleElement() {
            stack.push("Single");
            assertEquals("Stack: [Single]", stack.toString());
        }

        @Test
        @DisplayName("Строковое представление стека с несколькими элементами")
        void testToStringMultipleElements() {
            stack.push("First");
            stack.push("Second");
            stack.push("Third");

            String result = stack.toString();
            assertEquals("Stack: [Third, Second, First]", result);
        }

        @Test
        @DisplayName("Строковое представление после операций")
        void testToStringAfterOperations() {
            stack.push("A");
            stack.push("B");
            stack.pop();
            stack.push("C");

            String result = stack.toString();
            assertEquals("Stack: [C, A]", result);
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Комплексный тест всех операций")
        void testComplexScenario() {
            // Начальное состояние
            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());

            // Добавляем элементы
            stack.push("Start");
            stack.push("Middle");
            stack.push("End");

            // Проверяем состояние
            assertFalse(stack.isEmpty());
            assertEquals(3, stack.size());
            assertEquals("End", stack.peek());

            // Извлекаем элементы
            assertEquals("End", stack.pop());
            assertEquals("Middle", stack.pop());

            // Добавляем новые элементы
            stack.push("New");

            // Проверяем текущее состояние
            assertEquals(2, stack.size());
            assertEquals("New", stack.peek());
            assertEquals("Stack: [New, Start]", stack.toString());

            // Очищаем стек
            stack.clear();
            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
        }

        @Test
        @DisplayName("Тест с большим количеством элементов")
        void testLargeStack() {
            final int COUNT = 1000;

            for (int i = 0; i < COUNT; i++) {
                intStack.push(i);
            }

            assertEquals(COUNT, intStack.size());
            assertEquals(COUNT - 1, intStack.peek());

            for (int i = COUNT - 1; i >= 0; i--) {
                assertEquals(i, intStack.pop());
            }

            assertTrue(intStack.isEmpty());
            assertEquals(0, intStack.size());
        }

        @Test
        @DisplayName("Тест с пользовательскими объектами")
        void testWithCustomObjects() {
            class Person {
                private String name;
                private int age;

                public Person(String name, int age) {
                    this.name = name;
                    this.age = age;
                }

                @Override
                public String toString() {
                    return name + "(" + age + ")";
                }

                @Override
                public boolean equals(Object obj) {
                    if (this == obj) return true;
                    if (obj == null || getClass() != obj.getClass()) return false;
                    Person person = (Person) obj;
                    return age == person.age && name.equals(person.name);
                }
            }

            MyStack<Person> personStack = new MyStack<>();
            Person alice = new Person("Alice", 25);
            Person bob = new Person("Bob", 30);

            personStack.push(alice);
            personStack.push(bob);

            assertEquals(bob, personStack.pop());
            assertEquals(alice, personStack.pop());
            assertTrue(personStack.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты граничных случаев")
    class EdgeCaseTests {

        @Test
        @DisplayName("Многократное добавление и удаление")
        void testRepeatedPushPop() {
            for (int i = 0; i < 100; i++) {
                stack.push("Element" + i);
                assertEquals("Element" + i, stack.pop());
                assertTrue(stack.isEmpty());
            }
        }

        @Test
        @DisplayName("Проверка порядка элементов при LIFO")
        void testLIFOOrder() {
            String[] elements = {"A", "B", "C", "D", "E"};

            for (String element : elements) {
                stack.push(element);
            }

            for (int i = elements.length - 1; i >= 0; i--) {
                assertEquals(elements[i], stack.pop());
            }
        }

        @Test
        @DisplayName("Тест с mixed типами (в разных стеках)")
        void testMixedTypesInDifferentStacks() {
            MyStack<Object> objectStack = new MyStack<>();

            objectStack.push("String");
            objectStack.push(123);
            objectStack.push(45.67);
            objectStack.push(true);

            assertEquals(true, objectStack.pop());
            assertEquals(45.67, objectStack.pop());
            assertEquals(123, objectStack.pop());
            assertEquals("String", objectStack.pop());
        }

        @Test
        @DisplayName("Тест после множества операций")
        void testAfterManyOperations() {
            // Симуляция интенсивного использования
            for (int i = 0; i < 1000; i++) {
                stack.push("Temp" + i);
                stack.peek();
            }

            assertEquals(1000, stack.size());

            for (int i = 0; i < 500; i++) {
                stack.pop();
            }

            assertEquals(500, stack.size());

            stack.clear();
            assertTrue(stack.isEmpty());

            // Добавляем снова
            stack.push("Final");
            assertEquals("Final", stack.peek());
        }
    }
}