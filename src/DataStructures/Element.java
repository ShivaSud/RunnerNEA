package DataStructures;

public class Element <E> {
    private double priority; // The priority of the element.
    private final E value; // The value contained within the element.
    private Element<E> previous; // A reference to the previous element in the structure.
    private Element<E> next; // A reference to the next element in the structure.

    // Constructor to initialize an element with its priority, value, previous, and next references.
    public Element(double priority, E value, Element<E> previous, Element<E> next){
        this.priority = priority;
        this.value = value;
        this.previous = previous;
        this.next = next;
    }

    // Converts the value of the element to a string representation.
    @Override
    public String toString(){
        return value.toString();
    }

    // Getter method for the element's priority.
    public double Priority(){
        return priority;
    }

    // Setter method to update the element's priority.
    public void Priority(double Priority){
        priority = Priority;
    }

    // Getter method for the element's value.
    public E Value(){
        return value;
    }

    // Getter method for the reference to the previous element.
    public Element<E> Previous(){
        return previous;
    }

    // Setter method to update the reference to the previous element.
    public void Previous(Element<E> value){
        previous = value;
    }

    // Getter method for the reference to the next element.
    public Element<E> Next(){
        return next;
    }

    // Setter method to update the reference to the next element.
    public void Next(Element<E> value) {
        next = value;
    }
}