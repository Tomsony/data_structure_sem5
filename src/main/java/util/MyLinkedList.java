/**import java.util.NoSuchElementException;
 import java.util.Spliterator;
 import java.util.Spliterators;
 import java.util.stream.Stream;
 import java.util.stream.StreamSupport;

 // Класс реализует двусвязанный список
 public class MyLinkedList<T> implements Iterable<T> {
 private Node<T> head; // Указатель на начало списка

 /**
 * Метод превращает двусвязанный список в поток данных
 * Spliterator<T> - специализированный интерфейс для распределения и последовательной обработки элементов
 * spliteratorUnknownSize - говорит, что длина последовательности неизвестна заранее
 * Spliterator.ORDERED - указывает, что элементы имеют порядок
 * StreamSupport.stream(...) - Создает Stream<T> из Spliterator.
 * Второй аргумент false означает, что поток создается последовательным (не параллельным). Если бы поставили true, поток был бы параллельным.
 *
 public Stream<T> stream() {
 return StreamSupport.stream(
 Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED),
 false
 );
 }
 // Добавление элемента в список
 public void add(T data) {
 if (head == null) { // Пустой список -> новый элемент = голова
 head = new Node<>(data);
 } else {
 Node<T> current = head; // Ссылка на первый узел списка
 while (current.next != null) {
 current = current.next; // Перемещаемся до последнего элемента списка
 }
 current.next = new Node<>(data); // Создаем новый узел и присваиваем его следующему после последнего узла
 }
 }

 /// метод iterator() класса Iterator<T> реализует интерфейс Iterable<T>
 @Override
 public Iterator<T> iterator() {
 return new MyIterator();
 }

 // Внутренний класс итератора
 private class MyIterator implements Iterator<T> {
 private Node<T> current = head;

 // возвращает true, если в коллекции есть следующий элемент.
 @Override
 public boolean hasNext() {
 return current != null;
 }

 // возвращает следующий элемент коллекции
 @Override
 public T next() {
 if (!hasNext()) {
 throw new NoSuchElementException();
 }

 T data = current.data;
 current = current.next;
 return data;
 }

 // Можно реализовать, если нужен
 @Override
 public void remove() {
 throw new UnsupportedOperationException();
 }
 }
 }

 public class Main {
 public static void main(String[] args) {
 MyLinkedList<String> list = new MyLinkedList<>();
 list.add("Andrew Tate");
 list.add("Andrew Banga");
 list.add("Andrew Korilski");
 list.add("Andrew Ivanov");
 list.add("Andrew Sargisov");

 for (String person : list) { // Аналог for each
 System.out.println(person);
 }
 // any of
 boolean result1 = list.stream().anyMatch(item -> item.startsWith("A"));
 System.out.println("Есть элементы, начинающиеся с 'A'? " + result1);
 // all of
 boolean result2 = list.stream().allMatch(item -> item.length() > 3);
 System.out.println("Есть слова длиннее 5 символов? " + result2);
 // none of
 boolean result3 = list.stream().noneMatch(item -> item.isEmpty());
 System.out.println("Есть ли пустые строки? " + result3);
 }
 }

 public interface Iterator <T>{
 T next(); // возвращает следующий элемент коллекции
 boolean hasNext(); // возвращает true, если в коллекции есть следующий элемент.
 void remove(); // удаляет текущий элемент коллекции
 }
 }**/