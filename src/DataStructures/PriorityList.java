package DataStructures;

import java.util.ArrayList;

public class PriorityList<E> {
    private Element<E> front; // The first element in the priority list.
    private Element<E> tail; // The last element in the priority list.
    private int length = 0; // The number of elements in the list.

    // Checks if the priority list is empty.
    public boolean isEmpty() {
        return length == 0;
    }

    // Converts the priority list into a string array representation.
    public String[] asArray() {
        ArrayList<String> a = new ArrayList<>();
        Element<E> e = front;
        while (e != null) {
            a.add(String.valueOf(e.Value())); // Adds the value of each element to the array.
            e = e.Next(); // Moves to the next element.
        }
        return a.toArray(new String[0]);
    }

    // Removes an element with the specified value from the priority list.
    public E remove(E value) {
        if (isEmpty()) {
            throw new IllegalArgumentException("List is empty.");
        }
        Element<E> current = front;
        while (current != null && !current.Value().equals(value)) {
            current = current.Next(); // Searches for the element with the specified value.
        }
        if (current == null) {
            throw new IllegalArgumentException("Value not found: " + value); // Throws an error if not found.
        }
        if (current == front) {
            return pop(); // Removes the first element if it matches the value.
        } else {
            if (current == tail) {
                tail = current.Previous(); // Updates the tail if the last element is removed.
                tail.Next(null);
            } else {
                current.Previous().Next(current.Next()); // Links the previous and next elements.
                current.Next().Previous(current.Previous());
            }
            length--;
        }
        return current.Value(); // Returns the value of the removed element.
    }

    // Removes and returns the first element in the priority list.
    public E pop() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("Queue is empty.");
        }
        Element<E> temp = front;
        front = front.Next(); // Updates the front to the next element.
        if (front != null) {
            front.Previous(null);
        } else {
            tail = null; // If the list is empty after the operation, reset the tail.
        }
        length--;
        return temp.Value(); // Returns the value of the removed element.
    }

    // Adds a new element with the specified value and priority to the priority list.
    public void add(E value, double priority) {
        Element<E> newElement = new Element<E>(priority, value, null, null);
        if (isEmpty()) {
            front = tail = newElement; // Initializes the list if it is empty.
        } else {
            Element<E> current = front;
            while (current != null && current.Priority() <= priority) {
                current = current.Next(); // Finds the correct position based on priority.
            }
            if (current == front) {
                newElement.Next(front); // Inserts the new element at the front.
                front.Previous(newElement);
                front = newElement;
            } else if (current == null) {
                tail.Next(newElement); // Appends the new element to the end of the list.
                newElement.Previous(tail);
                tail = newElement;
            } else {
                newElement.Previous(current.Previous()); // Inserts the new element in the middle of the list.
                newElement.Next(current);
                current.Previous().Next(newElement);
                current.Previous(newElement);
            }
        }
        length++;
    }

    // Changes the priority of an existing element in the priority list.
    public void changePriority(E value, double newPriority) {
        if (isEmpty()) {
            throw new IllegalArgumentException("List is empty.");
        }

        Element<E> current = front;
        while (current != null && !current.Value().equals(value)) {
            current = current.Next(); // Searches for the element with the specified value.
        }

        if (current == null) {
            throw new IllegalArgumentException("Value not found: " + value); // Throws an error if not found.
        }

        // Removes the element from its current position.
        if (current == front) {
            front = current.Next();
            if (front != null) {
                front.Previous(null);
            } else {
                tail = null;
            }
        } else if (current == tail) {
            tail = current.Previous();
            tail.Next(null);
        } else {
            current.Previous().Next(current.Next());
            current.Next().Previous(current.Previous());
        }
        length--;

        // Updates the priority and re-adds the element to the correct position.
        current.Priority(newPriority);
        add(current.Value(), newPriority);
    }
}