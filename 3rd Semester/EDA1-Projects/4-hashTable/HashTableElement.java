public class HashTableElement<AnyType> {
    public AnyType element;
    public boolean exists = false;

    public HashTableElement(AnyType element) {
        this.element = element;
        this.exists = true;
    }

    public void remove() {
        this.exists = false;
    }

    public String toString() {
        return this.element.toString();
    }
}