import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;
import trees.BaseNode;
import trees.TreeNode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса trees.TreeNode
 */
class TreeNodeTest {

    private TreeNode<String> rootNode;
    private TreeNode<Integer> numericNode;

    @BeforeEach
    void setUp() {
        rootNode = new TreeNode<>("Root");
        numericNode = new TreeNode<>(100);
    }

    @Test
    @DisplayName("Создание trees.TreeNode с корректным значением")
    void testTreeNodeCreationWithValidValue() {
        assertNotNull(rootNode);
        assertEquals("Root", rootNode.getValue());

        assertNotNull(numericNode);
        assertEquals(100, numericNode.getValue());
    }

    @Test
    @DisplayName("Создание trees.TreeNode с null значением должно выбрасывать исключение")
    void testTreeNodeCreationWithNullValueThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TreeNode<String>(null)
        );

        assertEquals("Значение узла не может быть пустым", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Параметризованный тест: создание trees.TreeNode с null значением")
    void testTreeNodeCreationWithNullValueParameterized(String nullValue) {
        assertThrows(IllegalArgumentException.class,
                () -> new TreeNode<String>(nullValue));
    }

    @Test
    @DisplayName("Изначально левый и правый потомки должны быть null")
    void testInitialChildrenAreNull() {
        assertNull(rootNode.getLeftChild());
        assertNull(rootNode.getRightChild());

        assertNull(numericNode.getLeftChild());
        assertNull(numericNode.getRightChild());
    }

    @Test
    @DisplayName("Установка и получение левого потомка")
    void testSetAndGetLeftChild() {
        TreeNode<String> leftChild = new TreeNode<>("Left Child");
        rootNode.setLeftChild(leftChild);

        assertNotNull(rootNode.getLeftChild());
        assertEquals("Left Child", rootNode.getLeftChild().getValue());
        assertEquals(leftChild, rootNode.getLeftChild());
    }

    @Test
    @DisplayName("Установка и получение правого потомка")
    void testSetAndGetRightChild() {
        TreeNode<String> rightChild = new TreeNode<>("Right Child");
        rootNode.setRightChild(rightChild);

        assertNotNull(rootNode.getRightChild());
        assertEquals("Right Child", rootNode.getRightChild().getValue());
        assertEquals(rightChild, rootNode.getRightChild());
    }

    @Test
    @DisplayName("Установка null левого потомка")
    void testSetLeftChildToNull() {
        TreeNode<String> leftChild = new TreeNode<>("Left Child");
        rootNode.setLeftChild(leftChild);

        // Убедимся, что потомок установлен
        assertNotNull(rootNode.getLeftChild());

        // Установим null
        rootNode.setLeftChild(null);
        assertNull(rootNode.getLeftChild());
    }

    @Test
    @DisplayName("Установка null правого потомка")
    void testSetRightChildToNull() {
        TreeNode<String> rightChild = new TreeNode<>("Right Child");
        rootNode.setRightChild(rightChild);

        // Убедимся, что потомок установлен
        assertNotNull(rootNode.getRightChild());

        // Установим null
        rootNode.setRightChild(null);
        assertNull(rootNode.getRightChild());
    }

    @Test
    @DisplayName("Создание бинарного дерева с несколькими уровнями")
    void testMultiLevelBinaryTree() {
        // Создаем дерево:
        //      Root
        //     /    \
        //   Left   Right
        //   /
        // LeftLeft

        TreeNode<String> leftChild = new TreeNode<>("Left");
        TreeNode<String> rightChild = new TreeNode<>("Right");
        TreeNode<String> leftLeftChild = new TreeNode<>("LeftLeft");

        rootNode.setLeftChild(leftChild);
        rootNode.setRightChild(rightChild);
        leftChild.setLeftChild(leftLeftChild);

        // Проверяем структуру
        assertEquals("Left", rootNode.getLeftChild().getValue());
        assertEquals("Right", rootNode.getRightChild().getValue());
        assertEquals("LeftLeft", rootNode.getLeftChild().getLeftChild().getValue());
        assertNull(rootNode.getLeftChild().getRightChild());
    }

    @Test
    @DisplayName("Тест метода toString с null потомками")
    void testToStringWithNullChildren() {
        String result = rootNode.toString();

        assertTrue(result.contains("trees.TreeNode"));
        assertTrue(result.contains("key-value: = Root"));
        assertTrue(result.contains("left child: = null"));
        assertTrue(result.contains("right child: = null"));
    }

    @Test
    @DisplayName("Тест метода toString с установленными потомками")
    void testToStringWithChildren() {
        TreeNode<String> leftChild = new TreeNode<>("Left");
        TreeNode<String> rightChild = new TreeNode<>("Right");

        rootNode.setLeftChild(leftChild);
        rootNode.setRightChild(rightChild);

        String result = rootNode.toString();

        assertTrue(result.contains("trees.TreeNode"));
        assertTrue(result.contains("key-value: = Root"));
        assertTrue(result.contains("left child: = Left"));
        assertTrue(result.contains("right child: = Right"));
    }

    @Test
    @DisplayName("Тест метода toString с числовыми значениями")
    void testToStringWithNumericValues() {
        TreeNode<Integer> leftChild = new TreeNode<>(50);
        TreeNode<Integer> rightChild = new TreeNode<>(150);

        numericNode.setLeftChild(leftChild);
        numericNode.setRightChild(rightChild);

        String result = numericNode.toString();

        assertTrue(result.contains("trees.TreeNode"));
        assertTrue(result.contains("key-value: = 100"));
        assertTrue(result.contains("left child: = 50"));
        assertTrue(result.contains("right child: = 150"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "A", "Hello World", "Special@Chars!#$"})
    @DisplayName("Параметризованный тест с различными строковыми значениями")
    void testVariousStringValues(String value) {
        TreeNode<String> node = new TreeNode<>(value);
        assertEquals(value, node.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 999, Integer.MAX_VALUE, Integer.MIN_VALUE})
    @DisplayName("Параметризованный тест с различными целочисленными значениями")
    void testVariousIntegerValues(int value) {
        TreeNode<Integer> node = new TreeNode<>(value);
        assertEquals(value, node.getValue());
    }

    @Test
    @DisplayName("Тест независимости узлов - изменение одного узла не влияет на другие")
    void testNodeIndependence() {
        TreeNode<String> node1 = new TreeNode<>("Node1");
        TreeNode<String> node2 = new TreeNode<>("Node2");
        TreeNode<String> node3 = new TreeNode<>("Node3");

        node1.setLeftChild(node2);
        node1.setRightChild(node3);

        // Изменяем значение node2 - не должно влиять на node1
        node2.setValue("Modified Node2");

        assertEquals("Modified Node2", node1.getLeftChild().getValue());
        assertEquals("Node3", node1.getRightChild().getValue());

        // Удаляем связь - node1 не должен меняться
        node2.setLeftChild(new TreeNode<>("New Child"));
        assertEquals("Modified Node2", node1.getLeftChild().getValue());
    }

    @Test
    @DisplayName("Тест циклических ссылок (должно работать корректно)")
    void testCyclicReferences() {
        TreeNode<String> nodeA = new TreeNode<>("A");
        TreeNode<String> nodeB = new TreeNode<>("B");
        TreeNode<String> nodeC = new TreeNode<>("C");

        // Создаем цикл: A -> B -> C -> A
        nodeA.setLeftChild(nodeB);
        nodeB.setLeftChild(nodeC);
        nodeC.setLeftChild(nodeA);

        // Должно работать без StackOverflow
        assertEquals("B", nodeA.getLeftChild().getValue());
        assertEquals("C", nodeA.getLeftChild().getLeftChild().getValue());
        assertEquals("A", nodeA.getLeftChild().getLeftChild().getLeftChild().getValue());
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

        Person parent = new Person("Parent", 40);
        Person leftChild = new Person("LeftChild", 15);
        Person rightChild = new Person("RightChild", 12);

        TreeNode<Person> parentNode = new TreeNode<>(parent);
        TreeNode<Person> leftNode = new TreeNode<>(leftChild);
        TreeNode<Person> rightNode = new TreeNode<>(rightChild);

        parentNode.setLeftChild(leftNode);
        parentNode.setRightChild(rightNode);

        assertEquals(parent, parentNode.getValue());
        assertEquals(leftChild, parentNode.getLeftChild().getValue());
        assertEquals(rightChild, parentNode.getRightChild().getValue());

        // Проверяем toString
        String result = parentNode.toString();
        assertTrue(result.contains("Parent(40)"));
        assertTrue(result.contains("LeftChild(15)"));
        assertTrue(result.contains("RightChild(12)"));
    }

    @Test
    @DisplayName("Тест наследования от trees.BaseNode")
    void testInheritanceFromBaseNode() {
        TreeNode<String> node = new TreeNode<>("Test");

        // Проверяем, что унаследованные методы работают
        assertEquals("Test", node.getValue());

        node.setValue("Modified");
        assertEquals("Modified", node.getValue());

        // Проверяем, что это действительно trees.TreeNode
        assertTrue(node instanceof TreeNode);
        assertTrue(node instanceof BaseNode);
    }
    /**
     * Дополнительные тесты для граничных случаев trees.TreeNode
     */
    @Test
    @DisplayName("Тест с очень большим деревом (глубина)")
    void testDeepTreeStructure() {
        TreeNode<Integer> root = new TreeNode<>(0);
        TreeNode<Integer> current = root;

        // Создаем цепочку из 100 узлов
        for (int i = 1; i <= 100; i++) {
            TreeNode<Integer> newNode = new TreeNode<>(i);
            current.setLeftChild(newNode);
            current = newNode;
        }

        // Проверяем, что можем пройти по всей цепочке
        current = root;
            int count = 0;
            while (current != null && count < 1000) { // защита от бесконечного цикла
                current = current.getLeftChild();
                count++;
            }

            assertEquals(101, count);
        }

        @Test
        @DisplayName("Тест замены существующего потомка")
        void testReplaceExistingChild() {
            TreeNode<String> root = new TreeNode<>("Root");
            TreeNode<String> firstLeft = new TreeNode<>("First Left");
            TreeNode<String> secondLeft = new TreeNode<>("Second Left");

            root.setLeftChild(firstLeft);
            assertEquals("First Left", root.getLeftChild().getValue());

            root.setLeftChild(secondLeft);
            assertEquals("Second Left", root.getLeftChild().getValue());
        }

        @Test
        @DisplayName("Тест с одинаковыми значениями в разных узлах")
        void testSameValuesInDifferentNodes() {
            TreeNode<String> node1 = new TreeNode<>("Same");
            TreeNode<String> node2 = new TreeNode<>("Same");
            TreeNode<String> node3 = new TreeNode<>("Same");

            node1.setLeftChild(node2);
            node1.setRightChild(node3);

            assertEquals("Same", node1.getLeftChild().getValue());
            assertEquals("Same", node1.getRightChild().getValue());

            // Узлы разные, но значения одинаковые
            assertNotSame(node1.getLeftChild(), node1.getRightChild());
            assertEquals(node1.getLeftChild().getValue(), node1.getRightChild().getValue());
        }
}