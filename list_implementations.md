## **List Implementations Deep Dive**

### **1.1 ArrayList**

**Definition:** Resizable-array implementation of List interface. **Backed by dynamic array**, provides **fast random access (O(1))** but **slow insertions/deletions in middle (O(n))**.

**Key Characteristics:**

- ✅ **Dynamic array** - automatically resizes when capacity exceeded
- ✅ **Fast random access** - O(1) get/set by index (implements `RandomAccess` marker interface)
- ✅ **Allows null** and duplicates
- ❌ **Not synchronized** - not thread-safe
- ⚡ **Initial capacity:** 10 (default)
- ⚡ **Growth rate:** ~50% (newCapacity = oldCapacity + oldCapacity >> 1)

**Capacity Growth Example:**

```
10 → 15 → 22 → 33 → 49 → 73 → 109...
```

---

#### **ArrayList-Specific Methods**

**Capacity Management (Interview Important):**

| Method                            | Purpose                  | When to Use                                |
| --------------------------------- | ------------------------ | ------------------------------------------ |
| `ensureCapacity(int minCapacity)` | Pre-allocates capacity   | Before bulk adds to avoid multiple resizes |
| `trimToSize()`                    | Reduces capacity to size | After bulk removes to save memory          |

**Example:**

```java
// ✅ Good: pre-allocate if you know size
List<String> list = new ArrayList<>(1000);

// ✅ Optimize before bulk operations
list.ensureCapacity(5000);

// ✅ Trim after removing many elements
list.trimToSize();
```

---

#### **Thread Safety (Common Interview Question)**

**Problem:** ArrayList is **NOT synchronized**

**⚠️ DON'T use Vector (legacy) - use modern alternatives below:**

**Modern Solutions:**

| Approach                    | Code                                              | Use Case                |
| --------------------------- | ------------------------------------------------- | ----------------------- |
| **Synchronized Wrapper** ⭐ | `Collections.synchronizedList(new ArrayList<>())` | Simple thread-safety    |
| **CopyOnWriteArrayList** ⭐ | `new CopyOnWriteArrayList<>()`                    | Read-heavy, rare writes |
| **External Lock**           | `synchronized(list) { ... }`                      | Compound operations     |
| ~~**Vector**~~ ❌           | ~~`new Vector<>()`~~                              | **LEGACY - Don't use!** |

```java
// ✅ Interview Answer: How to make ArrayList thread-safe?
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// ⚠️ Compound operations still need explicit sync
synchronized(syncList) {
    if (syncList.isEmpty()) {
        syncList.add("First");
    }
}

// ❌ WRONG: Don't use Vector (legacy since Java 1.0)
// Vector<String> vector = new Vector<>();  // Avoid!
```

---

#### **Fail-Fast Iterator (Interview Question)**

**Q: What happens if you modify ArrayList during iteration?**

**A:** Throws `ConcurrentModificationException` (fail-fast behavior)

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// ❌ Wrong: ConcurrentModificationException
for (String s : list) {
    list.remove(s);  // Structural modification!
}

// ✅ Correct: Use iterator.remove()
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("B")) {
        it.remove();  // Safe
    }
}

