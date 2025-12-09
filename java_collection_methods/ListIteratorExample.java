import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorExample {
    public static void main(String[] args) {
        // ===================================================
        // 1️⃣ Creating a List for ListIterator
        // ===================================================
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        System.out.println("Original List: " + fruits);

        // ===================================================
        // 2️⃣ Methods Inherited from Iterator Interface
        // ===================================================
        // hasNext() -> check if more elements forward
        // next() -> move cursor forward and return element
        // remove() -> remove last returned element

        ListIterator<String> iterator = fruits.listIterator();
        
        System.out.println("hasNext: " + iterator.hasNext());
        System.out.println("next: " + iterator.next());
        System.out.println("hasNext: " + iterator.hasNext());

        // ===================================================
        // 3️⃣ ListIterator-Specific Methods
        // ===================================================
        // hasPrevious() -> check if more elements backward
        // previous() -> move cursor backward and return element
        // nextIndex() -> index of element returned by next()
        // previousIndex() -> index of element returned by previous()
        // set(E) -> replace last returned element
        // add(E) -> insert element at current position

        // Bidirectional traversal
        ListIterator<String> iterator2 = fruits.listIterator();
        System.out.print("\nForward traversal: ");
        while (iterator2.hasNext()) {
            System.out.print(iterator2.next() + " ");
        }
        System.out.println();

        System.out.print("Backward traversal: ");
        while (iterator2.hasPrevious()) {
            System.out.print(iterator2.previous() + " ");
        }
        System.out.println();

        // Starting from specific position
        ListIterator<String> iterator3 = fruits.listIterator(2);
        System.out.println("\nStarting from index 2: " + iterator3.next());

        // Index tracking
        ListIterator<String> iterator4 = fruits.listIterator();
        System.out.println("\nInitial nextIndex: " + iterator4.nextIndex());
        System.out.println("Initial previousIndex: " + iterator4.previousIndex());
        iterator4.next();
        System.out.println("After next() - nextIndex: " + iterator4.nextIndex());
        System.out.println("After next() - previousIndex: " + iterator4.previousIndex());

        // ===================================================
        // 4️⃣ Modification Methods (add, set, remove)
        // ===================================================
        // add(E) -> insert element at current position
        // set(E) -> replace last returned element
        // remove() -> remove last returned element

        List<String> modifiableList = new ArrayList<>();
        modifiableList.add("Apple");
        modifiableList.add("Banana");
        modifiableList.add("Cherry");
        System.out.println("\nModifiable List: " + modifiableList);

        ListIterator<String> modIterator = modifiableList.listIterator();
        
        // Add operation
        modIterator.next(); // moves to Apple
        modIterator.add("Orange"); // adds Orange after Apple
        System.out.println("After add 'Orange': " + modifiableList);

        // Set operation
        modIterator.next(); // moves to Banana
        modIterator.set("Mango"); // replaces Banana with Mango
        System.out.println("After set 'Mango': " + modifiableList);

        // Remove operation
        modIterator.next(); // moves to Cherry
        modIterator.remove(); // removes Cherry
        System.out.println("After remove: " + modifiableList);

        // ===================================================
        // 5️⃣ Common Use Cases
        // ===================================================

        // Reverse iteration
        List<String> numbers = new ArrayList<>(List.of("One", "Two", "Three", "Four"));
        System.out.print("\nReverse iteration: ");
        ListIterator<String> reverseIter = numbers.listIterator(numbers.size());
        while (reverseIter.hasPrevious()) {
            System.out.print(reverseIter.previous() + " ");
        }
        System.out.println();

        // Replace all occurrences
        List<String> words = new ArrayList<>(List.of("cat", "dog", "cat", "bird"));
        ListIterator<String> replaceIter = words.listIterator();
        while (replaceIter.hasNext()) {
            if (replaceIter.next().equals("cat")) {
                replaceIter.set("tiger");
            }
        }
        System.out.println("After replacing 'cat' with 'tiger': " + words);

        // Insert after specific element
        List<String> animals = new ArrayList<>(List.of("Lion", "Tiger", "Bear"));
        ListIterator<String> insertIter = animals.listIterator();
        while (insertIter.hasNext()) {
            if (insertIter.next().equals("Tiger")) {
                insertIter.add("Elephant");
                break;
            }
        }
        System.out.println("After inserting 'Elephant' after 'Tiger': " + animals);

        // Safe removal while iterating
        List<String> items = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        ListIterator<String> removeIter = items.listIterator();
        while (removeIter.hasNext()) {
            String item = removeIter.next();
            if (item.equals("B") || item.equals("D")) {
                removeIter.remove(); // Safe removal
            }
        }
        System.out.println("After removing 'B' and 'D': " + items);
    }
}
