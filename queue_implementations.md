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

# **Queue Implementations Deep Dive**

---

## **2.1 PriorityQueue**

**Definition:** Unbounded priority queue based on a **priority heap**. Elements are ordered according to their **natural ordering** or by a **Comparator** provided at queue construction time.

**Key Characteristics:**

- ✅ **Heap-based** - binary heap data structure (min-heap by default)
- ✅ **Priority ordering** - NOT FIFO! Elements ordered by priority (lowest/highest first)
- ✅ **Unbounded** - grows automatically as needed
- ✅ **Allows duplicates** - can have multiple elements with same priority
- ❌ **Does NOT allow null** - throws NullPointerException
- ❌ **Not synchronized** - not thread-safe
- ⚡ **Initial capacity:** 11 (default)

**Ordering:**

- **Natural ordering:** Elements must implement `Comparable<E>`
- **Custom ordering:** Provide `Comparator<E>` at construction
- **Min-heap (default):** Smallest element at head
- **Max-heap:** Use `Collections.reverseOrder()` comparator

---

#### **PriorityQueue-Specific Methods**

**Constructor Options (Interview Important):**

| Constructor                               | Purpose                           | Example                                          |
| ----------------------------------------- | --------------------------------- | ------------------------------------------------ |
| `PriorityQueue()`                         | Natural ordering, capacity 11     | `new PriorityQueue<>()`                          |
| `PriorityQueue(int initialCapacity)`      | Natural ordering, custom capacity | `new PriorityQueue<>(100)`                       |
| `PriorityQueue(Comparator<E> comparator)` | Custom ordering                   | `new PriorityQueue<>(Comparator.reverseOrder())` |
| `PriorityQueue(Collection<E> c)`          | From collection                   | `new PriorityQueue<>(list)`                      |

**Queue Operations:**

| Method               | Purpose                     | Complexity | Throws Exception?  |
| -------------------- | --------------------------- | ---------- | ------------------ |
| `boolean offer(E e)` | Insert element              | O(log n)   | No (returns false) |
| `E poll()`           | Remove and return head      | O(log n)   | No (returns null)  |
| `E peek()`           | Return head without removal | O(1)       | No (returns null)  |
| `boolean add(E e)`   | Insert element              | O(log n)   | Yes (if fails)     |
| `E remove()`         | Remove and return head      | O(log n)   | Yes (if empty)     |
| `E element()`        | Return head without removal | O(1)       | Yes (if empty)     |

**Other Methods:**

| Method                       | Purpose                       | Complexity |
| ---------------------------- | ----------------------------- | ---------- |
| `boolean remove(Object o)`   | Remove specific element       | O(n)       |
| `boolean contains(Object o)` | Check if element exists       | O(n)       |
| `int size()`                 | Get number of elements        | O(1)       |
| `void clear()`               | Remove all elements           | O(n)       |
| `Object[] toArray()`         | Convert to array (unordered!) | O(n)       |
| `Iterator<E> iterator()`     | Returns iterator (unordered!) | O(1)       |

---

#### **Performance Summary (Interview)**

| Operation              | Complexity   | Reason                               |
| ---------------------- | ------------ | ------------------------------------ |
| `offer(e)` / `add(e)`  | **O(log n)** | Heap insertion (bubble up)           |
| `poll()` / `remove()`  | **O(log n)** | Remove root, reheapify (bubble down) |
| `peek()` / `element()` | **O(1)**     | Access root of heap                  |
| `remove(Object)`       | **O(n)**     | Linear search + O(log n) removal     |
| `contains(Object)`     | **O(n)**     | Linear search through heap           |
| `size()`               | **O(1)**     | Cached size field                    |

---

#### **Interview Questions & Answers**

**Q1: What is a PriorityQueue and how does it differ from a regular Queue?**

- **A:** PriorityQueue orders elements by **priority** (natural or custom), not FIFO. Highest-priority element is always at head. Regular Queue is strictly FIFO.

**Q2: What data structure does PriorityQueue use internally?**

- **A:** **Binary heap** (array-based complete binary tree). Min-heap by default (smallest element at root).

**Q3: Can PriorityQueue contain null elements?**

