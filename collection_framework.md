# **Java Collection Framework – Complete Guide**

## **Introduction**

The **Java Collection Framework (JCF)** provides a set of **interfaces, classes, and algorithms** to store and manipulate groups of objects.

**Key Features:**

- Provides a **standardized architecture** for working with collections (lists, sets, queues, etc.).
- Allows **dynamic storage**, unlike arrays.
- Supports **generics** for type safety.
- Includes **algorithms** like sorting, searching, and filtering.
- Reduces the need to write **custom data structures**.

**Main Interfaces in JCF:**

1. **Collection** – The root interface for most collections.
2. **List** – Ordered collection, allows duplicates.
3. **Set** – Unique elements, no duplicates.
4. **Queue** – FIFO structure, can prioritize elements.
5. **Map** – Key-value pairs (not part of Collection interface).

---

## **Usage of Collections**

**Why use collections instead of arrays?**

- Arrays are **fixed in size** and **homogeneous**.
- Collections are **dynamic**, can grow or shrink.
- Collections provide **ready-to-use methods**: add, remove, search, iterate, sort, filter.
- Collections support **heterogeneous storage** (using `Object`) or **homogeneous storage** (with generics).

**Specialties of Collection Interfaces:**

| Interface | Specialty                                                                    |
| --------- | ---------------------------------------------------------------------------- |
| List      | Ordered, indexed access, allows duplicates                                   |
| Queue     | FIFO access, can process elements sequentially                               |
| Set       | Ensures uniqueness, efficient lookup, may maintain insertion or sorted order |

---

## **Collection Framework Hierarchy**

**Legend:** `Interface` → **Implementation**

```
«Iterable<E>»
    ↓
«Collection<E>»
    ↓
    ├── «List<E>»
    │   ├── ArrayList
    │   ├── LinkedList
    │   └── «Vector<E>» «legacy, not recommended»
    │       └── «Stack<E>» «legacy, not recommended»
    │
    ├── «Set<E>»
    │   ├── HashSet
    │   ├── LinkedHashSet
    │   └── «SortedSet<E>»
    │       └── «NavigableSet<E>»
    │           └── TreeSet
    │
    └── «Queue<E>»
        ├── PriorityQueue
        ├── LinkedList
        └── «Deque<E>»
            ├── ArrayDeque
            └── LinkedList

«Map<K,V>» (separate hierarchy - does not extend Collection)
    ├── HashMap
    ├── LinkedHashMap
    ├── IdentityHashMap
    ├── WeakHashMap
    ├── EnumMap
    ├── «Hashtable<K,V>» «legacy, not recommended»
    │   └── «Properties» «legacy, not recommended»
    └── «SortedMap<K,V>»
        └── «NavigableMap<K,V>»
            └── TreeMap

Legacy Interfaces
    ├── «Enumeration<E>» «legacy, not recommended»
    └── «Dictionary<K,V>» «legacy, not recommended»

Concurrent Collections (java.util.concurrent)
    ├── ConcurrentHashMap        (implements «ConcurrentMap<K,V>»)
    ├── CopyOnWriteArrayList     (implements «List<E>»)
    ├── CopyOnWriteArraySet      (implements «Set<E>»)
    ├── ConcurrentLinkedQueue    (implements «Queue<E>»)
    └── ConcurrentSkipListMap    (implements «ConcurrentNavigableMap<K,V>»)

```

**Key Relationships:**

- **Iterable<E>** → Root interface providing iteration capability (`iterator()`, `forEach()`, `spliterator()`)
- **Collection<E>** → Extends Iterable, adds bulk operations
- **Map<K,V>** → **Independent hierarchy** (not a Collection), uses key-value pairs
- **Deque** → "Double-ended queue", supports both stack and queue operations
- **SortedSet/SortedMap** → Maintain sorted order
- **NavigableSet/NavigableMap** → Extend sorted interfaces with navigation methods

---

## **Quick Interface Comparison**

