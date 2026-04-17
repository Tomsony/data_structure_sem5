/**
 * Класс представляет собой реализацию узла для стека.
 * Наследуется от основного класса BaseNode.
 */
public class StackNode<T> extends BaseNode<T> {
    private StackNode<T> next; // ссылка на следующий узел

    /**
     * Конструктор узла стека
     *
     * @param value значение узла
     * @throws IllegalArgumentException если значение равно null
     */
    public StackNode(T value) {
        super(value);   // передаем значение в родительский конструктор (суперкласс)
    }

    /**
     * Получить следующий узел
     * @return следующий узел
     */
    public StackNode<T> getNext() {
        return next;
    }

    /**
     * Установить следующий узел
     * @param next следующий узел
     */
    public void setNext(StackNode<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        String nextInfo = (next != null) ? next.getValue().toString() : "null";
        return "StackNode{ " +
                "значение: = " + getValue() +
                ", следующий: = " + nextInfo + " }";
    }
}