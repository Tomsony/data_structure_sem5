package util; // или ваш пакет

import trees.BaseNode;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Реализация двусвязного списка на основе базового узла {@link BaseNode}.
 * Список поддерживает все основные операции: добавление, удаление, поиск,
 * обход в прямом и обратном порядке, преобразование в поток и т.д.
 *
 * @param <T> тип хранимых значений
 */
public class MyLinkedList<T> implements Iterable<T> {

 /**
  * Конкретный класс узла для двусвязного списка.
  * Расширяет BaseNode, добавляя ссылки на соседние узлы.
  */
 private static class LinkedNode<T> extends BaseNode<T> {
  LinkedNode<T> next; // ссылка на следующий узел
  LinkedNode<T> prev; // ссылка на предыдущий узел

  /**
   * Конструктор узла.
   * @param value значение узла (не может быть null)
   * @throws IllegalArgumentException если value == null
   */
  LinkedNode(T value) {
   super(value);
   this.next = null;
   this.prev = null;
  }

  /**
   * Конструктор с указанием связей.
   * @param value значение узла
   * @param prev предыдущий узел
   * @param next следующий узел
   */
  LinkedNode(T value, LinkedNode<T> prev, LinkedNode<T> next) {
   super(value);
   this.prev = prev;
   this.next = next;
  }
 }

 // ------------------- Поля списка -------------------
 private LinkedNode<T> head;      // первый элемент списка
 private LinkedNode<T> tail;      // последний элемент списка
 private int size;                // количество элементов
 private int modCount;            // счётчик изменений (для fail-fast итераторов)

 // ------------------- Конструкторы -------------------
 /**
  * Создаёт пустой список.
  */
 public MyLinkedList() {
  head = null;
  tail = null;
  size = 0;
  modCount = 0;
 }

 /**
  * Создаёт список, содержащий элементы указанной коллекции.
  * @param collection коллекция, элементы которой будут добавлены в список
  * @throws NullPointerException если коллекция null
  */
 public MyLinkedList(Collection<? extends T> collection) {
  this();
  addAll(collection);
 }

 // ------------------- Основные операции добавления -------------------
 /**
  * Добавляет элемент в конец списка.
  * @param element добавляемый элемент
  * @return true (всегда, так как список изменяемый)
  * @throws IllegalArgumentException если element == null
  */
 public boolean add(T element) {
  addLast(element);
  return true;
 }

 /**
  * Добавляет элемент в начало списка.
  * @param element добавляемый элемент
  * @throws IllegalArgumentException если element == null
  */
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

 /**
  * Добавляет элемент в конец списка.
  * @param element добавляемый элемент
  * @throws IllegalArgumentException если element == null
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
  * @param index позиция (от 0 до size)
  * @param element вставляемый элемент
  * @throws IndexOutOfBoundsException если index < 0 или index > size
  * @throws IllegalArgumentException если element == null
  */
 public void add(int index, T element) {
  checkPositionIndex(index);
  checkNotNull(element);
  if (index == size) {
   addLast(element);
  } else if (index == 0) {
   addFirst(element);
  } else {
   LinkedNode<T> current = getNodeAtIndex(index);
   LinkedNode<T> newNode = new LinkedNode<>(element, current.prev, current);
   current.prev.next = newNode;
   current.prev = newNode;
   size++;
   modCount++;
  }
 }

 /**
  * Добавляет все элементы из коллекции в конец списка.
  * @param collection коллекция элементов
  * @return true если список изменился
  * @throws NullPointerException если коллекция null или содержит null
  */
 public boolean addAll(Collection<? extends T> collection) {
  if (collection == null) throw new NullPointerException("Коллекция не может быть null");
  boolean changed = false;
  for (T elem : collection) {
   add(elem);
   changed = true;
  }
  return changed;
 }

 // ------------------- Операции получения элементов -------------------
 /**
  * Возвращает первый элемент списка (без удаления).
  * @return первый элемент
  * @throws NoSuchElementException если список пуст
  */
 public T getFirst() {
  if (head == null) throw new NoSuchElementException("Список пуст");
  return head.getValue();
 }

 /**
  * Возвращает последний элемент списка (без удаления).
  * @return последний элемент
  * @throws NoSuchElementException если список пуст
  */
 public T getLast() {
  if (tail == null) throw new NoSuchElementException("Список пуст");
  return tail.getValue();
 }

 /**
  * Возвращает элемент по индексу.
  * @param index позиция элемента (от 0 до size-1)
  * @return элемент
  * @throws IndexOutOfBoundsException если index вне допустимого диапазона
  */
 public T get(int index) {
  return getNodeAtIndex(index).getValue();
 }

