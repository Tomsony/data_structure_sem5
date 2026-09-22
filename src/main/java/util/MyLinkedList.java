package util;

import trees.BaseNode;

import java.util.Collection;    // Для конструктора и addAll
import java.util.ConcurrentModificationException;   // Для fail-fast итераторов
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация двусвязного списка на основе базового узла {@link BaseNode}.
 * Поддерживает: добавление/удаление в начало/конец/по индексу, поиск,
 * обход в прямом и обратном порядке, fail-fast итераторы.
 *
 * ВАЖНО: список НЕ поддерживает null-элементы (по дизайну).
 *
 * @param <T> тип хранимых значений
 * @author Артём Томских, ИВТ-23
 */
public class MyLinkedList<T> implements Iterable<T> {

    /**
     * Конкретный класс узла для двусвязного списка.
     * Расширяет BaseNode, добавляя ссылки на соседние узлы.
     */
    private static class LinkedNode<T> extends BaseNode<T> {
        LinkedNode<T> next; // ссылка на следующий узел
        LinkedNode<T> prev; // ссылка на предыдущий узел

        LinkedNode(T value) {
            super(value);
            this.next = null;
            this.prev = null;
        }

        LinkedNode(T value, LinkedNode<T> prev, LinkedNode<T> next) {
            super(value);
            this.prev = prev;
            this.next = next;
        }
    }

    // ------------------- Поля списка -------------------
    private LinkedNode<T> head;  // первый элемент списка
    private LinkedNode<T> tail;  // последний элемент списка
    private int size;            // количество элементов
    private int modCount;        // счётчик изменений (для fail-fast итераторов)

    // ------------------- Конструкторы -------------------

    /** Создаёт пустой список. */
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    /**
     * Создаёт список, содержащий элементы указанной коллекции.
     *
     * @throws NullPointerException если коллекция null
     * @throws  IllegalArgumentException если среди элементов есть null
     */
    public MyLinkedList(Collection<? extends T> collection) {
        this();
        addAll(collection);
    }

    // ------------------- Добавление -------------------

    /** Добавляет элемент в конец списка.
     * Сложность O(1)
     */
    public void add(T element) {
        addLast(element);
    }