// ✅ Correct: Use removeIf (Java 8+)
list.removeIf(s -> s.equals("B"));
```

---

#### **Performance Summary (Interview)**

| Operation             | Complexity         | Reason                         |
| --------------------- | ------------------ | ------------------------------ |
| `get(index)`          | **O(1)**           | Direct array access            |
| `set(index, element)` | **O(1)**           | Direct array access            |
| `add(element)`        | **O(1) amortized** | Append at end (O(n) if resize) |
| `add(index, element)` | **O(n)**           | Must shift elements right      |
| `remove(index)`       | **O(n)**           | Must shift elements left       |
| `contains(element)`   | **O(n)**           | Linear search                  |

---

#### **Interview Questions & Answers**

**Q1: Why is ArrayList faster than LinkedList for random access?**

- **A:** ArrayList uses contiguous array (O(1) index access). LinkedList must traverse nodes (O(n)).

**Q2: When does ArrayList resize?**

- **A:** When size exceeds capacity. New capacity = old × 1.5 (grows by ~50%).

**Q3: ArrayList vs Vector?**

- **A:** Both use dynamic array. Vector is **synchronized** (thread-safe but slower). ArrayList is **not synchronized** (faster, single-threaded).

**Q4: How to avoid resizing overhead?**

- **A:** Use `ArrayList(int initialCapacity)` constructor or `ensureCapacity(int)` method.

**Q5: Can ArrayList store primitives?**

- **A:** No, only objects. Use wrapper classes: `ArrayList<Integer>` (autoboxing handles conversion).

**Q6: What is RandomAccess interface?**

- **A:** **Marker interface** (no methods) indicating fast random access. Used by algorithms to choose optimal traversal (index vs iterator).

---

#### **When to Use**

✅ **Use ArrayList when:**

- Frequent **random access** by index
- Mostly **read operations**
- **Iteration** needed
- Size **predictable** or **stable**

❌ **Avoid when:**

- Frequent **insertions/deletions in middle**
- **Thread-safety** needed (use CopyOnWriteArrayList)
- Need **queue operations** (use ArrayDeque)

---

### **1.2 LinkedList**

**Definition:** Doubly-linked list implementation of **List and Deque** interfaces. Permits all elements (including `null`). Provides **fast insertions/deletions at ends (O(1))** but **slow random access (O(n))**.

**Key Characteristics:**

- ✅ **Doubly-linked list** - each node has `prev` and `next` pointers
- ✅ **Fast add/remove at ends** - O(1) for first/last operations
- ✅ **Implements List + Deque** - can be used as list, queue, stack, or deque
- ✅ **Allows null** and duplicates
- ❌ **Not synchronized** - not thread-safe
- ❌ **Slow random access** - O(n) get/set by index (must traverse)
- ⚡ **High memory overhead** - each node stores data + 2 references

**Index Optimization:**

- Operations that index into the list **traverse from beginning or end**, whichever is **closer** to specified index
- Example: `get(80)` in 100-element list traverses backwards from end

---

#### **LinkedList-Specific Methods (Interview Important)**

**Deque Methods (Double-Ended Queue):**

| Category             | Methods                         | Complexity | Use Case              |
| -------------------- | ------------------------------- | ---------- | --------------------- |
| **Add at Ends**      | `addFirst(e)`, `addLast(e)`     | O(1)       | Deque operations      |
| **Remove from Ends** | `removeFirst()`, `removeLast()` | O(1)       | Deque operations      |
| **Get from Ends**    | `getFirst()`, `getLast()`       | O(1)       | Peek at ends          |
| **Offer (Queue)**    | `offerFirst(e)`, `offerLast(e)` | O(1)       | Returns boolean       |
| **Poll (Queue)**     | `pollFirst()`, `pollLast()`     | O(1)       | Returns null if empty |
| **Peek (Queue)**     | `peekFirst()`, `peekLast()`     | O(1)       | Returns null if empty |

**Stack Methods:**

| Method    | Purpose         | Complexity | Equivalent      |
| --------- | --------------- | ---------- | --------------- |
| `push(e)` | Push onto stack | O(1)       | `addFirst(e)`   |
| `pop()`   | Pop from stack  | O(1)       | `removeFirst()` |
| `peek()`  | Peek at top     | O(1)       | `peekFirst()`   |

**Queue Methods:**

| Method      | Purpose                    | Complexity | Equivalent    |
| ----------- | -------------------------- | ---------- | ------------- |
| `offer(e)`  | Add to queue               | O(1)       | `addLast(e)`  |
| `poll()`    | Remove from queue          | O(1)       | `pollFirst()` |
| `peek()`    | Peek at head               | O(1)       | `peekFirst()` |
| `element()` | Get head (throws if empty) | O(1)       | `getFirst()`  |

**Other Specific Methods:**

| Method                     | Purpose                              | Complexity |
| -------------------------- | ------------------------------------ | ---------- |
| `descendingIterator()`     | Returns iterator in reverse order    | O(1)       |
| `removeFirstOccurrence(o)` | Removes first occurrence (head→tail) | O(n)       |
| `removeLastOccurrence(o)`  | Removes last occurrence (head→tail)  | O(n)       |

---

#### **Thread Safety (Same as ArrayList)**

⚠️ **Not synchronized!** Wrap with `Collections.synchronizedList()` if needed.

```java
List<String> syncList = Collections.synchronizedList(new LinkedList<>());
```

---

#### **Fail-Fast Iterator (Same as ArrayList)**

Throws `ConcurrentModificationException` if structurally modified during iteration (except via iterator's own methods).

---

#### **Performance Summary (Interview)**

| Operation             | Complexity | Reason                                           |
| --------------------- | ---------- | ------------------------------------------------ |
| `get(index)`          | **O(n)**   | Must traverse nodes (optimized: from closer end) |
| `set(index, element)` | **O(n)**   | Must traverse nodes                              |
| `add(element)`        | **O(1)**   | Add at tail (no traversal)                       |
| `add(index, element)` | **O(n)**   | Traverse to index, then O(1) insert              |
| `addFirst(element)`   | **O(1)**   | Direct head manipulation                         |
| `addLast(element)`    | **O(1)**   | Direct tail manipulation                         |
| `removeFirst()`       | **O(1)**   | Direct head manipulation                         |
| `removeLast()`        | **O(1)**   | Direct tail manipulation                         |
| `remove(index)`       | **O(n)**   | Traverse to index, then O(1) remove              |
| `remove(Object)`      | **O(n)**   | Must search through nodes                        |
| `contains(element)`   | **O(n)**   | Must search through nodes                        |

---

#### **Interview Questions & Answers**

**Q1: ArrayList vs LinkedList - when to use which?**

- **A:**
  - **ArrayList:** Random access (get/set), mostly reads, predictable size
  - **LinkedList:** Frequent add/remove at **ends**, queue/stack operations

**Q2: Why is LinkedList slower for random access?**

- **A:** Must **traverse nodes** sequentially from head or tail. ArrayList uses direct array indexing (O(1)).

**Q3: Can LinkedList be used as a Stack?**

- **A:** Yes! Use `push()`, `pop()`, `peek()` methods. But **ArrayDeque is faster** (no node overhead).

**Q4: Can LinkedList be used as a Queue?**

- **A:** Yes! Use `offer()`, `poll()`, `peek()` methods. Implements `Deque` interface.

**Q5: What is the memory overhead of LinkedList?**

- **A:** High - each node stores data + 2 references (prev + next) = ~24 bytes overhead per element.

**Q6: Why doesn't LinkedList have capacity like ArrayList?**

- **A:** Nodes are allocated **individually** on demand. No contiguous array, so no capacity/resize concept.

**Q7: LinkedList vs ArrayDeque for queue operations?**

- **A:** **ArrayDeque is faster** - cache-friendly array, no node allocation overhead. Use LinkedList only if need List + Deque features.

---

#### **When to Use**

✅ **Use LinkedList when:**

- Frequent **insertions/deletions at beginning or end** (O(1))
- Need **both List and Deque** functionality
- Implementing **queue or stack** (though ArrayDeque is better)
- **Unknown size**, frequent structural changes

❌ **Avoid when:**

- Need **random access** by index (use ArrayList)
- **Memory-constrained** (high overhead per element)
- Need **fast iteration** (ArrayList is cache-friendly)
- Need **queue/stack only** (use ArrayDeque instead)

**As a Stack:**

| Method           | Purpose                                       | Complexity |
| ---------------- | --------------------------------------------- | ---------- |
| `void push(E e)` | Pushes element onto stack (same as addFirst)  | O(1)       |
| `E pop()`        | Pops element from stack (same as removeFirst) | O(1)       |

**As a Queue:**

| Method               | Purpose                                            | Complexity |
| -------------------- | -------------------------------------------------- | ---------- |
| `boolean offer(E e)` | Adds element (same as offerLast)                   | O(1)       |
| `E poll()`           | Retrieves and removes head (same as pollFirst)     | O(1)       |
| `E peek()`           | Retrieves head without removal (same as peekFirst) | O(1)       |

**Usage Example:**

```java
// As a List
LinkedList<String> list = new LinkedList<>();
list.add("A");
list.addFirst("B");  // O(1) - efficient!
list.addLast("C");   // O(1)

