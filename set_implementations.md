# **Set Implementations Deep Dive**

---

## **3.1 HashSet**

**Definition:** Hash table implementation of the **Set** interface. Backed by a **HashMap** instance. Offers **constant-time performance** for basic operations (add, remove, contains).

**Key Characteristics:**

- ✅ **Hash table based** - backed by HashMap (actually stores elements as keys)
- ✅ **Fast operations** - O(1) average for add, remove, contains
- ✅ **No duplicates** - guaranteed by Set contract
- ✅ **Allows one null** element
- ❌ **Unordered** - no guaranteed iteration order
- ❌ **Not synchronized** - not thread-safe
- ⚡ **Initial capacity:** 16 (default)
- ⚡ **Load factor:** 0.75 (default) - resizes when 75% full

---

#### **HashSet-Specific Characteristics**

**Constructor Options (Interview Important):**

| Constructor                                      | Purpose                               | Example                    |
| ------------------------------------------------ | ------------------------------------- | -------------------------- |
| `HashSet()`                                      | Default capacity 16, load factor 0.75 | `new HashSet<>()`          |
| `HashSet(int initialCapacity)`                   | Custom capacity, default load factor  | `new HashSet<>(100)`       |
| `HashSet(int initialCapacity, float loadFactor)` | Custom capacity and load factor       | `new HashSet<>(100, 0.8f)` |
| `HashSet(Collection<? extends E> c)`             | From collection                       | `new HashSet<>(list)`      |

**Internal Implementation:**

```java
// HashSet is backed by HashMap
private transient HashMap<E,Object> map;
private static final Object PRESENT = new Object();

public boolean add(E e) {
    return map.put(e, PRESENT) == null;  // Element stored as key
}
```

---

#### **Performance Summary (Interview)**

| Operation          | Average Case       | Worst Case | Reason                               |
| ------------------ | ------------------ | ---------- | ------------------------------------ |
| `add(e)`           | **O(1)**           | **O(n)**   | Hash collision (rare with good hash) |
| `remove(Object)`   | **O(1)**           | **O(n)**   | Hash collision                       |
| `contains(Object)` | **O(1)**           | **O(n)**   | Hash collision                       |
| `size()`           | **O(1)**           | **O(1)**   | Cached size field                    |
| `isEmpty()`        | **O(1)**           | **O(1)**   | Size check                           |
| `clear()`          | **O(n)**           | **O(n)**   | Must clear all buckets               |
| `iterator.next()`  | **O(1)** amortized | **O(1)**   | Iterate through buckets              |

**Note:** Worst case O(n) occurs only with **many hash collisions** (poor hash function or many equal hash codes).

---

#### **Interview Questions & Answers**

**Q1: How does HashSet prevent duplicates?**

- **A:** Uses `equals()` and `hashCode()`. When adding element:
  1. Computes `hashCode()` to find bucket
  2. Uses `equals()` to check if element already exists in bucket
  3. Adds only if not found

**Q2: What happens if you add null to HashSet?**

- **A:** **Allowed!** HashSet accepts **one null element**. Multiple adds of null have no effect (still only one null).

**Q3: Why is HashSet backed by HashMap?**

- **A:** Reuses HashMap's hash table implementation. Elements stored as **keys**, value is dummy `PRESENT` object.

**Q4: What is load factor and why does it matter?**

- **A:** **Load factor** (default 0.75) determines when to resize. Lower = faster lookups, more memory. Higher = less memory, more collisions.
  ```
  Capacity 16, load factor 0.75 → resizes when size reaches 12
  ```

**Q5: HashSet vs TreeSet - when to use which?**

- **A:**
  - **HashSet:** Need **fast operations** (O(1)), **no ordering** required
  - **TreeSet:** Need **sorted order** (O(log n)), range queries, or NavigableSet operations

**Q6: What happens if hashCode() is poorly implemented?**

- **A:** Many collisions → all elements in same bucket → **degrades to O(n)** (linear search within bucket).

**Q7: Can you iterate HashSet in insertion order?**

- **A:** **No!** HashSet is unordered. Use **LinkedHashSet** for insertion order.

**Q8: How to make HashSet thread-safe?**

- **A:**
  ```java
  Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
  // Or use ConcurrentHashMap.newKeySet() for better concurrency
  Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
  ```

---

#### **Usage Examples**