| Interface | Duplicates             | Ordered                  | Null Elements        | Index Access | Use Case                                 |
| --------- | ---------------------- | ------------------------ | -------------------- | ------------ | ---------------------------------------- |
| **List**  | ✅ Yes                 | ✅ Yes (insertion order) | ✅ Yes\*             | ✅ Yes       | Sequential access, positional operations |
| **Set**   | ❌ No                  | ⚠️ Depends\*\*           | ✅ Yes\*\*\*         | ❌ No        | Unique elements, membership testing      |
| **Queue** | ✅ Yes                 | ✅ Yes (FIFO)\*\*\*\*    | ❌ No**\***          | ❌ No        | FIFO processing, task scheduling         |
| **Deque** | ✅ Yes                 | ✅ Yes                   | ❌ No**\***          | ❌ No        | Double-ended operations, stack/queue     |
| **Map**   | Keys: ❌<br>Values: ✅ | ⚠️ Depends**\*\***       | ⚠️ Depends**\*\*\*** | N/A          | Key-value associations, lookups          |

**Notes:**

**Why "⚠️ Depends"?** The behavior **varies based on specific implementation** of the interface.

- `*` **List - Null Elements:** All List implementations (ArrayList, LinkedList) allow null elements.

- `**` **Set - Ordered:** Behavior depends on implementation:

  - **HashSet** → ❌ Unordered (no predictable iteration order)
  - **LinkedHashSet** → ✅ Insertion order maintained
  - **TreeSet** → ✅ Sorted order (natural ordering or comparator)

- `***` **Set - Null Elements:** Most allow null, but TreeSet does not:

  - **HashSet, LinkedHashSet** → ✅ Allow one null element
  - **TreeSet** → ❌ Throws NullPointerException (requires Comparable elements)

- `****` **Queue - Ordered:** Most follow FIFO, but PriorityQueue uses priority:

  - **LinkedList, ArrayDeque** → ✅ FIFO order
  - **PriorityQueue** → ✅ Priority order (not FIFO)

- `*****` **Queue/Deque - Null Elements:** Most implementations do not allow null:

  - **PriorityQueue, ArrayDeque** → ❌ Throw NullPointerException
  - **LinkedList** → ✅ Allows null (but generally avoided in Queue context)

- `******` **Map - Ordered:** Behavior depends on implementation:

  - **HashMap** → ❌ Unordered (no predictable iteration order)
  - **LinkedHashMap** → ✅ Insertion order maintained (or access order if configured)
  - **TreeMap** → ✅ Sorted order by keys (natural ordering or comparator)

- `*******` **Map - Null Elements:** Varies by implementation for both keys and values:
  - **Keys:**
    - **HashMap, LinkedHashMap** → ✅ Allow one null key
    - **TreeMap** → ❌ No null keys (throws NullPointerException)
    - **ConcurrentHashMap** → ❌ No null keys (throws NullPointerException)
  - **Values:**
    - **HashMap, LinkedHashMap, TreeMap** → ✅ Allow null values
    - **ConcurrentHashMap** → ❌ No null values (throws NullPointerException)

---

## **⚠️ Legacy Classes vs Modern Alternatives (Java 17)**

**Legacy classes from Java 1.0/1.1 should be avoided in modern code:**

| ❌ Legacy Class | ⭐ Modern Alternative                           | Reason                                                                   |
| --------------- | ----------------------------------------------- | ------------------------------------------------------------------------ |
| **Vector**      | `ArrayList` or `Collections.synchronizedList()` | Vector is synchronized (performance overhead), ArrayList is faster       |
| **Stack**       | `ArrayDeque`                                    | Stack extends Vector (legacy), ArrayDeque is faster and more complete    |
| **Hashtable**   | `HashMap` or `ConcurrentHashMap`                | Hashtable is synchronized (slow), doesn't allow nulls, HashMap is faster |
| **Enumeration** | `Iterator`                                      | Enumeration has verbose method names, Iterator is more flexible          |

**Why avoid legacy classes?**

- ❌ **Poor performance** - unnecessary synchronization overhead
- ❌ **Limited functionality** - missing modern methods
- ❌ **Not recommended** - Oracle documentation suggests alternatives
- ❌ **No generic type safety** - requires explicit casting in older code

**Modern alternatives offer:**

- ✅ **Better performance** - no synchronization unless needed
- ✅ **More features** - Java 8+ lambdas, streams, default methods
- ✅ **Flexible thread-safety** - use `Collections.synchronized*()` or concurrent collections when needed
- ✅ **Better design** - cleaner API, follows modern Java conventions