// As a Queue (FIFO)
Queue<String> queue = new LinkedList<>();
queue.offer("First");
queue.offer("Second");
String head = queue.poll();  // "First"

// As a Stack (LIFO)
Deque<String> stack = new LinkedList<>();
stack.push("Bottom");
stack.push("Top");
String top = stack.pop();  // "Top"
```

**When to Use LinkedList:**

- ✅ Frequent **insertions/deletions** at beginning or end
- ✅ **Queue** or **Deque** operations (FIFO/LIFO)
- ✅ **Stack** implementation
- ✅ **Memory flexibility** (no wasted capacity like ArrayList)
- ❌ Avoid for random access by index
- ❌ Avoid for memory-constrained environments
- ❌ Avoid for read-heavy workloads (use ArrayList instead)

---

### **1.3 Vector**

**Definition:** Legacy synchronized implementation of a growable array. **Similar to ArrayList but synchronized** on every method.

**Key Characteristics:**

- ✅ **Synchronized** - thread-safe (all methods synchronized)
- ✅ **Legacy class** - exists since Java 1.0
- ✅ **Allows null elements** and **duplicates**
- ✅ **Fast random access** - O(1) get/set by index
- ❌ **Performance overhead** - synchronization on every method
- ⚡ **Initial capacity:** 10 (default), grows by 100% (doubles) when full

**Vector-Specific Considerations:**

| Aspect                  | Details                                                                                    |
| ----------------------- | ------------------------------------------------------------------------------------------ |
| **Capacity Management** | Initial: 10, Growth: doubles (2x) by default or by `capacityIncrement`                     |
| **Synchronization**     | Every method synchronized - high overhead even for single-threaded use                     |
| **Legacy Status**       | ⚠️ Consider using `ArrayList` + `Collections.synchronizedList()` or `CopyOnWriteArrayList` |
| **Thread Safety**       | Built-in, but coarse-grained (entire method locked)                                        |
| **Performance**         | Slower than ArrayList due to synchronization overhead                                      |

**Vector-Specific Methods (Beyond List Interface):**

| Method                                 | Purpose                                                            |
| -------------------------------------- | ------------------------------------------------------------------ |
| `void addElement(E obj)`               | Adds element at end (legacy, use `add()` instead)                  |
| `E elementAt(int index)`               | Returns element at index (legacy, use `get()` instead)             |
| `Enumeration<E> elements()`            | Returns enumeration of elements (legacy, use `iterator()` instead) |
| `E firstElement()`                     | Returns first element (throws exception if empty)                  |
| `E lastElement()`                      | Returns last element (throws exception if empty)                   |
| `void removeElementAt(int index)`      | Removes element at index (legacy, use `remove(int)` instead)       |
| `void removeAllElements()`             | Removes all elements (legacy, use `clear()` instead)               |
| `int capacity()`                       | Returns current capacity of vector                                 |
| `void ensureCapacity(int minCapacity)` | Increases capacity to at least `minCapacity`                       |
| `void setSize(int newSize)`            | Sets size of vector (truncates or pads with nulls)                 |
| `void trimToSize()`                    | Trims capacity to current size                                     |

**Usage Example:**

```java
// Legacy style (avoid)
Vector<String> vector = new Vector<>();
vector.addElement("A");  // Legacy method
String first = vector.firstElement();

