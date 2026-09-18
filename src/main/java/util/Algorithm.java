    package util;

    import java.util.Iterator;
    import java.util.function.Consumer;  // accept(T t) — выполняет действие, ничего не возвращает
    import java.util.function.Function;
    import java.util.function.Predicate; // test(T t) — возвращает true/false (проверка условия)

    /**
     * Данный класс реализует универсальные функции из С++ для работы с диапазонами
     *      Все функции принимают {@link Iterable}, что позволяет работать
     * с любыми пользовательскими структурами данных (LinkedList, BST, MyArrayList и др.),
     * реализующими этот интерфейс.
     *
     *  - for_each
     *  - any_of
     *  - transform
     *  - copy_if
     *
     * @author Артём Томских, ИВТ-23
     */
    public class Algorithm {
        /**
         * Выполнить переданную функцию (действие) для каждого элемента любого Iterable-объекта
         * Сигнатура в C++: for_each(InputIterator first, InputIterator last, Function fn)
         *
         * @param items любая коллекция или объект, чей клас реализует интерфейс Iterable<T>
         *              и предоставляет итератор по элементам типа T
         * @param action принимает аргумент и выполняет какое-то действие над ним
         *
         * <? super T> - это wildcard с нижней границей, Consumer который умеет принимать тип T или любой его супертип
         * Пример: если мы обрабатываем список List<Integer>, то T = Integer.
         * Consumer<? super Integer> примет:
         * Consumer<Integer> — логично,
         * Consumer<Number> — потому что Number супертип Integer,
         * Consumer<Object> — объект Object тоже супертип.
         *
         * Это увеличивает гибкость метода, следуя принципу PECS (Producer Extends, Consumer Super).
         * Потребитель использует super, чтобы можно было передавать широкие обработчики.
         */
        public static <T> void for_each(Iterable<T> items, Consumer<? super T> action){
            // Получаем итератор у коллекции
            Iterator<T> iterator = items.iterator();

            // Пока в коллекции есть следующий элемент
            while (iterator.hasNext()) {
                // Достаем этот элемент
                T item = iterator.next();

                // Выполняем действие (лямбду), которое передали в метод
                action.accept(item);
            }
        }

        /**
         * Проверяет существует ли в контейнере элемент, для которого предикат возвращает true
         * Сигнатура C++: bool any_of(InputIterator first, InputIterator last, UnaryPredicate pred)
         * @param items любая коллекция или объект, чей клас реализует интерфейс Iterable<T>
         *              и предоставляет итератор по элементам типа T
         * @param pred объект предикат
         * @return true - объект выполняет условие, false - нет
         */
        public static <T> boolean any_of(Iterable<T> items, Predicate<? super T> pred){
            for (T item : items){
                if (pred.test(item)){
                   return true;     // нашли подходящий - выход
                }
            }
            return false;       // ни один не подошёл
        }

        /**
         * Преобразует новый элемент исходной коллекции с помощью заданной функции
         *      и возвращает новый {@link MyArrayList} с результатами.
         * Исходная коллекция не изменяется. Порядок элементов сохраняется
         * Аналог в C++: {@code std::transform(first, last, out, fn)}
         *
         * @param items объект реализующих элементов
         * @param mapper функция преобразования, применяемая к каждому элементу
         * @return result - динамический массив с преобразованными элементами
         * @param <T> тип исходных элементов
         * @param <R> тип результирующих элементов
         */
        public static <T, R> MyArrayList<R> transform(Iterable<T> items, Function<? super T, ? extends R> mapper) {
            MyArrayList<R> result = new MyArrayList<>();
            for (T item : items) {
                result.add(mapper.apply(item));
            }
            return result;
        }

        /**
         * Копирует в новый {@link MyArrayList} только те элементы,
         *      которые удовлетворяют заданному условию.
         * Исходная коллекция не изменяется. Порядок элементов сохраняется.
         * Аналог в C++: {@code std::copy_if(first, last, out, pred)}
         *
         * @param items объект, реализующий {@link Iterable}
         * @param pred предикат — условие отбора
         * @return новый {@link MyArrayList} с элементами, удовлетворяющими условию
         * @param <T> тип элементов
         */
        public static <T> MyArrayList<T> copy_if(Iterable<T> items, Predicate<? super T> pred) {
            MyArrayList<T> result = new MyArrayList<>();
            for (T item : items) {
                if (pred.test(item)) {
                    result.add(item);
                }
            }
            return result;
        }


        /**
         * Проверяет, удовлетворяют ли абсолютно ВСЕ элементы контейнера заданному условию.
         * Сигнатура C++: bool all_of(InputIterator first, InputIterator last, UnaryPredicate pred)
         *
         * @param items любая коллекция или объект, чей класс реализует интерфейс Iterable<T>
         *              и предоставляет итератор по элементам типа T
         * @param pred  объект-предикат (условие проверки)
         * @param <T>   тип элементов в коллекции
         * @return true - если каждый элемент коллекции прошел проверку, false - если хотя бы один не подошел
         */
        public static <T> boolean all_of(Iterable<T> items, Predicate<? super T> pred) {
            // Перебираем элементы с помощью цикла for-each
            for (T item : items) {
                // Если хотя бы ОДИН элемент НЕ удовлетворяет условию (вернул false)
                if (!pred.test(item)) {
                    return false; // Условие все элементы нарушено — досрочный выход
                }
            }
            return true; // Все элементы успешно прошли проверку
        }

        /**
         * Проверяет, что НИ ОДИН элемент в контейнере НЕ удовлетворяет заданному условию.
         * Сигнатура C++: bool none_of(InputIterator first, InputIterator last, UnaryPredicate pred)
         *
         * @param items любая коллекция или объект, чей класс реализует интерфейс Iterable<T>
         *              и предоставляет итератор по элементам типа T
         * @param pred  объект-предикат (условие проверки)
         * @param <T>   тип элементов в коллекции
         * @return true - если ни один элемент не подошел под условие, false - если нашелся хотя бы один подходящий
         */
        public static <T> boolean none_of(Iterable<T> items, Predicate<? super T> pred) {
            // Перебираем элементы коллекции
            for (T item : items) {
                // Если нашелся хотя бы ОДИН элемент, для которого условие выполнилось (вернул true)
                if (pred.test(item)) {
                    return false; // Правило "ни одного" нарушено — досрочный выход
                }
            }
            return true; // Действительно, ни один из элементов не подошел под предикат
        }


    }