**📝 Note:** Legacy classes have been **removed from main documentation tables** and examples. They are **not recommended for Java 17** development and are only mentioned here for historical context and legacy code maintenance.

---

## **Collection Interface – Key Concepts**

- **Extends `Iterable<E>`** — Inherits `iterator()`, `forEach()`, and `spliterator()` methods
- Root interface in the collection hierarchy representing a group of objects (elements).
- Some collections allow **duplicates**, others don't.
- Some collections are **ordered**, others unordered.
- The JDK provides **no direct implementations** – use subinterfaces like **List**, **Set**, or **Queue**.
- Collections are typically used for **maximum generality** in passing and manipulating groups of objects.

---

### **Standard Constructor Conventions**

- **No-argument constructor** – creates an empty collection.
- **Constructor accepting a Collection** – creates a new collection with the same elements (copy constructor).

---

### **Optional Operations**

- Some methods are **optional**.
- If not implemented, they throw `UnsupportedOperationException`.
- Methods marked "optional operation" in documentation may not be supported by all implementations.

---

### **Element Restrictions**

- Some implementations **prohibit null elements** → `NullPointerException`.
- Some have **type restrictions** → `ClassCastException`.
- Adding ineligible elements results in **unchecked exceptions**.
- Querying ineligible elements may throw exceptions or simply return false.

---

### **Thread Safety**

- Collections are **not thread-safe by default**.
- Undefined behavior occurs if a collection is modified by one thread while another is accessing it.
- Each collection can define its **own synchronization policy**.

---

### **Equals and hashCode**

- Methods like `contains(Object o)` use **`equals()`** for comparison.
- Implementations may **optimize** by checking hash codes before calling `equals()`.

---

### **Self-Referential Collections**

- Recursive operations (`clone()`, `equals()`, `hashCode()`, `toString()`) may fail if a collection contains itself.
- Most implementations do **not handle self-referential collections**.

---

# **Complete Collection Interface Methods – Linear, Easy-to-Learn List**

---

## **1️⃣ Adding Elements**

1. **`add(E e)`** – Add a single element
2. **`addAll(Collection<? extends E> c)`** – Add all elements from another collection

---

## **2️⃣ Removing Elements**

3. **`remove(Object o)`** – Remove **one element** (first occurrence for List, first matching for Queue, element for Set)
4. **`removeAll(Collection<?> c)`** – Remove **all elements** present in another collection
5. **`clear()`** – Remove **all elements**
6. **`removeIf(Predicate<? super E> filter)`** – Remove all elements satisfying a condition (**Java 8+ default method**)

---

## **3️⃣ Querying / Checking**

7. **`contains(Object o)`** – Check if a single element exists
8. **`containsAll(Collection<?> c)`** – Check if all elements in another collection exist
9. **`isEmpty()`** – Check if the collection has no elements
10. **`size()`** – Get the number of elements

---

## **4️⃣ Iteration / Traversal (Methods inherited from Iterable<E>)**

11. **`iterator()`** – Get an iterator to loop over elements
12. **`forEach(Consumer<? super E> action)`** – Perform an action on each element (**Java 8+ default method**)
13. **`spliterator()`** – Returns a Spliterator to traverse/partition elements efficiently (**Java 8+ default method**)

---

## **5️⃣ Retaining / Filtering**

14. **`retainAll(Collection<?> c)`** – Keep only elements present in another collection, remove the rest

---

## **6️⃣ Conversion**

15. **`toArray()`** – Convert collection to **`Object[]`**
16. **`toArray(T[] a)`** – Convert collection to **typed array `T[]`**
17. **`toArray(IntFunction<T[]> generator)`** – Convert collection to **typed array using generator function** (**Java 11+ default method**)

---

## **7️⃣ Stream Operations (Java 8+)**

18. **`stream()`** – Returns a sequential Stream of elements (**Java 8+ default method**)
19. **`parallelStream()`** – Returns a parallel Stream of elements (**Java 8+ default method**)

---

## **8️⃣ Object Methods (inherited)**

20. **`equals(Object o)`** – Checks equality with another collection (order matters for List/Queue, not for Set)
21. **`hashCode()`** – Returns hash code of the collection

---

### **✅ Tips for Memorization**

**Order for mental map:**
**Add → Remove → Query → Iterate → Retain → Convert → Stream → Object methods**

---

# **Java Collection Interfaces – Notes**