**Basic Operations:**

```java
Set<String> set = new HashSet<>();
set.add("Apple");
set.add("Banana");
set.add("Apple");  // Duplicate - not added
set.add(null);     // Allowed

System.out.println(set.size());         // 3
System.out.println(set.contains("Apple")); // true
set.remove("Banana");
```

**Remove Duplicates from List:**

```java
List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3, 4);
Set<Integer> unique = new HashSet<>(list);
System.out.println(unique); // [1, 2, 3, 4] (unordered)
```

**Set Operations:**

```java
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Union
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2); // [1, 2, 3, 4, 5, 6]

// Intersection
Set<Integer> intersection = new HashSet<>(set1);
intersection.retainAll(set2); // [3, 4]

// Difference
Set<Integer> difference = new HashSet<>(set1);
difference.removeAll(set2); // [1, 2]
```

---

#### **When to Use**

✅ **Use HashSet when:**

- Need **fast membership testing** (contains) - O(1)
- Need to **remove duplicates** from collection
- **No ordering** required
- Need **fast add/remove** operations
- Implementing **set operations** (union, intersection, difference)

❌ **Avoid when:**

- Need **sorted order** (use TreeSet)
- Need **insertion order** (use LinkedHashSet)
- Need **thread-safety** (use ConcurrentHashMap.newKeySet())
- Elements don't have good `hashCode()` implementation

---

## **3.2 LinkedHashSet**

**Definition:** Hash table and linked list implementation of **Set** interface. Maintains a **doubly-linked list** running through entries, preserving **insertion order**.

**Key Characteristics:**

- ✅ **Hash table + linked list** - combines HashMap performance with LinkedList ordering
- ✅ **Insertion order** - iterates in order elements were added
- ✅ **Fast operations** - O(1) for add, remove, contains (like HashSet)
- ✅ **No duplicates** - guaranteed by Set contract
- ✅ **Allows one null** element
- ❌ **Not synchronized** - not thread-safe
- ⚡ **Slightly higher memory** overhead than HashSet (linked list pointers)

---

#### **LinkedHashSet-Specific Characteristics**

**Constructor Options:**

| Constructor                                            | Purpose                               | Example                          |
| ------------------------------------------------------ | ------------------------------------- | -------------------------------- |
| `LinkedHashSet()`                                      | Default capacity 16, load factor 0.75 | `new LinkedHashSet<>()`          |
| `LinkedHashSet(int initialCapacity)`                   | Custom capacity                       | `new LinkedHashSet<>(100)`       |
| `LinkedHashSet(int initialCapacity, float loadFactor)` | Custom capacity and load factor       | `new LinkedHashSet<>(100, 0.8f)` |
| `LinkedHashSet(Collection<? extends E> c)`             | From collection                       | `new LinkedHashSet<>(list)`      |

**Internal Structure:**

```
Hash Table (for O(1) lookups)
     +
Doubly-Linked List (for insertion order)
```

---

#### **Performance Summary (Interview)**

| Operation          | Complexity | Notes                                   |
| ------------------ | ---------- | --------------------------------------- |
| `add(e)`           | **O(1)**   | Same as HashSet + link update           |
| `remove(Object)`   | **O(1)**   | Same as HashSet + unlink                |
| `contains(Object)` | **O(1)**   | Same as HashSet                         |
| `iterator.next()`  | **O(1)**   | Follows linked list (predictable order) |

**Note:** Slightly slower than HashSet due to linked list maintenance, but same O(1) complexity.

---

#### **Interview Questions & Answers**

**Q1: LinkedHashSet vs HashSet - what's the difference?**

- **A:**
  - **HashSet:** Unordered, fastest
  - **LinkedHashSet:** Insertion order maintained, slightly slower

**Q2: How does LinkedHashSet maintain insertion order?**

- **A:** Uses **doubly-linked list** connecting entries in addition to hash table. Each entry has `before` and `after` pointers.

**Q3: LinkedHashSet vs TreeSet for ordered iteration?**

- **A:**
  - **LinkedHashSet:** Insertion order, O(1) operations
  - **TreeSet:** Sorted order (natural/comparator), O(log n) operations

**Q4: Can you change iteration order in LinkedHashSet?**

- **A:** **No!** Order is fixed at insertion time. Unlike LinkedHashMap (which has access-order mode).

