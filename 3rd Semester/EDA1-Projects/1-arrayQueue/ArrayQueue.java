import CostumExceptions.EmptyException;
import CostumExceptions.FullException;

public class ArrayQueue<AnyType> implements Queue<AnyType> {
    private int head;
    private int tail;
    private AnyType[] elements;
    private int size;
    private int controller = 0;

    public ArrayQueue() {
        this.elements = (AnyType[]) new Object[50];
        this.head = 0;
        this.tail = 0;
        this.size = 50;
    }

    public ArrayQueue(int size) {
        this.elements = (AnyType[]) new Object[size];
        this.head = 0;
        this.tail = 0;
        this.size = size;
    }

    @Override
    public void enqueue(AnyType o) throws FullException {
        if (this.controller != 0 && this.tail == this.head) throw new FullException();
        this.elements[this.tail] = o;
        if (this.tail == this.size - 1) {
            this.tail = 0;
        } else {
            this.tail++;
        }
        this.controller++;
    }

    @Override
    public AnyType front() throws EmptyException {
        if (this.empty()) throw new EmptyException();

        return this.elements[this.head];
    }

    @Override
    public AnyType dequeue() throws EmptyException {
        if (this.empty()) throw new EmptyException();

        AnyType result = this.elements[this.head];

        if (this.head == size - 1) {
            this.head = 0;
        } else {
            this.head++;
        }

        this.controller--;

        return result;
    }

    @Override
    public int size() {
        return this.controller;
    }

    @Override
    public boolean empty() {
        return this.controller == 0;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("[");

        if (this.head < this.tail) {
            for (int i = this.head; i < this.tail; i++) {
                string.append(this.elements[i]);
                if (i != this.tail - 1) string.append(";");
            }
        }

        if (this.head == this.tail) {
            if (this.controller != 0) {
                for (int i = this.head; i < this.size; i++) {
                    string.append(this.elements[i]);
                    if (i != this.size - 1) string.append(";");
                }

                for (int i = 0; i < this.tail; i++) {
                    string.append(this.elements[i]);
                    if (i != this.tail - 1) string.append(";");
                }
            }
        }

        if (this.head > this.tail) {
            for (int i = this.head; i < this.size; i++) {
                string.append(this.elements[i]);
                string.append(";");
            }

            for (int i = 0; i < this.tail; i++) {
                string.append(this.elements[i]);
                if (i != this.tail - 1) string.append(",");
            }
        }

        string.append("]");

        return string.toString();
    }
}