## **1. List Interface**

- **Definition:** Ordered collection, allows duplicates, elements accessible by index.
- **Common Implementations:** `ArrayList`, `LinkedList`.

### **List-Specific Methods (Not in Collection/Iterable):**

| Method                                        | Purpose                                                             |
| --------------------------------------------- | ------------------------------------------------------------------- |
| `E get(int index)`                            | Returns element at specified position                               |
| `E set(int index, E element)`                 | Replaces element at specified position                              |
| `int indexOf(Object o)`                       | Returns index of first occurrence, or -1 if not found               |
| `int lastIndexOf(Object o)`                   | Returns index of last occurrence, or -1 if not found                |
| `List<E> subList(int fromIndex, int toIndex)` | Returns view of portion of list                                     |
| `ListIterator<E> listIterator()`              | Returns list iterator over elements in proper sequence              |
| `ListIterator<E> listIterator(int index)`     | Returns list iterator starting at specified position                |
| `void replaceAll(UnaryOperator<E> operator)`  | Replaces each element with result of operator (**Java 8+ default**) |
| `void sort(Comparator<? super E> c)`          | Sorts list according to comparator (**Java 8+ default**)            |

### **Overloaded Methods from Collection:**

| Method                                                 | Purpose                                                    | Base Method in Collection                   |
| ------------------------------------------------------ | ---------------------------------------------------------- | ------------------------------------------- |
| `void add(int index, E element)`                       | Inserts element at specified position                      | `boolean add(E e)`                          |
| `boolean addAll(int index, Collection<? extends E> c)` | Inserts all elements from collection at specified position | `boolean addAll(Collection<? extends E> c)` |
| `E remove(int index)`                                  | Removes element at specified position                      | `boolean remove(Object o)`                  |

### **Static Factory Methods (Java 9+):**

| Method                                                    | Purpose                                                           |
| --------------------------------------------------------- | ----------------------------------------------------------------- |
| `static <E> List<E> of()`                                 | Returns unmodifiable list with zero elements                      |
| `static <E> List<E> of(E e1)`                             | Returns unmodifiable list with one element                        |
| `static <E> List<E> of(E... elements)`                    | Returns unmodifiable list with arbitrary number of elements       |
| `static <E> List<E> copyOf(Collection<? extends E> coll)` | Returns unmodifiable list containing elements of given collection |

**Key Points:**

- Maintains **insertion order**
- Allows **duplicate elements**
- Supports **positional access** (index-based)

---

---

## **2. Queue Interface**

- **Definition:** Collection designed for **FIFO (First-In-First-Out)** order
- **Common Implementations:** `LinkedList`, `PriorityQueue`, `ArrayDeque`

### **Queue-Specific Methods (Not in Collection/Iterable):**

Queue provides **two versions** of methods: one throws exception, one returns special value.

| Method               | Purpose                                                            | Throws Exception   |
| -------------------- | ------------------------------------------------------------------ | ------------------ |
| `boolean offer(E e)` | Inserts element into queue without violating capacity restrictions | No (returns false) |
| `E poll()`           | Retrieves and removes head of queue, returns null if empty         | No (returns null)  |
| `E peek()`           | Retrieves but does not remove head of queue, returns null if empty | No (returns null)  |
| `E element()`        | Retrieves but does not remove head of queue                        | Yes (if empty)     |

### **Overloaded Methods from Collection:**

| Method       | Purpose                             | Base Method in Collection  | Behavior Difference                 |
| ------------ | ----------------------------------- | -------------------------- | ----------------------------------- |
| `E remove()` | Retrieves and removes head of queue | `boolean remove(Object o)` | Different signature: no-arg version |

**Note:** `add(E e)` is inherited from Collection but throws `IllegalStateException` if capacity restricted (unlike `offer(E e)` which returns false).

**Key Points:**

- Designed for **queue operations** (FIFO by default)
- **Exception-throwing methods**: `add()`, `remove()`, `element()`
- **Special-value methods**: `offer()`, `poll()`, `peek()`
- `PriorityQueue` orders elements by priority, not FIFO
- `Deque` extends `Queue` and adds **stack-like operations** (`addFirst`, `removeLast`, etc.)

---

## **3. Set Interface**

- **Definition:** A collection that contains **no duplicate elements**. Sets are generally **unordered**, although some implementations maintain order.