**Q5: What's the memory overhead vs HashSet?**

- **A:** ~**20% more memory** - each entry stores 2 additional references (before/after pointers).

---

#### **Usage Examples**

**Preserving Insertion Order:**

```java
Set<String> orderedSet = new LinkedHashSet<>();
orderedSet.add("Banana");
orderedSet.add("Apple");
orderedSet.add("Cherry");

// Iterates in insertion order
for (String s : orderedSet) {
    System.out.println(s);  // Banana, Apple, Cherry
}
```

**Remove Duplicates While Preserving Order:**

```java
List<Integer> list = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5);
Set<Integer> orderedUnique = new LinkedHashSet<>(list);
System.out.println(orderedUnique); // [3, 1, 4, 5, 9, 2, 6]
```

---

#### **When to Use**

✅ **Use LinkedHashSet when:**

- Need **fast operations** (O(1)) **AND** **insertion order**
- Need to **remove duplicates** while **preserving order**
- Implementing **LRU-like behavior** (insertion order tracking)
- Need **predictable iteration order**

❌ **Avoid when:**

- **Don't care about order** (use HashSet - faster, less memory)
- Need **sorted order** (use TreeSet)
- **Memory-constrained** (HashSet uses less memory)

---

## **3.3 TreeSet**

**Definition:** **NavigableSet** implementation based on a **TreeMap** (Red-Black tree). Elements are ordered using their **natural ordering** or by a **Comparator**.

**Key Characteristics:**

- ✅ **Red-Black tree** - self-balancing binary search tree
- ✅ **Sorted order** - always maintains sorted order
- ✅ **Navigable operations** - ceiling, floor, higher, lower
- ✅ **No duplicates** - guaranteed by Set contract
- ❌ **Does NOT allow null** - throws NullPointerException (comparison required)
- ❌ **Not synchronized** - not thread-safe
- ⚡ **O(log n) operations** - slower than HashSet but provides ordering

---

#### **TreeSet-Specific Characteristics**

**Constructor Options (Interview Important):**

| Constructor                                 | Purpose                           | Example                                    |
| ------------------------------------------- | --------------------------------- | ------------------------------------------ |
| `TreeSet()`                                 | Natural ordering                  | `new TreeSet<>()`                          |
| `TreeSet(Comparator<? super E> comparator)` | Custom ordering                   | `new TreeSet<>(Comparator.reverseOrder())` |
| `TreeSet(Collection<? extends E> c)`        | From collection, natural ordering | `new TreeSet<>(list)`                      |
| `TreeSet(SortedSet<E> s)`                   | From sorted set, same ordering    | `new TreeSet<>(sortedSet)`                 |

**Ordering Requirements:**

- **Natural ordering:** Elements must implement `Comparable<E>`
- **Custom ordering:** Provide `Comparator<E>` at construction
- **Consistent with equals:** `compareTo()`/`compare()` should be consistent with `equals()`

---

#### **NavigableSet-Specific Methods (Interview Important)**

**Range View Operations:**

| Method                                | Purpose                | Returns                |
| ------------------------------------- | ---------------------- | ---------------------- |
| `SortedSet<E> headSet(E toElement)`   | Elements < toElement   | View (changes reflect) |
| `SortedSet<E> tailSet(E fromElement)` | Elements ≥ fromElement | View (changes reflect) |
| `SortedSet<E> subSet(E from, E to)`   | Elements [from, to)    | View (changes reflect) |

**NavigableSet Operations:**

| Method                             | Purpose                 | Complexity |
| ---------------------------------- | ----------------------- | ---------- |
| `E first()`                        | Returns first (lowest)  | O(log n)   |
| `E last()`                         | Returns last (highest)  | O(log n)   |
| `E lower(E e)`                     | Largest element < e     | O(log n)   |
| `E floor(E e)`                     | Largest element ≤ e     | O(log n)   |
| `E ceiling(E e)`                   | Smallest element ≥ e    | O(log n)   |
| `E higher(E e)`                    | Smallest element > e    | O(log n)   |
| `E pollFirst()`                    | Remove and return first | O(log n)   |
| `E pollLast()`                     | Remove and return last  | O(log n)   |
| `Iterator<E> descendingIterator()` | Reverse order iterator  | O(1)       |
| `NavigableSet<E> descendingSet()`  | Reverse view            | O(1)       |

---