// Modern style (preferred)
vector.add("B");  // Standard List method
String element = vector.get(0);

// Check capacity
int capacity = vector.capacity();  // Vector-specific
```

**When to Use Vector:**

- ⚠️ **Legacy code maintenance** - when working with old codebases
- ⚠️ **Simple thread-safety** - when performance isn't critical
- ❌ **Not recommended for new code** - use alternatives:
  - `ArrayList` for single-threaded
  - `Collections.synchronizedList(new ArrayList<>())` for synchronized access
  - `CopyOnWriteArrayList` for concurrent read-heavy scenarios
  - `ConcurrentLinkedQueue` for concurrent queue operations

**Why Vector is Discouraged:**

1. ❌ **Synchronizes every method** - unnecessary overhead for single-threaded use
2. ❌ **Coarse-grained locking** - locks entire collection even for read operations
3. ❌ **Legacy API** - has obsolete methods (addElement, removeElementAt, etc.)
4. ✅ **Better alternatives exist** - more flexible and performant

---

### **1.4 Stack**

**Definition:** Legacy LIFO (Last-In-First-Out) stack implementation that **extends Vector**. Represents a stack of objects.

**Key Characteristics:**

- ✅ **Extends Vector** - inherits all Vector methods and synchronization
- ✅ **LIFO operations** - push, pop, peek
- ✅ **Synchronized** - thread-safe (inherits from Vector)
- ❌ **Legacy class** - exists since Java 1.0
- ❌ **Discouraged** - use `Deque` (ArrayDeque or LinkedList) instead

**Stack-Specific Considerations:**

| Aspect              | Details                                                                 |
| ------------------- | ----------------------------------------------------------------------- |
| **Inheritance**     | Extends Vector (violates LIFO encapsulation - can access by index)      |
| **Synchronization** | Inherited from Vector - synchronized methods                            |
| **Legacy Status**   | ⚠️ **Discouraged** - use `Deque<E> stack = new ArrayDeque<>()` instead  |
| **Design Flaw**     | Extends Vector, exposing non-stack operations (get, add at index, etc.) |
| **Performance**     | Slower than ArrayDeque due to synchronization and Vector overhead       |

**Stack-Specific Methods:**

| Method                 | Purpose                                                     | Equivalent Deque Method    |
| ---------------------- | ----------------------------------------------------------- | -------------------------- |
| `E push(E item)`       | Pushes item onto top of stack                               | `addFirst(E)` or `push(E)` |
| `E pop()`              | Removes and returns top element (throws if empty)           | `removeFirst()` or `pop()` |
| `E peek()`             | Returns top element without removal (throws if empty)       | `peekFirst()` or `peek()`  |
| `boolean empty()`      | Tests if stack is empty                                     | `isEmpty()`                |
| `int search(Object o)` | Returns 1-based position from top (1 = top, -1 = not found) | No direct equivalent       |

**Usage Example:**

```java
// ❌ Old way (discouraged)
Stack<String> stack = new Stack<>();
stack.push("First");
stack.push("Second");
String top = stack.pop();  // "Second"
String peek = stack.peek(); // "First"