- **A:** **No!** Throws `NullPointerException` because null cannot be compared.

**Q4: How do you create a max-heap PriorityQueue?**

- **A:** Use `Collections.reverseOrder()` comparator:
  ```java
  PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
  ```

**Q5: Is iteration order guaranteed in PriorityQueue?**

- **A:** **No!** `iterator()` does NOT guarantee priority order. Only `poll()` guarantees ordering.

**Q6: Can you access elements by index in PriorityQueue?**

- **A:** **No!** PriorityQueue is NOT a List. No random access. Only access via `peek()` and `poll()`.

**Q7: PriorityQueue vs TreeSet - when to use which?**

- **A:**
  - **PriorityQueue:** Need to repeatedly **remove minimum** (or maximum). Allows duplicates.
  - **TreeSet:** Need **sorted set** without duplicates. Supports range queries.

---

#### **Usage Examples**

**Min-Heap (Default):**

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(5);
minHeap.offer(2);
minHeap.offer(8);
minHeap.offer(1);

System.out.println(minHeap.poll()); // 1 (smallest)
System.out.println(minHeap.poll()); // 2
System.out.println(minHeap.poll()); // 5
```

**Max-Heap:**

```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
maxHeap.offer(5);
maxHeap.offer(2);
maxHeap.offer(8);

System.out.println(maxHeap.poll()); // 8 (largest)
System.out.println(maxHeap.poll()); // 5
```

**Custom Comparator:**

```java
// Priority by string length (shortest first)
PriorityQueue<String> queue = new PriorityQueue<>(
    Comparator.comparingInt(String::length)
);
queue.offer("apple");
queue.offer("pie");
queue.offer("banana");

System.out.println(queue.poll()); // "pie" (length 3)
System.out.println(queue.poll()); // "apple" (length 5)
```

---

#### **When to Use**

✅ **Use PriorityQueue when:**

- Need **priority-based processing** (task scheduling, event simulation)
- Implementing **Dijkstra's algorithm**, **Huffman coding**, **A\* search**
- Need **top K elements** (min/max heap)
- **Median finding** with two heaps
- **Merge K sorted lists**

❌ **Avoid when:**

- Need **strict FIFO** order (use LinkedList or ArrayDeque)
- Need **sorted set without duplicates** (use TreeSet)
- Need **random access** (not supported)
- Need **thread-safety** (use PriorityBlockingQueue)

---

## **2.2 ArrayDeque**

**Definition:** Resizable-array implementation of the **Deque** interface. No capacity restrictions; grows as necessary. **⭐ Modern replacement for legacy Stack class** - faster for stack operations and **LinkedList** for queue operations.

**Key Characteristics:**

- ✅ **Circular array** - efficient use of space
- ✅ **Fast operations** - O(1) for add/remove at both ends
- ✅ **No capacity restrictions** - grows automatically
- ✅ **Faster than LinkedList** - cache-friendly, no node overhead
- ✅ **Faster than Stack** ⚠️ (legacy) - no synchronization overhead
- ❌ **Does NOT allow null** - throws NullPointerException
- ❌ **Not synchronized** - not thread-safe
- ⚡ **Initial capacity:** 16 (default), grows by doubling

**⚠️ Use ArrayDeque instead of Stack class:**

```java
// ❌ WRONG: Stack is legacy (extends Vector, synchronized overhead)
// Stack<String> stack = new Stack<>();  // Avoid!

