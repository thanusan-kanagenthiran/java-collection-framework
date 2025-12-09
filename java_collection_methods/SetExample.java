
import java.util.*;

public class SetExample {
    public static void main(String[] args) {
        // ===================================================
        // 1️⃣ Static Factory Methods (Java 9+)
        // ===================================================
        // of() -> creates unmodifiable set
        // copyOf() -> creates unmodifiable copy
        // Sets enforce uniqueness - no duplicate elements
        // These sets are immutable and don't allow null elements

        Set<String> emptySet = Set.of(); // of()
        System.out.println("Empty Set: " + emptySet);

        Set<String> singleSet = Set.of("Peach"); // of(E e1)
        System.out.println("Single-element Set: " + singleSet);

        Set<String> multiSet = Set.of("Apple", "Banana", "Cherry"); // of(E... elements)
        System.out.println("Multi-element Set: " + multiSet);

        Set<String> originalFruits = new HashSet<>(Arrays.asList("Apple", "Mango"));
        Set<String> copySet = Set.copyOf(originalFruits); // copyOf(Collection)
        System.out.println("Copy of originalFruits: " + copySet);

        // ===================================================
        // 2️⃣ Immutability of Factory-Created Sets
        // ===================================================
        // Attempting to modify unmodifiable sets throws exception

        try {
            singleSet.add("Mango");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable set: " + e);
        }

        try {
            copySet.remove("Apple");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot remove from unmodifiable set: " + e);
        }
    }
}
