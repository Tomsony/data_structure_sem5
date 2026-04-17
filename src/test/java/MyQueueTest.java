import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса MyQueue
 */
class MyQueueTest {

    private MyQueue<String> queue;
    private MyQueue<Integer> intQueue;

    @BeforeEach
    void setUp() {
        queue = new MyQueue<>();
        intQueue = new MyQueue<>();
    }

    @Nested
    @DisplayName("Тесты конструктора и начального состояния")
    class ConstructorTests {

        @Test
        @DisplayName("Создание пустой очереди")
        void testEmptyQueueCreation() {
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
            assertNull(queue.peek());
            assertNull(queue.dequeue());
        }
    }

    @Nested
    @DisplayName("Тесты операции add")
    class AddTests {

        @Test
        @DisplayName("Добавление одного элемента в очередь")
        void testAddSingleElement() {
            queue.add("First");

            assertFalse(queue.isEmpty());
            assertEquals(1, queue.size());
            assertEquals("First", queue.peek());
        }

        @Test
        @DisplayName("Добавление нескольких элементов в очередь")
        void testAddMultipleElements() {
            queue.add("First");
            queue.add("Second");
            queue.add("Third");

            assertEquals(3, queue.size());
            assertEquals("First", queue.peek()); // FIFO - первый элемент остается первым
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "Hello World", "Special@Chars!#$"})
        @DisplayName("Добавление различных строковых значений")
        void testAddVariousStringValues(String value) {
            queue.add(value);
            assertEquals(value, queue.peek()); // Для одного элемента peek вернет его
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 999, Integer.MAX_VALUE, Integer.MIN_VALUE})
        @DisplayName("Добавление различных целочисленных значений")
        void testAddVariousIntegerValues(int value) {
            intQueue.add(value);
            assertEquals(value, intQueue.peek());
        }

        @Test
        @DisplayName("Добавление null значения")
        void testAddNullValue() {
            queue.add(null);

            assertFalse(queue.isEmpty());
            assertEquals(1, queue.size());
            assertNull(queue.peek());
        }

        @Test
        @DisplayName("Проверка порядка добавления (FIFO)")
        void testFIFOOrderAfterAdd() {
            queue.add("A");
            queue.add("B");
            queue.add("C");

            assertEquals("A", queue.peek()); // Первый добавленный должен быть первым
        }
    }

    @Nested
    @DisplayName("Тесты операции dequeue")
    class DequeueTests {

        @Test
        @DisplayName("Извлечение из пустой очереди")
        void testDequeueFromEmptyQueue() {
            assertNull(queue.dequeue());
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Извлечение одного элемента")
        void testDequeueSingleElement() {
            queue.add("Only");

            String result = queue.dequeue();

            assertEquals("Only", result);
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Извлечение нескольких элементов в порядке FIFO")
        void testDequeueMultipleElementsFIFO() {
            queue.add("First");
            queue.add("Second");
            queue.add("Third");

            assertEquals("First", queue.dequeue());
            assertEquals("Second", queue.dequeue());
            assertEquals("Third", queue.dequeue());
            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Извлечение после добавления null")
        void testDequeueAfterAddingNull() {
            queue.add(null);

            assertNull(queue.dequeue());
            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Чередование операций add и dequeue")
        void testAddDequeueInterleaved() {
            queue.add("A");
            queue.add("B");
            assertEquals("A", queue.dequeue());

            queue.add("C");
            assertEquals("B", queue.dequeue());
            assertEquals("C", queue.dequeue());

            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Извлечение всех элементов и проверка состояния")
        void testDequeueAllElements() {
            queue.add("A");
            queue.add("B");
            queue.add("C");

            queue.dequeue();
            queue.dequeue();
            queue.dequeue();

            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
            assertNull(queue.peek());
            assertNull(queue.dequeue());
        }
    }

    @Nested
    @DisplayName("Тесты операции peek")
    class PeekTests {

        @Test
        @DisplayName("Просмотр первой элемента пустой очереди")
        void testPeekEmptyQueue() {
            assertNull(queue.peek());
        }

        @Test
        @DisplayName("Просмотр первого элемента без удаления")
        void testPeekWithoutRemoval() {
            queue.add("First");

            String result = queue.peek();

            assertEquals("First", result);
            assertFalse(queue.isEmpty());
            assertEquals(1, queue.size());

            // Повторный peek должен вернуть тот же элемент
            assertEquals("First", queue.peek());
            assertEquals(1, queue.size());
        }

        @Test
        @DisplayName("Просмотр после нескольких операций add")
        void testPeekAfterMultipleAdds() {
            queue.add("First");
            assertEquals("First", queue.peek());

            queue.add("Second");
            assertEquals("First", queue.peek()); // FIFO - первый остается первым

            queue.add("Third");
            assertEquals("First", queue.peek());
        }

        @Test
        @DisplayName("Просмотр после операции dequeue")
        void testPeekAfterDequeue() {
            queue.add("A");
            queue.add("B");
            queue.add("C");

            queue.dequeue();
            assertEquals("B", queue.peek());

            queue.dequeue();
            assertEquals("C", queue.peek());
        }
    }

    @Nested
    @DisplayName("Тесты метода isEmpty")
    class IsEmptyTests {

        @Test
        @DisplayName("Проверка пустой очереди")
        void testIsEmptyOnEmptyQueue() {
            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Проверка непустой очереди")
        void testIsEmptyOnNonEmptyQueue() {
            queue.add("Element");
            assertFalse(queue.isEmpty());
        }

        @Test
        @DisplayName("Проверка после очистки очереди")
        void testIsEmptyAfterClear() {
            queue.add("A");
            queue.add("B");
            queue.clear();

            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Проверка после добавления и удаления всех элементов")
        void testIsEmptyAfterAddAndDequeue() {
            queue.add("Element");
            queue.dequeue();

            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Проверка после множества операций")
        void testIsEmptyAfterManyOperations() {
            for (int i = 0; i < 100; i++) {
                queue.add("Element" + i);
            }
            assertFalse(queue.isEmpty());

            for (int i = 0; i < 100; i++) {
                queue.dequeue();
            }
            assertTrue(queue.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты метода size")
    class SizeTests {

        @Test
        @DisplayName("Размер пустой очереди")
        void testSizeOfEmptyQueue() {
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Размер после добавления элементов")
        void testSizeAfterAdd() {
            queue.add("A");
            assertEquals(1, queue.size());

            queue.add("B");
            assertEquals(2, queue.size());

            queue.add("C");
            assertEquals(3, queue.size());
        }

        @Test
        @DisplayName("Размер после удаления элементов")
        void testSizeAfterDequeue() {
            queue.add("A");
            queue.add("B");
            queue.add("C");

            queue.dequeue();
            assertEquals(2, queue.size());

            queue.dequeue();
            assertEquals(1, queue.size());

            queue.dequeue();
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Размер после очистки")
        void testSizeAfterClear() {
            queue.add("A");
            queue.add("B");
            queue.add("C");

            queue.clear();
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Размер при чередовании операций")
        void testSizeWithInterleavedOperations() {
            assertEquals(0, queue.size());

            queue.add("A");
            assertEquals(1, queue.size());

            queue.add("B");
            assertEquals(2, queue.size());

            queue.dequeue();
            assertEquals(1, queue.size());

            queue.add("C");
            assertEquals(2, queue.size());

            queue.dequeue();
            assertEquals(1, queue.size());

            queue.dequeue();
            assertEquals(0, queue.size());
        }
    }

    @Nested
    @DisplayName("Тесты метода clear")
    class ClearTests {

        @Test
        @DisplayName("Очистка пустой очереди")
        void testClearEmptyQueue() {
            queue.clear();

            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Очистка очереди с одним элементом")
        void testClearSingleElementQueue() {
            queue.add("Single");
            queue.clear();

            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
            assertNull(queue.peek());
        }

        @Test
        @DisplayName("Очистка очереди с несколькими элементами")
        void testClearMultiElementQueue() {
            queue.add("A");
            queue.add("B");
            queue.add("C");

            queue.clear();

            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
            assertNull(queue.peek());
            assertNull(queue.dequeue());
        }

        @Test
        @DisplayName("Добавление элементов после очистки")
        void testAddAfterClear() {
            queue.add("A");
            queue.add("B");
            queue.clear();

            queue.add("New");

            assertFalse(queue.isEmpty());
            assertEquals(1, queue.size());
            assertEquals("New", queue.peek());
        }

        @Test
        @DisplayName("Многократная очистка")
        void testMultipleClears() {
            queue.add("A");
            queue.clear();
            queue.clear(); // Повторная очистка пустой очереди

            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }
    }

    @Nested
    @DisplayName("Тесты метода toString")
    class ToStringTests {

        @Test
        @DisplayName("Строковое представление пустой очереди")
        void testToStringEmptyQueue() {
            assertEquals("Queue: []", queue.toString());
        }

        @Test
        @DisplayName("Строковое представление очереди с одним элементом")
        void testToStringSingleElement() {
            queue.add("Single");
            assertEquals("Queue: [Single]", queue.toString());
        }

        @Test
        @DisplayName("Строковое представление очереди с несколькими элементами")
        void testToStringMultipleElements() {
            queue.add("First");
            queue.add("Second");
            queue.add("Third");

            String result = queue.toString();
            assertEquals("Queue: [First, Second, Third]", result);
        }

        @Test
        @DisplayName("Строковое представление после операций")
        void testToStringAfterOperations() {
            queue.add("A");
            queue.add("B");
            queue.dequeue();
            queue.add("C");

            String result = queue.toString();
            assertEquals("Queue: [B, C]", result);
        }

        @Test
        @DisplayName("Строковое представление с null элементами")
        void testToStringWithNullElements() {
            queue.add(null);
            queue.add("NotNull");

            String result = queue.toString();
            assertTrue(result.contains("null"));
            assertTrue(result.contains("NotNull"));
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Комплексный тест всех операций")
        void testComplexScenario() {
            // Начальное состояние
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());

            // Добавляем элементы
            queue.add("First");
            queue.add("Second");
            queue.add("Third");

            // Проверяем состояние
            assertFalse(queue.isEmpty());
            assertEquals(3, queue.size());
            assertEquals("First", queue.peek());

            // Извлекаем элементы
            assertEquals("First", queue.dequeue());
            assertEquals("Second", queue.dequeue());

            // Добавляем новые элементы
            queue.add("Fourth");
            queue.add("Fifth");

            // Проверяем текущее состояние
            assertEquals(3, queue.size());
            assertEquals("Third", queue.peek());
            assertEquals("Queue: [Third, Fourth, Fifth]", queue.toString());

            // Очищаем очередь
            queue.clear();
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Тест с большим количеством элементов")
        void testLargeQueue() {
            final int COUNT = 1000;

            for (int i = 0; i < COUNT; i++) {
                intQueue.add(i);
            }

            assertEquals(COUNT, intQueue.size());
            assertEquals(0, intQueue.peek()); // FIFO - первый элемент

            for (int i = 0; i < COUNT; i++) {
                assertEquals(i, intQueue.dequeue());
            }

            assertTrue(intQueue.isEmpty());
            assertEquals(0, intQueue.size());
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

            MyQueue<Person> personQueue = new MyQueue<>();
            Person alice = new Person("Alice", 25);
            Person bob = new Person("Bob", 30);
            Person charlie = new Person("Charlie", 35);

            personQueue.add(alice);
            personQueue.add(bob);
            personQueue.add(charlie);

            assertEquals(alice, personQueue.dequeue());
            assertEquals(bob, personQueue.dequeue());
            assertEquals(charlie, personQueue.dequeue());
            assertTrue(personQueue.isEmpty());
        }

        @Test
        @DisplayName("Тест поведения front и rear указателей")
        void testFrontRearPointers() {
            // Пустая очередь
            queue.add("A");
            // front и rear указывают на один узел

            queue.add("B");
            // front указывает на A, rear на B

            queue.add("C");
            // front указывает на A, rear на C

            assertEquals("A", queue.dequeue());
            // front теперь указывает на B, rear на C

            assertEquals("B", queue.dequeue());
            // front и rear указывают на C

            assertEquals("C", queue.dequeue());
            // front и rear становятся null
            assertTrue(queue.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты граничных случаев")
    class EdgeCaseTests {

        @Test
        @DisplayName("Многократное добавление и удаление")
        void testRepeatedAddDequeue() {
            for (int i = 0; i < 100; i++) {
                queue.add("Element" + i);
                assertEquals("Element" + i, queue.dequeue());
                assertTrue(queue.isEmpty());
            }
        }

        @Test
        @DisplayName("Проверка порядка элементов при FIFO")
        void testFIFOOrder() {
            String[] elements = {"A", "B", "C", "D", "E"};

            for (String element : elements) {
                queue.add(element);
            }

            for (String element : elements) {
                assertEquals(element, queue.dequeue());
            }
        }

        @Test
        @DisplayName("Тест с mixed типами (в разных очередях)")
        void testMixedTypesInDifferentQueues() {
            MyQueue<Object> objectQueue = new MyQueue<>();

            objectQueue.add("String");
            objectQueue.add(123);
            objectQueue.add(45.67);
            objectQueue.add(true);

            assertEquals("String", objectQueue.dequeue());
            assertEquals(123, objectQueue.dequeue());
            assertEquals(45.67, objectQueue.dequeue());
            assertEquals(true, objectQueue.dequeue());
        }

        @Test
        @DisplayName("Тест после множества операций")
        void testAfterManyOperations() {
            // Симуляция интенсивного использования
            for (int i = 0; i < 1000; i++) {
                queue.add("Temp" + i);
                queue.peek();
            }

            assertEquals(1000, queue.size());

            for (int i = 0; i < 500; i++) {
                queue.dequeue();
            }

            assertEquals(500, queue.size());

            queue.clear();
            assertTrue(queue.isEmpty());

            // Добавляем снова
            queue.add("Final");
            assertEquals("Final", queue.peek());
        }

        @Test
        @DisplayName("Тест с одним элементом - особый случай")
        void testSingleElementSpecialCase() {
            queue.add("Solo");

            assertEquals("Solo", queue.peek());
            assertEquals("Solo", queue.dequeue());
            assertTrue(queue.isEmpty());

            // После удаления единственного элемента
            assertNull(queue.peek());
            assertNull(queue.dequeue());
        }
    }
}