### **Common Implementations:**

| Implementation  | Key Feature                                                      |
| --------------- | ---------------------------------------------------------------- |
| `HashSet`       | Unordered, allows null, fast access (O(1) average)               |
| `LinkedHashSet` | Maintains insertion order                                        |
| `TreeSet`       | Sorted order (natural or via comparator), implements `SortedSet` |

### **Set-Specific Methods (Not in Collection/Iterable):**

**None** — all methods are inherited from `Collection` (like `add()`, `remove()`, `contains()`, etc.)

### **Static Factory Methods (Java 9+):**

| Method                                                   | Purpose                                                          |
| -------------------------------------------------------- | ---------------------------------------------------------------- |
| `static <E> Set<E> of()`                                 | Returns unmodifiable set with zero elements                      |
| `static <E> Set<E> of(E e1)`                             | Returns unmodifiable set with one element                        |
| `static <E> Set<E> of(E... elements)`                    | Returns unmodifiable set with arbitrary number of elements       |
| `static <E> Set<E> copyOf(Collection<? extends E> coll)` | Returns unmodifiable set containing elements of given collection |

---

## 4. Map Interface

- **Definition:** An object that maps **keys to values**. A map **cannot contain duplicate keys**; each key can map to **at most one value**.
- **Not part of Collection hierarchy** — Map is a separate interface.
- **Common Implementations:** `HashMap`, `LinkedHashMap`, `TreeMap`, `Hashtable`, `ConcurrentHashMap`.

### Key Characteristics

- **No duplicate keys** — each key maps to at most one value.
- **Three collection views:**
  - **Set of keys** (`keySet()`)
  - **Collection of values** (`values()`)
  - **Set of key-value mappings** (`entrySet()`)
- **Order:** Some implementations (`TreeMap`) maintain sorted order; others (`HashMap`) do not.
- **Null keys/values:** Some implementations (`HashMap`) allow null; others (`TreeMap`, `Hashtable`) do not.
- **Mutable keys warning:** Avoid using mutable objects as keys, as changes affecting `equals()` can break the map.

---

### Map-Specific Methods

#### 1️⃣ Adding / Updating Entries

| Method                                        | Purpose                                                                |
| --------------------------------------------- | ---------------------------------------------------------------------- |
| `V put(K key, V value)`                       | Associates specified value with specified key                          |
| `void putAll(Map<? extends K,? extends V> m)` | Copies all mappings from specified map to this map                     |
| `default V putIfAbsent(K key, V value)`       | Associates key with value only if key not already mapped (**Java 8+**) |

#### 2️⃣ Retrieving Values

| Method                                               | Purpose                                                             |
| ---------------------------------------------------- | ------------------------------------------------------------------- |
| `V get(Object key)`                                  | Returns value mapped to key, or null if not present                 |
| `default V getOrDefault(Object key, V defaultValue)` | Returns value for key, or defaultValue if not present (**Java 8+**) |

#### 3️⃣ Removing Entries

| Method                                             | Purpose                                                              |
| -------------------------------------------------- | -------------------------------------------------------------------- |
| `V remove(Object key)`                             | Removes mapping for key if present                                   |
| `default boolean remove(Object key, Object value)` | Removes entry only if key is mapped to specified value (**Java 8+**) |
| `void clear()`                                     | Removes all mappings from map                                        |

#### 4️⃣ Checking / Querying

| Method                                | Purpose                                            |
| ------------------------------------- | -------------------------------------------------- |
| `boolean containsKey(Object key)`     | Returns true if map contains mapping for key       |
| `boolean containsValue(Object value)` | Returns true if map maps one or more keys to value |
| `boolean isEmpty()`                   | Returns true if map contains no key-value mappings |
| `int size()`                          | Returns number of key-value mappings               |

#### 5️⃣ Collection Views

| Method                           | Purpose                                            |
| -------------------------------- | -------------------------------------------------- |
| `Set<K> keySet()`                | Returns Set view of keys contained in map          |
| `Collection<V> values()`         | Returns Collection view of values contained in map |
| `Set<Map.Entry<K,V>> entrySet()` | Returns Set view of key-value mappings             |

#### 6️⃣ Compute Operations (Java 8+)

