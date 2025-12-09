<div style="
  position: sticky;
  top: 0;
  background: #f8f9fa;
  padding: 10px 15px;
  z-index: 100;
  border-bottom: 2px solid #007bff;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  font-family: Arial, sans-serif;
  border-radius: 0 0 8px 8px;
">
  <a href="README.md" style="
    text-decoration: none;
    color: #007bff;
    font-weight: bold;
    font-size: 14px;
  ">
    ← Back to Home
  </a>
</div>

# **Map Implementations Deep Dive**

---

## **4.1 HashMap**

**Definition:** Hash table based implementation of the **Map** interface. Provides **constant-time performance** for basic operations (get, put). Permits **null values and one null key**.

**Key Characteristics:**

- ✅ **Hash table based** - array of buckets (linked lists/trees)
- ✅ **Fast operations** - O(1) average for get, put, remove
- ✅ **Allows one null key** and **multiple null values**
- ❌ **Unordered** - no guaranteed iteration order
- ❌ **Not synchronized** - not thread-safe
- ⚡ **Initial capacity:** 16 (default)
- ⚡ **Load factor:** 0.75 (default) - resizes when 75% full
- ⚡ **Java 8+ optimization:** Buckets convert to **trees** when > 8 elements (improved worst-case)

---

#### **HashMap Internal Structure**

**Basic Structure:**

```
Array of Buckets (default 16)
│
├─ Bucket 0: Node → Node → Node (linked list)
├─ Bucket 1: Node → Node (linked list)
├─ Bucket 2: (empty)
├─ Bucket 3: TreeNode (Red-Black tree) ← Java 8+ optimization
└─ ...
```

**Node Structure:**

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;        // Cached hash code
    final K key;           // Key
    V value;               // Value
    Node<K,V> next;        // Next node in bucket (linked list)
}
```

**Java 8+ Tree Optimization:**

- When bucket has **≥ 8 elements** → converts to **Red-Black tree** (O(log n) worst-case)
- When bucket shrinks to **≤ 6 elements** → converts back to **linked list**

---

#### **HashMap-Specific Characteristics**

**Constructor Options (Interview Important):**

| Constructor                                      | Purpose                               | Example                      |
| ------------------------------------------------ | ------------------------------------- | ---------------------------- |
| `HashMap()`                                      | Default capacity 16, load factor 0.75 | `new HashMap<>()`            |
| `HashMap(int initialCapacity)`                   | Custom capacity, default load factor  | `new HashMap<>(100)`         |
| `HashMap(int initialCapacity, float loadFactor)` | Custom capacity and load factor       | `new HashMap<>(100, 0.9f)`   |
| `HashMap(Map<? extends K,? extends V> m)`        | From another map                      | `new HashMap<>(existingMap)` |

**How HashMap Works (Interview Critical):**

1. **put(K key, V value):**

   ```
   1. Compute hash code: hash = hash(key.hashCode())
   2. Find bucket index: index = hash & (capacity - 1)
   3. Check if key exists in bucket (equals())
   4. If exists: replace value
   5. If not exists: add new node
   6. If size > threshold: resize (double capacity)
   ```

2. **get(Object key):**
   ```
   1. Compute hash code
   2. Find bucket index
   3. Search bucket for matching key (equals())
   4. Return value or null
   ```

**Load Factor Impact:**

| Load Factor        | Memory Usage | Performance | Collision Risk |
| ------------------ | ------------ | ----------- | -------------- |
| **0.5** (low)      | ⬆️ High      | ⬆️ Faster   | ⬇️ Lower       |
| **0.75** (default) | ➡️ Balanced  | ➡️ Balanced | ➡️ Balanced    |
| **1.0** (high)     | ⬇️ Lower     | ⬇️ Slower   | ⬆️ Higher      |

---

#### **Performance Summary (Interview)**

| Operation              | Average Case | Worst Case (Pre-Java 8) | Worst Case (Java 8+) |
| ---------------------- | ------------ | ----------------------- | -------------------- |
| `put(k, v)`            | **O(1)**     | **O(n)**                | **O(log n)**         |
| `get(key)`             | **O(1)**     | **O(n)**                | **O(log n)**         |
| `remove(key)`          | **O(1)**     | **O(n)**                | **O(log n)**         |
| `containsKey(key)`     | **O(1)**     | **O(n)**                | **O(log n)**         |
| `containsValue(value)` | **O(n)**     | **O(n)**                | **O(n)**             |
| `size()`               | **O(1)**     | **O(1)**                | **O(1)**             |

**Notes:**

- Worst case occurs with **many hash collisions** (all keys in same bucket)
- Java 8+ tree optimization improves worst case from O(n) to O(log n)

---

#### **Interview Questions & Answers**

**Q1: How does HashMap work internally?**

- **A:** Uses **array of buckets**. Each bucket is a linked list (or tree in Java 8+). Key's hash code determines bucket index. Collisions handled by chaining.

**Q2: What is hash collision and how does HashMap handle it?**

- **A:** Collision = multiple keys map to same bucket. Handled by **chaining** (linked list). Java 8+ converts to **tree** when bucket has ≥8 elements.

**Q3: Why does HashMap require good hashCode() and equals()?**

- **A:**
  - `hashCode()`: Determines bucket (must be consistent)
  - `equals()`: Compares keys within bucket (must be accurate)
  - Poor implementation → all keys in one bucket → O(n) performance

**Q4: Can HashMap have null keys and values?**

- **A:** Yes! **One null key** (stored in bucket 0) and **multiple null values** allowed.

**Q5: What happens when HashMap resizes?**

- **A:** When size > capacity × loadFactor:
  1. Doubles capacity (16→32→64...)
  2. Rehashes all entries (recalculates bucket indices)
  3. Redistributes entries to new buckets
  4. O(n) operation, but amortized O(1) per put

**Q6: How to make HashMap thread-safe?**

- **A:**

  ```java
  // Option 1: Synchronized wrapper (low concurrency)
  Map<K,V> syncMap = Collections.synchronizedMap(new HashMap<>());

  // Option 2: ConcurrentHashMap (high concurrency - recommended)
  Map<K,V> concurrentMap = new ConcurrentHashMap<>();
  ```

**Q8: What is the significance of capacity being power of 2?**

- **A:** Enables fast modulo operation: `index = hash & (capacity - 1)` (bitwise AND) instead of `hash % capacity` (expensive modulo).

**Q9: Why does Java 8+ convert buckets to trees?**

- **A:** Improves worst-case from O(n) to O(log n) when many collisions (e.g., DOS attack with crafted keys).

**Q10: What happens if you modify a key object after adding to HashMap?**

- **A:** ⚠️ **Breaks HashMap!** Hash code changes → bucket index changes → key becomes "lost" (cannot be found via get()).

---

#### **Usage Examples**

**Basic Operations:**

```java
Map<String, Integer> map = new HashMap<>();
map.put("Apple", 1);
map.put("Banana", 2);
map.put("Cherry", 3);
map.put(null, 0);        // Null key allowed