#### **Performance Summary (Interview)**

| Operation               | Complexity             | Reason                         |
| ----------------------- | ---------------------- | ------------------------------ |
| `add(e)`                | **O(log n)**           | Tree insertion + balancing     |
| `remove(Object)`        | **O(log n)**           | Tree removal + balancing       |
| `contains(Object)`      | **O(log n)**           | Binary search in tree          |
| `first()` / `last()`    | **O(log n)**           | Navigate to leftmost/rightmost |
| `ceiling()` / `floor()` | **O(log n)**           | Tree navigation                |
| `iterator.next()`       | **O(log n)** amortized | In-order traversal             |

**Note:** All operations **O(log n)** - slower than HashSet (O(1)) but provides sorting.

---

#### **Interview Questions & Answers**

**Q1: TreeSet vs HashSet - when to use which?**

- **A:**
  - **HashSet:** Fast operations (O(1)), no ordering
  - **TreeSet:** Sorted order (O(log n)), range queries, NavigableSet operations

**Q2: Can TreeSet contain null?**

- **A:** **No!** Throws `NullPointerException` because elements must be comparable (null cannot be compared).

**Q3: What data structure does TreeSet use?**

- **A:** **Red-Black Tree** (self-balancing binary search tree) - guarantees O(log n) operations.

**Q4: How does TreeSet handle duplicates?**

- **A:** Uses `compareTo()` (or `compare()`) to detect duplicates. If comparison returns 0, element considered duplicate and **not added**.

**Q5: What happens if elements are not Comparable?**

- **A:** Throws `ClassCastException` at runtime when adding second element (first element added successfully).
  ```java
  TreeSet<Object> set = new TreeSet<>();
  set.add(new Object()); // OK
  set.add(new Object()); // ClassCastException!
  ```

**Q6: TreeSet vs TreeMap - what's the relationship?**

- **A:** TreeSet is **backed by TreeMap**. Elements stored as keys, value is dummy `PRESENT` object (same as HashSet/HashMap).

**Q7: What does "consistent with equals" mean?**

- **A:** `(x.compareTo(y) == 0)` should have same boolean value as `x.equals(y)`. Violating this can cause unexpected behavior.

**Q8: How to create descending order TreeSet?**

- **A:** Use `Collections.reverseOrder()` or `Comparator.reverseOrder()`:
  ```java
  TreeSet<Integer> descending = new TreeSet<>(Collections.reverseOrder());
  ```

---

#### **Usage Examples**

**Natural Ordering:**

```java
TreeSet<Integer> set = new TreeSet<>();
set.add(5);
set.add(2);
set.add(8);
set.add(1);

System.out.println(set); // [1, 2, 5, 8] - sorted!
```

**Custom Comparator:**

```java
// Sort strings by length
TreeSet<String> set = new TreeSet<>(Comparator.comparingInt(String::length));
set.add("apple");
set.add("pie");
set.add("banana");

System.out.println(set); // [pie, apple, banana] - by length
```

**NavigableSet Operations:**

```java
TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));

System.out.println(set.first());      // 1
System.out.println(set.last());       // 9
System.out.println(set.lower(5));     // 3 (largest < 5)
System.out.println(set.floor(5));     // 5 (largest ≤ 5)
System.out.println(set.ceiling(6));   // 7 (smallest ≥ 6)
System.out.println(set.higher(5));    // 7 (smallest > 5)
```

**Range Views:**

```java
TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

SortedSet<Integer> headSet = set.headSet(5);   // [1, 2, 3, 4]
SortedSet<Integer> tailSet = set.tailSet(5);   // [5, 6, 7, 8, 9]
SortedSet<Integer> subSet = set.subSet(3, 7);  // [3, 4, 5, 6]

// Views reflect changes to original set
set.add(0);
System.out.println(headSet); // [0, 1, 2, 3, 4]
```

**Descending Order:**

```java
TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));

// Descending iterator
Iterator<Integer> descIter = set.descendingIterator();
while (descIter.hasNext()) {
    System.out.println(descIter.next()); // 9, 7, 5, 3, 1
}

// Descending view
NavigableSet<Integer> descSet = set.descendingSet();
System.out.println(descSet); // [9, 7, 5, 3, 1]
```

---

#### **When to Use**

✅ **Use TreeSet when:**

