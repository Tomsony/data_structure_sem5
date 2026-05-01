package util;

/**
 * Реализация минимальной кучи (Min-Heap) на основе массива.
 * Свойства кучи:
 *
 *     Родитель всегда меньше или равен потомкам
 *     Полное бинарное дерево (все уровни заполнены, кроме, возможно, последнего)
 *     Для min-heap: в корне самый минимальный элемент, идёт по возрастанию вглубь

 * Сложность операций:
 *   insert (добавление) — O(log n)
 *   extractMin (извлечение минимума) — O(log n)
 *   peekMin (просмотр минимума) — O(1)
 *   size, isEmpty — O(1)
 *
 * @param <T> тип элементов, должен реализовывать Comparable
 */
public class MyHeap<T extends Comparable<T>> {

    /** Начальная ёмкость кучи */
    private static final int INITIAL_CAPACITY = 10;

    /** Массив для хранения элементов */
    private T[] heap;

    /** Текущее количество элементов в куче */
    private int size;

    /**
     * Создаёт пустую кучу с начальной ёмкостью.
     */
    @SuppressWarnings("unchecked")
    public MyHeap() {
        this.heap = (T[]) new Comparable[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Создаёт кучу из массива элементов (heapify).
     * Сложность: O(n).
     *
     * @param elements массив элементов для построения кучи
     */
    @SuppressWarnings("unchecked")
    public MyHeap(T[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Массив не может быть null");
        }
        this.heap = (T[]) new Comparable[elements.length + 1];
        this.size = elements.length;

        // Копируем элементы, начиная с индекса 1 (для удобства вычислений потомков)
        System.arraycopy(elements, 0, this.heap, 1, elements.length);

        // Восстанавливаем свойство кучи (heapify)
        for (int i = size / 2; i >= 1; i--) {
            sink(i);
        }
    }

    // ================= ОСНОВНЫЕ ОПЕРАЦИИ =================

    /**
     * Добавляет элемент в кучу.
     * <p>
     * Алгоритм:
     * <ol>
     *   <li>Добавляем элемент в конец массива (последний лист)</li>
     *   <li>"Всплываем" (sift up) — поднимаем элемент вверх,
     *       пока не восстановится свойство кучи</li>
     * </ol>
     * Сложность: O(log n)
     *
     * @param value добавляемое значение (не null)
     * @throws IllegalArgumentException если value == null
     */
    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Значение не может быть null");
        }

        // Увеличиваем массив, если нужно
        if (size + 1 >= heap.length) {
            resize(heap.length * 2);
        }

        // Добавляем элемент в конец
        heap[++size] = value;

        // Всплываем вверх для восстановления свойства кучи
        swim(size);
    }

    /**
     * Извлекает и удаляет минимальный элемент (корень кучи).
     * <p>
     * Алгоритм:
     * <ol>
     *   <li>Сохраняем корень (минимальный элемент)</li>
     *   <li>Перемещаем последний элемент на место корня</li>
     *   <li>"Тонем" (sift down) — опускаем элемент вниз,
     *       пока не восстановится свойство кучи</li>
     * </ol>
     * Сложность: O(log n)
     *
     * @return минимальный элемент или null, если куча пуста
     */
    public T extractMin() {
        if (isEmpty()) {
            return null;
        }

        T min = heap[1];              // Минимальный элемент — корень (индекс 1)
        heap[1] = heap[size];         // Перемещаем последний элемент в корень
        heap[size] = null;            // Очищаем ссылку для GC
        size--;

        sink(1);                      // Опускаем новый корень вниз

        // Уменьшаем массив, если элементов стало мало
        if (size > 0 && size < heap.length / 4) {
            resize(heap.length / 2);
        }

        return min;
    }

    /**
     * Возвращает минимальный элемент без удаления.
     * Сложность: O(1)
     *
     * @return минимальный элемент или null, если куча пуста
     */
    public T peekMin() {
        if (isEmpty()) {
            return null;
        }
        return heap[1];
    }

    // ================= ВСПОМОГАТЕЛЬНЫЕ ОПЕРАЦИИ =================

    /**
     * Возвращает количество элементов в куче.
     *
     * @return размер кучи
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли куча.
     *
     * @return true, если куча пуста
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает кучу, удаляя все элементы.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        for (int i = 1; i <= size; i++) {
            heap[i] = null;     // Помогаем Garbage Collector
        }
        size = 0;
    }

    // ================= ВНУТРЕННИЕ МЕТОДЫ =================

    /**
     * "Всплытие" элемента вверх по куче (sift up).
     * Поднимает элемент, пока он меньше родителя.
     *
     * @param k индекс элемента для всплытия
     */
    private void swim(int k) {
        // Пока не достигли корня и родитель больше текущего элемента
        while (k > 1 && greater(parent(k), k)) {
            swap(k, parent(k));   // Меняем местами с родителем
            k = parent(k);        // Поднимаемся на уровень выше
        }
    }

    /**
     * "Погружение" элемента вниз по куче (sift down).
     * Опускает элемент, пока он больше одного из потомков.
     *
     * @param k индекс элемента для погружения
     */
    private void sink(int k) {
        while (leftChild(k) <= size) {   // Пока есть хотя бы левый потомок
            int smallerChild = leftChild(k);

            // Если правый потомок существует и меньше левого — выбираем его
            if (rightChild(k) <= size && greater(smallerChild, rightChild(k))) {
                smallerChild = rightChild(k);
            }

            // Если текущий элемент меньше или равен меньшему потомку — свойство кучи восстановлено
            if (!greater(k, smallerChild)) {
                break;
            }

            swap(k, smallerChild);   // Меняем с меньшим потомком
            k = smallerChild;        // Опускаемся на уровень ниже
        }
    }

    /**
     * Сравнивает два элемента по индексам.
     *
     * @param i индекс первого элемента
     * @param j индекс второго элемента
     * @return true, если heap[i] > heap[j]
     */
    private boolean greater(int i, int j) {
        return heap[i].compareTo(heap[j]) > 0;
    }

    /**
     * Меняет местами два элемента в массиве.
     *
     * @param i индекс первого элемента
     * @param j индекс второго элемента
     */
    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // ================= ИНДЕКСНАЯ АРИФМЕТИКА =================

    /**
     * Индекс родителя для узла с индексом k.
     * Для кучи на базе массива с 1-индексацией:
     * parent(k) = k / 2
     */
    private int parent(int k) {
        return k / 2;
    }

    /**
     * Индекс левого потомка для узла с индексом k.
     * leftChild(k) = 2 * k
     */
    private int leftChild(int k) {
        return 2 * k;
    }

    /**
     * Индекс правого потомка для узла с индексом k.
     * rightChild(k) = 2 * k + 1
     */
    private int rightChild(int k) {
        return 2 * k + 1;
    }

    // ================= УПРАВЛЕНИЕ ПАМЯТЬЮ =================

    /**
     * Изменяет размер внутреннего массива.
     *
     * @param newCapacity новая ёмкость
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newHeap = (T[]) new Comparable[newCapacity];
        // Копируем элементы с учётом 1-индексации
        System.arraycopy(heap, 0, newHeap, 0, size + 1);
        heap = newHeap;
    }

    // ================= ВЫВОД =================

    /**
     * Возвращает строковое представление кучи (уровневый обход).
     *
     * @return строка с элементами кучи
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Heap[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Heap[");
        for (int i = 1; i <= size; i++) {
            sb.append(heap[i]);
            if (i < size) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}