// ✅ Modern way (recommended)
Deque<String> stack = new ArrayDeque<>();
stack.push("First");
stack.push("Second");
String top = stack.pop();  // "Second"
String peek = stack.peek(); // "First"
```

**Why Stack is Discouraged:**

| Problem                         | Explanation                                                    |
| ------------------------------- | -------------------------------------------------------------- |
| ❌ **Extends Vector**           | Inherits all Vector methods, breaking stack abstraction        |
| ❌ **Can access by index**      | `stack.get(0)` violates LIFO principle                         |
| ❌ **Synchronization overhead** | All methods synchronized (even if not needed)                  |
| ❌ **Poor design**              | Should implement interface, not extend concrete class          |
| ❌ **Slower than ArrayDeque**   | ArrayDeque is faster and doesn't have synchronization overhead |

**Design Flaw Example:**

```java
Stack<String> stack = new Stack<>();
stack.push("A");
stack.push("B");
stack.push("C");

// ❌ Breaking LIFO - can access by index!
String bottom = stack.get(0);  // "A" - should not be possible in true stack!
stack.add(1, "X");  // Insert in middle - violates stack semantics!
```

**Modern Alternative (ArrayDeque):**

```java
// ✅ Recommended approach
Deque<String> stack = new ArrayDeque<>();
stack.push("A");
stack.push("B");
stack.push("C");