// ✅ CORRECT: Use ArrayDeque for stack operations
Deque<String> stack = new ArrayDeque<>();
stack.push("item");
stack.pop();
```

---

#### **ArrayDeque-Specific Methods**

**Deque Operations (Both Ends):**

| Operation Type | First Element                   | Last Element                  | Complexity |
| -------------- | ------------------------------- | ----------------------------- | ---------- |
| **Insert**     | `addFirst(e)` / `offerFirst(e)` | `addLast(e)` / `offerLast(e)` | O(1)       |
| **Remove**     | `removeFirst()` / `pollFirst()` | `removeLast()` / `pollLast()` | O(1)       |
| **Examine**    | `getFirst()` / `peekFirst()`    | `getLast()` / `peekLast()`    | O(1)       |

**Stack Operations (LIFO):**

| Method    | Purpose         | Complexity | Equivalent      |
| --------- | --------------- | ---------- | --------------- |
| `push(e)` | Push onto stack | O(1)       | `addFirst(e)`   |
| `pop()`   | Pop from stack  | O(1)       | `removeFirst()` |
| `peek()`  | Peek at top     | O(1)       | `peekFirst()`   |

**Queue Operations (FIFO):**

| Method                 | Purpose           | Complexity | Equivalent     |
| ---------------------- | ----------------- | ---------- | -------------- |
| `offer(e)` / `add(e)`  | Add to queue      | O(1)       | `offerLast(e)` |
| `poll()` / `remove()`  | Remove from queue | O(1)       | `pollFirst()`  |
| `peek()` / `element()` | Peek at head      | O(1)       | `peekFirst()`  |

---

#### **Performance Summary (Interview)**

| Operation                        | Complexity | Reason                         |
| -------------------------------- | ---------- | ------------------------------ |
| `addFirst(e)` / `addLast(e)`     | **O(1)**   | Circular array, O(n) if resize |
| `removeFirst()` / `removeLast()` | **O(1)**   | Direct array access            |
| `getFirst()` / `getLast()`       | **O(1)**   | Direct array access            |
| `remove(Object)`                 | **O(n)**   | Linear search required         |
| `contains(Object)`               | **O(n)**   | Linear search required         |
| `size()`                         | **O(1)**   | Cached size field              |

---

#### **Interview Questions & Answers**

**Q1: Why is ArrayDeque faster than LinkedList for queue operations?**

- **A:** ArrayDeque uses **contiguous array** (cache-friendly), while LinkedList allocates separate nodes (pointer-chasing, memory overhead).

**Q2: Why is ArrayDeque preferred over Stack class?**

- **A:** Stack is **legacy (Java 1.0)** - extends Vector (synchronized overhead + design flaw). ArrayDeque is **faster, not synchronized**, and properly implements Deque interface. **Always use ArrayDeque for stack operations in modern Java.**

**Q3: Can ArrayDeque be used as both Stack and Queue?**

- **A:** **Yes!** Implements Deque, which supports both LIFO (stack) and FIFO (queue) operations.

**Q4: Why doesn't ArrayDeque allow null elements?**

- **A:** Uses `null` as sentinel value to indicate **empty slots** in circular array.

**Q5: How does ArrayDeque grow?**

- **A:** **Doubles** in size when full (like ArrayList grows 1.5x). Default initial capacity is 16.

**Q6: ArrayDeque vs LinkedList - when to use which?**

- **A:**
  - **ArrayDeque:** Stack/Queue operations, better performance (usually)
  - **LinkedList:** Need both List + Deque features (random access by index)

---

#### **Usage Examples**

**As Stack (LIFO):**

```java
Deque<String> stack = new ArrayDeque<>();
stack.push("First");
stack.push("Second");
stack.push("Third");

System.out.println(stack.pop());  // "Third"
System.out.println(stack.peek()); // "Second"
```

**As Queue (FIFO):**

```java
Deque<String> queue = new ArrayDeque<>();
queue.offer("First");
queue.offer("Second");
queue.offer("Third");

System.out.println(queue.poll());  // "First"
System.out.println(queue.peek());  // "Second"
```

**As Deque (Both Ends):**

```java
Deque<Integer> deque = new ArrayDeque<>();
deque.addFirst(1);  // [1]
deque.addLast(2);   // [1, 2]
deque.addFirst(0);  // [0, 1, 2]
deque.addLast(3);   // [0, 1, 2, 3]