- Need **sorted order** (natural or custom)
- Need **range queries** (headSet, tailSet, subSet)
- Need **NavigableSet operations** (ceiling, floor, higher, lower)
- Need **first/last element** access
- Implementing **sorted unique collection**

❌ **Avoid when:**

- **Don't need ordering** (use HashSet - much faster O(1))
- Need **fast operations** (O(1)) over ordering
- Elements cannot be compared (no Comparable/Comparator)
- Need **null elements** (use HashSet or LinkedHashSet)

---

## **Comparison: HashSet vs LinkedHashSet vs TreeSet**

| Feature             | HashSet         | LinkedHashSet            | TreeSet                     |
| ------------------- | --------------- | ------------------------ | --------------------------- |
| **Data Structure**  | Hash table      | Hash table + linked list | Red-Black tree              |
| **Ordering**        | ❌ Unordered    | ✅ Insertion order       | ✅ Sorted order             |
| **Add**             | O(1)            | O(1)                     | O(log n)                    |
| **Remove**          | O(1)            | O(1)                     | O(log n)                    |
| **Contains**        | O(1)            | O(1)                     | O(log n)                    |
| **Null elements**   | ✅ One null     | ✅ One null              | ❌ No null                  |
| **Memory overhead** | Low             | Medium                   | Medium                      |
| **Thread-Safe**     | ❌ No           | ❌ No                    | ❌ No                       |
| **Duplicates**      | ❌ No           | ❌ No                    | ❌ No                       |
| **Best for**        | Fast operations | Insertion order          | Sorted order, range queries |

---

## **Quick Decision Guide**

```
Need Set implementation?
├─ Need sorted order?
│   └─ Yes → TreeSet
│       ├─ Natural ordering (Comparable)
│       └─ Custom ordering (Comparator)
│
├─ Need insertion order?
│   └─ Yes → LinkedHashSet
│
├─ Need fastest operations?
│   └─ Yes → HashSet
│
└─ Need thread-safety?
    ├─ HashSet → Collections.synchronizedSet() or ConcurrentHashMap.newKeySet()
    ├─ LinkedHashSet → Collections.synchronizedSet()
    └─ TreeSet → Collections.synchronizedSortedSet() or ConcurrentSkipListSet
```

**General Recommendation:**

- ✅ **HashSet** - default choice (fastest, most common)
- ✅ **LinkedHashSet** - when insertion order matters
- ✅ **TreeSet** - when sorted order or range queries needed

---

## **Common Interview Scenarios**

### **Scenario 1: Remove Duplicates from List**

**Use:** `HashSet` (fastest) or `LinkedHashSet` (preserve order)

```java
List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3);
Set<Integer> unique = new HashSet<>(list); // or LinkedHashSet
```

### **Scenario 2: Find Common Elements (Intersection)**

**Use:** `HashSet` with `retainAll()`

```java
Set<Integer> set1 = new HashSet<>(list1);
set1.retainAll(list2); // Intersection
```

### **Scenario 3: Find Top K Frequent Elements**

**Use:** `HashMap` (count) + `PriorityQueue` or `TreeSet`

### **Scenario 4: Range Queries (e.g., find all elements between X and Y)**

**Use:** `TreeSet` with `subSet(from, to)`

### **Scenario 5: Maintain Sorted Unique Collection**

**Use:** `TreeSet`

### **Scenario 6: LRU Cache (Keys)**

**Use:** `LinkedHashSet` (for insertion order tracking)

---

## **⚡ Performance Analysis – Set Implementations**

### **Detailed Time Complexity**

#### **HashSet Performance**

| Operation            | Average Case | Worst Case | Explanation                                                            |
| -------------------- | ------------ | ---------- | ---------------------------------------------------------------------- |
| `add(E e)`           | **O(1)**     | **O(n)**   | Hash-based insertion, worst case when all elements hash to same bucket |
| `remove(Object o)`   | **O(1)**     | **O(n)**   | Hash-based lookup and removal                                          |
| `contains(Object o)` | **O(1)**     | **O(n)**   | Hash-based lookup                                                      |
| `size()`             | **O(1)**     | **O(1)**   | Cached size field                                                      |
| `isEmpty()`          | **O(1)**     | **O(1)**   | Check size == 0                                                        |
| `clear()`            | **O(n)**     | **O(n)**   | Must clear all buckets                                                 |
| `iterator.next()`    | **O(1)**     | **O(1)**   | Iterating through buckets                                              |