String top = stack.pop();     // "C" - LIFO
String peek = stack.peek();   // "B"
boolean empty = stack.isEmpty();

// Cannot access by index - proper stack encapsulation!
// stack.get(0);  // ❌ Compile error - no such method
```

**When to Use Stack:**

- ⚠️ **Legacy code maintenance** - when working with old codebases
- ❌ **Not recommended for new code** - use `ArrayDeque` instead:
  ```java
  Deque<E> stack = new ArrayDeque<>();  // ✅ Fast, no synchronization
  ```

---

## **Comparison: ArrayList vs LinkedList**

| Feature                   | ArrayList                | LinkedList                      |
| ------------------------- | ------------------------ | ------------------------------- |
| **Data Structure**        | Dynamic array            | Doubly-linked list              |
| **Random Access**         | O(1) - Fast              | O(n) - Slow                     |
| **Insert at End**         | O(1) amortized           | O(1)                            |
| **Insert at Beginning**   | O(n)                     | O(1)                            |
| **Insert in Middle**      | O(n)                     | O(n)                            |
| **Remove from End**       | O(1)                     | O(1)                            |
| **Remove from Beginning** | O(n)                     | O(1)                            |
| **Memory Overhead**       | Low                      | High                            |
| **Thread-Safe**           | ❌ No                    | ❌ No                           |
| **Synchronization**       | None                     | None                            |
| **Introduced**            | Java 1.2                 | Java 1.2                        |
| **Recommendation**        | ✅ Modern                | ✅ Modern                       |
| **Best Use Case**         | Random access, iteration | Queue/Deque, add/remove at ends |

**Quick Decision Guide:**

```
Need List implementation?
├─ Frequent random access by index? → ArrayList
├─ Frequent add/remove at ends? → LinkedList
├─ Need thread-safety? → Collections.synchronizedList(ArrayList) or CopyOnWriteArrayList
└─ Need stack or queue operations? → ArrayDeque (recommended)