deque.removeFirst(); // [1, 2, 3]
deque.removeLast();  // [1, 2]
```

---

#### **When to Use**

✅ **Use ArrayDeque when:**

- Need **stack operations** (LIFO) - **preferred over Stack class**
- Need **queue operations** (FIFO) - **faster than LinkedList**
- Need **deque operations** (add/remove at both ends)
- **No null elements** needed
- **Single-threaded** context

❌ **Avoid when:**

- Need **null elements** (use LinkedList)
- Need **thread-safety** (use ConcurrentLinkedDeque)
- Need **List features** (random access by index) - use LinkedList

---

## **2.3 LinkedList (as Queue)**

**Already covered in detail in `list_implementations.md`**

**Quick Summary for Queue Context:**

- ✅ Implements **both List and Deque**
- ✅ Allows **null elements** (unlike ArrayDeque)
- ✅ O(1) operations at **both ends**
- ❌ Slower than ArrayDeque (node allocation overhead)
- ❌ Higher memory overhead (prev/next pointers)

**Use LinkedList as Queue when:**

- Need **both List and Queue** functionality
- Need **null elements** in queue
- Already using LinkedList for other reasons

**Otherwise, prefer ArrayDeque for queue operations!**

---

## **Comparison: PriorityQueue vs ArrayDeque vs LinkedList (Queue Implementations)**

| Feature             | PriorityQueue       | ArrayDeque            | LinkedList         |
| ------------------- | ------------------- | --------------------- | ------------------ |
| **Data Structure**  | Binary heap         | Circular array        | Doubly-linked list |
| **Ordering**        | Priority-based      | FIFO or LIFO          | FIFO or LIFO       |
| **Add (offer)**     | O(log n)            | O(1) amortized        | O(1)               |
| **Remove (poll)**   | O(log n)            | O(1)                  | O(1)               |
| **Peek**            | O(1)                | O(1)                  | O(1)               |
| **Remove specific** | O(n)                | O(n)                  | O(n)               |
| **Contains**        | O(n)                | O(n)                  | O(n)               |
| **Null elements**   | ❌ No               | ❌ No                 | ✅ Yes             |
| **Random access**   | ❌ No               | ❌ No                 | ✅ Yes (O(n))      |
| **Memory overhead** | Low                 | Low                   | High               |
| **Thread-Safe**     | ❌ No               | ❌ No                 | ❌ No              |
| **Best for**        | Priority processing | Stack/Queue (general) | List + Queue       |

---

## **Quick Decision Guide**

```
Need Queue implementation?
├─ Need priority-based processing? → PriorityQueue
├─ Need FIFO (or LIFO) processing?
│   ├─ Allow null elements? → LinkedList
│   ├─ Need List + Queue? → LinkedList
│   └─ Best performance? → ArrayDeque (recommended)
└─ Need thread-safety?
    ├─ PriorityQueue → PriorityBlockingQueue
    ├─ ArrayDeque → ConcurrentLinkedDeque
    └─ LinkedList → Collections.synchronizedList()
