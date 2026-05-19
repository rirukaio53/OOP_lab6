import java.util.*;

/**
 * Типізована колекція на основі двозв'язного списку,
 * що реалізує інтерфейс {@link List}.
 *
 * <p>Внутрішня структура — двозв'язний список із вузлами {@link Node}.
 * Підтримує всі операції стандартного інтерфейсу {@code List}, включаючи
 * додавання, видалення, пошук, ітерацію та підсписки.</p>
 *
 * <p>Не є потокобезпечним. Для використання в багатопотоковому середовищі
 * слід застосовувати зовнішню синхронізацію.</p>
 *
 * @param <T> тип елементів колекції
 * @author Student
 * @version 1.0
 */
public class DoublyLinkedList<T> implements List<T> {

    // ------------------------------------------------------------------ //
    //  Внутрішній клас вузла                                               //
    // ------------------------------------------------------------------ //

    /**
     * Вузол двозв'язного списку.
     *
     * @param <T> тип значення вузла
     */
    private static class Node<T> {
        /** Значення вузла. */
        T data;
        /** Посилання на попередній вузол. */
        Node<T> prev;
        /** Посилання на наступний вузол. */
        Node<T> next;

        /**
         * Створює вузол із заданим значенням.
         *
         * @param data значення вузла
         */
        Node(T data) {
            this.data = data;
        }
    }

    // ------------------------------------------------------------------ //
    //  Поля                                                                //
    // ------------------------------------------------------------------ //

    /** Перший вузол списку. */
    private Node<T> head;
    /** Останній вузол списку. */
    private Node<T> tail;
    /** Кількість елементів. */
    private int size;
    /** Лічильник структурних змін (для fail-fast ітератора). */
    private int modCount;

    // ------------------------------------------------------------------ //
    //  Конструктори                                                        //
    // ------------------------------------------------------------------ //

    /**
     * Порожній конструктор — створює порожній список.
     */
    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    /**
     * Конструктор копіювання — приймає один об'єкт узагальненого класу
     * та додає його до списку.
     *
     * @param element початковий елемент списку
     */
    public DoublyLinkedList(T element) {
        this();
        add(element);
    }

    /**
     * Конструктор із стандартної колекції — копіює всі елементи
     * переданої колекції у новий список у тому самому порядку.
     *
     * @param collection колекція, елементи якої будуть скопійовані;
     *                   не може бути {@code null}
     * @throws NullPointerException якщо {@code collection} є {@code null}
     */
    public DoublyLinkedList(Collection<? extends T> collection) {
        this();
        addAll(collection);
    }

    // ------------------------------------------------------------------ //
    //  Допоміжні методи                                                   //
    // ------------------------------------------------------------------ //