 /**
  * Заменяет элемент по индексу новым значением.
  * @param index позиция элемента
  * @param element новое значение
  * @return старое значение
  * @throws IndexOutOfBoundsException если index вне диапазона
  * @throws IllegalArgumentException если element == null
  */
 public T set(int index, T element) {
  checkNotNull(element);
  LinkedNode<T> node = getNodeAtIndex(index);
  T oldValue = node.getValue();
  node.setValue(element);
  return oldValue;
 }

 // ------------------- Операции удаления -------------------
 /**
  * Удаляет и возвращает первый элемент списка.
  * @return удалённый элемент
  * @throws NoSuchElementException если список пуст
  */
 public T removeFirst() {
  if (head == null) throw new NoSuchElementException("Список пуст");
  T removedValue = head.getValue();
  if (head == tail) {
   head = tail = null;
  } else {
   head = head.next;
   head.prev = null;
  }
  size--;
  modCount++;
  return removedValue;
 }

 /**
  * Удаляет и возвращает последний элемент списка.
  * @return удалённый элемент
  * @throws NoSuchElementException если список пуст
  */
 public T removeLast() {
  if (tail == null) throw new NoSuchElementException("Список пуст");
  T removedValue = tail.getValue();
  if (head == tail) {
   head = tail = null;
  } else {
   tail = tail.prev;
   tail.next = null;
  }
  size--;
  modCount++;
  return removedValue;
 }

 /**
  * Удаляет элемент по индексу.
  * @param index позиция удаляемого элемента
  * @return удалённый элемент
  * @throws IndexOutOfBoundsException если index вне диапазона
  */
 public T remove(int index) {
  checkElementIndex(index);
  if (index == 0) return removeFirst();
  if (index == size - 1) return removeLast();
  LinkedNode<T> nodeToRemove = getNodeAtIndex(index);
  nodeToRemove.prev.next = nodeToRemove.next;
  nodeToRemove.next.prev = nodeToRemove.prev;
  T removedValue = nodeToRemove.getValue();
  size--;
  modCount++;
  return removedValue;
 }

 /**
  * Удаляет все элементы из списка.
  */
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

 // ------------------- Поиск и индексы -------------------
 /**
  * Возвращает индекс первого вхождения элемента (по equals).
  * @param element искомый элемент (может быть null)
  * @return индекс или -1, если элемент не найден
  */
 public int indexOf(Object element) {
  int index = 0;
  if (element == null) {
   for (LinkedNode<T> cur = head; cur != null; cur = cur.next) {
    if (cur.getValue() == null) return index;
    index++;
   }
  } else {
   for (LinkedNode<T> cur = head; cur != null; cur = cur.next) {
    if (element.equals(cur.getValue())) return index;
    index++;
   }
  }
  return -1;
 }

 /**
  * Возвращает индекс последнего вхождения элемента (по equals).
  * @param element искомый элемент (может быть null)
  * @return индекс или -1, если элемент не найден
  */
 public int lastIndexOf(Object element) {
  int index = size - 1;
  if (element == null) {
   for (LinkedNode<T> cur = tail; cur != null; cur = cur.prev) {
    if (cur.getValue() == null) return index;
    index--;
   }
  } else {
   for (LinkedNode<T> cur = tail; cur != null; cur = cur.prev) {
    if (element.equals(cur.getValue())) return index;
    index--;
   }
  }
  return -1;
 }

 /**
  * Проверяет, содержит ли список указанный элемент.
  * @param element элемент для проверки
  * @return true если содержит
  */
 public boolean contains(Object element) {
  return indexOf(element) != -1;
 }

 // ------------------- Вспомогательные методы -------------------
 /**
  * Возвращает размер списка.
  * @return количество элементов
  */
 public int size() {
  return size;
 }

 /**
  * Проверяет, пуст ли список.
  * @return true если пуст
  */
 public boolean isEmpty() {
  return size == 0;
 }

 /**
  * Преобразует список в массив.
  * @return новый массив, содержащий все элементы в правильном порядке
  */
 @SuppressWarnings("unchecked")
 public T[] toArray() {
  T[] result = (T[]) new Object[size];
  int i = 0;
  for (LinkedNode<T> cur = head; cur != null; cur = cur.next) {
   result[i++] = cur.getValue();
  }
  return result;
 }

