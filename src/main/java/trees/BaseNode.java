package trees;

/**
 * Базовый абстрактный класс для узлов
 * Содержит общую логику для всех типов узлов
 */
public abstract class BaseNode<T> {   // Абстрактный класс - это класс, который нельзя инстанцировать.
                                        // Он служит шаблоном для других классов.
    protected T value;      // Доступ в том же классе
                            // Доступ в классах-наследниках (в других пакетах)
                            // Доступ в других классах того же пакета

    /**
     * Конструктор базового узла
     * @param value значение узла
     * @throws IllegalArgumentException если значение равно null
     */
    public BaseNode(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Значение узла не может быть пустым");
        }
        this.value = value;
    }

    /**
     * Получение значения узла
     * @return значение узла
     */
    public T getValue() {
        return value;
    }

    /**
     * Установить значение узла
     * @param value новое значение узла
     */
    public void setValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Значение узла не может быть пустым");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return "Узел{ " + "ключ-значение: = " + value + " }";
    }
}