System.out.println(map.get("Apple"));      // 1
System.out.println(map.containsKey("Banana")); // true
System.out.println(map.size());            // 4
map.remove("Cherry");
```

**Efficient Iteration:**

```java
// ✅ Best: Iterate over entrySet
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// ❌ Avoid: Iterate over keySet (extra lookups)
for (String key : map.keySet()) {
    Integer value = map.get(key); // Extra O(1) lookup per iteration
}
```

**Compute Methods (Java 8+):**

```java
Map<String, Integer> wordCount = new HashMap<>();

// Count word occurrences
wordCount.compute("apple", (k, v) -> v == null ? 1 : v + 1);

// computeIfAbsent: Initialize if absent
wordCount.computeIfAbsent("banana", k -> 0);

// computeIfPresent: Update if present
wordCount.computeIfPresent("apple", (k, v) -> v + 1);

// merge: Combine values
wordCount.merge("cherry", 1, Integer::sum); // Add 1, or sum if exists
```

**getOrDefault (Java 8+):**

```java
int count = map.getOrDefault("unknown", 0); // Returns 0 if key not found
```

---

#### **When to Use**

✅ **Use HashMap when:**

- Need **fast key-value lookups** (O(1))
- **No ordering** required
- **Single-threaded** or externally synchronized
- Need **null keys/values**
- Implementing **cache, index, lookup table**

❌ **Avoid when:**

- Need **sorted order** (use TreeMap)
- Need **insertion order** (use LinkedHashMap)
- Need **thread-safety** (use ConcurrentHashMap)
- Keys are **mutable** (dangerous - can break map)

---

## **4.2 LinkedHashMap**

**Definition:** Hash table and linked list implementation of **Map** interface. Maintains a **doubly-linked list** of entries, preserving **insertion order** (or optionally **access order**).

**Key Characteristics:**

- ✅ **Hash table + doubly-linked list** - combines HashMap performance with ordering
- ✅ **Insertion order** (default) - iterates in order entries were added
- ✅ **Access order** (optional) - iterates in order entries were accessed (useful for LRU cache)
- ✅ **Fast operations** - O(1) for get, put, remove (like HashMap)
- ✅ **Allows one null key** and **multiple null values**
- ❌ **Not synchronized** - not thread-safe
- ⚡ **Slightly slower than HashMap** - linked list maintenance overhead
- ⚡ **Higher memory overhead** - each entry stores before/after pointers

---

#### **LinkedHashMap-Specific Characteristics**

**Constructor Options (Interview Important):**

| Constructor                                                                 | Purpose                                       | Example                                |
| --------------------------------------------------------------------------- | --------------------------------------------- | -------------------------------------- |
| `LinkedHashMap()`                                                           | Insertion order, default capacity/load factor | `new LinkedHashMap<>()`                |
| `LinkedHashMap(int initialCapacity)`                                        | Insertion order, custom capacity              | `new LinkedHashMap<>(100)`             |
| `LinkedHashMap(int initialCapacity, float loadFactor)`                      | Insertion order, custom capacity/load factor  | `new LinkedHashMap<>(100, 0.75f)`      |
| `LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)` | **Access order mode**                         | `new LinkedHashMap<>(16, 0.75f, true)` |
| `LinkedHashMap(Map<? extends K,? extends V> m)`                             | From another map                              | `new LinkedHashMap<>(existingMap)`     |

**Access Order Mode (Interview Important):**

```java
// accessOrder = true: Iterate in LRU order (least-recently-used first)
LinkedHashMap<String, Integer> lruMap = new LinkedHashMap<>(16, 0.75f, true);

lruMap.put("A", 1);
lruMap.put("B", 2);
lruMap.put("C", 3);
lruMap.get("A"); // Access "A" → moves to end

// Iteration order: B, C, A (A was accessed last)
```

**LRU Cache Implementation:**

```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LRUCache(int maxSize) {
        super(16, 0.75f, true); // Access order mode
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize; // Remove oldest when size exceeds max
    }
}

// Usage
LRUCache<String, String> cache = new LRUCache<>(3);
cache.put("1", "One");
cache.put("2", "Two");
cache.put("3", "Three");
cache.put("4", "Four"); // Removes "1" (least recently used)
```

---

#### **Performance Summary (Interview)**

| Operation          | Complexity | Notes                                           |
| ------------------ | ---------- | ----------------------------------------------- |
| `put(k, v)`        | **O(1)**   | Same as HashMap + link update                   |
| `get(key)`         | **O(1)**   | Same as HashMap (+ move to end if access order) |
| `remove(key)`      | **O(1)**   | Same as HashMap + unlink                        |
| `containsKey(key)` | **O(1)**   | Same as HashMap                                 |
| `iterator.next()`  | **O(1)**   | Follows linked list (predictable order)         |

**Note:** Slightly slower than HashMap due to linked list maintenance.

---

#### **Interview Questions & Answers**

**Q1: LinkedHashMap vs HashMap - what's the difference?**

- **A:**
  - **HashMap:** Unordered, fastest
  - **LinkedHashMap:** Insertion/access order maintained, slightly slower

**Q2: What are the two ordering modes in LinkedHashMap?**

- **A:**
  1. **Insertion order** (default): Iterates in order entries were added
  2. **Access order**: Iterates in order entries were accessed (get/put updates order)

**Q3: How do you implement LRU cache with LinkedHashMap?**

- **A:** Use **access order mode** + override `removeEldestEntry()`:
  ```java
  new LinkedHashMap<K,V>(capacity, 0.75f, true) {
      protected boolean removeEldestEntry(Map.Entry eldest) {
          return size() > maxSize;
      }
  };
  ```

**Q4: What is the memory overhead vs HashMap?**

- **A:** ~**20-30% more memory** - each entry stores 2 additional references (before/after pointers).

**Q5: Can you change from insertion order to access order after creation?**

- **A:** **No!** Order mode is set at construction time and cannot be changed.

---

#### **Usage Examples**

**Insertion Order (Default):**

```java
Map<String, Integer> map = new LinkedHashMap<>();
map.put("C", 3);
map.put("A", 1);
map.put("B", 2);

// Iterates in insertion order: C, A, B
for (String key : map.keySet()) {
    System.out.println(key);
}
```

**Access Order Mode:**

```java
Map<String, Integer> map = new LinkedHashMap<>(16, 0.75f, true);
map.put("A", 1);
map.put("B", 2);
map.put("C", 3);

