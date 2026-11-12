package DataStructures;

import java.util.ArrayList;

public class HashTable<E, T> {
    private final int MAX_SIZE = 17; // Maximum size of the hash table.
    KeyValuePair<E, T>[] arr = new KeyValuePair[MAX_SIZE]; // Array to store key-value pairs.
    String[] outputArr = new String[MAX_SIZE]; // Array to store string representations of values.
    ArrayList<E> keys = new ArrayList<>(); // List to store keys for quick access.
    int length = 0; // Current number of elements in the hash table.

    // Returns the string array representation of the hash table values.
    public String[] asArray() { return outputArr; }

    // Converts the hash table to a string representation.
    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                string += "{" + arr[i].Key() + " : " + arr[i].Value() + "}";
            }
        }
        return string;
    }

    // Adds a key-value pair to the hash table.
    public void add(E Key, T Value) {
        if (length() == MAX_SIZE + 1) { // Checks if the hash table is full.
            throw new IllegalArgumentException();
        }
        int index = Math.abs(Key.hashCode()) % MAX_SIZE; // Calculates the index using the hash code.
        while (arr[index] != null) {
            index = (index + 1) % MAX_SIZE; // Handles collisions using linear probing.
        }
        KeyValuePair<E, T> pair = new KeyValuePair<>(); // Creates a new key-value pair.
        pair.setKey(Key);
        pair.setValue(Value);
        arr[index] = pair; // Stores the pair in the calculated index.
        outputArr[index] = Value.toString(); // Updates the string array.
        keys.add(Key); // Adds the key to the keys list.
        length++;
    }

    // Retrieves the value associated with a given key.
    public T item(E key) {
        int startIndex = Math.abs(key.hashCode()) % MAX_SIZE;
        int index = startIndex;
        do {
            if (arr[index] == null) {
                throw new IllegalArgumentException("Key not found.");
            }
            if (key.equals(arr[index].Key())) {
                return arr[index].Value(); // Returns the value if the key matches.
            }
            index = (index + 1) % MAX_SIZE;
        } while (index != startIndex); // Loops back to the start index if necessary.

        throw new IllegalArgumentException("Key not found.");
    }

    // Deletes a key-value pair from the hash table using the key.
    public void delete(E Key) {
        int startIndex = Math.abs(Key.hashCode()) % MAX_SIZE;
        int index = startIndex;
        while (arr[index] != null) {
            if (arr[index].Key().equals(Key)) {
                arr[index] = null; // Removes the key-value pair.
                outputArr[index] = null; // Updates the string array.
                length--;
                return;
            }
            index = (index + 1) % MAX_SIZE;
            if (index == startIndex) break; // Stops if the entire table has been scanned.
        }
        throw new IllegalArgumentException("Key not found.");
    }

    // Checks if a given key exists in the hash table.
    public boolean contains(E Key) {
        int startIndex = Math.abs(Key.hashCode()) % MAX_SIZE;
        int index = startIndex;
        do {
            KeyValuePair<E, T> entry = arr[index];
            if (entry == null) {
                return false;
            } else if (Key.equals(entry.Key())) {
                return true; // Returns true if the key is found.
            }
            index = (index + 1) % MAX_SIZE;
        } while (index != startIndex);

        return false;
    }

    // Updates the value associated with a given key.
    public void changeValue(E key, T newValue) {
        int startIndex = Math.abs(key.hashCode()) % MAX_SIZE;
        int index = startIndex;
        boolean keyFound = false;

        do {
            if (arr[index] == null) {
                break;
            }
            if (key.equals(arr[index].Key())) {
                arr[index].setValue(newValue); // Updates the value.
                outputArr[index] = newValue.toString(); // Updates the string array.
                keyFound = true;
                break;
            }
            index = (index + 1) % MAX_SIZE;
        } while (index != startIndex);

        if (!keyFound) {
            throw new IllegalArgumentException("Key not found.");
        }
    }

    // Returns a list of all keys in the hash table.
    public ArrayList<E> getKeys() {
        return keys;
    }

    // Returns the current number of elements in the hash table.
    public int length() {
        return length;
    }

    // Checks if the hash table is empty.
    public boolean isEmpty() {
        return length == 0;
    }
}