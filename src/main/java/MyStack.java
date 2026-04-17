/**
 * Класс представляет реализацию стека (LIFO - Last In First Out)
 * с использованием связного списка на основе StackNode.
 *
 * @param <T> тип элементов в стеке
 */
public class MyStack<T> {
    private StackNode<T> top; // ссылка на верхний элемент стека
    private int size;         // количество элементов в стеке

    /**
     * Конструктор по умолчанию.
     * Создает пустой стек.
     */
    public MyStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Добавляет элемент на вершину стека.
     * @param element элемент для добавления в стек
     */
    public void push(T element) {
        StackNode<T> newNode = new StackNode<>(element);
        newNode.setNext(top); // новый элемент указывает на предыдущий верхний
        top = newNode;        // обновляем вершину стека
        size++;
    }

    /**
     * Удаляет и возвращает элемент с вершины стека.
     *
     * @return элемент с вершины стека или null, если стек пуст
     */
    public T pop() {
        if (isEmpty()) {
            return null;
        }

        T data = top.getValue();
        top = top.getNext(); // перемещаем вершину на следующий элемент
        size--;
        return data;
    }

    /**
     * Возвращает элемент с вершины стека без его удаления.
     *
     * @return элемент с вершины стека или null, если стек пуст
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return top.getValue();
    }

    /**
     * Проверяет, является ли стек пустым.
     *
     * @return true если стек пуст, false в противном случае
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Возвращает количество элементов в стеке.
     *
     * @return количество элементов в стеке
     */
    public int size() {
        return size;
    }

    /**
     * Очищает стек, удаляя все элементы.
     */
    public void clear() {
        top = null;
        size = 0;
    }

    /**
     * Возвращает строковое представление стека.
     *
     * @return строковое представление стека в формате "Stack: [элемент1, элемент2, ...]"
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Stack: []";
        }

        StringBuilder sb = new StringBuilder("Stack: [");
        StackNode<T> current = top;
        while (current != null) {
            sb.append(current.getValue());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}