map.get("A"); // Access A → moves to end

// Iteration order: B, C, A
```

**Simple LRU Cache:**

```java
Map<String, String> lruCache = new LinkedHashMap<String, String>(16, 0.75f, true) {
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
        return size() > 100; // Max 100 entries
    }
};
```

---

#### **When to Use**

✅ **Use LinkedHashMap when:**

- Need **fast operations** (O(1)) **AND** **predictable iteration order**
- Implementing **LRU cache** (with access order mode)
- Need to **preserve insertion order** (e.g., configuration maps)
- Need **ordered map** without sorting overhead

❌ **Avoid when:**

- **Don't care about order** (use HashMap - faster, less memory)
- Need **sorted order** (use TreeMap)
- **Memory-constrained** (HashMap uses less memory)
- Need **thread-safety** (no concurrent LinkedHashMap alternative)

---

## **4.3 TreeMap**

**Definition:** **Red-Black tree** based implementation of **NavigableMap**. Entries are sorted according to **natural ordering** of keys or by a **Comparator**.

**Key Characteristics:**

- ✅ **Red-Black tree** - self-balancing binary search tree
- ✅ **Sorted order** - always maintains sorted order by keys
- ✅ **NavigableMap operations** - ceilingKey, floorKey, subMap, etc.
- ✅ **No duplicates** - guaranteed by Map contract (keys unique)
- ❌ **Does NOT allow null keys** - throws NullPointerException (comparison required)
- ✅ **Allows null values**
- ❌ **Not synchronized** - not thread-safe
- ⚡ **O(log n) operations** - slower than HashMap but provides sorting

---

#### **TreeMap-Specific Characteristics**

**Constructor Options (Interview Important):**

| Constructor                                 | Purpose                            | Example                                    |
| ------------------------------------------- | ---------------------------------- | ------------------------------------------ |
| `TreeMap()`                                 | Natural ordering                   | `new TreeMap<>()`                          |
| `TreeMap(Comparator<? super K> comparator)` | Custom ordering                    | `new TreeMap<>(Comparator.reverseOrder())` |
| `TreeMap(Map<? extends K,? extends V> m)`   | From another map, natural ordering | `new TreeMap<>(existingMap)`               |
| `TreeMap(SortedMap<K,? extends V> m)`       | From sorted map, same ordering     | `new TreeMap<>(sortedMap)`                 |

**Ordering Requirements:**

- **Natural ordering:** Keys must implement `Comparable<K>`
- **Custom ordering:** Provide `Comparator<K>` at construction
- **Consistent with equals:** `compareTo()`/`compare()` should be consistent with `equals()`

---

#### **NavigableMap-Specific Methods (Interview Important)**

**Range View Operations:**

| Method                                | Purpose                      | Returns                |
| ------------------------------------- | ---------------------------- | ---------------------- |
| `SortedMap<K,V> headMap(K toKey)`     | Entries with keys < toKey    | View (changes reflect) |
| `SortedMap<K,V> tailMap(K fromKey)`   | Entries with keys ≥ fromKey  | View (changes reflect) |
| `SortedMap<K,V> subMap(K from, K to)` | Entries with keys [from, to) | View (changes reflect) |

**NavigableMap Operations:**

| Method                               | Purpose                    | Complexity |
| ------------------------------------ | -------------------------- | ---------- |
| `K firstKey()`                       | Returns first (lowest) key | O(log n)   |
| `K lastKey()`                        | Returns last (highest) key | O(log n)   |
| `K lowerKey(K key)`                  | Largest key < key          | O(log n)   |
| `K floorKey(K key)`                  | Largest key ≤ key          | O(log n)   |
| `K ceilingKey(K key)`                | Smallest key ≥ key         | O(log n)   |
| `K higherKey(K key)`                 | Smallest key > key         | O(log n)   |
| `Map.Entry<K,V> firstEntry()`        | Returns first entry        | O(log n)   |
| `Map.Entry<K,V> lastEntry()`         | Returns last entry         | O(log n)   |
| `Map.Entry<K,V> pollFirstEntry()`    | Remove and return first    | O(log n)   |
| `Map.Entry<K,V> pollLastEntry()`     | Remove and return last     | O(log n)   |
| `NavigableMap<K,V> descendingMap()`  | Reverse view               | O(1)       |
| `NavigableSet<K> descendingKeySet()` | Reverse key set            | O(1)       |

---

#### **Performance Summary (Interview)**

| Operation                     | Complexity             | Reason                         |
| ----------------------------- | ---------------------- | ------------------------------ |
| `put(k, v)`                   | **O(log n)**           | Tree insertion + balancing     |
| `get(key)`                    | **O(log n)**           | Binary search in tree          |
| `remove(key)`                 | **O(log n)**           | Tree removal + balancing       |
| `containsKey(key)`            | **O(log n)**           | Binary search                  |
| `firstKey()` / `lastKey()`    | **O(log n)**           | Navigate to leftmost/rightmost |
| `ceilingKey()` / `floorKey()` | **O(log n)**           | Tree navigation                |
| `iterator.next()`             | **O(log n)** amortized | In-order traversal             |

---

#### **Interview Questions & Answers**

**Q1: TreeMap vs HashMap - when to use which?**

- **A:**
  - **HashMap:** Fast operations (O(1)), no ordering
  - **TreeMap:** Sorted order (O(log n)), range queries, NavigableMap operations

**Q2: Can TreeMap have null keys?**

- **A:** **No!** Throws `NullPointerException` because keys must be comparable (null cannot be compared). **Null values are allowed**.

**Q3: What data structure does TreeMap use?**

- **A:** **Red-Black Tree** (self-balancing binary search tree) - guarantees O(log n) operations.

**Q4: TreeMap vs TreeSet - what's the relationship?**

- **A:** TreeSet is **backed by TreeMap**. TreeSet stores elements as keys, value is dummy `PRESENT` object.

**Q5: What happens if keys are not Comparable?**

- **A:** Throws `ClassCastException` at runtime when adding second entry:
  ```java
  TreeMap<Object, String> map = new TreeMap<>();
  map.put(new Object(), "A"); // OK (first entry)
  map.put(new Object(), "B"); // ClassCastException!
  ```

**Q6: How to create descending order TreeMap?**

- **A:** Use `Collections.reverseOrder()` or `Comparator.reverseOrder()`:
  ```java
  TreeMap<Integer, String> descending = new TreeMap<>(Collections.reverseOrder());
  ```

**Q7: What does "consistent with equals" mean?**

- **A:** `(k1.compareTo(k2) == 0)` should have same boolean value as `k1.equals(k2)`. Violating this can cause unexpected behavior.

**Q8: Can you use mutable objects as keys in TreeMap?**

- **A:** ⚠️ **Dangerous!** If key's comparison logic changes after insertion, tree structure breaks (keys become unsorted).

---

#### **Usage Examples**

**Natural Ordering:**

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(3, "Three");
map.put(1, "One");
map.put(2, "Two");

System.out.println(map); // {1=One, 2=Two, 3=Three} - sorted by keys!
```