```

**General Recommendation:**

- ✅ **PriorityQueue** for priority-based processing
- ✅ **ArrayDeque** for general stack/queue operations (fastest)
- ✅ **LinkedList** only if you need List + Queue features or null elements

---

## **Common Interview Scenarios**

### **Scenario 1: Implement Task Scheduler**

**Use:** `PriorityQueue` with custom comparator (priority by deadline or importance)

### **Scenario 2: Implement Browser Back/Forward**

**Use:** Two `ArrayDeque` stacks (back stack, forward stack)

### **Scenario 3: Implement BFS (Breadth-First Search)**

**Use:** `ArrayDeque` as FIFO queue

### **Scenario 4: Implement DFS (Depth-First Search)**

**Use:** `ArrayDeque` as LIFO stack (or recursion)

### **Scenario 5: Find Top K Elements**

**Use:** `PriorityQueue` (min-heap of size K)

### **Scenario 6: Sliding Window Maximum**

**Use:** `ArrayDeque` as deque (add/remove at both ends)

---

## **⚡ Performance Analysis – Queue Implementations**

### **Detailed Time Complexity**

#### **PriorityQueue Performance**

| Operation                 | Complexity   | Explanation                                            |
| ------------------------- | ------------ | ------------------------------------------------------ |
| `offer(E e)` / `add(E e)` | **O(log n)** | Insert and bubble up in heap (sift-up operation)       |
| `poll()` / `remove()`     | **O(log n)** | Remove root, move last to top, bubble down (sift-down) |
| `peek()` / `element()`    | **O(1)**     | Access root element (no removal)                       |
| `remove(Object o)`        | **O(n)**     | Must search through heap, then reheapify O(log n)      |
| `contains(Object o)`      | **O(n)**     | Linear search through heap array                       |
| `size()`                  | **O(1)**     | Cached size field                                      |
| `clear()`                 | **O(n)**     | Must clear all elements                                |
| `iterator.next()`         | **O(1)**     | Array traversal (NOT in priority order!)               |

**Important Notes:**

- **Heap property:** Parent ≤ children (min-heap) or parent ≥ children (max-heap)
- **NOT fully sorted:** Only guarantees root is min/max, not entire ordering
- **Iterator order:** Iterating does NOT give sorted order (use poll() for sorted)
- **Null elements:** NOT allowed (throws NullPointerException)

**Best Use Cases:**

- ✅ **Priority-based processing** (task scheduling, event processing)
- ✅ **Top-K problems** (find K largest/smallest elements)
- ✅ **Merge K sorted lists**
- ✅ **Dijkstra's algorithm** (shortest path)
- ✅ **Heap sort** implementation
- ❌ Avoid for simple FIFO queue (use ArrayDeque instead)
- ❌ Not efficient for arbitrary element removal

**Heap Growth:**

```
Initial capacity: 11
Growth: capacity * 2 if small, capacity * 1.5 if large
Example: 11 → 22 → 44 → 88 → 176...
```

---

#### **ArrayDeque Performance**

| Operation                                  | Complexity         | Explanation                        |
| ------------------------------------------ | ------------------ | ---------------------------------- |
| `offer(E e)` / `add(E e)` / `addLast(E e)` | **O(1) amortized** | Add to tail, occasionally resize   |
| `offerFirst(E e)` / `addFirst(E e)`        | **O(1) amortized** | Add to head, occasionally resize   |
| `poll()` / `remove()` / `removeFirst()`    | **O(1)**           | Remove from head                   |
| `pollLast()` / `removeLast()`              | **O(1)**           | Remove from tail                   |
| `peek()` / `element()` / `getFirst()`      | **O(1)**           | Access head element                |
| `peekLast()` / `getLast()`                 | **O(1)**           | Access tail element                |
| `remove(Object o)`                         | **O(n)**           | Linear search and removal          |
| `contains(Object o)`                       | **O(n)**           | Linear search                      |
| `size()`                                   | **O(1)**           | Calculated from head/tail pointers |
| `clear()`                                  | **O(n)**           | Must null out references           |

**Internal Structure:**

```
Circular array implementation:
[_, _, _, head, elem1, elem2, tail, _, _]
         ↑                        ↑
         head pointer            tail pointer