**Best Use Cases:**

- ✅ **Fast membership testing** (`contains()`)
- ✅ **Removing duplicates** from collections
- ✅ **Intersection/union operations** on sets
- ✅ **Caching unique values**
- ✅ **No ordering** required
- ❌ No index access
- ❌ Unordered iteration
- ❌ Cannot find min/max efficiently

**Hash Collision Impact:**

- **Good hash function:** O(1) average case (uniform distribution)
- **Poor hash function:** Degrades to O(n) (all elements in same bucket)
- **Java 8+ optimization:** Buckets with ≥8 elements convert to balanced trees (O(log n) worst case)

**Load Factor & Resizing:**

```
Default capacity: 16
Default load factor: 0.75
Resizes when: size > capacity × load factor
New capacity: capacity × 2

Example: 16 → 24 → 48 → 96 → 192...
Resize is O(n) operation (rehash all elements)
```

---

#### **LinkedHashSet Performance**

| Operation            | Average Case | Worst Case | Explanation                          |
| -------------------- | ------------ | ---------- | ------------------------------------ |
| `add(E e)`           | **O(1)**     | **O(n)**   | HashMap + link to doubly-linked list |
| `remove(Object o)`   | **O(1)**     | **O(n)**   | HashMap removal + unlink from list   |
| `contains(Object o)` | **O(1)**     | **O(n)**   | Hash-based lookup (same as HashSet)  |
| `size()`             | **O(1)**     | **O(1)**   | Cached size field                    |
| `iterator.next()`    | **O(1)**     | **O(1)**   | Follow linked list order             |

**Best Use Cases:**

- ✅ **Predictable iteration order** (insertion order)
- ✅ **Remove duplicates while preserving order**
- ✅ Same performance as HashSet with ordering guarantee
- ✅ **LRU cache keys** (track insertion order)
- ❌ Slightly higher memory overhead than HashSet
- ❌ Still unordered in terms of natural/sorted order

**Memory Overhead:**

```
HashSet: HashMap (buckets + nodes)
LinkedHashSet: HashMap + doubly-linked list (additional 2 references per entry)
```

---

#### **TreeSet Performance (SortedSet/NavigableSet)**

| Operation              | Complexity     | Explanation                                           |
| ---------------------- | -------------- | ----------------------------------------------------- |
| `add(E e)`             | **O(log n)**   | Balanced Red-Black tree insertion                     |
| `remove(Object o)`     | **O(log n)**   | Balanced tree removal with rebalancing                |
| `contains(Object o)`   | **O(log n)**   | Binary search in balanced tree                        |
| `first()`              | **O(log n)**   | Navigate to leftmost node                             |
| `last()`               | **O(log n)**   | Navigate to rightmost node                            |
| `ceiling(E e)`         | **O(log n)**   | Find smallest element ≥ e                             |
| `floor(E e)`           | **O(log n)**   | Find largest element ≤ e                              |
| `higher(E e)`          | **O(log n)**   | Find smallest element > e                             |
| `lower(E e)`           | **O(log n)**   | Find largest element < e                              |
| `subSet(E from, E to)` | **O(log n)**   | Create view (actual traversal is O(k) for k elements) |
| `headSet(E to)`        | **O(log n)**   | Create view of elements < to                          |
| `tailSet(E from)`      | **O(log n)**   | Create view of elements ≥ from                        |
| `size()`               | **O(1)**       | Cached size field                                     |
| `iterator.next()`      | **O(log n)\*** | In-order tree traversal                               |

`*` Amortized **O(1)** per element over entire iteration (total O(n) for n elements)

**Best Use Cases:**

- ✅ **Sorted order** required (natural or custom comparator)
- ✅ **Range queries** (subSet, headSet, tailSet)
- ✅ **NavigableSet operations** (ceiling, floor, higher, lower)
- ✅ **Find min/max** efficiently
- ✅ **Ordered iteration**
- ✅ **Top-K elements** (maintain sorted set of size K)
- ❌ Slower than HashSet for basic operations (O(log n) vs O(1))
- ❌ Elements must be **Comparable** or use **Comparator**
- ❌ **No null elements** allowed (throws NullPointerException)

**Tree Structure:**