**Custom Comparator:**

```java
// Sort by string length
TreeMap<String, Integer> map = new TreeMap<>(Comparator.comparingInt(String::length));
map.put("apple", 1);
map.put("pie", 2);
map.put("banana", 3);

System.out.println(map.keySet()); // [pie, apple, banana] - by length
```

**NavigableMap Operations:**

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(1, "One");
map.put(3, "Three");
map.put(5, "Five");
map.put(7, "Seven");
map.put(9, "Nine");

System.out.println(map.firstKey());      // 1
System.out.println(map.lastKey());       // 9
System.out.println(map.lowerKey(5));     // 3 (largest < 5)
System.out.println(map.floorKey(5));     // 5 (largest ≤ 5)
System.out.println(map.ceilingKey(6));   // 7 (smallest ≥ 6)
System.out.println(map.higherKey(5));    // 7 (smallest > 5)
```

**Range Views:**

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(1, "One");
map.put(2, "Two");
map.put(3, "Three");
map.put(4, "Four");
map.put(5, "Five");

SortedMap<Integer, String> headMap = map.headMap(3);   // {1=One, 2=Two}
SortedMap<Integer, String> tailMap = map.tailMap(3);   // {3=Three, 4=Four, 5=Five}
SortedMap<Integer, String> subMap = map.subMap(2, 5);  // {2=Two, 3=Three, 4=Four}

// Views reflect changes
map.put(0, "Zero");
System.out.println(headMap); // {0=Zero, 1=One, 2=Two}
```

**Descending Order:**

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(1, "One");
map.put(3, "Three");
map.put(5, "Five");

// Descending map view
NavigableMap<Integer, String> descMap = map.descendingMap();
System.out.println(descMap); // {5=Five, 3=Three, 1=One}

// Descending key set
NavigableSet<Integer> descKeys = map.descendingKeySet();
System.out.println(descKeys); // [5, 3, 1]
```

---

#### **When to Use**

✅ **Use TreeMap when:**

- Need **sorted order by keys**
- Need **range queries** (headMap, tailMap, subMap)
- Need **NavigableMap operations** (ceilingKey, floorKey, etc.)
- Need **first/last entry** access
- Implementing **sorted key-value collection**

❌ **Avoid when:**

- **Don't need ordering** (use HashMap - much faster O(1))
- Need **insertion order** (use LinkedHashMap)
- Keys cannot be compared (no Comparable/Comparator)
- Need **null keys** (use HashMap or LinkedHashMap)

---

## **4.4 ConcurrentHashMap**

**Definition:** **Thread-safe** hash table implementation supporting **full concurrency** for retrievals and **high expected concurrency** for updates. Part of **java.util.concurrent** package.

**Key Characteristics:**

- ✅ **Thread-safe** - designed for concurrent access
- ✅ **Fine-grained locking** - lock-free reads, segment-level writes (Java 7), node-level writes (Java 8+)
- ✅ **High concurrency** - multiple threads can read/write simultaneously
- ✅ **No blocking reads** - retrievals don't block
- ❌ **Does NOT allow null keys or values** - throws NullPointerException
- ⚡ **Better than synchronized HashMap** - fine-grained locking

---

#### **ConcurrentHashMap Evolution**

**Java 7: Segment-Based Locking**

```
Divided into segments (default 16)
Each segment is independently lockable
Allows 16 concurrent writes (one per segment)
```

**Java 8+: Node-Based Locking (CAS + Synchronized)**

```
Lock-free reads (volatile reads)
CAS (Compare-And-Swap) for updates when possible
Synchronized blocks only for specific nodes when needed
Better scalability - more concurrent writes
```

---

#### **ConcurrentHashMap-Specific Methods**

**Atomic Operations (Interview Important):**

| Method                                           | Purpose                              | Atomicity |
| ------------------------------------------------ | ------------------------------------ | --------- |
| `V putIfAbsent(K key, V value)`                  | Put only if key not present          | ✅ Atomic |
| `boolean remove(Object key, Object value)`       | Remove only if key maps to value     | ✅ Atomic |
| `boolean replace(K key, V oldValue, V newValue)` | Replace only if key maps to oldValue | ✅ Atomic |
| `V replace(K key, V value)`                      | Replace only if key present          | ✅ Atomic |

**Bulk Operations (Java 8+):**

| Method                                                               | Purpose                            |
| -------------------------------------------------------------------- | ---------------------------------- |
| `void forEach(BiConsumer<K,V> action)`                               | Perform action on each entry       |
| `V search(BiFunction<K,V,V> searchFunction)`                         | Search entries, return first match |
| `V reduce(BiFunction<K,V,V> transformer, BiFunction<V,V,V> reducer)` | Reduce entries to single result    |

**Concurrent Modifications (Java 8+):**

| Method                                                  | Purpose            | Atomicity |
| ------------------------------------------------------- | ------------------ | --------- |
| `compute(K key, BiFunction remappingFunction)`          | Compute new value  | ✅ Atomic |
| `computeIfAbsent(K key, Function mappingFunction)`      | Compute if absent  | ✅ Atomic |
| `computeIfPresent(K key, BiFunction remappingFunction)` | Compute if present | ✅ Atomic |
| `merge(K key, V value, BiFunction remappingFunction)`   | Merge values       | ✅ Atomic |

---

#### **Performance Summary (Interview)**

| Operation          | Complexity | Concurrency                             |
| ------------------ | ---------- | --------------------------------------- |
| `get(key)`         | **O(1)**   | ✅ Lock-free (no blocking)              |
| `put(k, v)`        | **O(1)**   | ✅ High (node-level locking in Java 8+) |
| `remove(key)`      | **O(1)**   | ✅ High (node-level locking)            |
| `containsKey(key)` | **O(1)**   | ✅ Lock-free                            |
| `size()`           | **O(n)\*** | ⚠️ Approximate (weakly consistent)      |

`*` **Note:** `size()` is approximate in concurrent context (may not reflect in-progress updates).

---

#### **Interview Questions & Answers**

**Q1: ConcurrentHashMap vs synchronized HashMap?**

- **A:** **ConcurrentHashMap** is better:
  - **Synchronized HashMap:** Locks entire map for every operation
  - **ConcurrentHashMap:** Lock-free reads, fine-grained write locks

**Q2: Why doesn't ConcurrentHashMap allow null keys/values?**

- **A:** **Ambiguity in concurrent context:**
  ```java
  V value = map.get(key);
  // Is value null because:
  // 1. Key not present? OR
  // 2. Key present with null value?
  // Cannot distinguish without additional locking!
  ```

**Q4: How does ConcurrentHashMap achieve high concurrency?**

- **A:**
  - **Reads:** Lock-free (volatile reads)
  - **Writes:** CAS operations when possible, fine-grained synchronized blocks only when needed
  - **Java 8+:** Node-level locking (not segment-level)

**Q5: Is size() accurate in ConcurrentHashMap?**

- **A:** **No!** Returns **approximate** count. May not reflect in-progress updates. Use for **estimation only**.

**Q6: What are the atomic methods in ConcurrentHashMap?**

- **A:** `putIfAbsent`, `remove(key, value)`, `replace`, `compute`, `computeIfAbsent`, `computeIfPresent`, `merge`

**Q7: When to use ConcurrentHashMap?**

- **A:** When multiple threads need to **read/write** map concurrently. Provides better performance than synchronized alternatives.

---

#### **Usage Examples**

**Basic Concurrent Operations:**

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Thread-safe put/get
map.put("Apple", 1);
Integer value = map.get("Apple");

// Atomic putIfAbsent
map.putIfAbsent("Banana", 2); // Only puts if "Banana" not present
```