When full, array doubles in size
```

**Best Use Cases:**

- ✅ **General-purpose queue** (FIFO) - fastest choice
- ✅ **Stack** (LIFO) - faster than Stack class
- ✅ **Deque operations** (add/remove at both ends)
- ✅ **BFS/DFS algorithms**
- ✅ **Sliding window problems**
- ✅ **Cache with size limit**
- ❌ **No null elements** allowed (throws NullPointerException)
- ❌ Not thread-safe

**Memory Efficiency:**

- More memory-efficient than LinkedList (no node overhead)
- Grows by doubling capacity when full

---

#### **LinkedList Performance (as Queue)**

| Operation                           | Complexity | Explanation                 |
| ----------------------------------- | ---------- | --------------------------- |
| `offer(E e)` / `add(E e)`           | **O(1)**   | Add to tail                 |
| `offerFirst(E e)` / `addFirst(E e)` | **O(1)**   | Add to head                 |
| `poll()` / `remove()`               | **O(1)**   | Remove from head            |
| `pollLast()` / `removeLast()`       | **O(1)**   | Remove from tail            |
| `peek()` / `element()`              | **O(1)**   | Access head element         |
| `peekLast()` / `getLast()`          | **O(1)**   | Access tail element         |
| `remove(Object o)`                  | **O(n)**   | Linear search through nodes |
| `contains(Object o)`                | **O(n)**   | Linear search               |
| `size()`                            | **O(1)**   | Cached size field           |

**Best Use Cases:**

- ✅ When you need **both List and Queue** functionality
- ✅ When **null elements** are required (allowed, but not recommended in Queue context)
- ❌ Generally **avoid** for pure queue usage (ArrayDeque is faster)
- ❌ Higher memory overhead than ArrayDeque

---

### **Space Complexity Comparison**

| Implementation    | Memory Per Element | Extra Overhead | Notes                                              |
| ----------------- | ------------------ | -------------- | -------------------------------------------------- |
| **PriorityQueue** | Element            | Array capacity | Heap stored in array, may have unused capacity     |
| **ArrayDeque**    | Element            | Array capacity | Circular array, may have unused capacity           |
| **LinkedList**    | Element + 24 bytes | Node overhead  | Each node has 2 pointers (prev/next) + element ref |

**Memory Example for 1000 elements:**

- **PriorityQueue:** ~16-20 KB (depends on element type + array overhead)
- **ArrayDeque:** ~16-20 KB (circular array)
- **LinkedList:** ~40 KB (16 KB elements + 24 KB node overhead)

---

### **Performance Tips for Queue Implementations**

#### **✅ PriorityQueue Best Practices**

1. **Pre-size for known capacity**:

   ```java
   // ✅ Avoid multiple resizes
   PriorityQueue<Integer> pq = new PriorityQueue<>(1000);

   // ❌ Starts at 11, resizes multiple times
   PriorityQueue<Integer> pq = new PriorityQueue<>();
   ```

2. **Use appropriate comparator**:

   ```java
   // ✅ Min-heap (default, smallest first)
   PriorityQueue<Integer> minHeap = new PriorityQueue<>();

   // ✅ Max-heap (largest first)
   PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

   // ✅ Custom comparator
   PriorityQueue<Task> pq = new PriorityQueue<>(
       Comparator.comparing(Task::getPriority).thenComparing(Task::getDeadline)
   );
   ```

3. **Poll for sorted order, not iterator**:

   ```java
   // ✅ Get elements in priority order
   while (!pq.isEmpty()) {
       System.out.println(pq.poll()); // Sorted output
   }

   // ❌ Iterator does NOT give sorted order
   for (Integer i : pq) {
       System.out.println(i); // Unordered!
   }
   ```

4. **Top-K elements pattern**:

   ```java
   // Find K largest elements using min-heap
   PriorityQueue<Integer> minHeap = new PriorityQueue<>();
   for (int num : numbers) {
       minHeap.offer(num);
       if (minHeap.size() > k) {
           minHeap.poll(); // Remove smallest
       }
   }
   // Result: minHeap contains K largest elements
   ```

5. **Avoid remove() and contains()**:

   ```java
   // ❌ O(n) operations
   pq.remove(element);     // Linear search
   pq.contains(element);   // Linear search

   // ✅ Use for priority processing only (poll/peek)
   while (!pq.isEmpty()) {
       process(pq.poll());
   }
   ```

---

#### **✅ ArrayDeque Best Practices**

1. **Use for stack operations** (not Stack class):

   ```java
   // ✅ Fast stack with ArrayDeque
   Deque<String> stack = new ArrayDeque<>();
   stack.push("item");    // O(1)
   stack.pop();           // O(1)
   stack.peek();          // O(1)

   // ❌ Legacy Stack class (synchronized overhead)
   Stack<String> stack = new Stack<>();
   ```

2. **Use for queue operations**:

   ```java
   // ✅ Fast queue with ArrayDeque
   Deque<String> queue = new ArrayDeque<>();
   queue.offer("item");   // O(1)
   queue.poll();          // O(1)
   queue.peek();          // O(1)
   ```

3. **Deque for sliding window**:

   ```java
   // Sliding window maximum using deque
   Deque<Integer> deque = new ArrayDeque<>();
   for (int i = 0; i < nums.length; i++) {
       // Remove elements outside window
       while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
           deque.pollFirst();
       }
       // Remove smaller elements (maintain decreasing order)
       while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
           deque.pollLast();
       }
       deque.offerLast(i);
   }
   ```

4. **Pre-size if capacity known**:

   ```java
   // ✅ Avoid resizes
   Deque<String> deque = new ArrayDeque<>(10000);

   // ❌ Starts at 16, doubles multiple times
   Deque<String> deque = new ArrayDeque<>();
   ```

5. **BFS with ArrayDeque**:
   ```java
   Queue<Node> queue = new ArrayDeque<>();
   queue.offer(root);
   while (!queue.isEmpty()) {
       Node node = queue.poll();
       // Process node
       for (Node child : node.children) {
           queue.offer(child);
       }
   }
   ```

---

#### **⚠️ Common Performance Pitfalls**

1. **Using LinkedList as queue instead of ArrayDeque**:

   ```java
   // ❌ Slower, more memory
   Queue<String> queue = new LinkedList<>();

   // ✅ Faster, less memory
   Queue<String> queue = new ArrayDeque<>();
   ```

2. **Using Stack class instead of ArrayDeque**:

   ```java
   // ❌ Legacy synchronized class
   Stack<String> stack = new Stack<>();

   // ✅ Modern, faster
   Deque<String> stack = new ArrayDeque<>();
   ```

3. **Iterating PriorityQueue expecting sorted order**:

   ```java
   // ❌ Does NOT give sorted order
   for (Integer i : priorityQueue) {
       System.out.println(i); // Wrong!
   }

   // ✅ Poll for sorted order
   while (!priorityQueue.isEmpty()) {
       System.out.println(priorityQueue.poll());
   }
   ```

4. **Adding null to PriorityQueue/ArrayDeque**:

   ```java
   // ❌ NullPointerException
   PriorityQueue<String> pq = new PriorityQueue<>();
   pq.offer(null); // NPE

   ArrayDeque<String> deque = new ArrayDeque<>();
   deque.offer(null); // NPE

   // ✅ LinkedList allows null (but avoid in Queue context)
   LinkedList<String> list = new LinkedList<>();
   list.offer(null); // OK, but not recommended
   ```

5. **Using remove(Object) on large PriorityQueue**:

   ```java
   // ❌ O(n) operation
   priorityQueue.remove(element); // Linear search

   // ✅ Only use poll/peek for priority queue
   priorityQueue.poll(); // O(log n)
   ```

6. **Not providing Comparator for custom objects**:

   ```java
   // ❌ ClassCastException if Task doesn't implement Comparable
   PriorityQueue<Task> pq = new PriorityQueue<>();

   // ✅ Provide Comparator
   PriorityQueue<Task> pq = new PriorityQueue<>(
       Comparator.comparing(Task::getPriority)
   );
   ```

---

### **PriorityQueue vs ArrayDeque vs LinkedList: When to Use What**

| Scenario            | PriorityQueue | ArrayDeque | LinkedList | Reason                             |
| ------------------- | ------------- | ---------- | ---------- | ---------------------------------- |
| FIFO queue          | ❌            | ✅         | ✅         | ArrayDeque is fastest              |
| LIFO stack          | ❌            | ✅         | ✅         | ArrayDeque is fastest              |
| Priority processing | ✅            | ❌         | ❌         | PriorityQueue maintains heap order |
| Top-K elements      | ✅            | ❌         | ❌         | Heap structure optimal             |
| Double-ended queue  | ❌            | ✅         | ✅         | Both support, ArrayDeque faster    |
| Allow null          | ❌            | ❌         | ✅         | Only LinkedList allows             |
| Memory efficiency   | Medium        | ✅         | ❌         | ArrayDeque has no node overhead    |
| BFS algorithm       | ❌            | ✅         | ✅         | ArrayDeque is fastest FIFO         |
| DFS algorithm       | ❌            | ✅         | ✅         | ArrayDeque is fastest LIFO         |
| Task scheduling     | ✅            | ❌         | ❌         | Priority-based                     |
| Sliding window      | ❌            | ✅         | ❌         | Need add/remove both ends          |

**General Recommendations:**

- **Default queue choice:** ArrayDeque (fastest, most memory-efficient)
- **Need priority ordering:** PriorityQueue
- **Need both List and Queue:** LinkedList (but consider separate data structures)
- **Stack operations:** ArrayDeque (not Stack class)
- **Never use:** Stack class (legacy, use ArrayDeque instead)
