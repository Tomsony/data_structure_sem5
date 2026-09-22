//package trees;
//
//import util.MyArrayList;
//
//import java.util.concurrent.ThreadLocalRandom;
//
//// Вспомогательный класс для измерения времени поиска в сортированном массиве и BST
//public class TimePlot {
//
//    public static void main(String[] args) {
//        // Параметры для замера
//        int size = 100_000;     // размер массива
//        int min = 1;            // Значение первого элемента
//        int max = size * 10;    // Значение последнего элемента
//        int maxIterations = 50_000_000; // Количество итераций в циклах поиска
//
//
//        // 1. Создадим сортированный массив
//        int[] sortedArray = MyArrayList.createSortedArray(size,min,max);
//
//        // 2. Создадим экземпляр BST
//        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
//        ThreadLocalRandom random = ThreadLocalRandom.current();
//
//        // Заполняем BST случайными числами из того же диапазона
//        for (int i = 0; i < size; i++) {
//            int randomValue = random.nextInt(min, max + 1);
//            bst.insert(randomValue);
//        }
//
//        // ==== БИНАРНЫЙ ПОИСК ====
//        System.out.println("\n--- Бинарный поиск в массиве ---");
//        long binaryTime = measureSearchTime(
//                sortedArray,
//                min,
//                max,
//                maxIterations,
//                TimePlot::binaryOfIndex
//        );
//        System.out.println("Время: " + binaryTime + " мс");
//
//        // ==== ПОИСК В BST ====
//        System.out.println("\n--- Поиск в BST ---");
//        long bstTime = measureBSTSearchTime(
//                bst,
//                min,
//                max,
//                maxIterations
//        );
//        System.out.println("Время: " + bstTime + " мс");
//    }
//
//    /**
//     * Замер времени выполнения кода в наносекундах
//     * @param task измеряемый участок кода
//     * @return время выполнения кода
//     */
//    public static long measureTime(Runnable task) {
//        // start содержит начальное время измерения
//        long start = System.currentTimeMillis();
//        // run - метод Runnable, который запускает поток
//        task.run();
//        return System.currentTimeMillis() - start;
//    }
//
//    /**
//     * Для избежания конфликта типов в лямбда-выражении, создан функциональный интерфейс
//     */
//    @FunctionalInterface
//    interface IntArraySearch {
//        // @param array отсортированный массив
//        // @param key искомый элемент поиска
//        int search(int[] array, int key);
//    }
//
//    /**
//     * Универсальный метод для измерения работы бинарного и интерполяционного поиска
//     *
//     * @param array      отсортированный массив
//     * @param min        первый элемент
//     * @param max        последний элемент
//     * @param iterations количество итераций для увеличения времени работы
//     * @param searchFunc ссылка на метод с алгоритмом поиска
//     */
//    public static long measureSearchTime(int[] array,
//                                         int min,
//                                         int max,
//                                         int iterations,
//                                         IntArraySearch searchFunc) {
//        return measureTime(() -> {
//            ThreadLocalRandom random = ThreadLocalRandom.current(); // ThreadLocalRandom работает быстрее Math.random()
//            for (int k = 0; k < iterations; k++) {
//                int randomValue = random.nextInt(min, max + 1); // Искомый элемент поиска
//                searchFunc.search(array, randomValue);
//            }
//        });
//    }
//
//    /**
//     * Бинарный поиск в сортированном массиве
//     * @param sortedArray отсортированный по возрастанию массив из Task_1
//     * @param element     искомый элемент
//     * @return -1 если элемент не найден или искомое число
//     */
//    public static int binaryOfIndex(int[] sortedArray, int element) {
//        int left = 0; // левая граница диапазона
//        int right = sortedArray.length - 1; // правая
//
//        while (left <= right) {
//            int middle = left + (right - left) / 2; // середина массива
//            int current = sortedArray[middle]; // текущий элемент
//
//            if (current == element) { // Середина массива - искомое число?
//                return middle; // Если да - возвращаем
//            } else if (current < element) { // Середина массива - меньше искомого числа?
//                left = middle + 1; // Если да - увеличиваем левую границу на 1
//            } else if (current > element) {
//                right = middle - 1; // Если да - уменьшаем правую границу на 1
//            }
//        }
//
//        return -1;
//    }
//
//    public static long measureBSTSearchTime(BinarySearchTree<Integer> bst,
//                                            int min,
//                                            int max,
//                                            int iterations){
//        return measureTime(() -> {
//            ThreadLocalRandom random = ThreadLocalRandom.current();
//            for (int k = 0; k < iterations; k++) {
//                int randomValue = random.nextInt(min, max + 1);
//                bst.search(randomValue); // Нестатический метод
//            }
//        });
//    }
//}