**Atomic Compute Operations:**

```java
ConcurrentHashMap<String, Integer> wordCount = new ConcurrentHashMap<>();

// Thread-safe word counting
wordCount.compute("apple", (k, v) -> v == null ? 1 : v + 1);

// Or using merge (simpler)
wordCount.merge("apple", 1, Integer::sum);
```

**Bulk Operations (Java 8+):**

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("A", 1);
map.put("B", 2);
map.put("C", 3);

// forEach (parallel)
map.forEach(1, (k, v) -> System.out.println(k + ": " + v));

// search (parallel)
Integer result = map.search(1, (k, v) -> v > 2 ? v : null); // Returns 3

// reduce (parallel)
Integer sum = map.reduce(1, (k, v) -> v, Integer::sum); // Sum all values
```

**ConcurrentHashMap as Set:**

```java
// Create concurrent set
Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
concurrentSet.add("Apple");
concurrentSet.add("Banana");
```

---

#### **When to Use**

✅ **Use ConcurrentHashMap when:**

- Need **thread-safe map** with **high concurrency**
- Multiple threads **reading and writing** simultaneously
- Need **atomic operations** (putIfAbsent, compute, etc.)
- Performance critical concurrent application

❌ **Avoid when:**

- **Single-threaded** application (use HashMap - no overhead)
- Need **null keys/values** (use synchronized HashMap)
- Need **exact size()** (ConcurrentHashMap's size is approximate)

---

## **Comparison: HashMap vs LinkedHashMap vs TreeMap vs ConcurrentHashMap**

| Feature            | HashMap      | LinkedHashMap             | TreeMap         | ConcurrentHashMap     |
| ------------------ | ------------ | ------------------------- | --------------- | --------------------- |
| **Data Structure** | Hash table   | Hash table + linked list  | Red-Black tree  | Hash table + CAS      |
| **Ordering**       | ❌ Unordered | ✅ Insertion/Access order | ✅ Sorted order | ❌ Unordered          |
| **Get**            | O(1)         | O(1)                      | O(log n)        | O(1)                  |
| **Put**            | O(1)         | O(1)                      | O(log n)        | O(1)                  |
| **Remove**         | O(1)         | O(1)                      | O(log n)        | O(1)                  |
| **Null keys**      | ✅ One       | ✅ One                    | ❌ No           | ❌ No                 |
| **Null values**    | ✅ Yes       | ✅ Yes                    | ✅ Yes          | ❌ No                 |
| **Thread-Safe**    | ❌ No        | ❌ No                     | ❌ No           | ✅ Yes (fine-grained) |
| **Concurrency**    | N/A          | N/A                       | N/A             | ⬆️ High               |
| **Memory**         | Low          | Medium                    | Medium          | Medium                |
| **Since**          | 1.2          | 1.4                       | 1.2             | 1.5                   |
| **Best for**       | General use  | Insertion order           | Sorted keys     | Concurrent access     |

---

## **Quick Decision Guide**

```
Need Map implementation?
├─ Need sorted order by keys? → TreeMap
│
├─ Need insertion/access order? → LinkedHashMap
│   └─ LRU cache? → LinkedHashMap(capacity, loadFactor, true)
│
├─ Need thread-safety?
│   ├─ High concurrency? → ConcurrentHashMap ⭐ (recommended)
│   └─ Simple sync? → Collections.synchronizedMap(HashMap)
│
└─ General use (single-threaded)?
    └─ HashMap ⭐ (fastest, default choice)