```
Red-Black Tree properties:
- Self-balancing binary search tree
- Height: O(log n)
- All operations: O(log n)
- Maintains sorted order during modifications
```

---

### **Space Complexity Comparison**

| Implementation    | Memory Per Element      | Extra Overhead                        | Notes                                      |
| ----------------- | ----------------------- | ------------------------------------- | ------------------------------------------ |
| **HashSet**       | Element + node          | Hash table buckets                    | Backed by HashMap, stores elements as keys |
| **LinkedHashSet** | Element + node + 2 refs | Linked list + buckets                 | Additional doubly-linked list overhead     |
| **TreeSet**       | Element + node          | Tree node (parent, 2 children, color) | Red-Black tree node overhead               |

**Memory Estimate for 1000 elements:**

- **HashSet:** ~32-48 KB (depends on load factor, bucket utilization)
- **LinkedHashSet:** ~48-64 KB (additional linked list pointers)
- **TreeSet:** ~40-56 KB (tree node overhead with parent/child pointers)

---

### **Performance Tips for Set Implementations**

#### **✅ HashSet Best Practices**

1. **Pre-size for known capacity**:

   ```java
   // ✅ Avoid resizes for 10,000 elements
   // Formula: expectedSize / loadFactor + 1
   Set<String> set = new HashSet<>((int) (10000 / 0.75 + 1));

   // ❌ Starts at 16, resizes multiple times
   Set<String> set = new HashSet<>();
   ```

2. **Use good hash functions**:

   ```java
   // ✅ Override hashCode() properly
   @Override
   public int hashCode() {
       return Objects.hash(field1, field2, field3); // Good distribution
   }

   // ❌ Poor hash function
   @Override
   public int hashCode() {
       return 1; // All objects hash to same bucket!
   }
   ```

3. **Check containment before adding**:

   ```java
   // ✅ add() returns boolean
   if (set.add(element)) {
       // Element was not present, now added
   }

   // ❌ Unnecessary double lookup
   if (!set.contains(element)) {
       set.add(element);
   }
   ```

4. **Use for deduplication**:

   ```java
   // ✅ O(n) deduplication
   List<String> list = Arrays.asList("a", "b", "a", "c", "b");
   Set<String> unique = new HashSet<>(list);

   // ❌ O(n²) with List.contains()
   List<String> unique = new ArrayList<>();
   for (String s : list) {
       if (!unique.contains(s)) unique.add(s);
   }
   ```

---

#### **✅ LinkedHashSet Best Practices**

1. **Use for order-preserving deduplication**:

   ```java
   // ✅ Removes duplicates, preserves first occurrence order
   List<String> list = Arrays.asList("a", "b", "a", "c", "b");
   Set<String> unique = new LinkedHashSet<>(list);
   // Result: [a, b, c] (insertion order)
   ```

2. **Track insertion order**:

   ```java
   // ✅ Process elements in insertion order
   Set<String> processedOrder = new LinkedHashSet<>();
   processedOrder.add("first");
   processedOrder.add("second");
   for (String s : processedOrder) {
       // Guaranteed insertion order
   }
   ```

3. **Convert between List and Set**:

   ```java
   // List → Set (preserve order, remove duplicates)
   Set<String> set = new LinkedHashSet<>(list);

   // Set → List (preserve order)
   List<String> list = new ArrayList<>(set);
   ```

---

#### **✅ TreeSet Best Practices**

1. **Provide Comparator for custom ordering**:

   ```java
   // ✅ Custom comparator
   Set<String> set = new TreeSet<>(Comparator.comparing(String::length));

   // ✅ Reverse natural order
   Set<Integer> set = new TreeSet<>(Collections.reverseOrder());

   // ❌ ClassCastException if elements not Comparable
   Set<MyClass> set = new TreeSet<>(); // MyClass must implement Comparable
   ```

2. **Use range operations**:

   ```java
   TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 5, 10, 15, 20));

   // Get elements between 5 and 15 (inclusive from, exclusive to)
   Set<Integer> range = set.subSet(5, 16); // [5, 10, 15]

   // Get elements < 10
   Set<Integer> head = set.headSet(10); // [1, 5]

   // Get elements ≥ 10
   Set<Integer> tail = set.tailSet(10); // [10, 15, 20]
   ```

