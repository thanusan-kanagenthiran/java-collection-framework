# ListIterator Interface

## **Overview**

`ListIterator<E>` extends `Iterator<E>` and provides bidirectional traversal and modification capabilities for lists.

**Key Features:**

- **Bidirectional traversal** - forward and backward iteration
- **Safe modification** - add, remove, or set elements during iteration
- **Index tracking** - know current position in the list
- **No ConcurrentModificationException** - when using iterator's own modification methods

---

## **Hierarchy**

```
Iterator<E>
    ↑
    |
ListIterator<E>
```

---

## **Methods**

### **Inherited from Iterator:**

| Method              | Purpose                                               |
| ------------------- | ----------------------------------------------------- |
| `boolean hasNext()` | Returns true if more elements in forward direction    |
| `E next()`          | Returns next element and advances cursor forward      |
| `void remove()`     | Removes last element returned by next() or previous() |

### **ListIterator-Specific Methods:**

| Method                  | Purpose                                                       |
| ----------------------- | ------------------------------------------------------------- |
| `boolean hasPrevious()` | Returns true if more elements when traversing backward        |
| `E previous()`          | Returns previous element and moves cursor backward            |
| `int nextIndex()`       | Returns index of element that would be returned by next()     |
| `int previousIndex()`   | Returns index of element that would be returned by previous() |
| `void set(E e)`         | Replaces last element returned by next() or previous()        |
| `void add(E e)`         | Inserts element at current cursor position                    |

---

## **Key Differences from Iterator**

| Feature           | Iterator        | ListIterator                       |
| ----------------- | --------------- | ---------------------------------- |
| Direction         | Forward only    | Bidirectional (forward & backward) |
| Modification      | remove() only   | remove(), set(), add()             |
| Position tracking | No              | Yes (nextIndex, previousIndex)     |
| Available for     | All Collections | List only                          |

---

## **Usage Examples**

### **1. Bidirectional Traversal**

```java
List<String> fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry"));

ListIterator<String> iterator = fruits.listIterator();

// Forward traversal
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}

// Backward traversal
while (iterator.hasPrevious()) {
    System.out.println(iterator.previous());
}
```

### **2. Starting from Specific Position**

```java
ListIterator<String> iterator = fruits.listIterator(2); // starts at index 2
System.out.println(iterator.next()); // Cherry
```

### **3. Safe Modification During Iteration**

```java
ListIterator<String> iterator = fruits.listIterator();

// Add element
iterator.next(); // moves to Apple
iterator.add("Orange"); // adds Orange after Apple
// List: [Apple, Orange, Banana, Cherry]

// Replace element
iterator.next(); // moves to Banana
iterator.set("Mango"); // replaces Banana with Mango
// List: [Apple, Orange, Mango, Cherry]

// Remove element
iterator.next(); // moves to Cherry
iterator.remove(); // removes Cherry
// List: [Apple, Orange, Mango]
```

### **4. Index Tracking**

```java
ListIterator<String> iterator = fruits.listIterator();

System.out.println("Next index: " + iterator.nextIndex()); // 0
iterator.next();
System.out.println("Next index: " + iterator.nextIndex()); // 1
System.out.println("Previous index: " + iterator.previousIndex()); // 0
```

---

## **⚠️ Important Rules**

### **1. Modification Method Preconditions**

```java
// ❌ WRONG - Can't call set/remove without next() or previous()
ListIterator<String> iter = list.listIterator();
iter.set("New"); // IllegalStateException

// ✅ CORRECT
iter.next();
iter.set("New"); // Works
```

### **2. add() vs set()**

- `add(E e)` - Inserts at cursor position (doesn't require next/previous call)
- `set(E e)` - Replaces last returned element (requires prior next/previous)

```java
ListIterator<String> iter = list.listIterator();

iter.add("First"); // ✅ Can call immediately
iter.set("Second"); // ❌ IllegalStateException - no element to replace

iter.next();
iter.set("Second"); // ✅ Now works
```

### **3. remove() Rules**

```java
iter.next();
iter.remove(); // ✅ Removes element returned by next()
iter.remove(); // ❌ IllegalStateException - can only call once per next/previous

iter.next();
iter.add("New");
iter.remove(); // ❌ IllegalStateException - add() invalidates last returned element
```

---

## **Performance Considerations**

| Implementation | listIterator()            | Iteration     | Modification                          |
| -------------- | ------------------------- | ------------- | ------------------------------------- |
| **ArrayList**  | O(1) to create            | O(1) per step | O(n) for add/remove (due to shifting) |
| **LinkedList** | O(n) if starting at index | O(1) per step | O(1) for add/remove                   |

**Best Practices:**

- Use `listIterator()` without index for LinkedList (avoid O(n) positioning cost)
- For ArrayList, modification via ListIterator is still O(n) but avoids ConcurrentModificationException
- Prefer `listIterator()` over regular iterator when you need bidirectional access or modification

---

## **Common Use Cases**

### **1. Reverse Iteration**

```java
ListIterator<String> iter = list.listIterator(list.size());
while (iter.hasPrevious()) {
    System.out.println(iter.previous());
}
```

### **2. Replace All Occurrences**

```java
ListIterator<String> iter = list.listIterator();
while (iter.hasNext()) {
    if (iter.next().equals("Old")) {
        iter.set("New");
    }
}
```

### **3. Insert After Specific Element**

```java
ListIterator<String> iter = list.listIterator();
while (iter.hasNext()) {
    if (iter.next().equals("Target")) {
        iter.add("Inserted");
        break;
    }
}
```

### **4. Remove While Iterating (Safe)**

```java
ListIterator<String> iter = list.listIterator();
while (iter.hasNext()) {
    String element = iter.next();
    if (shouldRemove(element)) {
        iter.remove(); // Safe, no ConcurrentModificationException
    }
}
```

---

## **🔑 Key Takeaways**

1. **ListIterator is bidirectional** - can traverse forward and backward
2. **Supports modification** - add, remove, set during iteration
3. **Position aware** - tracks cursor position with nextIndex/previousIndex
4. **List-specific** - only available for List implementations
5. **Thread-unsafe** - like Iterator, not safe for concurrent modification
6. **Fail-fast** - throws ConcurrentModificationException if list modified externally
7. **State requirements** - set() and remove() need prior next() or previous() call

---

## **Comparison with Other Iterators**

| Feature      | Iterator                 | ListIterator               | for-each loop       |
| ------------ | ------------------------ | -------------------------- | ------------------- |
| Direction    | Forward                  | Bidirectional              | Forward             |
| Modification | remove()                 | remove(), set(), add()     | None                |
| Index access | No                       | Yes                        | No                  |
| Performance  | Same as iteration        | Same as iteration          | Same as iteration   |
| Use when     | Simple forward traversal | Need backward/modification | Read-only traversal |