```

**Java 17 Recommendations:**

- ✅ **HashMap** - default choice (95% of use cases)
- ✅ **LinkedHashMap** - when order matters or implementing LRU cache
- ✅ **TreeMap** - when sorted keys or range queries needed
- ✅ **ConcurrentHashMap** - for concurrent access

---

## **Common Interview Scenarios**

### **Scenario 1: Implement Cache**

**Use:** `LinkedHashMap` with access order + `removeEldestEntry()`

### **Scenario 2: Count Word Frequencies**

**Use:** `HashMap<String, Integer>` with `merge(key, 1, Integer::sum)`

### **Scenario 3: Sorted Key-Value Store**

**Use:** `TreeMap` for sorted keys

### **Scenario 4: Concurrent Counter**

**Use:** `ConcurrentHashMap` with `compute()` or `merge()`

### **Scenario 5: Configuration Properties (Order Matters)**

**Use:** `LinkedHashMap` to preserve insertion order

### **Scenario 6: Range Queries on Keys**

**Use:** `TreeMap` with `subMap()`, `headMap()`, `tailMap()`

### **Scenario 7: Thread-Safe Shared Cache**

**Use:** `ConcurrentHashMap` for high-concurrency access

---

## **⚡ Performance Analysis – Map Implementations**

### **Detailed Time Complexity**

#### **HashMap Performance**

| Operation                     | Average Case | Worst Case | Explanation                                                        |
| ----------------------------- | ------------ | ---------- | ------------------------------------------------------------------ |
| `put(K key, V value)`         | **O(1)**     | **O(n)**   | Hash-based insertion, worst case when all keys hash to same bucket |
| `get(Object key)`             | **O(1)**     | **O(n)**   | Hash-based lookup                                                  |
| `remove(Object key)`          | **O(1)**     | **O(n)**   | Hash-based removal                                                 |
| `containsKey(Object key)`     | **O(1)**     | **O(n)**   | Hash-based lookup                                                  |
| `containsValue(Object value)` | **O(n)**     | **O(n)**   | Must scan all entries                                              |
| `keySet()`                    | **O(1)**     | **O(1)**   | Returns view (actual iteration is O(n))                            |
| `values()`                    | **O(1)**     | **O(1)**   | Returns view (actual iteration is O(n))                            |
| `entrySet()`                  | **O(1)**     | **O(1)**   | Returns view (actual iteration is O(n))                            |
| `size()`                      | **O(1)**     | **O(1)**   | Cached size field                                                  |
| `clear()`                     | **O(n)**     | **O(n)**   | Must clear all buckets                                             |
| `iterator.next()`             | **O(1)**     | **O(1)**   | Iterating through buckets                                          |

**Java 8+ Tree Optimization:**

- Buckets with **≥8 elements** convert to **Red-Black tree** (O(log n) worst case)
- Buckets shrink to **≤6 elements** convert back to **linked list**
- This improves worst-case from O(n) to O(log n) for large buckets

**Best Use Cases:**

- ✅ **Fast key-value lookups** (primary use case)
- ✅ **Caching** data by key
- ✅ **Counting/grouping** operations
- ✅ **Associative arrays**
- ✅ Allows **one null key** and **multiple null values**
- ❌ Unordered iteration
- ❌ Not thread-safe (use ConcurrentHashMap for concurrency)

**Load Factor & Capacity:**

```
Default initial capacity: 16
Default load factor: 0.75
Resize threshold: capacity × load factor
New capacity after resize: capacity × 2

Example progression:
16 → 32 → 64 → 128 → 256 → 512...

Resize operation is O(n) (rehash all entries)
```

**Hash Function Quality:**

```
Good hash function → Uniform distribution → O(1) operations
Poor hash function → All collisions → O(n) operations (degraded to linked list)