 // ------------------- Итераторы -------------------
 /**
  * Возвращает итератор, обходящий список в прямом порядке.
  * @return итератор
  */
 @Override
 public Iterator<T> iterator() {
  return new Iterator<T>() {
   private LinkedNode<T> current = head;   // начинаем с начала списка
   private LinkedNode<T> lastReturned = null;  // Последний узел, возвращённый методом next().

   // Ожидаемое количество модификаций списка. При создании итератора запоминаем текущее значение modCount.
   // Если в процессе итерации кто-то изменит список напрямую (не через этот итератор),
   // то modCount изменится, expectedModCount останется прежним, и итератор заметит это.
   private int expectedModCount = modCount;

   @Override
   public boolean hasNext() {
    // если current не указывает на null – значит, есть следующий элемент.
    // Когда current станет null, это означает, что мы дошли до конца списка.
    return current != null;
   }

   @Override
   public T next() {
    // Проверка на конкурентную модификацию (fail-fast)
    if (modCount != expectedModCount)
     throw new ConcurrentModificationException();
    // Проверка, есть ли следующий элемент (если нет – бросить исключение)
    if (!hasNext())
     throw new NoSuchElementException();
    // Сохраняем ссылку на текущий узел как последний возвращённый
    lastReturned = current;
    // Получаем значение из текущего узла
    T value = current.getValue();
    // Передвигаем указатель current на следующий узел
    current = current.next;
    // Возвращаем сохранённое значение
    return value;
   }

   @Override
   public void remove() {
    // Удаление элемента, который был возвращён последним вызовом next().
    // Особенности:
    // - Нельзя вызвать remove() без предшествующего next() (тогда lastReturned == null)
    // - Нельзя вызвать remove() дважды подряд (после удаления lastReturned сбрасывается в null)
    // - Нельзя удалять, если список был изменён извне (ConcurrentModificationException)
    if (lastReturned == null)
     throw new IllegalStateException();
    if (modCount != expectedModCount)
     throw new ConcurrentModificationException();
    unlinkNode(lastReturned);
    expectedModCount = modCount;
    lastReturned = null;
   }
  };
 }

 /**
  * Возвращает итератор, обходящий список в обратном порядке.
  * @return итератор от хвоста к голове
  */
 public Iterator<T> descendingIterator() {
  return new Iterator<T>() {
   private LinkedNode<T> current = tail;  // начинаем с конца списка
   private LinkedNode<T> lastReturned = null;
   private int expectedModCount = modCount;

   @Override
   public boolean hasNext() {
    return current != null;
   }

   @Override
   public T next() {
    if (modCount != expectedModCount)
     throw new ConcurrentModificationException();
    if (!hasNext())
     throw new NoSuchElementException();
    lastReturned = current;
    T value = current.getValue();
    current = current.prev;
    return value;
   }

   @Override
   public void remove() {
    if (lastReturned == null)
     throw new IllegalStateException();
    if (modCount != expectedModCount)
     throw new ConcurrentModificationException();
    unlinkNode(lastReturned);
    expectedModCount = modCount;
    lastReturned = null;
   }
  };
 }

 // ------------------- Приватные вспомогательные методы -------------------
 /**
  * Проверяет, что элемент не null (список не допускает null).
  */
 private void checkNotNull(T element) {
  if (element == null) {
   throw new IllegalArgumentException("Список не поддерживает null элементы");
  }
 }

 /**
  * Проверяет, что индекс для вставки (add) корректен.
  */
 private void checkPositionIndex(int index) {
  if (index < 0 || index > size) {
   throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
  }
 }

 /**
  * Проверяет, что индекс для доступа/удаления (get/remove) корректен.
  */
 private void checkElementIndex(int index) {
  if (index < 0 || index >= size) {
   throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
  }
 }

 /**
  * Возвращает узел по индексу, оптимизируя обход с начала или с конца.
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
  * Удаляет указанный узел из списка, корректно перевязывая связи.
  * Необходим для итератора и удаления по значению.
  */
 private void unlinkNode(LinkedNode<T> node) {
  if (node == head) {
   removeFirst();
   return;
  }
  if (node == tail) {
   removeLast();
   return;
  }
  node.prev.next = node.next;
  node.next.prev = node.prev;
  size--;
  modCount++;
 }

 // ------------------- Дополнительно: массив и строковое представление -------------------
 @Override
 public String toString() {
  if (isEmpty()) return "[]";
  StringBuilder sb = new StringBuilder("[");
  LinkedNode<T> cur = head;
  while (cur != null) {
   sb.append(cur.getValue());
   if (cur.next != null) sb.append(", ");
   cur = cur.next;
  }
  sb.append("]");
  return sb.toString();
 }
}