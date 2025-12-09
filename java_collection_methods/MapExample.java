
import java.util.*;

public class MapExample {
    public static void main(String[] args) {
        // ===================================================
        // 1️⃣ Adding / Updating Entries
        // ===================================================
        // put() -> adds or updates key-value pair
        // putAll() -> adds all entries from another map
        // putIfAbsent() -> adds only if key doesn't exist

        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.putAll(Map.of("Mango", 30, "Peach", 12));
        map.putIfAbsent("Cherry", 15);
        map.put("Apple", 25);

        System.out.println("After additions: " + map);

        // ===================================================
        // 2️⃣ Retrieving Values
        // ===================================================
        // get() -> retrieves value by key
        // getOrDefault() -> retrieves value or default if absent

        System.out.println("Apple value: " + map.get("Apple"));
        System.out.println("Pear value (default 0): " + map.getOrDefault("Pear", 0));

        // ===================================================
        // 3️⃣ Removing Entries
        // ===================================================
        // remove(key) -> removes entry by key
        // remove(key, value) -> removes only if key maps to value
        // clear() -> removes all entries

        map.remove("Banana");
        map.remove("Cherry", 15);
        System.out.println("After removals: " + map);

        // ===================================================
        // 4️⃣ Checking / Querying
        // ===================================================
        // containsKey() -> checks if key exists
        // containsValue() -> checks if value exists
        // isEmpty() -> checks if map is empty
        // size() -> returns number of entries

        System.out.println("Contains key Mango? " + map.containsKey("Mango"));
        System.out.println("Contains value 12? " + map.containsValue(12));
        System.out.println("Is map empty? " + map.isEmpty());
        System.out.println("Map size: " + map.size());

        // ===================================================
        // 5️⃣ Collection Views
        // ===================================================
        // keySet() -> returns set of all keys
        // values() -> returns collection of all values
        // entrySet() -> returns set of key-value pairs

        System.out.println("Keys (keySet): " + map.keySet());
        System.out.println("Values (values): " + map.values());
        System.out.println("Entries (entrySet): " + map.entrySet());

        // ===================================================
        // 6️⃣ Compute Operations (Java 8+)
        // ===================================================
        // compute() -> computes new value for key
        // computeIfAbsent() -> computes value if key absent
        // computeIfPresent() -> computes value if key present

        map.compute("Apple", (k, v) -> v + 10);
        map.computeIfAbsent("Pear", k -> 5);
        map.computeIfPresent("Mango", (k, v) -> v + 5);

        System.out.println("After compute operations: " + map);

        // ===================================================
        // 7️⃣ Replace Operations (Java 8+)
        // ===================================================
        // replace(K, V) -> replaces value for key
        // replace(K, oldV, newV) -> replaces only if old value matches
        // replaceAll() -> applies function to all values

        map.replace("Mango", 35);
        map.replace("Peach", 12, 15);
        map.replaceAll((k, v) -> v * 2); // replaceAll(BiFunction)

        System.out.println("After replace operations: " + map);

        // ===================================================
        // 8️⃣ Merge Operation (Java 8+)
        // ===================================================
        map.merge("Pear", 10, Integer::sum); // merge(K, V, BiFunction)
        System.out.println("After merge: " + map);

        // ===================================================
        // 9️⃣ Iteration (Java 8+)
        // ===================================================
        System.out.println("Iteration using forEach:");
        map.forEach((k, v) -> System.out.println(k + " -> " + v));

        // ===================================================
        // 🔟 Object Methods
        // ===================================================
        Map<String, Integer> anotherMap = new HashMap<>(); // empty map for comparison
        System.out.println("Maps equal? " + map.equals(anotherMap)); // equals(Object)
        System.out.println("Map hashCode: " + map.hashCode()); // hashCode()

        Map<String, Integer> clonedMap = new TreeMap<>(map); // clone via copy constructor
        System.out.println("Maps equal? " + map.equals(clonedMap)); // equals(Object)
        System.out.println("Map hashCode: " + map.hashCode()); // hashCode()

        // ===================================================
        // Clear Method Demonstration
        // ===================================================
        Map<String, Integer> copyMap = new HashMap<>(map); // copy to safely test clear
        System.out.println("Copy before clear: " + copyMap);
        copyMap.clear();
        System.out.println("Copy after clear: " + copyMap);
        System.out.println("Original map remains intact: " + map);
    }
}
