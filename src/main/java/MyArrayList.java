/**
 * Класс реализует Динамический Массив со всеми стандартными операциями
 *
 * @param <T> тип элементов в массиве
 */
public class MyArrayList<T> {
    private static final int DEFAULT_CAPACITY = 10;     // Cтандартная начальная емкость
    private int size;                                   // Текущее количество элементов в массиве

    private Object[] elements;       /** Хранит сами элементы массива
                                     * в Java дженерики реализованы через type erasure (стирание типов).
                                     * Во время выполнения информация о T теряется, и все становится Object.
                                     */

    /**
     * Конструктор по умолчанию
     *
     * В данном конструкторе используется механизм constructor chaining (цепочка конструкторов)
     * Этот подход позволяет:
     * 1) Избежать дублирования кода в обоих конструкторах
     * 2) Проверять валидацию в одном конструкторе с параметром
     * 3) Можно изменять логику конструктора в одном месте
     */
    public MyArrayList() {
        this(DEFAULT_CAPACITY);     // Вызов конструктора с параметром (DEFAULT_CAPACITY)
    }

    /**
     * Конструктор с параметром
     *
     * @param initialCapacity начальная ёмкость
     */
    public MyArrayList(int initialCapacity){
        if (initialCapacity < 0){
            throw new IllegalArgumentException("Размер динамического массива не может быть меньше 0");
        }
        this.elements = new Object[initialCapacity];    // создаём массив нужного размера
        this.size = 0;                                  // массив пустой
    }

    /// Возвращает текущее количество элементов
    public int size(){
        return size;
    }

    /**
     * Проверяет пустой ли массив
     *
     * @return true, если массив пустой и false, если в нём есть элементы
     */
    public boolean isEmpty(){
        return size == 0;
    }

    // ================== ДОБАВЛЕНИЕ ЭЛЕМЕНТОВ В МАССИВ ==================

    /**
     * Добавление элемента в конец массива
     *
     * @param element добавляемый элемент
     * @return  true, если элемент успешно добавлен
     */
    public boolean add(T element){
        // Сначала проверяем нужно ли увеличить массив
        ensureCapacity(size + 1);    // Проверка места в массиве
        elements[size++] = element;             // Добавляем элемент
        return true;

    }

    /**
     * Проверка наличия места в массиве под новый элемент
     *
     * @param minCapacity минимально требуемая емкость (логический размер + 1)
     */
    private void ensureCapacity(int minCapacity){
        if (minCapacity > elements.length){                 // Проверяем нужно ли расширять массив
            // Если ДА: Создаем массив большего размера
            int newCapacity = elements.length * 3/2 + 1;    // Новый размер для массива увеличен в *1.5 + 1
            Object[] newArray = new Object[newCapacity];    // Создаем экземпляр массива нового размера

            copyArrayElements(elements, newArray, size);    // Копируем элементы из старого в новый массив

            elements = newArray;                            // Заменяем старый массив новым
            }
        // Если НЕТ: ничего не делаем <--> места хватает.
        }

    /**
     * Копирование элементов из одного массива в другой
     *
     * @param oldArray  массив ИЗ которого копируем
     * @param newArray  массив В который копируем
     * @param count  количество элементов для копирования (логический размер)
     */
    private void copyArrayElements(Object[] oldArray, Object[] newArray, int count){
        for (int i = 0; i < count; i++){
            newArray[i] = oldArray[i];
        }
    }

    /**
     * Добавление элемента в массив по индексу
     *
     * @param index индекс элемента в массиве
     * @param element добавляемый элемент
     */
    public void add(int index, T element){
        checkIndexForAdd(index);              // Проверка на наличие индекса в массиве
        ensureCapacity(size + 1); // Проверка на наличие места в массиве
        moveElementsRight(index);            // Сдвиг элементов вправо

        elements[index] = element;           // Вставка элемента по индексу
        size++;                              // Увеличение размера на 1
    }

    /**
     * Проверка наличия индекса в массиве для добавления нового элемента
     *
     * @param index проверяемый индекс
     */
    private void checkIndexForAdd(int index){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    ///  Проверка наличия индекса в массиве
    private void checkIndex(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    /**
     * Сдвигает элементы массива вправо начиная с указанного индекса.
     * Освобождает место для вставки нового элемента.
     *
     * @param fromIndex индекс, с которого начинается сдвиг
     * @throws ArrayIndexOutOfBoundsException если сдвиг невозможен
     */
    private void moveElementsRight(int fromIndex){
        // Начинаем с конца чтобы не перезаписывать еще не скопированные элементы
        for(int i = size; i > fromIndex; i--){
            // Перезаписываем каждый элемент в правый соседний, чтобы освободить место для нашего fromIndex
            // На первой итерации копируем последний элемент в null
            elements[i] = elements[i - 1];
        }
    }

    // ================== ГЕТТЕРЫ И СЕТТЕРЫ ==================

    /**
     * Получение элемента по его индексу
     *
     * @SuppressWarnings("unchecked") - подавление предупреждения о непроверяемом приведении.
     * Безопасность гарантируется инкапсуляцией массива и контролем всех операций записи.
     * При правильном использовании дженериков ClassCastException невозможен.
     *
     * @param index индекс элемента, который нужно получить
     * @return возвращает элемент
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);              // Проверка наличия индекса в массиве
        return (T) elements[index];
    }

    /**
     * Установить по индексу элемента массива новое значение
     *
     * @param index индекс элемента, значение которого нужно изменить
     * @param element значение, на которое мы меняем
     * @return старое значение по этому индексу
     */
    @SuppressWarnings("unchecked")
    public T set(int index, T element){
        checkIndex(index);                  // Проверка наличия индекса в массиве
        T oldValue = (T) elements[index];   // Сохраняем старое значение по этому индексу
        elements[index] = element;          // Замена на желаемое значение
        return oldValue;                    // Возврат старого значения нужен для потенциального его использования
    }

    /**
     * Заполнение монотонно возрастающего массива
     * @param size - определяет размер массива
     * @param min  - нижняя граница диапазона
     * @param max  - верхняя граница диапазона
     */
    public static int[] createSortedArray(int size, int min, int max) {
        int[] arr = new int[size]; // объявление массива размера size
        arr[0] = min; // Первый элемент
        int remaining = max - min; // оставшаяся разница между текущим значением и максимальным
        int availableIncrements = size - 1; // количество шагов

        for (int i = 1; i < size; i++) {
            int maxIncrement = remaining - (availableIncrements - 1); // Максимальное возможное приращение на этом шаге
            int increment = 1 + (int)(Math.random() * maxIncrement);
            arr[i] = arr[i - 1] + increment;
            remaining -= increment; // Обновляем оставшееся пространство
            availableIncrements--;
        }
        return arr;
    }


    //todo: добавить метод для поиска и проверки
    //      добавить удаление
    //      очистка массива и итератор
}
