import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса StackNode
 */
class StackNodeTest {

    private StackNode<String> topNode;
    private StackNode<Integer> numericNode;

    @BeforeEach
    void setUp() {
        topNode = new StackNode<>("Top");
        numericNode = new StackNode<>(42);
    }

    @Test
    @DisplayName("Создание StackNode с корректным значением")
    void testStackNodeCreationWithValidValue() {
        assertNotNull(topNode);
        assertEquals("Top", topNode.getValue());

        assertNotNull(numericNode);
        assertEquals(42, numericNode.getValue());
    }

    @Test
    @DisplayName("Создание StackNode с null значением должно выбрасывать исключение")
    void testStackNodeCreationWithNullValueThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new StackNode<String>(null)
        );

        assertEquals("Значение узла не может быть пустым", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Параметризованный тест: создание StackNode с null значением")
    void testStackNodeCreationWithNullValueParameterized(String nullValue) {
        assertThrows(IllegalArgumentException.class,
                () -> new StackNode<String>(nullValue));
    }

    @Test
    @DisplayName("Изначально следующий узел должен быть null")
    void testInitialNextIsNull() {
        assertNull(topNode.getNext());
        assertNull(numericNode.getNext());
    }

    @Test
    @DisplayName("Установка и получение следующего узла")
    void testSetAndGetNext() {
        StackNode<String> secondNode = new StackNode<>("Second");
        topNode.setNext(secondNode);

        assertNotNull(topNode.getNext());
        assertEquals("Second", topNode.getNext().getValue());
        assertEquals(secondNode, topNode.getNext());
    }

    @Test
    @DisplayName("Установка null следующего узла")
    void testSetNextToNull() {
        StackNode<String> secondNode = new StackNode<>("Second");
        topNode.setNext(secondNode);

        // Убедимся, что следующий узел установлен
        assertNotNull(topNode.getNext());

        // Установим null
        topNode.setNext(null);
        assertNull(topNode.getNext());
    }

    @Test
    @DisplayName("Создание стека из нескольких узлов")
    void testMultiNodeStack() {
        // Создаем стек: Top -> Second -> Third
        StackNode<String> secondNode = new StackNode<>("Second");
        StackNode<String> thirdNode = new StackNode<>("Third");

        topNode.setNext(secondNode);
        secondNode.setNext(thirdNode);

        // Проверяем структуру стека
        assertEquals("Second", topNode.getNext().getValue());
        assertEquals("Third", topNode.getNext().getNext().getValue());
        assertNull(topNode.getNext().getNext().getNext());
    }

    @Test
    @DisplayName("Тест метода toString с null следующим узлом")
    void testToStringWithNullNext() {
        String result = topNode.toString();

        assertTrue(result.contains("StackNode"));
        assertTrue(result.contains("значение: = Top"));
        assertTrue(result.contains("следующий: = null"));
    }

    @Test
    @DisplayName("Тест метода toString с установленным следующим узлом")
    void testToStringWithNext() {
        StackNode<String> secondNode = new StackNode<>("Second");
        topNode.setNext(secondNode);

        String result = topNode.toString();

        assertTrue(result.contains("StackNode"));
        assertTrue(result.contains("значение: = Top"));
        assertTrue(result.contains("следующий: = Second"));
    }

    @Test
    @DisplayName("Тест метода toString с числовыми значениями")
    void testToStringWithNumericValues() {
        StackNode<Integer> secondNode = new StackNode<>(100);
        numericNode.setNext(secondNode);

        String result = numericNode.toString();

        assertTrue(result.contains("StackNode"));
        assertTrue(result.contains("значение: = 42"));
        assertTrue(result.contains("следующий: = 100"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "A", "Hello World", "Special@Chars!#$"})
    @DisplayName("Параметризованный тест с различными строковыми значениями")
    void testVariousStringValues(String value) {
        StackNode<String> node = new StackNode<>(value);
        assertEquals(value, node.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 999, Integer.MAX_VALUE, Integer.MIN_VALUE})
    @DisplayName("Параметризованный тест с различными целочисленными значениями")
    void testVariousIntegerValues(int value) {
        StackNode<Integer> node = new StackNode<>(value);
        assertEquals(value, node.getValue());
    }

    @Test
    @DisplayName("Тест независимости узлов - изменение одного узла не влияет на другие")
    void testNodeIndependence() {
        StackNode<String> node1 = new StackNode<>("Node1");
        StackNode<String> node2 = new StackNode<>("Node2");
        StackNode<String> node3 = new StackNode<>("Node3");

        node1.setNext(node2);
        node2.setNext(node3);

        // Изменяем значение node2 - не должно влиять на связь node1->node2
        node2.setValue("Modified Node2");

        assertEquals("Modified Node2", node1.getNext().getValue());
        assertEquals("Node3", node1.getNext().getNext().getValue());

        // Изменяем связь node2->node4, должно влиять на node1->node2->node4
        StackNode<String> node4 = new StackNode<>("Node4");
        node2.setNext(node4);
        assertEquals("Node4", node1.getNext().getNext().getValue());
    }

    @Test
    @DisplayName("Тест циклических ссылок (кольцевая структура)")
    void testCyclicReferences() {
        StackNode<String> nodeA = new StackNode<>("A");
        StackNode<String> nodeB = new StackNode<>("B");
        StackNode<String> nodeC = new StackNode<>("C");

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

        StackNode<Person> node1 = new StackNode<>(person1);
        StackNode<Person> node2 = new StackNode<>(person2);
        StackNode<Person> node3 = new StackNode<>(person3);

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
    @DisplayName("Тест наследования от BaseNode")
    void testInheritanceFromBaseNode() {
        StackNode<String> node = new StackNode<>("Test");

        // Проверяем, что унаследованные методы работают
        assertEquals("Test", node.getValue());

        node.setValue("Modified");
        assertEquals("Modified", node.getValue());

        // Проверяем, что это действительно StackNode
        assertTrue(node instanceof StackNode);
        assertTrue(node instanceof BaseNode);
    }

    @Test
    @DisplayName("Тест замены существующего следующего узла")
    void testReplaceExistingNext() {
        StackNode<String> firstNext = new StackNode<>("First Next");
        StackNode<String> secondNext = new StackNode<>("Second Next");

        topNode.setNext(firstNext);
        assertEquals("First Next", topNode.getNext().getValue());

        topNode.setNext(secondNext);
        assertEquals("Second Next", topNode.getNext().getValue());
    }

    @Test
    @DisplayName("Тест с очень глубоким стеком")
    void testDeepStack() {
        StackNode<Integer> current = new StackNode<>(0);
        StackNode<Integer> top = current;

        // Создаем стек из 1000 узлов
        for (int i = 1; i <= 1000; i++) {
            StackNode<Integer> newNode = new StackNode<>(i);
            newNode.setNext(top);
            top = newNode;
        }

        // Проверяем, что можем пройти по всему стеку
        current = top;
        int count = 0;
        while (current != null && count < 10000) { // защита от бесконечного цикла
            current = current.getNext();
            count++;
        }

        assertEquals(1001, count); // 1000 + начальный узел
    }
}