```

---

## **⚡ Performance Analysis – List Implementations**

### **Detailed Time Complexity**

#### **ArrayList Performance**

| Operation               | Complexity         | Explanation                                                        |
| ----------------------- | ------------------ | ------------------------------------------------------------------ |
| `add(E e)`              | **O(1) amortized** | Appending is O(1) unless array needs resize (grows by ~50% → O(n)) |
| `add(int index, E e)`   | **O(n)**           | Must shift all elements after index to the right                   |
| `remove(int index)`     | **O(n)**           | Must shift all elements after index to the left                    |
| `remove(Object o)`      | **O(n)**           | Must search for element (O(n)) + shift remaining elements          |
| `get(int index)`        | **O(1)**           | Direct array access by index                                       |
| `set(int index, E e)`   | **O(1)**           | Direct array access by index                                       |
| `contains(Object o)`    | **O(n)**           | Linear search through all elements                                 |
| `indexOf(Object o)`     | **O(n)**           | Linear search from beginning                                       |
| `lastIndexOf(Object o)` | **O(n)**           | Linear search from end                                             |
| `clear()`               | **O(n)**           | Must null out all references for garbage collection                |
| `size()`                | **O(1)**           | Cached size field                                                  |
| `iterator.next()`       | **O(1)**           | Sequential array access                                            |

**Best Use Cases:**

- ✅ Frequent **random access** by index (`get(i)`, `set(i)`)
- ✅ **Iteration** (cache-friendly sequential memory layout)
- ✅ **Appending** elements at the end
- ✅ **Read-heavy** workloads
- ❌ Avoid for frequent **insertions/deletions in middle**
- ❌ Avoid for **add/remove at beginning**

**Capacity Growth:**

```
Initial: 10 → 15 → 22 → 33 → 49 → 73 → 109 → 163...
Growth formula: newCapacity = oldCapacity + (oldCapacity >> 1)  // +50%
```

---

#### **LinkedList Performance**

| Operation             | Complexity | Explanation                                                              |
| --------------------- | ---------- | ------------------------------------------------------------------------ |
| `add(E e)`            | **O(1)**   | Add to tail is constant time (maintains tail reference)                  |
| `add(int index, E e)` | **O(n)**   | Must traverse to index (optimized: starts from nearest end), then insert |
| `addFirst(E e)`       | **O(1)**   | Direct head manipulation                                                 |
| `addLast(E e)`        | **O(1)**   | Direct tail manipulation                                                 |
| `remove(int index)`   | **O(n)**   | Must traverse to index, then remove                                      |
| `removeFirst()`       | **O(1)**   | Direct head manipulation                                                 |
| `removeLast()`        | **O(1)**   | Direct tail manipulation                                                 |
| `remove(Object o)`    | **O(n)**   | Must search through nodes sequentially                                   |
| `get(int index)`      | **O(n)**   | Must traverse from head or tail to index (optimized)                     |
| `set(int index, E e)` | **O(n)**   | Must traverse to index, then update                                      |
| `contains(Object o)`  | **O(n)**   | Must search through all nodes                                            |
| `indexOf(Object o)`   | **O(n)**   | Sequential search from beginning                                         |
| `size()`              | **O(1)**   | Cached size field                                                        |
| `iterator.next()`     | **O(1)**   | Follow node's next pointer                                               |

**Best Use Cases:**

- ✅ Frequent **insertions/deletions at beginning/end** (Queue/Deque operations)
- ✅ **Stack** or **Queue** implementations
- ✅ **Add/remove at both ends** (double-ended queue)
- ✅ When you need **ListIterator** with add/remove during iteration
- ❌ Avoid for **random access** by index
- ❌ Avoid for **memory-constrained** environments (each node has 3 references overhead)

**Memory Overhead:**

```
Each node stores:
- Element reference (8 bytes on 64-bit JVM)
- Previous node reference (8 bytes)
- Next node reference (8 bytes)
= 24 bytes overhead per element (vs ArrayList's ~0 bytes when within capacity)
```

---

### **Space Complexity Comparison**

| Implementation | Memory Per Element | Extra Overhead  | Notes                                         |
| -------------- | ------------------ | --------------- | --------------------------------------------- |
| **ArrayList**  | Element size       | Unused capacity | Grows by 50%, may waste space until trimmed   |
| **LinkedList** | Element + 24 bytes | Node overhead   | Each element wrapped in Node with 2 pointers  |
| **Vector**     | Element size       | Unused capacity | Similar to ArrayList, grows by 100% (doubles) |

**Example:** 1000 Integer objects (each Integer ~16 bytes)

- **ArrayList:** ~16 KB (elements) + possible unused capacity
- **LinkedList:** ~40 KB (16 KB elements + 24 KB node overhead)

---

### **Performance Tips for List Implementations**

#### **✅ ArrayList Best Practices**

1. **Pre-allocate capacity** for large collections:

   ```java
   // ✅ Avoid multiple resizes
   List<String> list = new ArrayList<>(10000);

   // ❌ Starts at 10, resizes ~15 times to reach 10,000
   List<String> list = new ArrayList<>();
   ```

2. **Trim to size** after bulk removals:

   ```java
   list.removeIf(s -> s.isEmpty());
   ((ArrayList<String>) list).trimToSize(); // Free unused capacity
   ```

3. **Use ensureCapacity** before bulk adds:

   ```java
   list.ensureCapacity(list.size() + 5000); // Pre-allocate
   for (int i = 0; i < 5000; i++) list.add(data[i]);
   ```

4. **Batch removals** (remove from end to beginning):

   ```java
   // ✅ O(n) - remove from end (no shifting)
   for (int i = list.size() - 1; i >= 0; i--) {
       if (condition) list.remove(i);
   }

   // ❌ O(n²) - remove from beginning (shifts all elements each time)
   for (int i = 0; i < list.size(); i++) {
       if (condition) list.remove(i);
   }
   ```

5. **Use removeIf()** for filtering:

   ```java
   // ✅ Optimized single-pass removal
   list.removeIf(s -> s.isEmpty());

   // ❌ Multiple shifts
   list.removeAll(Collections.singleton(""));
   ```

---

#### **✅ LinkedList Best Practices**

1. **Use Deque methods** for ends:

   ```java
   LinkedList<String> list = new LinkedList<>();
   list.addFirst("first");   // O(1)
   list.addLast("last");     // O(1)
   list.removeFirst();        // O(1)
   list.removeLast();         // O(1)
   ```

2. **Prefer Deque interface**:

   ```java
   // ✅ Use specific interface
   Deque<String> deque = new LinkedList<>();

   // ❌ Too general
   List<String> list = new LinkedList<>();
   ```

3. **Avoid random access**:

   ```java
   // ❌ O(n²) - get(i) is O(n)
   for (int i = 0; i < list.size(); i++) {
       process(list.get(i));
   }

   // ✅ O(n) - iterator is O(1) per element
   for (String s : list) {
       process(s);
   }
   ```

4. **Use ListIterator for modifications**:
   ```java
   ListIterator<String> it = list.listIterator();
   while (it.hasNext()) {
       String s = it.next();
       if (condition) it.remove(); // O(1) removal during iteration
   }
   ```

---

#### **⚠️ Common Performance Pitfalls**

1. **Using LinkedList for random access**:

   ```java
   // ❌ O(n²) with LinkedList
   for (int i = 0; i < list.size(); i++) {
       System.out.println(list.get(i)); // Each get() is O(n)
   }

   // ✅ O(n) with ArrayList
   // ✅ O(n) with LinkedList using iterator
   for (String s : list) {
       System.out.println(s);
   }
   ```

2. **Inserting at beginning of ArrayList**:

   ```java
   // ❌ O(n²) - shifts entire array each time
   for (int i = 0; i < 1000; i++) {
       list.add(0, element);
   }

   // ✅ O(n) - use LinkedList or add to end then reverse
   LinkedList<String> linked = new LinkedList<>();
   for (int i = 0; i < 1000; i++) {
       linked.addFirst(element); // O(1)
   }
   ```

3. **Not pre-sizing ArrayList**:

   ```java
   // ❌ Resizes ~15 times for 10,000 elements
   List<String> list = new ArrayList<>();
   for (int i = 0; i < 10000; i++) list.add(data[i]);

   // ✅ No resizing
   List<String> list = new ArrayList<>(10000);
   for (int i = 0; i < 10000; i++) list.add(data[i]);
   ```

4. **Using Vector/Stack for new code**:

   ```java
   // ❌ Synchronized overhead on every operation
   Vector<String> vector = new Vector<>();
   Stack<String> stack = new Stack<>();

   // ✅ Use modern alternatives
   List<String> list = new ArrayList<>();
   Deque<String> stack = new ArrayDeque<>();
   ```

---

### **ArrayList vs LinkedList: When to Use What**

| Scenario                | ArrayList | LinkedList | Reason                                     |
| ----------------------- | --------- | ---------- | ------------------------------------------ |
| Random access by index  | ✅        | ❌         | O(1) vs O(n)                               |
| Sequential iteration    | ✅        | ✅         | Both O(n), ArrayList is cache-friendly     |
| Add/remove at end       | ✅        | ✅         | Both O(1)                                  |
| Add/remove at beginning | ❌        | ✅         | O(n) vs O(1)                               |
| Add/remove in middle    | ❌        | ✅\*       | O(n) vs O(n), but LinkedList avoids shifts |
| Memory efficiency       | ✅        | ❌         | No node overhead                           |
| Unknown size            | ✅        | ✅         | ArrayList handles resizing well            |
| Large datasets          | ✅        | ❌         | Better cache locality                      |
| Queue/Stack operations  | ❌        | ✅         | Use ArrayDeque instead                     |

`*` LinkedList still O(n) for middle operations due to traversal, but avoids expensive array shifts

**General Recommendation:**

- **Default choice:** ArrayList (unless you have specific reason for LinkedList)
- **Queue/Stack:** Use ArrayDeque (better than both ArrayList and LinkedList)
- **Double-ended queue:** LinkedList or ArrayDeque
