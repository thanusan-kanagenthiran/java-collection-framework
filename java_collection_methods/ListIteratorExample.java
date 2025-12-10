import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorExample {
    
    public static void main(String[] args) {

        // ===================================================
        // 0️⃣ Initialization
        // ===================================================
        List<String> fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Date"));
        System.out.println("Original List: " + fruits);

        // ===================================================
        // 1️⃣ Forward Traversal & Index Starting Point
        // ===================================================
        
        // 1. Starting from the beginning (index 0)
        ListIterator<String> forwardIter = fruits.listIterator();
        System.out.print("\n1. Forward Traversal (Start Index 0): ");
        while (forwardIter.hasNext()) {
            System.out.print(forwardIter.next() + " ");
        }
        System.out.println();
        
        // 2. Starting from a specified index (index 2)
        // Iterator is positioned before 'Cherry' (index 2).
        ListIterator<String> iteratorAtIndex = fruits.listIterator(2);
        System.out.print("2. Traversal starting from index 2: ");
        while (iteratorAtIndex.hasNext()) {
            System.out.print(iteratorAtIndex.next() + " "); // Cherry Date
        }
        System.out.println();

        // ===================================================
        // 2️⃣ Backward Traversal
        // ===================================================
        // Iterator must be positioned at list.size() (the end) to start backward traversal.
        ListIterator<String> backwardIter = fruits.listIterator(fruits.size());

        System.out.print("\n3. Backward Traversal: ");
        while (backwardIter.hasPrevious()) {
            System.out.print(backwardIter.previous() + " "); // Date Cherry Banana Apple
        }
        System.out.println();

        // ===================================================
        // 3️⃣ Index Tracking (nextIndex() / previousIndex())
        // ===================================================
        // Use a new iterator starting at index 0 for a clean demo of index movement.
        ListIterator<String> indexTracker = fruits.listIterator(0);

        System.out.println("\n4. Index Tracking Demo (Fresh Iterator at index 0):");
        System.out.println("Initial nextIndex: " + indexTracker.nextIndex());      // 0
        System.out.println("Initial previousIndex: " + indexTracker.previousIndex());  // -1

        indexTracker.next(); // Returns 'Apple', cursor moves between 'Apple' (index 0) and 'Banana' (index 1)
        System.out.println("After next() - nextIndex: " + indexTracker.nextIndex());     // 1
        System.out.println("After next() - previousIndex: " + indexTracker.previousIndex()); // 0

        indexTracker.previous(); // Returns 'Apple', cursor moves back to index 0 position
        System.out.println("After previous() - nextIndex: " + indexTracker.nextIndex());     // 0
        System.out.println("After previous() - previousIndex: " + indexTracker.previousIndex()); // -1

        // ===================================================
        // 4️⃣ Modification Methods (add, set, remove)
        // ===================================================
        
        // Re-initialize 'fruits' to ensure a clean slate before modification
        fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Date"));
        System.out.println("\n5. Modification Methods Demo (Clean Slate):");
        System.out.println("Initial list for modification: " + fruits);
        
        ListIterator<String> modIterator = fruits.listIterator();

        // 1. ADD Operation
        modIterator.next(); // returns 'Apple'
        modIterator.add("Orange"); 
        System.out.println("-> After add 'Orange': " + fruits); // [Apple, Orange, Banana, Cherry, Date]

        // 2. SET Operation
        modIterator.next(); // returns 'Orange'
        modIterator.set("Mango"); // Replaces the last returned element ('Orange')
        System.out.println("-> After set 'Mango' (replaces Orange): " + fruits); // [Apple, Mango, Banana, Cherry, Date]

        // 3. REMOVE Operation
        modIterator.next(); // returns 'Banana'
        modIterator.remove(); // Removes the last returned element ('Banana')
        System.out.println("-> After remove (Banana): " + fruits); // [Apple, Mango, Cherry, Date]
        
        // ===================================================
        // 5️⃣ Common Use Cases
        // ===================================================

        // A. Reverse iteration
        List<String> numbers = new ArrayList<>(List.of("One", "Two", "Three", "Four"));
        System.out.print("\n6A. Reverse iteration: ");
        ListIterator<String> reverseIter = numbers.listIterator(numbers.size()); 
        while (reverseIter.hasPrevious()) {
            System.out.print(reverseIter.previous() + " "); 
        }
        System.out.println();

        // B. Replace all occurrences
        List<String> words = new ArrayList<>(List.of("cat", "dog", "cat", "bird"));
        ListIterator<String> replaceIter = words.listIterator();
        while (replaceIter.hasNext()) {
            if (replaceIter.next().equals("cat")) {
                replaceIter.set("tiger");
            }
        }
        System.out.println("6B. After replacing 'cat' with 'tiger': " + words);

        // C. Safe removal while iterating
        List<String> items = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        ListIterator<String> removeIter = items.listIterator();
        while (removeIter.hasNext()) {
            String item = removeIter.next();
            if (item.equals("B") || item.equals("D")) {
                removeIter.remove(); 
            }
        }
        System.out.println("6C. After removing 'B' and 'D': " + items);
    }
}