| Method                                                                                             | Purpose                                                  |
| -------------------------------------------------------------------------------------------------- | -------------------------------------------------------- |
| `default V compute(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)`          | Computes mapping for specified key and its current value |
| `default V computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)`                | Computes value if key not already associated             |
| `default V computeIfPresent(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)` | Computes new mapping if key is present and non-null      |

#### 7️⃣ Replace Operations (Java 8+)

| Method                                                                          | Purpose                                                       |
| ------------------------------------------------------------------------------- | ------------------------------------------------------------- |
| `default V replace(K key, V value)`                                             | Replaces entry for key only if currently mapped to some value |
| `default boolean replace(K key, V oldValue, V newValue)`                        | Replaces entry only if currently mapped to specified value    |
| `default void replaceAll(BiFunction<? super K,? super V,? extends V> function)` | Replaces each entry's value with result of function           |

#### 8️⃣ Merge Operation (Java 8+)

| Method                                                                                           | Purpose                                                                                 |
| ------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------- |
| `default V merge(K key, V value, BiFunction<? super V,? super V,? extends V> remappingFunction)` | Associates key with value if not mapped or mapped to null; otherwise computes new value |

#### 9️⃣ Iteration (Java 8+)

| Method                                                         | Purpose                                                  |
| -------------------------------------------------------------- | -------------------------------------------------------- |
| `default void forEach(BiConsumer<? super K,? super V> action)` | Performs given action for each entry until all processed |

#### 🔟 Object Methods

| Method                     | Purpose                                              |
| -------------------------- | ---------------------------------------------------- |
| `boolean equals(Object o)` | Compares specified object with this map for equality |
| `int hashCode()`           | Returns hash code value for this map                 |

**Important Notes on equals():**

- ✅ `equals()` checks **content equality** (same key-value pairs), not object references
- ✅ `==` checks **reference equality** (same object in memory)
- ✅ Works across **different Map implementations** (HashMap equals TreeMap if same content)
- ✅ **Order doesn't matter** for equality — only the key-value mappings matter
- ✅ Two maps are equal if they contain the **same mappings** (each key maps to same value in both)

---

### Static Factory Methods (Java 9+)

| Method                                                                           | Purpose                                                       |
| -------------------------------------------------------------------------------- | ------------------------------------------------------------- |
| `static <K,V> Map<K,V> of()`                                                     | Returns unmodifiable map with zero mappings                   |
| `static <K,V> Map<K,V> of(K k1, V v1)`                                           | Returns unmodifiable map with one mapping                     |
| `static <K,V> Map<K,V> of(K k1, V v1, K k2, V v2)`                               | Returns unmodifiable map with two mappings                    |
| ... (overloaded up to 10 key-value pairs)                                        |                                                               |
| `static <K,V> Map<K,V> ofEntries(Map.Entry<? extends K,? extends V>... entries)` | Returns unmodifiable map from entries                         |
| `static <K,V> Map<K,V> copyOf(Map<? extends K,? extends V> map)`                 | Returns unmodifiable map containing entries of given map      |
| `static <K,V> Map.Entry<K,V> entry(K k, V v)`                                    | Returns unmodifiable Map.Entry containing given key and value |

---

### Nested Interface: Map.Entry<K,V>

Represents a **key-value pair** (mapping) in a map.

**Common Methods:**

| Method                     | Purpose                                                |
| -------------------------- | ------------------------------------------------------ |
| `K getKey()`               | Returns key corresponding to this entry                |
| `V getValue()`             | Returns value corresponding to this entry              |
| `V setValue(V value)`      | Replaces value corresponding to this entry             |
| `boolean equals(Object o)` | Compares specified object with this entry for equality |
| `int hashCode()`           | Returns hash code value for this map entry             |

**Static Methods (Java 8+):**

| Method                                                                                     | Purpose                                                                  |
| ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------ |
| `static <K extends Comparable<? super K>,V> Comparator<Map.Entry<K,V>> comparingByKey()`   | Returns comparator that compares entries by key (natural order)          |
| `static <K,V extends Comparable<? super V>> Comparator<Map.Entry<K,V>> comparingByValue()` | Returns comparator that compares entries by value (natural order)        |
| `static <K,V> Comparator<Map.Entry<K,V>> comparingByKey(Comparator<? super K> cmp)`        | Returns comparator that compares entries by key using given comparator   |
| `static <K,V> Comparator<Map.Entry<K,V>> comparingByValue(Comparator<? super V> cmp)`      | Returns comparator that compares entries by value using given comparator |

