package util;

import trees.BaseNode;

/**
 * Класс представляет собой реализацию узла для очереди/связного списка.
 * Наследуется от основного класса trees.BaseNode.
 */
public class QueueNode<T> extends BaseNode<T> {
    private QueueNode<T> next; // ссылка на следующий узел

    /**
     * Конструктор узла очереди
     *
     * @param value значение узла
     * @throws IllegalArgumentException если значение равно null
     */
    public QueueNode(T value) {
        super(value);   // передаем значение в родительский конструктор (суперкласс)
    }

    /**
     * Получить следующий узел
     *
     * @return следующий узел
     */
    public QueueNode<T> getNext() {
        return next;
    }

    /**
     * Установить следующий узел
     *
     * @param next следующий узел
     */
    public void setNext(QueueNode<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        String nextInfo = (next != null) ? next.getValue().toString() : "null";
        return "util.QueueNode{ " +
                "value: = " + getValue() +
                ", next: = " + nextInfo + " }";
    }
}