    /** Добавляет элемент в начало списка. */
    public void addFirst(T element) {
        checkNotNull(element);
        LinkedNode<T> newNode = new LinkedNode<>(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
        modCount++;
    }

    /** Добавляет элемент в конец списка.
     * Сложность O(1)
     */
    public void addLast(T element) {
        checkNotNull(element);
        LinkedNode<T> newNode = new LinkedNode<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * Вставляет элемент на указанную позицию.
     *
     * @throws IndexOutOfBoundsException если index вне [0, size]
     * Сложность O(n)
     */
    public void add(int index, T element) {
        checkPositionIndex(index);
        checkNotNull(element);
        if (index == size) {
            addLast(element);
        } else if (index == 0) {
            addFirst(element);
        } else {
            // узел который занимает этот индекс
            LinkedNode<T> current = getNodeAtIndex(index);
            // новый узел, перед которым стоит current.prev, а после current
            LinkedNode<T> newNode = new LinkedNode<>(element, current.prev, current);
            // старый левый сосед указывает на новый узел
            current.prev.next = newNode;
            // правый сосед считает новый - своим предыдущим
            current.prev = newNode;
            size++;
            modCount++;
        }
    }

    /** Добавляет все элементы коллекции в конец списка. */
    public boolean addAll(Collection<? extends T> collection) {
        if (collection == null) throw new NullPointerException("Коллекция не может быть null");

        // Создаем временный массив нужного размера
        Object[] tempData = new Object[collection.size()];
        int index = 0;

        // Копируем элементы и проверяем на null
        for (T elem: collection){
            if (elem == null){
                throw new IllegalArgumentException("Элемент коллекции не может быть null");
            }
            tempData[index++] = elem;
        }

        // Если коллекция пустая ничего не меняем
        if (tempData.length == 0 ){
            return false;
        }

        // Безопасно добавляем элементы в структуру списка
        // Благодаря проверке - отсутствие ConcurrentModificationException
        for (Object elem : tempData){
            @SuppressWarnings("unchecked")
            T castedElem = (T) elem;
            addLast(castedElem);
        }
        return true;
    }

    // ------------------- Получение -------------------

    /** @throws NoSuchElementException если список пуст */
    public T getFirst() {
        if (head == null) throw new NoSuchElementException("Список пуст");
        return head.getValue();
    }

    /** @throws NoSuchElementException если список пуст */
    public T getLast() {
        if (tail == null) throw new NoSuchElementException("Список пуст");
        return tail.getValue();
    }

    /** @throws IndexOutOfBoundsException если index вне [0, size) */
    public T get(int index) {
        return getNodeAtIndex(index).getValue();
    }

    /** Заменяет элемент по индексу. @return старое значение */
    public T set(int index, T element) {
        checkNotNull(element);
        LinkedNode<T> node = getNodeAtIndex(index);
        T oldValue = node.getValue();
        node.setValue(element);
        return oldValue;
    }

    // ------------------- Удаление -------------------

    /** @throws NoSuchElementException если список пуст */
    public T removeFirst() {
        if (head == null) throw new NoSuchElementException("Список пуст");
        T removedValue = head.getValue();
        head = head.next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        modCount++;
        return removedValue;
    }

    /** @throws NoSuchElementException если список пуст */
    public T removeLast() {
        if (tail == null) throw new NoSuchElementException("Список пуст");
        T removedValue = tail.getValue();
        tail = tail.prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        modCount++;
        return removedValue;
    }

    /** Удаляет элемент по индексу. @return удалённый элемент */
    public T remove(int index) {
        checkElementIndex(index);
        LinkedNode<T> node = getNodeAtIndex(index);
        T removedValue = node.getValue();
        unlinkNode(node);
        return removedValue;
    }

    /**
     * Удаляет элемент по значению.
     *
     * @return true, если элемент был найден и удалён
     */
    public boolean remove(Object element) {
        for (LinkedNode<T> cur = head; cur != null; cur = cur.next) {
            if (element == null ? cur.getValue() == null : element.equals(cur.getValue())) {
                unlinkNode(cur);
                return true;
            }
        }
        return false;
    }

    /** Удаляет все элементы из списка. */
    public void clear() {
        // Помогаем GC: разрываем все связи
        LinkedNode<T> current = head;
        while (current != null) {
            LinkedNode<T> next = current.next;
            current.prev = null;
            current.next = null;
            current = next;
        }
        head = tail = null;
        size = 0;
        modCount++;
    }

    // ------------------- Поиск -------------------

    /** Индекс первого вхождения (или -1). */
    public int indexOf(Object element) {
        int index = 0;
        for (LinkedNode<T> cur = head; cur != null; cur = cur.next) {
            if (element == null ? cur.getValue() == null : element.equals(cur.getValue())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /** Индекс последнего вхождения (или -1). */
    public int lastIndexOf(Object element) {
        int index = size - 1;
        for (LinkedNode<T> cur = tail; cur != null; cur = cur.prev) {
            if (element == null ? cur.getValue() == null : element.equals(cur.getValue())) {
                return index;
            }
            index--;
        }
        return -1;
    }

    public boolean contains(Object element) {
        return indexOf(element) != -1;
    }

    // ------------------- Размер / состояние -------------------

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** @return новый массив со всеми элементами в порядке списка */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] result = (T[]) new Object[size];
        int i = 0;
        for (LinkedNode<T> cur = head; cur != null; cur = cur.next) {
            result[i++] = cur.getValue();
        }
        return result;
    }

    // ------------------- ИТЕРАТОРЫ -------------------

    /**
     * Прямой итератор (от head к tail).
     * Реализует fail-fast через сверку modCount.
     * @return возвращает указатель для обхода списка
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    /**
     * Обратный итератор (от tail к head).
     * Также поддерживает fail-fast.
     */
    public Iterator<T> descendingIterator() {
        return new DescendingIterator();
    }

    /**
     * Именованный вложенный итератор прямого обхода.
     * Не static — имеет доступ к head, tail, size, modCount.
     *
     * Fail-fast: если после создания итератора список структурно
     * изменился (add / remove / clear), то и hasNext(), и next()
     * бросят ConcurrentModificationException.
     */
    private class LinkedListIterator implements Iterator<T> {
        // Узел, на котором итератор стоит в данный момент.
        // Для прямого обхода это голова (head), для обратного — хвост (tail).
        private LinkedNode<T> current = head;
        // Ссылка на узел, который мы только что вернули пользователю методом next()
        private LinkedNode<T> lastReturned = null;
        // счетчик изменений списка на момент своего создания.
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            checkForComodification();
            return current != null;
        }

        @Override
        public T next() {
            checkForComodification();
            if (current == null) {
                throw new NoSuchElementException();
            }
            lastReturned = current;
            T value = current.getValue();
            current = current.next;
            return value;
        }

        /**
         * Удаляет элемент, возвращённый последним next().
         *
         * @throws IllegalStateException если next() ещё не вызывался
         *                               или элемент уже удалён
         */
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            checkForComodification();
            unlinkNode(lastReturned);
            expectedModCount = modCount;
            lastReturned = null;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Именованный вложенный итератор обратного обхода.
     * Также fail-fast.
     */
    private class DescendingIterator implements Iterator<T> {
        private LinkedNode<T> current = tail;
        private LinkedNode<T> lastReturned = null;
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            checkForComodification(expectedModCount);
            return current != null;
        }

        @Override
        public T next() {
            checkForComodification(expectedModCount);
            if (current == null) {
                throw new NoSuchElementException();
            }
            lastReturned = current;
            T value = current.getValue();
            current = current.prev;
            return value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            checkForComodification(expectedModCount);
            unlinkNode(lastReturned);
            expectedModCount = modCount;
            lastReturned = null;
        }
    }

    // ------------------- Приватные хелперы -------------------

    private void checkForComodification(int expected) {
        if (modCount != expected) {
            throw new ConcurrentModificationException();
        }
    }

    private void checkNotNull(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Список не поддерживает null элементы");
        }
    }

    private void checkPositionIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Возвращает узел по индексу, оптимизируя обход с ближнего конца.
     */
    private LinkedNode<T> getNodeAtIndex(int index) {
        checkElementIndex(index);
        if (index < size / 2) {
            LinkedNode<T> cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
            return cur;
        } else {
            LinkedNode<T> cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
            return cur;
        }
    }

    /**
     * Удаляет узел из списка, корректно перевязывая связи.
     * Используется и remove(int), и remove(Object), и итераторами.
     * Не вызывает removeFirst/removeLast, чтобы не дублировать логику.
     */
    private void unlinkNode(LinkedNode<T> node) {
        if (node.prev != null) {
            node.prev.next = node.next; // левый сосед соединяется с правым
        } else {
            head = node.next; // иначе узел - голова списка
        }
        if (node.next != null) {
            node.next.prev = node.prev; // правый сосед соединяется с левым
        } else {
            tail = node.prev; // иначе узел - хвост списка
        }
        // Разрываем связи самого узла — помогает GC
        node.prev = null;
        node.next = null;
        size--;
        modCount++;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (LinkedNode<T> cur = head; cur != null; cur = cur.next) {
            sb.append(cur.getValue());
            if (cur.next != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}