3. **Use navigation methods**:

   ```java
   TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 5, 10, 15, 20));

   set.first();        // 1 (smallest)
   set.last();         // 20 (largest)
   set.ceiling(7);     // 10 (smallest ≥ 7)
   set.floor(7);       // 5 (largest ≤ 7)
   set.higher(10);     // 15 (smallest > 10)
   set.lower(10);      // 5 (largest < 10)
   ```

4. **Maintain top-K elements**:

   ```java
   // Maintain only top 10 elements
   TreeSet<Integer> topK = new TreeSet<>(Collections.reverseOrder());
   for (int num : numbers) {
       topK.add(num);
       if (topK.size() > 10) {
           topK.pollLast(); // Remove smallest (keep top 10)
       }
   }
   ```

5. **Implement Comparable properly**:

   ```java
   class Person implements Comparable<Person> {
       private String name;
       private int age;

       @Override
       public int compareTo(Person other) {
           // ✅ Consistent with equals
           int cmp = Integer.compare(this.age, other.age);
           if (cmp != 0) return cmp;
           return this.name.compareTo(other.name);
       }

       @Override
       public boolean equals(Object obj) {
           // Must be consistent with compareTo
       }
   }
   ```

---

#### **⚠️ Common Performance Pitfalls**

1. **Using List.contains() instead of Set**:

   ```java
   // ❌ O(n²) for large lists
   List<String> list = new ArrayList<>();
   for (String s : data) {
       if (!list.contains(s)) { // O(n) each time
           list.add(s);
       }
   }

   // ✅ O(n) with Set
   Set<String> set = new HashSet<>(data); // Automatic deduplication
   ```

2. **Poor hashCode() implementation**:

   ```java
   // ❌ All objects hash to same bucket → O(n) operations
   @Override
   public int hashCode() {
       return 42; // NEVER do this!
   }

   // ✅ Good distribution
   @Override
   public int hashCode() {
       return Objects.hash(field1, field2);
   }
   ```

3. **Using TreeSet when HashSet would work**:

   ```java
   // ❌ O(n log n) operations when you don't need sorting
   Set<String> set = new TreeSet<>();
   set.add("item"); // O(log n)

   // ✅ O(n) operations when sorting not needed
   Set<String> set = new HashSet<>();
   set.add("item"); // O(1)
   ```

4. **Not handling null in TreeSet**:

   ```java
   // ❌ NullPointerException
   TreeSet<String> set = new TreeSet<>();
   set.add(null); // Throws NPE

   // ✅ HashSet allows null
   HashSet<String> set = new HashSet<>();
   set.add(null); // OK (only one null allowed)
   ```

5. **Modifying elements after adding to HashSet/TreeSet**:

   ```java
   // ❌ Breaks internal structure
   Person p = new Person("John", 30);
   Set<Person> set = new HashSet<>();
   set.add(p);
   p.setAge(40); // Changes hashCode → set.contains(p) may return false!

   // ✅ Use immutable objects or don't modify after adding
   ```

---

### **HashSet vs LinkedHashSet vs TreeSet: When to Use What**

| Scenario                 | HashSet | LinkedHashSet | TreeSet | Reason                         |
| ------------------------ | ------- | ------------- | ------- | ------------------------------ |
| Fast lookups             | ✅      | ✅            | ❌      | O(1) vs O(log n)               |
| Preserve insertion order | ❌      | ✅            | ❌      | LinkedHashSet maintains order  |
| Sorted iteration         | ❌      | ❌            | ✅      | TreeSet maintains sorted order |
| Range queries            | ❌      | ❌            | ✅      | subSet, headSet, tailSet       |
| Find min/max             | ❌      | ❌            | ✅      | first(), last()                |
| Memory efficiency        | ✅      | ❌            | ❌      | Least overhead                 |
| Allow null               | ✅      | ✅            | ❌      | TreeSet throws NPE             |
| Custom ordering          | ❌      | ❌            | ✅      | TreeSet with Comparator        |
| Large datasets           | ✅      | ✅            | ❌      | O(1) better than O(log n)      |
| Deduplication            | ✅      | ✅            | ✅      | All enforce uniqueness         |

**General Recommendations:**

- **Default choice:** HashSet (fastest for basic operations)
- **Need order preserved:** LinkedHashSet
- **Need sorted order or range queries:** TreeSet
- **Don't need Set behavior?** Consider ArrayList (if duplicates allowed and index access needed)