    /**
     * Повертає вузол за індексом.
     *
     * @param index індекс вузла (0-based)
     * @return вузол за вказаним індексом
     */
    private Node<T> nodeAt(int index) {
        checkIndex(index);
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    /**
     * Перевіряє коректність індексу для доступу до існуючих елементів.
     *
     * @param index індекс для перевірки
     * @throws IndexOutOfBoundsException якщо індекс виходить за межі
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Індекс: " + index + ", Розмір: " + size);
        }
    }

    /**
     * Перевіряє коректність індексу для операцій вставки.
     *
     * @param index індекс для перевірки
     * @throws IndexOutOfBoundsException якщо індекс виходить за межі
     */
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Індекс: " + index + ", Розмір: " + size);
        }
    }

    /**
     * Додає вузол перед заданим вузлом (або в кінець, якщо вузол {@code null}).
     *
     * @param data    значення нового вузла
     * @param successor вузол, перед яким вставляється новий (або {@code null})
     */
    private void linkBefore(T data, Node<T> successor) {
        Node<T> newNode = new Node<>(data);
        if (successor == null) {
            // Вставка в кінець
            if (tail == null) {
                head = newNode;
                tail = newNode;
            } else {
                newNode.prev = tail;
                tail.next = newNode;
                tail = newNode;
            }
        } else {
            Node<T> predecessor = successor.prev;
            newNode.prev = predecessor;
            newNode.next = successor;
            successor.prev = newNode;
            if (predecessor == null) {
                head = newNode;
            } else {
                predecessor.next = newNode;
            }
        }
        size++;
        modCount++;
    }

    /**
     * Видаляє вузол зі списку та повертає його значення.
     *
     * @param node вузол для видалення; не може бути {@code null}
     * @return значення видаленого вузла
     */
    private T unlink(Node<T> node) {
        T data = node.data;
        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;

        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.next = nextNode;
            node.prev = null;
        }

        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
            node.next = null;
        }

        node.data = null;
        size--;
        modCount++;
        return data;
    }

    // ------------------------------------------------------------------ //
    //  Реалізація List<T>                                                 //
    // ------------------------------------------------------------------ //

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            result[i++] = cur.data;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            a = (E[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            result[i++] = cur.data;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T t) {
        linkBefore(t, null);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<T> cur = head; cur != null; cur = cur.next) {
                if (cur.data == null) {
                    unlink(cur);
                    return true;
                }
            }
        } else {
            for (Node<T> cur = head; cur != null; cur = cur.next) {
                if (o.equals(cur.data)) {
                    unlink(cur);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null) {
            throw new NullPointerException("Колекція не може бути null");
        }
        boolean modified = false;
        for (T element : c) {
            add(element);
            modified = true;
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        checkIndexForAdd(index);
        if (c == null) {
            throw new NullPointerException("Колекція не може бути null");
        }
        boolean modified = false;
        Node<T> successor = (index == size) ? null : nodeAt(index);
        for (T element : c) {
            linkBefore(element, successor);
            modified = true;
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node<T> cur = head;
        while (cur != null) {
            Node<T> next = cur.next;
            if (c.contains(cur.data)) {
                unlink(cur);
                modified = true;
            }
            cur = next;
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<T> cur = head;
        while (cur != null) {
            Node<T> next = cur.next;
            if (!c.contains(cur.data)) {
                unlink(cur);
                modified = true;
            }
            cur = next;
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        Node<T> cur = head;
        while (cur != null) {
            Node<T> next = cur.next;
            cur.data = null;
            cur.prev = null;
            cur.next = null;
            cur = next;
        }
        head = null;
        tail = null;
        size = 0;
        modCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(int index) {
        return nodeAt(index).data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T set(int index, T element) {
        Node<T> node = nodeAt(index);
        T old = node.data;
        node.data = element;
        return old;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, T element) {
        checkIndexForAdd(index);
        if (index == size) {
            linkBefore(element, null);
        } else {
            linkBefore(element, nodeAt(index));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T remove(int index) {
        return unlink(nodeAt(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<T> cur = head; cur != null; cur = cur.next, index++) {
                if (cur.data == null) {
                    return index;
                }
            }
        } else {
            for (Node<T> cur = head; cur != null; cur = cur.next, index++) {
                if (o.equals(cur.data)) {
                    return index;
                }
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        int index = size - 1;
        if (o == null) {
            for (Node<T> cur = tail; cur != null; cur = cur.prev, index--) {
                if (cur.data == null) {
                    return index;
                }
            }
        } else {
            for (Node<T> cur = tail; cur != null; cur = cur.prev, index--) {
                if (o.equals(cur.data)) {
                    return index;
                }
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<T> listIterator(int index) {
        checkIndexForAdd(index);
        return new DoublyLinkedListIterator(index);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Повертає новий {@code DoublyLinkedList}, що містить елементи
     * від {@code fromIndex} включно до {@code toIndex} виключно.</p>
     */
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(
                    "fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", size=" + size);
        }
        DoublyLinkedList<T> sub = new DoublyLinkedList<>();
        Node<T> cur = nodeAt(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(cur.data);
            cur = cur.next;
        }
        return sub;
    }

    // ------------------------------------------------------------------ //
    //  Внутрішній клас ітератора                                          //
    // ------------------------------------------------------------------ //

    /**
     * Fail-fast двонаправлений ітератор для {@link DoublyLinkedList}.
     */
    private class DoublyLinkedListIterator implements ListIterator<T> {

        /** Вузол, що буде повернутий при наступному виклику {@link #next()}. */
        private Node<T> nextNode;
        /** Останній повернутий вузол (для {@link #set} та {@link #remove}). */
        private Node<T> lastReturned;
        /** Поточний індекс курсора. */
        private int cursor;
        /** Очікуване значення modCount. */
        private int expectedModCount;

        /**
         * Створює ітератор, що починається з позиції {@code index}.
         *
         * @param index початкова позиція ітератора
         */
        DoublyLinkedListIterator(int index) {
            nextNode = (index == size) ? null : nodeAt(index);
            cursor = index;
            expectedModCount = modCount;
        }

        /** Перевіряє факт зовнішньої модифікації списку. */
        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = nextNode;
            nextNode = nextNode.next;
            cursor++;
            return lastReturned.data;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public T previous() {
            checkForComodification();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastReturned = nextNode = (nextNode == null) ? tail : nextNode.prev;
            cursor--;
            return lastReturned.data;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            checkForComodification();
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            Node<T> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (nextNode == lastReturned) {
                nextNode = lastNext;
            } else {
                cursor--;
            }
            lastReturned = null;
            expectedModCount = modCount;
        }

        @Override
        public void set(T t) {
            checkForComodification();
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.data = t;
        }

        @Override
        public void add(T t) {
            checkForComodification();
            lastReturned = null;
            linkBefore(t, nextNode);
            cursor++;
            expectedModCount = modCount;
        }
    }

    // ------------------------------------------------------------------ //
    //  Перевизначення Object                                              //
    // ------------------------------------------------------------------ //

    /**
     * Повертає рядкове представлення списку у форматі {@code [e1, e2, ...]}.
     *
     * @return рядкове представлення
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            sb.append(cur.data);
            if (cur.next != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Перевіряє рівність двох списків поелементно.
     *
     * @param obj об'єкт для порівняння
     * @return {@code true}, якщо обидва списки містять однакові елементи
     *         в однаковому порядку
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        List<?> other = (List<?>) obj;
        if (size != other.size()) {
            return false;
        }
        Iterator<T> it1 = iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext()) {
            T e1 = it1.next();
            Object e2 = it2.next();
            if (!Objects.equals(e1, e2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Повертає хеш-код, сумісний із {@link List#hashCode()}.
     *
     * @return хеш-код списку
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        for (T e : this) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }
}