---

### Unmodifiable Maps (Java 9+)

- **Unmodifiable** — Cannot add, remove, or update entries (throws `UnsupportedOperationException`)
- **No null keys or values** — Throws `NullPointerException` if null keys/values provided
- **Serializable** — If all keys and values are serializable
- **Reject duplicate keys** — Throws `IllegalArgumentException` at creation time
- **Unspecified iteration order** — Order may change between releases
- **Value-based** — Treat equal instances as interchangeable

---

### Key Points

- Map is **not part of Collection hierarchy** but provides collection views.
- **HashMap** — Fast, unordered, allows one null key.
- **LinkedHashMap** — Maintains insertion order.
- **TreeMap** — Sorted order (implements `SortedMap`/`NavigableMap`).
- **Hashtable** — Legacy, synchronized, no null keys/values.
- **ConcurrentHashMap** — Thread-safe, high concurrency, no null keys/values.
- Use **compute methods** for atomic operations.
- Use **merge** for combining values.
- Use **entrySet()** for efficient iteration over key-value pairs.

---

## **⚡ Performance & Best Practices**

### **Quick Performance Reference**

For **detailed performance analysis** of specific implementations, see:

- **List implementations** → [`list_implementations.md`](list_implementations.md)
- **Set implementations** → [`set_implementations.md`](set_implementations.md)
- **Queue implementations** → [`queue_implementations.md`](queue_implementations.md)
- **Map implementations** → [`map_implementations.md`](map_implementations.md)

#### **Quick Operation Complexity Summary**

| Operation          | ArrayList | LinkedList | HashSet | TreeSet  | HashMap  | TreeMap  |
| ------------------ | --------- | ---------- | ------- | -------- | -------- | -------- |
| **add()**          | O(1)\*    | O(1)       | O(1)    | O(log n) | O(1)     | O(log n) |
| **remove(Object)** | O(n)      | O(n)       | O(1)    | O(log n) | O(1)     | O(log n) |
| **contains()**     | O(n)      | O(n)       | O(1)    | O(log n) | O(1)\*\* | O(log n) |
| **get(index)**     | O(1)      | O(n)       | N/A     | N/A      | N/A      | N/A      |
| **get(key)**       | N/A       | N/A        | N/A     | N/A      | O(1)     | O(log n) |
| **size()**         | O(1)      | O(1)       | O(1)    | O(1)     | O(1)     | O(1)     |

**Legend:**

- `*` O(1) amortized — occasionally O(n) when resizing
- `**` O(1) average — worst case O(n) with hash collisions
- **n** = number of elements

---

### **Choosing the Right Collection**

#### **Decision Tree:**

```
Need key-value pairs?
├─ Yes → Map
│   ├─ Need sorted keys? → TreeMap
│   ├─ Need insertion order? → LinkedHashMap
│   └─ Need fastest access? → HashMap
│
└─ No → Collection
    ├─ Need index access?
    │   └─ Yes → List
    │       ├─ Mostly random access? → ArrayList
    │       └─ Mostly add/remove at ends? → LinkedList (or ArrayDeque)
    │
    └─ Need unique elements?
        ├─ Yes → Set
        │   ├─ Need sorted order? → TreeSet
        │   ├─ Need insertion order? → LinkedHashSet
        │   └─ Need fastest access? → HashSet
        │
        └─ Need FIFO/LIFO processing? → Queue/Deque
            ├─ Stack operations? → ArrayDeque
            ├─ Priority-based? → PriorityQueue
            └─ Thread-safe? → ConcurrentLinkedQueue
```

---

### **Performance Tips**

#### **✅ Best Practices**

1. **Initialize with capacity** for large collections:

   ```java
   List<String> list = new ArrayList<>(10000); // Avoid multiple resizes
   Map<String, Integer> map = new HashMap<>(10000, 0.75f);
   ```

2. **Use appropriate collection** for your access pattern:

   - Random access by index? → **ArrayList**
   - Frequent add/remove at ends? → **ArrayDeque** or **LinkedList**
   - Fast lookups? → **HashSet** or **HashMap**
   - Sorted order? → **TreeSet** or **TreeMap**

