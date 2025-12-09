import java.util.*;

public class CollectionExample {
    public static void main(String[] args) {

        // ===================================================
        // 1️⃣ Adding Elements
        // ===================================================
        // add(E)      -> adds a single element
        // addAll()    -> adds another collection
        // Some collections reject duplicates (Set)
        // Some collections are read-only (UnsupportedOperationException)

        Collection<String> fruits = new ArrayList<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");

        Collection<String> moreFruits = List.of("Mango", "Orange");
        fruits.addAll(moreFruits);

        System.out.println("After adding: " + fruits);


        // ===================================================
        // 2️⃣ Removing Elements
        // ===================================================
        // remove(o)     -> removes element if found
        // removeAll()   -> removes all matching items
        // removeIf()    -> removes using a condition
        // clear()       -> removes everything

        fruits.remove("Banana");
        System.out.println("After remove Banana: " + fruits);

        fruits.removeAll(List.of("Mango", "Apple"));
        System.out.println("After removeAll Mango & Apple: " + fruits);

        fruits.removeIf(f -> f.startsWith("O"));
        System.out.println("After removeIf startsWith 'O': " + fruits);

        Collection<String> temp = new ArrayList<>(fruits);
        temp.clear();
        System.out.println("After clear(): " + temp);


        // ===================================================
        // 3️⃣ Querying / Checking
        // ===================================================
        // contains()      -> checks element existence
        // containsAll()   -> checks multiple elements
        // isEmpty()       -> checks if empty
        // size()          -> element count
        // List: O(n), HashSet: O(1) average-case

        fruits.clear();
        fruits.addAll(List.of("Apple", "Banana", "Cherry"));

        System.out.println("Fruits: " + fruits);
        System.out.println("Contains Banana? " + fruits.contains("Banana"));
        System.out.println("Contains [Apple, Cherry]? " + fruits.containsAll(List.of("Apple", "Cherry")));
        System.out.println("Is empty? " + fruits.isEmpty());
        System.out.println("Size: " + fruits.size());


        // ===================================================
        // 4️⃣ Iteration / Traversal
        // ===================================================
        // iterator()    -> manual loop, safe removal
        // forEach()     -> lambda loop
        // spliterator() -> supports splitting for parallel use
        // Modifying during iteration may throw ConcurrentModificationException

        System.out.print("Iterator: ");
        Iterator<String> it = fruits.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        System.out.print("forEach: ");
        fruits.forEach(f -> System.out.print(f + " "));
        System.out.println();

        System.out.print("Spliterator: ");
        Spliterator<String> sp = fruits.spliterator();
        sp.forEachRemaining(f -> System.out.print(f + " "));
        System.out.println();


        // ===================================================
        // 5️⃣ Retaining / Filtering
        // ===================================================
        // retainAll() keeps ONLY given elements

        fruits.retainAll(List.of("Apple", "Cherry"));
        System.out.println("After retainAll [Apple, Cherry]: " + fruits);


        // ===================================================
        // 6️⃣ Conversion to Array
        // ===================================================
        // toArray()              -> Object[]
        // toArray(T[] a)         -> typed array
        // toArray(generator)     -> modern Java

        Object[] arr1 = fruits.toArray();
        System.out.println("Object[]: " + Arrays.toString(arr1));

        String[] arr2 = fruits.toArray(new String[0]);
        System.out.println("Typed array: " + Arrays.toString(arr2));

        String[] arr3 = fruits.toArray(String[]::new);
        System.out.println("Generated array: " + Arrays.toString(arr3));


        // ===================================================
        // 7️⃣ Streams
        // ===================================================
        // stream()         -> sequential
        // parallelStream() -> multi-threaded (order not guaranteed)
        // Avoid shared state in parallel streams

        System.out.print("Stream: ");
        fruits.stream().forEach(f -> System.out.print(f + " "));
        System.out.println();

        System.out.print("ParallelStream: ");
        fruits.parallelStream().forEach(f -> System.out.print(f + " "));
        System.out.println();


        // ===================================================
        // 8️⃣ Object Methods
        // ===================================================
        // equals()   -> compares contents
        // hashCode() -> consistent with equals()
        // List: order matters, Set: order does not

        Collection<String> copy = new ArrayList<>(fruits);
        Collection<String> set  = new HashSet<>(fruits);

        System.out.println("Equals copy? " + fruits.equals(copy));
        System.out.println("Equals set? " + fruits.equals(set));
        System.out.println("HashCode: " + fruits.hashCode());


        
    }
}
