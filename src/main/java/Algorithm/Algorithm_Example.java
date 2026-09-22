package Algorithm;
import util.MyArrayList;
import util.MyLinkedList;

public class Algorithm_Example {

    /**
     * Данный класс реализует методы класса Algorithm, а именно:
     * *  - for_each
     * *  - any_of
     * *  - transform
     * *  - copy_if
     * *  - none_of
     * * -  all_of
     *
     * Приведены примеры использования в различных коллекциях: ArrayList, LinkedList, todo:BinarySearchTree
     * @author Артём Томских, ИВТ-23
     */
    public static void main(String[] args) {

        // СОЗДАЁМ КОЛЛЕКЦИЮ - ИСТОЧНИК, КОТОРАЯ РЕАЛИЗУЕТ ITERABLE
        MyArrayList<Integer> nums = new MyArrayList<>();
        int[] tempArray = new int[]{1, 2, 3, -4, 5, 3};
        for (int x : tempArray){
            nums.add(x);
        }
        System.out.println("Исходный массив: " + nums);
        System.out.println();

        // --------------------------------------------------------------------------
        // for_each - ВЫПОЛНИТЬ ДЕЙСТВИЕ ДЛЯ КАЖДОГО ЭЛЕМЕНТА
        System.out.println("for_each (вывод элементов): ");
        Algorithm.for_each(nums,x -> System.out.println(x + " "));
        System.out.println();

        // Пример 2: возводим каждое число в квадрат
        System.out.print("for_each (квадрат каждого): ");
        Algorithm.for_each(nums, x -> System.out.print((x * x) + " "));
        System.out.println("\n");
        // --------------------------------------------------------------------------

        // any_of - есть ли хотя бы один элемент с нужным свойством
            // проверка на наличие чисел x < 0
        boolean hasNegative = Algorithm.any_of(nums,x-> x < 0);
            // проверка2 на наличие чисел x = 0
        boolean hasZero = Algorithm.any_of(nums, x-> x == 0);
        System.out.println("any_of (есть отрицательные?): " + hasNegative);
        System.out.println("any_of (есть нули?):          " + hasZero);
        System.out.println();
        // --------------------------------------------------------------------------

        // all_of - все элементы удовлетворяют условию
            // проверка все ли числа положительные?
        boolean allPositive = Algorithm.all_of(nums,x -> x > 0);
            // проверка2 все ли числа меньше 10?
        boolean lessThanTen = Algorithm.all_of(nums,x-> x < 10);
        System.out.println("all_of (все положительные?): " + allPositive);
        System.out.println("all_of (все < 10?):          " + lessThanTen);
        System.out.println();
        // --------------------------------------------------------------------------

        // none_of - все элементы не удовлетворяют условию
        // проверка все ли числа отрицательные?
        boolean allNegative = Algorithm.none_of(nums, x -> x < 0);
        // проверка2 все числа не больше 10?
        boolean moreThanTen = Algorithm.none_of(nums, x-> x > 10);
        System.out.println("none_of (все отрицательные?): " + allNegative);
        System.out.println("none_of (все > 10?):          " + moreThanTen);
        System.out.println();
        // --------------------------------------------------------------------------

        // transform - преобразовать каждый элемент и вернуть новую коллекцию
            // Преобразуем исходную коллекцию в Integer массив квадратов
        MyArrayList<Integer> squares = Algorithm.transform(nums, x -> x * x);
            // Преобразуем коллекцию в String массив строк
        MyArrayList<String> strings = Algorithm.transform(nums, x -> "num= " + x);
        System.out.println("transform (x * x):           " + squares);
        System.out.println("transform (в строки):         " + strings);
        System.out.println();
        // --------------------------------------------------------------------------

        // copy_if - скопировать только элементы, удовлетворяющие условию
            // x > 0
        MyArrayList<Integer> positives = Algorithm.copy_if(nums, x -> x > 0);
            // x % 2 == 0
        MyArrayList<Integer> evens = Algorithm.copy_if(nums, x -> x % 2 == 0);
        System.out.println("copy_if (положительные):     " + positives);
        System.out.println("copy_if (чётные):            " + evens);
        System.out.println();
        // --------------------------------------------------------------------------

        // ДЕМОНСТРАЦИЯ С КОЛЛЕКЦИЕЙ MyLinkedList<>()
        // ---- MyLinkedList ----
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("apple");
        list.add("banana");
        list.add("cherry");

        System.out.print("for_each на MyLinkedList: ");
        Algorithm.for_each(list, s -> System.out.print(s + " "));
        System.out.println();

        MyArrayList<String> longNames = Algorithm.copy_if(list, s -> s.length() > 5);
        System.out.println("copy_if (длиннее 5): " + longNames);
        System.out.println();
    }
}