
import java.util.*;

public class QueueExample {
    public static void main(String[] args) {
        // ===================================================
        // 1️⃣ Creating a Queue
        // ===================================================
        // Queue follows FIFO (First-In-First-Out) order
        // LinkedList implements Queue interface

        Queue<String> queue = new LinkedList<>();

        // ===================================================
        // 2️⃣ Adding Elements
        // ===================================================
        // offer() -> adds element, returns false if capacity restricted
        // add() -> adds element, throws exception if capacity restricted
        // Both add to tail of queue

        queue.offer("Apple");
        queue.offer("Banana");
        queue.offer("Cherry");
        System.out.println("Queue after offer(): " + queue);

        queue.add("Date");
        System.out.println("Queue after add(): " + queue);

        // ===================================================
        // 3️⃣ Retrieving / Removing Elements
        // ===================================================
        // poll() -> removes head, returns null if empty
        // peek() -> retrieves head, returns null if empty
        // element() -> retrieves head, throws exception if empty
        // remove() -> removes head, throws exception if empty

        String head1 = queue.poll();
        System.out.println("poll(): " + head1);
        System.out.println("Queue after poll(): " + queue);

        String head2 = queue.peek();
        System.out.println("peek(): " + head2);
        System.out.println("Queue after peek(): " + queue);

        try {
            String head3 = queue.element(); // element()
            System.out.println("element(): " + head3);
        } catch (NoSuchElementException e) {
            System.out.println("Queue is empty, element() threw exception");
        }

        try {
            String removed = queue.remove(); // remove() // remove()
            System.out.println("remove(): " + removed);
            System.out.println("Queue after remove(): " + queue);
        } catch (NoSuchElementException e) {
            System.out.println("Queue is empty, remove() threw exception");
        }

        // ===================================================
        // 4️⃣ Iterating Over Queue
        // ===================================================
        // Iteration follows queue order but doesn't remove elements
        // Use poll() in a loop to consume and remove elements

        System.out.print("Iterating over queue: ");
        for (String item : queue) {
            System.out.print(item + " ");
        }
        System.out.println();

        // ===================================================
        // 5️⃣ Using PriorityQueue (priority ordering, not FIFO)
        // ===================================================
        // PriorityQueue orders elements by priority (natural or custom)
        // poll() always returns smallest/highest priority element
        // Not a true FIFO queue - uses heap data structure

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(50);
        pq.offer(20);
        pq.offer(40);
        System.out.println("PriorityQueue: " + pq);
        System.out.println("poll() from PriorityQueue: " + pq.poll());
        System.out.println("PriorityQueue after poll(): " + pq);

        // ===================================================
        // 6️⃣ Deque Example (double-ended queue)
        // ===================================================
        // Deque supports operations at both ends
        // Can be used as stack (LIFO) or queue (FIFO)
        // addFirst/addLast, removeFirst/removeLast available

        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("First");
        deque.addLast("Last");
        System.out.println("Deque: " + deque);
        System.out.println("removeLast(): " + deque.removeLast());
        System.out.println("Deque after removeLast(): " + deque);
    }
}
