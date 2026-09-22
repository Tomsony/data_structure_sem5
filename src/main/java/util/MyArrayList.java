package util;

import Algorithm.Algorithm;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Класс реализует Динамический Массив со всеми стандартными операциями
 * и поддерживает цикл for-each за счёт реализации интерфейса {@link Iterable}.
 *
 * Благодаря этому объект MyArrayList можно передавать в методы класса {@link Algorithm}
 * (for_each, any_of, all_of, none_of, transform, copy_if).
 *
 * @param <T> тип элементов в массиве
 * @author Артём Томских, ИВТ-23
 */
public class MyArrayList<T> implements Iterable<T> {

    private static final int DEFAULT_CAPACITY = 10;   // Стандартная начальная ёмкость
    private int size;                                 // Текущее количество элементов в массиве

    /**
     * Хранит сами элементы массива.
     * В Java дженерики реализованы через type erasure (стирание типов).
     * Во время выполнения информация о T теряется, и всё становится Object.
     * Поэтому массив всегда Object[], а конкретный тип T восстанавливается
     * приведением в методах get()/next()/set().
     */
    private Object[] elements;

    /**
     * Счётчик структурных изменений (add / add(index) / remove / clear).
     * Нужен для fail-fast итератора: если во время обхода коллекция
     * структурно изменилась, следующая проверка бросит
     * {@link ConcurrentModificationException}.
     * Замена элемента через set() структурным изменением НЕ считается,
     * поэтому modCount там не увеличивается.
     */
    private int modCount;

    // ================== КОНСТРУКТОРЫ ==================

    /**
     * Конструктор по умолчанию.
     *
     * Используется механизм constructor chaining (цепочка конструкторов).
     * Этот подход позволяет:
     * 1) Избежать дублирования кода в обоих конструкторах
     * 2) Проверять валидацию в одном конструкторе с параметром
     * 3) Изменять логику конструктора в одном месте
     */
    public MyArrayList() {
        this(DEFAULT_CAPACITY);   // Вызов конструктора с параметром (DEFAULT_CAPACITY)
    }

