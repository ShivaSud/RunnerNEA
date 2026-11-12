package DataStructures;

public class KeyValuePair<E, T> {
    private E Key; // The key of the key-value pair.
    private T Value; // The value of the key-value pair.

    // Getter method for the value of the key-value pair.
    public T Value() {
        return Value;
    }

    // Setter method to update the value of the key-value pair.
    public void setValue(T value) {
        Value = value;
    }

    // Setter method to update the key of the key-value pair.
    public void setKey(E key) {
        Key = key;
    }

    // Getter method for the key of the key-value pair.
    public E Key() {
        return Key;
    }

    // Converts the key of the key-value pair to its string representation.
    @Override
    public String toString() {
        return Key.toString();
    }
}