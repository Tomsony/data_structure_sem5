package util;

/**
 * Класс представляет реализацию очереди (FIFO - First In First Out)
 * с использованием связного списка на основе util.QueueNode.
 *
 * @param <T> тип элементов в очереди
 */
public class MyQueue<T> {
    private QueueNode<T> front; // ссылка на первый элемент в очереди
    private QueueNode<T> rear;  // ссылка на последний элемент в очереди
    private int size;           // количество элементов в очереди

    /**
     * Конструктор по умолчанию.
     * Создает пустую очередь.
     */
    public MyQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Добавляет элемент в конец очереди.
     *
     * @param element элемент для добавления в очередь
     * @throws IllegalArgumentException если элемент равен null
     */
    public void add(T element) {
        QueueNode<T> newNode = new QueueNode<>(element);

        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
        size++;
    }

    /**
     * Возвращает первый элемент очереди без его удаления.
     *
     * @return первый элемент очереди или null, если очередь пуста
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return front.getValue();
    }

    /**
     * Удаляет и возвращает первый элемент очереди.
     *
     * @return первый элемент очереди или null, если очередь пуста
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T data = front.getValue();
        front = front.getNext();

        if (front == null) {
            rear = null;
        }

        size--;
        return data;
    }

    /**
     * Проверяет, является ли очередь пустой.
     *
     * @return true если очередь пуста, false в противном случае
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Возвращает количество элементов в очереди.
     *
     * @return количество элементов в очереди
     */
    public int size() {
        return size;
    }

    /**
     * Очищает очередь, удаляя все элементы.
     */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Возвращает строковое представление очереди.
     *
     * @return строковое представление очереди в формате "Queue: [1, 2, 3, ...]"
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Queue: []";
        }

        StringBuilder sb = new StringBuilder("Queue: [");
        QueueNode<T> current = front;
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