HashMap uses: (hash ^ (hash >>> 16)) to reduce collisions
```

---

#### **LinkedHashMap Performance**

| Operation                     | Average Case | Worst Case | Explanation                              |
| ----------------------------- | ------------ | ---------- | ---------------------------------------- |
| `put(K key, V value)`         | **O(1)**     | **O(n)**   | HashMap + link to doubly-linked list     |
| `get(Object key)`             | **O(1)**     | **O(n)**   | HashMap lookup (may update access order) |
| `remove(Object key)`          | **O(1)**     | **O(n)**   | HashMap removal + unlink from list       |
| `containsKey(Object key)`     | **O(1)**     | **O(n)**   | Same as HashMap                          |
| `containsValue(Object value)` | **O(n)**     | **O(n)**   | Must scan entries                        |
| `iterator.next()`             | **O(1)**     | **O(1)**   | Follow linked list order                 |
| `size()`                      | **O(1)**     | **O(1)**   | Cached size field                        |

**Ordering Modes:**

1. **Insertion order** (default): Maintains order elements were added
2. **Access order**: Moves accessed elements to end (useful for LRU cache)

**Best Use Cases:**

- ✅ **Predictable iteration order** (insertion or access order)
- ✅ **LRU cache** implementation (with access-order mode)
- ✅ Same performance as HashMap with ordering guarantee
- ✅ **Configuration properties** (preserve order)
- ✅ **Maintaining history** of operations
- ❌ Slightly higher memory overhead than HashMap

**Memory Overhead:**

```
HashMap: buckets + nodes
LinkedHashMap: buckets + nodes + doubly-linked list (2 additional references per entry)
```

---

#### **TreeMap Performance (SortedMap/NavigableMap)**

| Operation                     | Complexity     | Explanation                                   |
| ----------------------------- | -------------- | --------------------------------------------- |
| `put(K key, V value)`         | **O(log n)**   | Balanced Red-Black tree insertion             |
| `get(Object key)`             | **O(log n)**   | Binary search in balanced tree                |
| `remove(Object key)`          | **O(log n)**   | Balanced tree removal with rebalancing        |
| `containsKey(Object key)`     | **O(log n)**   | Binary search                                 |
| `containsValue(Object value)` | **O(n)**       | Must traverse entire tree                     |
| `firstKey()` / `lastKey()`    | **O(log n)**   | Navigate to leftmost/rightmost node           |
| `ceilingKey(K key)`           | **O(log n)**   | Find smallest key ≥ given key                 |
| `floorKey(K key)`             | **O(log n)**   | Find largest key ≤ given key                  |
| `higherKey(K key)`            | **O(log n)**   | Find smallest key > given key                 |
| `lowerKey(K key)`             | **O(log n)**   | Find largest key < given key                  |
| `subMap(K from, K to)`        | **O(log n)**   | Create view (iteration is O(k) for k entries) |
| `headMap(K to)`               | **O(log n)**   | Create view of entries with keys < to         |
| `tailMap(K from)`             | **O(log n)**   | Create view of entries with keys ≥ from       |
| `size()`                      | **O(1)**       | Cached size field                             |
| `iterator.next()`             | **O(log n)\*** | In-order tree traversal                       |

`*` Amortized **O(1)** per element over entire iteration (total O(n) for n elements)

**Best Use Cases:**

- ✅ **Sorted order by keys** required
- ✅ **Range queries** (subMap, headMap, tailMap)
- ✅ **NavigableMap operations** (ceiling, floor, higher, lower)
- ✅ **Find min/max keys** efficiently
- ✅ **Ordered iteration** over keys
- ✅ **Time-series data** (keys are timestamps)
- ❌ Slower than HashMap for basic operations (O(log n) vs O(1))
- ❌ Keys must be **Comparable** or use **Comparator**
- ❌ **No null keys** allowed (throws NullPointerException)
- ❌ Allows **null values**

**Tree Structure:**

```
Red-Black Tree properties:
- Self-balancing binary search tree
- Height: O(log n)
- All operations: O(log n)
- Maintains sorted order during modifications
```

---

#### **ConcurrentHashMap Performance**

| Operation               | Complexity       | Notes                                           |
| ----------------------- | ---------------- | ----------------------------------------------- |
| `put(K key, V value)`   | **O(1) average** | Lock-free reads, fine-grained locking on writes |
| `get(Object key)`       | **O(1) average** | Lock-free (no synchronization on reads)         |
| `remove(Object key)`    | **O(1) average** | Fine-grained locking                            |
| `compute()` / `merge()` | **O(1) average** | Atomic operations                               |

**Best Use Cases:**

- ✅ **High-concurrency** environments
- ✅ **Thread-safe map** for multi-threaded access
- ✅ **Shared cache** across threads
- ✅ **Lock-free reads** (excellent for read-heavy workloads)
- ✅ **Atomic operations** (compute, merge, replace)
- ❌ **No null keys or values** (throws NullPointerException)
- ❌ Slightly higher memory overhead

**Concurrency Features:**

```
- Segment-based locking (Java 7) → Node-based locking (Java 8+)
- Lock-free reads (volatile reads)
- Fine-grained write locking (only lock affected bucket)
- Better scalability than synchronized HashMap
```

---

### **Space Complexity Comparison**

| Implementation        | Memory Per Entry            | Extra Overhead                         | Notes                        |
| --------------------- | --------------------------- | -------------------------------------- | ---------------------------- |
| **HashMap**           | Key + Value + node          | Hash table buckets                     | Unused capacity waste        |
| **LinkedHashMap**     | Key + Value + node + 2 refs | Doubly-linked list                     | Additional ordering pointers |
| **TreeMap**           | Key + Value + node          | Tree nodes (parent, 2 children, color) | Red-Black tree overhead      |
| **ConcurrentHashMap** | Key + Value + node          | Additional concurrency fields          | Segment/node metadata        |

**Memory Estimate for 10,000 entries:**

- **HashMap:** ~400-500 KB (depends on load factor, keys/values)
- **LinkedHashMap:** ~500-600 KB (additional linked list)
- **TreeMap:** ~450-550 KB (tree node overhead)
- **ConcurrentHashMap:** ~450-600 KB (concurrency metadata)

---

### **Performance Tips for Map Implementations**

#### **✅ HashMap Best Practices**

1. **Pre-size for known capacity**:

   ```java
   // ✅ Avoid resizes for 10,000 entries
   // Formula: expectedSize / loadFactor + 1
   Map<String, Integer> map = new HashMap<>((int) (10000 / 0.75 + 1));

   // ❌ Starts at 16, resizes ~10 times
   Map<String, Integer> map = new HashMap<>();
   ```

2. **Choose appropriate load factor**:

   ```java
   // Default (balanced): 0.75
   Map<String, Integer> map = new HashMap<>(capacity, 0.75f);

   // Memory-constrained (more collisions): 1.0
   Map<String, Integer> map = new HashMap<>(capacity, 1.0f);

   // Speed-sensitive (less collisions): 0.5
   Map<String, Integer> map = new HashMap<>(capacity, 0.5f);
   ```

3. **Use compute methods for atomic updates**:

   ```java
   // ✅ Single lookup with computeIfAbsent
   map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);

   // ❌ Two lookups
   if (!map.containsKey(key)) {
       map.put(key, new ArrayList<>());
   }
   map.get(key).add(value);
   ```

4. **Use merge for counting/aggregation**:

   ```java
   // ✅ Efficient word counting
   for (String word : words) {
       map.merge(word, 1, Integer::sum);
   }

   // ❌ Verbose and less efficient
   for (String word : words) {
       map.put(word, map.getOrDefault(word, 0) + 1);
   }
   ```

5. **Iterate over entrySet, not keySet**:

   ```java
   // ✅ One lookup per entry
   for (Map.Entry<K, V> entry : map.entrySet()) {
       K key = entry.getKey();
       V value = entry.getValue();
   }

   // ❌ Two lookups per entry
   for (K key : map.keySet()) {
       V value = map.get(key); // Extra lookup!
   }
   ```

6. **Implement proper hashCode() and equals()**:

   ```java
   // ✅ Good hashCode implementation
   @Override
   public int hashCode() {
       return Objects.hash(field1, field2, field3);
   }

   @Override
   public boolean equals(Object obj) {
       // Proper equals implementation
   }

   // ❌ Poor hashCode (all objects → same bucket)
   @Override
   public int hashCode() {
       return 42; // Never do this!
   }
   ```

---

#### **✅ LinkedHashMap Best Practices**

1. **Use for insertion-order iteration**:

   ```java
   // Preserves insertion order
   Map<String, Integer> map = new LinkedHashMap<>();
   map.put("first", 1);
   map.put("second", 2);
   // Iteration: first → second
   ```

2. **Implement LRU cache**:

   ```java
   // LRU cache with access-order
   Map<String, String> cache = new LinkedHashMap<>(16, 0.75f, true) {
       @Override
       protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
           return size() > MAX_CACHE_SIZE; // Remove oldest when full
       }
   };
   ```

3. **Use for configuration properties**:
   ```java
   // Maintain order of properties
   Map<String, String> config = new LinkedHashMap<>();
   config.put("host", "localhost");
   config.put("port", "8080");
   // Iteration preserves insertion order
   ```

---

#### **✅ TreeMap Best Practices**

1. **Provide Comparator for custom ordering**:

   ```java
   // ✅ Custom comparator
   Map<String, Integer> map = new TreeMap<>(Comparator.comparing(String::length));

   // ✅ Reverse natural order
   Map<Integer, String> map = new TreeMap<>(Collections.reverseOrder());

   // ❌ ClassCastException if keys not Comparable
   Map<MyClass, String> map = new TreeMap<>(); // MyClass must be Comparable
   ```

2. **Use range operations**:

   ```java
   TreeMap<Integer, String> map = new TreeMap<>();
   map.put(1, "one");
   map.put(5, "five");
   map.put(10, "ten");
   map.put(15, "fifteen");

   // Get entries between 5 and 15
   Map<Integer, String> range = map.subMap(5, true, 15, true);

   // Get entries with keys < 10
   Map<Integer, String> head = map.headMap(10);

   // Get entries with keys ≥ 10
   Map<Integer, String> tail = map.tailMap(10);
   ```

3. **Use navigation methods**:

   ```java
   TreeMap<Integer, String> map = new TreeMap<>();
   // ... populate map

   map.firstKey();        // Smallest key
   map.lastKey();         // Largest key
   map.ceilingKey(7);     // Smallest key ≥ 7
   map.floorKey(7);       // Largest key ≤ 7
   map.higherKey(10);     // Smallest key > 10
   map.lowerKey(10);      // Largest key < 10

   map.firstEntry();      // Entry with smallest key
   map.lastEntry();       // Entry with largest key
   map.pollFirstEntry();  // Remove and return entry with smallest key
   map.pollLastEntry();   // Remove and return entry with largest key
   ```

4. **Time-series data storage**:

   ```java
   // Store events by timestamp
   TreeMap<Long, Event> events = new TreeMap<>();
   events.put(System.currentTimeMillis(), event);

   // Get events in last hour
   long oneHourAgo = System.currentTimeMillis() - 3600_000;
   Map<Long, Event> recentEvents = events.tailMap(oneHourAgo);
   ```

---

#### **✅ ConcurrentHashMap Best Practices**

1. **Use for thread-safe operations**:

   ```java
   // ✅ Thread-safe without external synchronization
   Map<String, Integer> map = new ConcurrentHashMap<>();

   // ❌ Slower alternative
   Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());
   ```

2. **Use atomic operations**:

   ```java
   // ✅ Atomic increment
   map.compute(key, (k, v) -> v == null ? 1 : v + 1);

   // ✅ Atomic merge
   map.merge(key, 1, Integer::sum);

   // ❌ Not atomic (race condition)
   Integer count = map.get(key);
   map.put(key, count == null ? 1 : count + 1);
   ```

3. **Use bulk operations for better performance**:

   ```java
   // Parallel forEach
   map.forEach(1000, (k, v) -> process(k, v)); // Parallelism threshold

   // Parallel search
   String result = map.search(1000, (k, v) -> condition(k, v) ? v : null);

   // Parallel reduce
   int sum = map.reduce(1000, (k, v) -> v, Integer::sum);
   ```

---

#### **⚠️ Common Performance Pitfalls**

1. **Iterating keySet() instead of entrySet()**:

   ```java
   // ❌ O(2n) - two lookups per entry
   for (String key : map.keySet()) {
       Integer value = map.get(key);
       process(key, value);
   }

   // ✅ O(n) - one lookup per entry
   for (Map.Entry<String, Integer> entry : map.entrySet()) {
       process(entry.getKey(), entry.getValue());
   }
   ```

2. **Not pre-sizing HashMap**:

   ```java
   // ❌ Resizes multiple times for 10,000 entries
   Map<String, Integer> map = new HashMap<>();
   for (int i = 0; i < 10000; i++) map.put(key, value);

   // ✅ No resizing
   Map<String, Integer> map = new HashMap<>(14000); // 10000/0.75 + margin
   for (int i = 0; i < 10000; i++) map.put(key, value);
   ```

3. **Using containsKey() before get()**:

   ```java
   // ❌ Two lookups
   if (map.containsKey(key)) {
       return map.get(key);
   }

   // ✅ One lookup
   Integer value = map.get(key);
   if (value != null) {
       return value;
   }

   // ✅ Or use getOrDefault
   return map.getOrDefault(key, defaultValue);
   ```

4. **Using TreeMap when HashMap would work**:

   ```java
   // ❌ O(n log n) operations when sorting not needed
   Map<String, Integer> map = new TreeMap<>();
   map.put(key, value); // O(log n)

   // ✅ O(n) operations
   Map<String, Integer> map = new HashMap<>();
   map.put(key, value); // O(1)
   ```

5. **Poor hashCode() implementation**:

   ```java
   // ❌ All objects hash to same bucket
   @Override
   public int hashCode() {
       return 1; // Degrades to O(n) operations!
   }

   // ✅ Good distribution
   @Override
   public int hashCode() {
       return Objects.hash(id, name, age);
   }
   ```

6. **Modifying keys after insertion**:

   ```java
   // ❌ Breaks HashMap internal structure
   MutableKey key = new MutableKey(1);
   map.put(key, "value");
   key.setValue(2); // Changes hashCode → map.get(key) may fail!

   // ✅ Use immutable keys
   ImmutableKey key = new ImmutableKey(1);
   map.put(key, "value");
   ```

7. **Using Hashtable for new code**:

   ```java
   // ❌ Legacy, synchronized overhead
   Map<String, Integer> map = new Hashtable<>();

   // ✅ Use HashMap (single-threaded) or ConcurrentHashMap (multi-threaded)
   Map<String, Integer> map = new HashMap<>();
   // or
   Map<String, Integer> map = new ConcurrentHashMap<>();
   ```

8. **Using synchronized HashMap for concurrency**:

   ```java
   // ❌ Poor concurrency (global lock)
   Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());

   // ✅ Better concurrency (fine-grained locking)
   Map<String, Integer> map = new ConcurrentHashMap<>();
   ```

---

### **HashMap vs LinkedHashMap vs TreeMap: When to Use What**

| Scenario                 | HashMap | LinkedHashMap | TreeMap | Reason                          |
| ------------------------ | ------- | ------------- | ------- | ------------------------------- |
| Fast lookups             | ✅      | ✅            | ❌      | O(1) vs O(log n)                |
| Preserve insertion order | ❌      | ✅            | ❌      | LinkedHashMap maintains order   |
| Sorted iteration by keys | ❌      | ❌            | ✅      | TreeMap maintains sorted order  |
| Range queries            | ❌      | ❌            | ✅      | subMap, headMap, tailMap        |
| Find min/max keys        | ❌      | ❌            | ✅      | firstKey(), lastKey()           |
| Memory efficiency        | ✅      | ❌            | ❌      | Least overhead                  |
| Allow null keys          | ✅      | ✅            | ❌      | TreeMap throws NPE              |
| Allow null values        | ✅      | ✅            | ✅      | All allow                       |
| Custom key ordering      | ❌      | ❌            | ✅      | TreeMap with Comparator         |
| LRU cache                | ❌      | ✅            | ❌      | LinkedHashMap with access-order |
| Thread-safe              | ❌      | ❌            | ❌      | Use ConcurrentHashMap           |
| Large datasets           | ✅      | ✅            | ❌      | O(1) better than O(log n)       |
| Configuration properties | ❌      | ✅            | ❌      | Preserve order                  |
| Time-series data         | ❌      | ❌            | ✅      | Sorted by timestamp keys        |

**General Recommendations:**

- **Default choice:** HashMap (fastest, most common use case)
- **Need order preserved:** LinkedHashMap
- **Need sorted keys or range queries:** TreeMap
- **Need thread safety:** ConcurrentHashMap

---

## **⚠️ Legacy Map Implementation (Not Recommended)**

**Hashtable** - Legacy synchronized map from Java 1.0

| ❌ Issue                       | Modern Alternative                                                      |
| ------------------------------ | ----------------------------------------------------------------------- |
| Synchronized (slow)            | Use `HashMap` (single-threaded) or `ConcurrentHashMap` (multi-threaded) |
| No null keys/values            | Use `HashMap` (allows nulls) or `ConcurrentHashMap`                     |
| Coarse-grained locking         | Use `ConcurrentHashMap` (fine-grained locking)                          |
| Predates Collections Framework | Use modern Map implementations                                          |

**📝 Note:** Hashtable has been **removed from main comparison tables** and is **not recommended for Java 17** development. Only mentioned here for historical context and legacy code maintenance scenarios.