    /**
     * Конструктор с параметром.
     *
     * @param initialCapacity начальная ёмкость
     * @throws IllegalArgumentException если initialCapacity < 0
     */
    public MyArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                    "Размер динамического массива не может быть меньше 0");
        }
        this.elements = new Object[initialCapacity];   // создаём массив нужного размера
        this.size = 0;                                 // массив пустой
    }

    // ================== БАЗОВЫЕ МЕТОДЫ ==================

    /**
     * Возвращает текущее количество элементов.
     *
     * @return логический размер массива
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пустой ли массив.
     *
     * @return true, если массив пустой; false, если в нём есть элементы
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает массив.
     * Все ссылки на объекты зануляются, чтобы сборщик мусора
     * мог освободить память, на которую больше нет ссылок.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
        modCount++;   // структурное изменение — итераторы должны «упасть»
    }

    // ================== ДОБАВЛЕНИЕ ЭЛЕМЕНТОВ ==================

    /**
     * Добавление элемента в конец массива.
     *
     * @param element добавляемый элемент
     */
    public void add(T element) {
        ensureCapacity(size + 1);   // Проверяем, есть ли место под новый элемент
        elements[size++] = element; // Добавляем элемент и увеличиваем логический размер
        modCount++;                 // структурное изменение
    }

    /**
     * Добавление элемента в массив по индексу.
     * Все элементы, начиная с позиции index, сдвигаются вправо на 1.
     *
     * @param index   индекс, куда вставить элемент
     * @param element добавляемый элемент
     * @throws IndexOutOfBoundsException если index вне [0, size]
     */
    public void add(int index, T element) {
        checkIndexForAdd(index);    // Проверка на допустимость индекса
        ensureCapacity(size + 1);   // Проверка наличия места в массиве
        moveElementsRight(index);   // Сдвиг элементов вправо

        elements[index] = element;  // Вставка элемента по индексу
        size++;                     // Увеличение размера на 1
        modCount++;                 // структурное изменение
    }

    // ================== ЧТЕНИЕ / ИЗМЕНЕНИЕ ==================

    /**
     * Получение элемента по его индексу.
     *
     * @SuppressWarnings("unchecked") — подавление предупреждения о непроверяемом
     * приведении. Безопасность гарантируется инкапсуляцией массива и контролем
     * всех операций записи. При правильном использовании дженериков
     * ClassCastException невозможен.
     *
     * @param index индекс элемента, который нужно получить
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если index вне [0, size)
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);              // Проверка наличия индекса в массиве
        return (T) elements[index];
    }

    /**
     * Установить по индексу элемента массива новое значение.
     *
     * Замена значения — это НЕ структурное изменение,
     * поэтому modCount не увеличивается: fail-fast итератор не должен падать.
     *
     * @param index   индекс элемента, значение которого нужно изменить
     * @param element новое значение
     * @return старое значение по этому индексу
     * @throws IndexOutOfBoundsException если index вне [0, size)
     */
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);                    // Проверка наличия индекса в массиве
        T oldValue = (T) elements[index];     // Сохраняем старое значение
        elements[index] = element;            // Заменяем на желаемое
        return oldValue;                      // Возврат старого значения для возможного использования
    }

    /**
     * Удаление элемента по индексу.
     * Все элементы после index сдвигаются влево на 1.
     *
     * @param index индекс удаляемого элемента
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если index вне [0, size)
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);                          // Проверка наличия индекса
        T removed = (T) elements[index];            // Запоминаем удаляемый элемент

        int moved = size - index - 1;               // Сколько элементов нужно сдвинуть влево
        if (moved > 0) {
            // Используем System.arraycopy — он быстрее ручного цикла
            System.arraycopy(elements, index + 1, elements, index, moved);
        }
        elements[--size] = null;                    // Зануляем последнюю позицию для GC
        modCount++;                                 // структурное изменение
        return removed;
    }

    /**
     * Удаление первого вхождения элемента (сравнение через equals).
     *
     * @param element удаляемый элемент
     * @return true, если элемент был найден и удалён; false — иначе
     */
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index < 0) {
            return false;   // элемент не найден
        }
        remove(index);
        return true;
    }

    /**
     * Поиск индекса первого вхождения элемента.
     *
     * @param element искомый элемент (может быть null)
     * @return индекс или -1, если элемент не найден
     */
    public int indexOf(T element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    /**
     * Проверяет наличие элемента в массиве.
     *
     * @param element искомый элемент
     * @return true, если элемент есть в массиве
     */
    public boolean contains(T element) {
        return indexOf(element) >= 0;
    }

    // ================== РЕАЛИЗАЦИЯ ИТЕРАТОРА ==================

    /**
     * Возвращает fail-fast итератор для перебора элементов массива.
     * Реализация интерфейса {@link Iterable} позволяет использовать
     * цикл for-each и передавать массив в алгоритмы из {@link Algorithm}.
     *
     * @return итератор по элементам массива
     */
    @Override
    public Iterator<T> iterator() {
        return new MyArrayListIterator();
    }

    /**
     * Именованный вложенный итератор.
     * Не static, поэтому имеет доступ к полям внешнего класса:
     * elements, size, modCount.
     *
     * Реализует fail-fast: если во время обхода вызвать add/remove/clear,
     * следующий вызов hasNext()/next() бросит
     * {@link ConcurrentModificationException}.
     */
    private class MyArrayListIterator implements Iterator<T> {

        private int currentIndex = 0;                       // текущая позиция итератора
        private int expectedModCount = modCount;            // ожидаемое значение modCount

        /**
         * Индекс последнего возвращённого элемента.
         * -1, если next() ещё не вызывался или после remove().
         * Нужен для корректной работы remove() в итераторе.
         */
        private int lastReturnedIndex = -1;

        @Override
        public boolean hasNext() {
            checkForComodification();
            return currentIndex < size;   // есть ли следующий элемент
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            checkForComodification();
            if (currentIndex >= size) {
                throw new NoSuchElementException("Элементов больше нет");
            }
            lastReturnedIndex = currentIndex;
            return (T) elements[currentIndex++];   // возвращаем текущий и сдвигаемся
        }

        /**
         * Удаляет элемент, который был возвращён последним вызовом next().
         *
         * @throws IllegalStateException если next() ещё не вызывался
         *                               или элемент уже удалён
         */
        @Override
        public void remove() {
            if (lastReturnedIndex < 0) {
                throw new IllegalStateException(
                        "next() не был вызван или элемент уже удалён");
            }
            checkForComodification();

            // Удаляем элемент через внешний класс
            MyArrayList.this.remove(lastReturnedIndex);

            // После удаления хвост массива сдвинулся влево на 1,
            // поэтому currentIndex должен «откатиться» на позицию удалённого,
            // иначе мы пропустим следующий элемент.
            currentIndex = lastReturnedIndex;
            lastReturnedIndex = -1;

            // Синхронизируем счётчик, потому что remove() внешнего класса уже
            // увеличил modCount.
            expectedModCount = modCount;
        }

        /**
         * Проверяет, не была ли коллекция структурно изменена
         * после создания итератора (или после последнего remove()).
         */
        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException(
                        "Коллекция изменена во время итерации");
            }
        }
    }

    // ================== ВНУТРЕННИЕ ХЕЛПЕРЫ ==================

    /**
     * Проверка наличия места в массиве под новый элемент.
     * Если места не хватает — массив расширяется в 1.5 раза (+1 для случая
     * нулевой начальной ёмкости).
     *
     * @param minCapacity минимально требуемая ёмкость (логический размер + 1)
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {                 // Проверяем, нужно ли расширять массив
            int newCapacity = elements.length * 3 / 2 + 1;   // Новый размер: ×1.5 + 1
            if (newCapacity < minCapacity) {                 // Страховка для случая length == 0
                newCapacity = minCapacity;
            }
            Object[] newArray = new Object[newCapacity];     // Создаём массив нового размера
            System.arraycopy(elements, 0, newArray, 0, size); // Копируем элементы
            elements = newArray;                             // Заменяем старый массив новым
        }
        // Если места хватает — ничего не делаем
    }

    /**
     * Сдвигает элементы массива вправо начиная с указанного индекса.
     * Освобождает место для вставки нового элемента.
     *
     * ВАЖНО: требует, чтобы перед вызовом был вызван ensureCapacity(size + 1),
     * иначе elements[size] выйдет за пределы массива.
     *
     * @param fromIndex индекс, с которого начинается сдвиг
     */
    private void moveElementsRight(int fromIndex) {
        // Начинаем с конца, чтобы не перезаписывать ещё не скопированные элементы
        for (int i = size; i > fromIndex; i--) {
            // Перезаписываем каждый элемент в правого соседа, освобождая место
            // для нашего fromIndex. На первой итерации последний элемент
            // уезжает в «свободный слот».
            elements[i] = elements[i - 1];
        }
    }

    /**
     * Проверка наличия индекса в массиве для чтения/изменения/удаления.
     *
     * @param index проверяемый индекс (допустимо [0, size))
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс: " + index + ", размер: " + size);
        }
    }

    /**
     * Проверка наличия индекса в массиве для добавления нового элемента.
     * Для add допустим index == size (вставка в конец).
     *
     * @param index проверяемый индекс (допустимо [0, size])
     */
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Индекс: " + index + ", размер: " + size);
        }
    }

    // ================== toString ==================

    /**
     * Возвращает строковое представление массива.
     * Удобно при отладке и в тестах {@link Algorithm}.
     *
     * @return строка вида {@code [a, b, c]}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }
}