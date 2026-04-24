import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;
import trees.BaseNode;
import util.QueueNode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса util.QueueNode
 */
class QueueNodeTest {

    private QueueNode<String> firstNode;
    private QueueNode<Integer> numericNode;

    @BeforeEach
    void setUp() {
        firstNode = new QueueNode<>("First");
        numericNode = new QueueNode<>(42);
    }

    @Test
    @DisplayName("Создание util.QueueNode с корректным значением")
    void testQueueNodeCreationWithValidValue() {
        assertNotNull(firstNode);
        assertEquals("First", firstNode.getValue());

        assertNotNull(numericNode);
        assertEquals(42, numericNode.getValue());
    }

    @Test
    @DisplayName("Создание util.QueueNode с null значением должно выбрасывать исключение")
    void testQueueNodeCreationWithNullValueThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new QueueNode<String>(null)
        );

        assertEquals("Значение узла не может быть пустым", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Параметризованный тест: создание util.QueueNode с null значением")
    void testQueueNodeCreationWithNullValueParameterized(String nullValue) {
        assertThrows(IllegalArgumentException.class,
                () -> new QueueNode<String>(nullValue));
    }

    @Test
    @DisplayName("Изначально следующий узел должен быть null")
    void testInitialNextIsNull() {
        assertNull(firstNode.getNext());
        assertNull(numericNode.getNext());
    }

    @Test
    @DisplayName("Установка и получение следующего узла")
    void testSetAndGetNext() {
        QueueNode<String> secondNode = new QueueNode<>("Second");
        firstNode.setNext(secondNode);

        assertNotNull(firstNode.getNext());
        assertEquals("Second", firstNode.getNext().getValue());
        assertEquals(secondNode, firstNode.getNext());
    }

    @Test
    @DisplayName("Установка null следующего узла")
    void testSetNextToNull() {
        QueueNode<String> secondNode = new QueueNode<>("Second");
        firstNode.setNext(secondNode);

        // Убедимся, что следующий узел установлен
        assertNotNull(firstNode.getNext());

        // Установим null
        firstNode.setNext(null);
        assertNull(firstNode.getNext());
    }

    @Test
    @DisplayName("Создание цепочки из нескольких узлов")
    void testMultiNodeChain() {
        // Создаем цепочку: First -> Second -> Third
        QueueNode<String> secondNode = new QueueNode<>("Second");
        QueueNode<String> thirdNode = new QueueNode<>("Third");

        firstNode.setNext(secondNode);
        secondNode.setNext(thirdNode);

        // Проверяем цепочку
        assertEquals("Second", firstNode.getNext().getValue());
        assertEquals("Third", firstNode.getNext().getNext().getValue());
        assertNull(firstNode.getNext().getNext().getNext());
    }

    @Test
    @DisplayName("Тест метода toString с null следующим узлом")
    void testToStringWithNullNext() {
        String result = firstNode.toString();

        assertTrue(result.contains("util.QueueNode"));
        assertTrue(result.contains("value: = First"));
        assertTrue(result.contains("next: = null"));
    }

    @Test
    @DisplayName("Тест метода toString с установленным следующим узлом")
    void testToStringWithNext() {
        QueueNode<String> secondNode = new QueueNode<>("Second");
        firstNode.setNext(secondNode);

        String result = firstNode.toString();

        assertTrue(result.contains("util.QueueNode"));
        assertTrue(result.contains("value: = First"));
        assertTrue(result.contains("next: = Second"));
    }

    @Test
    @DisplayName("Тест метода toString с числовыми значениями")
    void testToStringWithNumericValues() {
        QueueNode<Integer> secondNode = new QueueNode<>(100);
        numericNode.setNext(secondNode);

        String result = numericNode.toString();

        assertTrue(result.contains("util.QueueNode"));
        assertTrue(result.contains("value: = 42"));
        assertTrue(result.contains("next: = 100"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "A", "Hello World", "Special@Chars!#$"})
    @DisplayName("Параметризованный тест с различными строковыми значениями")
    void testVariousStringValues(String value) {
        QueueNode<String> node = new QueueNode<>(value);
        assertEquals(value, node.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 999, Integer.MAX_VALUE, Integer.MIN_VALUE})
    @DisplayName("Параметризованный тест с различными целочисленными значениями")
    void testVariousIntegerValues(int value) {
        QueueNode<Integer> node = new QueueNode<>(value);
        assertEquals(value, node.getValue());
    }

    @Test
    @DisplayName("Тест независимости узлов - изменение одного узла не влияет на другие")
    void testNodeIndependence() {
        QueueNode<String> node1 = new QueueNode<>("Node1");
        QueueNode<String> node2 = new QueueNode<>("Node2");
        QueueNode<String> node3 = new QueueNode<>("Node3");

        node1.setNext(node2);
        node2.setNext(node3);

        // Изменяем значение node2 - не должно влиять на связь node1->node2
        node2.setValue("Modified Node2");

        assertEquals("Modified Node2", node1.getNext().getValue());
        assertEquals("Node3", node1.getNext().getNext().getValue());

        // Изменяем связь node2->node4, не должно влиять на node1->node2
        QueueNode<String> node4 = new QueueNode<>("Node4");
        node2.setNext(node4);
        assertEquals("Node4", node1.getNext().getNext().getValue());
    }

    @Test
    @DisplayName("Тест циклических ссылок (кольцевая структура)")
    void testCyclicReferences() {
        QueueNode<String> nodeA = new QueueNode<>("A");
        QueueNode<String> nodeB = new QueueNode<>("B");
        QueueNode<String> nodeC = new QueueNode<>("C");

        // Создаем цикл: A -> B -> C -> A
        nodeA.setNext(nodeB);
        nodeB.setNext(nodeC);
        nodeC.setNext(nodeA);

        // Должно работать без StackOverflow
        assertEquals("B", nodeA.getNext().getValue());
        assertEquals("C", nodeA.getNext().getNext().getValue());
        assertEquals("A", nodeA.getNext().getNext().getNext().getValue());
        assertEquals("B", nodeA.getNext().getNext().getNext().getNext().getValue());
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
        }

        Person person1 = new Person("Alice", 25);
        Person person2 = new Person("Bob", 30);
        Person person3 = new Person("Charlie", 35);

        QueueNode<Person> node1 = new QueueNode<>(person1);
        QueueNode<Person> node2 = new QueueNode<>(person2);
        QueueNode<Person> node3 = new QueueNode<>(person3);

        node1.setNext(node2);
        node2.setNext(node3);

        assertEquals(person1, node1.getValue());
        assertEquals(person2, node1.getNext().getValue());
        assertEquals(person3, node1.getNext().getNext().getValue());

        // Проверяем toString
        String result = node1.toString();
        assertTrue(result.contains("Alice(25)"));
        assertTrue(result.contains("Bob(30)"));
    }

    @Test
    @DisplayName("Тест наследования от trees.BaseNode")
    void testInheritanceFromBaseNode() {
        QueueNode<String> node = new QueueNode<>("Test");

        // Проверяем, что унаследованные методы работают
        assertEquals("Test", node.getValue());

        node.setValue("Modified");
        assertEquals("Modified", node.getValue());

        // Проверяем, что это действительно util.QueueNode
        assertTrue(node instanceof QueueNode);
        assertTrue(node instanceof BaseNode);
    }

    @Test
    @DisplayName("Тест замены существующего следующего узла")
    void testReplaceExistingNext() {
        QueueNode<String> firstNext = new QueueNode<>("First Next");
        QueueNode<String> secondNext = new QueueNode<>("Second Next");

        firstNode.setNext(firstNext);
        assertEquals("First Next", firstNode.getNext().getValue());

        firstNode.setNext(secondNext);
        assertEquals("Second Next", firstNode.getNext().getValue());
    }

    @Test
    @DisplayName("Тест с очень длинной цепочкой узлов")
    void testLongChain() {
        QueueNode<Integer> current = new QueueNode<>(0);
        QueueNode<Integer> head = current;

        // Создаем цепочку из 1000 узлов
        for (int i = 1; i <= 1000; i++) {
            QueueNode<Integer> newNode = new QueueNode<>(i);
            current.setNext(newNode);
            current = newNode;
        }

        // Проверяем, что можем пройти по всей цепочке
        current = head;
        int count = 0;
        while (current != null && count < 10000) { // защита от бесконечного цикла
            assertEquals(count, current.getValue());
            current = current.getNext();
            count++;
        }

        assertEquals(1001, count); // 0-1000 включительно
    }
}