import java.util.*;

/**
 * Демонстраційний клас для {@link DoublyLinkedList}.
 *
 * <p>Усі початкові дані задаються у виконавчому методі {@link #run()}.
 * Клас демонструє роботу трьох конструкторів та всіх основних методів
 * інтерфейсу {@link List}.</p>
 *
 * @author Student
 * @version 1.0
 */
public class Main {

    /**
     * Точка входу програми.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        new Main().run();
    }

    /**
     * Виконавчий метод, що містить усі початкові дані та демонстрацію
     * можливостей {@link DoublyLinkedList}.
     */
    public void run() {
        separator("ЛАБОРАТОРНА РОБОТА №6 — Двозв'язний список (реалізація List)");

        // ---------------------------------------------------------------- //
        //  1. Конструктори                                                   //
        // ---------------------------------------------------------------- //
        separator("1. Конструктори");

        // Конструктор 1 — порожній
        DoublyLinkedList<Integer> list1 = new DoublyLinkedList<>();
        System.out.println("Конструктор 1 (порожній): " + list1);
        System.out.println("  isEmpty() = " + list1.isEmpty());

        // Конструктор 2 — один об'єкт узагальненого класу
        DoublyLinkedList<String> list2 = new DoublyLinkedList<>("Привіт");
        System.out.println("Конструктор 2 (один елемент): " + list2);

        // Конструктор 3 — стандартна колекція об'єктів
        List<Integer> source = Arrays.asList(10, 20, 30, 40, 50);
        DoublyLinkedList<Integer> list3 = new DoublyLinkedList<>(source);
        System.out.println("Конструктор 3 (колекція " + source + "): " + list3);

        // ---------------------------------------------------------------- //
        //  2. Основна колекція для демонстрації                             //
        // ---------------------------------------------------------------- //
        separator("2. Робоча колекція (Integer)");

        DoublyLinkedList<Integer> list = new DoublyLinkedList<>(
                Arrays.asList(5, 15, 25, 35, 45));
        System.out.println("Початковий стан: " + list);
        System.out.println("size()         : " + list.size());

        // ---------------------------------------------------------------- //
        //  3. add / add(index, element)                                     //
        // ---------------------------------------------------------------- //
        separator("3. Додавання елементів");

        list.add(55);
        System.out.println("add(55)          : " + list);

        list.add(0, 0);
        System.out.println("add(0, 0)        : " + list);

        list.add(3, 99);
        System.out.println("add(3, 99)       : " + list);

        // ---------------------------------------------------------------- //
        //  4. get / set                                                     //
        // ---------------------------------------------------------------- //
        separator("4. get / set");

        System.out.println("get(0)           : " + list.get(0));
        System.out.println("get(3)           : " + list.get(3));

        Integer old = list.set(3, 100);
        System.out.println("set(3, 100) -> старий: " + old);
        System.out.println("Після set        : " + list);

        // ---------------------------------------------------------------- //
        //  5. remove                                                        //
        // ---------------------------------------------------------------- //
        separator("5. Видалення елементів");

        Integer removed = list.remove(0);
        System.out.println("remove(0) -> " + removed + " | список: " + list);

        boolean removedByValue = list.remove(Integer.valueOf(100));
        System.out.println("remove(100) -> " + removedByValue + " | список: " + list);

        // ---------------------------------------------------------------- //
        //  6. indexOf / lastIndexOf / contains                             //
        // ---------------------------------------------------------------- //
        separator("6. Пошук елементів");

        list.add(15); // дублюємо 15
        System.out.println("Список (з дублем 15): " + list);
        System.out.println("indexOf(15)         : " + list.indexOf(15));
        System.out.println("lastIndexOf(15)     : " + list.lastIndexOf(15));
        System.out.println("contains(25)        : " + list.contains(25));
        System.out.println("contains(999)       : " + list.contains(999));

        // ---------------------------------------------------------------- //
        //  7. addAll / removeAll / retainAll                               //
        // ---------------------------------------------------------------- //
        separator("7. Пакетні операції");

        List<Integer> extra = Arrays.asList(60, 70, 80);
        list.addAll(extra);
        System.out.println("addAll(" + extra + "): " + list);

        List<Integer> toRemove = Arrays.asList(60, 70, 80);
        list.removeAll(toRemove);
        System.out.println("removeAll(" + toRemove + "): " + list);

        DoublyLinkedList<Integer> copy = new DoublyLinkedList<>(list);
        copy.retainAll(Arrays.asList(15, 25, 45));
        System.out.println("retainAll([15,25,45]) на копії: " + copy);

        // ---------------------------------------------------------------- //
        //  8. toArray                                                       //
        // ---------------------------------------------------------------- //
        separator("8. toArray");

        Object[] arr1 = list.toArray();
        System.out.println("toArray()       : " + Arrays.toString(arr1));

        Integer[] arr2 = list.toArray(new Integer[0]);
        System.out.println("toArray(T[])    : " + Arrays.toString(arr2));

        // ---------------------------------------------------------------- //
        //  9. subList                                                       //
        // ---------------------------------------------------------------- //
        separator("9. subList");

        List<Integer> sub = list.subList(1, 4);
        System.out.println("subList(1, 4)   : " + sub);

        // ---------------------------------------------------------------- //
        //  10. ListIterator (прямий та зворотній обхід)                   //
        // ---------------------------------------------------------------- //
        separator("10. ListIterator");

        System.out.print("Прямий обхід    : ");
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        System.out.print("Зворотній обхід : ");
        while (it.hasPrevious()) {
            System.out.print(it.previous() + " ");
        }
        System.out.println();

        // ---------------------------------------------------------------- //
        //  11. equals / hashCode                                           //
        // ---------------------------------------------------------------- //
        separator("11. equals / hashCode");

        DoublyLinkedList<Integer> same = new DoublyLinkedList<>(list);
        System.out.println("Поточний список : " + list);
        System.out.println("Копія           : " + same);
        System.out.println("equals          : " + list.equals(same));
        System.out.println("hashCode рівні  : " + (list.hashCode() == same.hashCode()));

        // ---------------------------------------------------------------- //
        //  12. clear                                                        //
        // ---------------------------------------------------------------- //
        separator("12. clear");

        DoublyLinkedList<Integer> temp = new DoublyLinkedList<>(
                Arrays.asList(1, 2, 3));
        System.out.println("До clear        : " + temp);
        temp.clear();
        System.out.println("Після clear     : " + temp);
        System.out.println("isEmpty()       : " + temp.isEmpty());

        // ---------------------------------------------------------------- //
        //  13. Рядковий список (перевірка узагальненості)                 //
        // ---------------------------------------------------------------- //
        separator("13. Узагальненість — список рядків");

        DoublyLinkedList<String> words = new DoublyLinkedList<>(
                Arrays.asList("Яблуко", "Банан", "Вишня", "Диня"));
        System.out.println("Список слів     : " + words);
        words.add(2, "Груша");
        System.out.println("Після add(2,'Груша'): " + words);
        words.remove("Банан");
        System.out.println("Після remove('Банан'): " + words);

        separator("КІНЕЦЬ ДЕМОНСТРАЦІЇ");
    }

    /**
     * Виводить роздільник із заголовком для зручності читання виводу.
     *
     * @param title текст заголовку
     */
    private void separator(String title) {
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }
}