3. **Iterate over entrySet()** for Maps (not keySet()):

   ```java
   // ✅ Efficient (one lookup per entry)
   for (Map.Entry<K, V> entry : map.entrySet()) {
       K key = entry.getKey();
       V value = entry.getValue();
   }

   // ❌ Inefficient (two lookups per entry)
   for (K key : map.keySet()) {
       V value = map.get(key); // Extra O(1) lookup
   }
   ```

4. **Use computeIfAbsent()** instead of manual checks:

   ```java
   // ✅ Efficient (single lookup)
   map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);

   // ❌ Inefficient (two lookups)
   if (!map.containsKey(key)) {
       map.put(key, new ArrayList<>());
   }
   map.get(key).add(value);
   ```

5. **Choose parallelStream() wisely**:
   - ✅ Use for **large datasets** (10,000+ elements) with **CPU-intensive operations**
   - ❌ Avoid for **small datasets** (overhead > benefit)
   - ❌ Avoid for **I/O operations** (thread blocking)

#### **⚠️ Common Performance Pitfalls**

1. **Using List when Set is appropriate**:

   ```java
   // ❌ O(n²) duplicate check
   List<String> list = new ArrayList<>();
   if (!list.contains(item)) list.add(item); // O(n) check every time

   // ✅ O(1) duplicate check
   Set<String> set = new HashSet<>();
   set.add(item); // O(1) automatic duplicate handling
   ```

2. **Modifying collection during iteration**:

   ```java
   // ❌ Throws ConcurrentModificationException
   for (String s : list) {
       if (s.equals("remove")) list.remove(s);
   }

   // ✅ Use iterator.remove() or removeIf()
   list.removeIf(s -> s.equals("remove"));
   ```

3. **Using == instead of equals()**:

   ```java
   // ❌ Compares references
   if (list1 == list2) { ... }

   // ✅ Compares contents
   if (list1.equals(list2)) { ... }
   ```

4. **Not considering load factor** for HashMap:
   ```java
   // Default load factor 0.75 triggers resize at 75% capacity
   // For memory-constrained: new HashMap<>(initialCapacity, 1.0f)
   // For speed-sensitive: new HashMap<>(initialCapacity, 0.5f)
   ```

---

### **Concurrency Considerations**

| Collection    | Thread-Safe? | Alternative                                                        |
| ------------- | ------------ | ------------------------------------------------------------------ |
| **ArrayList** | ❌ No        | `Collections.synchronizedList()` or `CopyOnWriteArrayList`         |
| **HashSet**   | ❌ No        | `Collections.synchronizedSet()` or `ConcurrentHashMap.newKeySet()` |
| **HashMap**   | ❌ No        | `Collections.synchronizedMap()` or `ConcurrentHashMap`             |
| **TreeMap**   | ❌ No        | `Collections.synchronizedSortedMap()` or `ConcurrentSkipListMap`   |

**Note:** Synchronized wrappers have **lower concurrency** than `java.util.concurrent` alternatives.

---

### **Summary: When to Use What**

| Use Case                       | Recommended Collection             | Why?                              |
| ------------------------------ | ---------------------------------- | --------------------------------- |
| Store unique items             | **HashSet**                        | O(1) add/contains/remove          |
| Store items in order added     | **LinkedHashSet** or **ArrayList** | Maintains insertion order         |
| Store items in sorted order    | **TreeSet** or **TreeMap**         | Automatic sorting                 |
| Fast key-value lookup          | **HashMap**                        | O(1) get/put                      |
| Sorted key-value pairs         | **TreeMap**                        | O(log n) with sorted keys         |
| FIFO queue                     | **ArrayDeque** or **LinkedList**   | O(1) enqueue/dequeue              |
| Stack (LIFO)                   | **ArrayDeque**                     | O(1) push/pop                     |
| Priority processing            | **PriorityQueue**                  | O(log n) with heap structure      |
| Random access by index         | **ArrayList**                      | O(1) get/set by index             |
| Frequent insert/delete at ends | **LinkedList** or **ArrayDeque**   | O(1) addFirst/addLast             |
| Thread-safe map                | **ConcurrentHashMap**              | High concurrency, O(1) operations |
| LRU cache                      | **LinkedHashMap** (access-order)   | O(1) with insertion/access order  |

---
