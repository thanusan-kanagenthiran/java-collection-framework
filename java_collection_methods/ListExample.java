
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Comparator;

public class ListExample {
    public static void main(String[] args) {
        // ===================================================
        // 1️⃣ Creating a List
        // ===================================================
        // List allows duplicates and maintains insertion order
        // Elements are accessible by index (positional access)

        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Banana"); // duplicates allowed
        System.out.println("Original List: " + fruits);

        // ===================================================
        // 2️⃣ List-Specific Methods (Not in Collection/Iterable)
        // ===================================================
        // get(int) -> retrieve element by index
        // set(int, E) -> replace element at index
        // indexOf(Object) -> find first occurrence
        // lastIndexOf(Object) -> find last occurrence
        // subList(int, int) -> create view of portion
        // listIterator() -> bidirectional iterator
        // listIterator(int) -> iterator starting at position
        // replaceAll(UnaryOperator) -> transform all elements (Java 8+ default)
        // sort(Comparator) -> sort elements in place (Java 8+ default)

        System.out.println("Element at index 1 (get): " + fruits.get(1));

        fruits.set(2, "Orange");
        System.out.println("After set index 2 to 'Orange': " + fruits);

        System.out.println("First index of Banana (indexOf): " + fruits.indexOf("Banana"));

        System.out.println("Last index of Banana (lastIndexOf): " + fruits.lastIndexOf("Banana"));

        List<String> subList = fruits.subList(1, 3);
        System.out.println("SubList from index 1 to 3: " + subList);

        ListIterator<String> iterator1 = fruits.listIterator();
        System.out.print("ListIterator forward: ");
        while (iterator1.hasNext()) {
            System.out.print(iterator1.next() + " ");
        }
        System.out.println();

        ListIterator<String> iterator2 = fruits.listIterator(2);
        System.out.print("ListIterator from index 2: ");
        while (iterator2.hasNext()) {
            System.out.print(iterator2.next() + " ");
        }
        System.out.println();

        fruits.replaceAll(String::toUpperCase);
        System.out.println("After replaceAll (toUpperCase): " + fruits);

        fruits.sort(Comparator.naturalOrder());
        System.out.println("After sort (naturalOrder): " + fruits);

        // ===================================================
        // 3️⃣ Overloaded Methods from Collection
        // ===================================================
        // add(int, E) -> insert at specific position
        // addAll(int, Collection) -> insert collection at position
        // remove(int) -> remove by index (returns E)

        fruits.add(2, "GRAPES");
        System.out.println("After add at index 2: " + fruits);

        List<String> moreFruits = List.of("KIWI", "MANGO");
        fruits.addAll(1, moreFruits);
        System.out.println("After addAll at index 1: " + fruits);

        fruits.remove(4); // returns E (removed element)
        System.out.println("After remove at index 4: " + fruits);

        // ===================================================
        // 4️⃣ Static Factory Methods (Java 9+)
        // ===================================================
        // of() -> creates unmodifiable list
        // copyOf() -> creates unmodifiable copy
        // These lists are immutable and don't allow null elements

        List<String> emptyList = List.of();
        List<String> singleList = List.of("PEACH");
        List<String> multiList = List.of("APPLE", "BANANA", "CHERRY");
        List<String> copyList = List.copyOf(fruits);

        System.out.println("Empty List: " + emptyList);
        System.out.println("Single Element List: " + singleList);
        System.out.println("Multi-element List: " + multiList);
        System.out.println("Copy of fruits: " + copyList);
    }
}
