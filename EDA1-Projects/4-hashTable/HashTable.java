public abstract class HashTable<AnyType> {
    public HashTableElement<AnyType>[] elements;
    public int size;
    public int used;

    public HashTable() {
        this.elements = new HashTableElement[11];
        this.size = 11;
        this.used = 0;
    }

    public HashTable(int size) {
        this.elements = new HashTableElement[size];
        this.size = size;
        this.used = 0;
    }

    public int used() {
        return this.used;
    }

    public double loadFactor() {
        return (double) this.used / (double) this.size;
    }

    public abstract int searchPosition(AnyType element);

    public void allocateTable(int size) {
        this.elements = new HashTableElement[size];
        this.size = size;
        this.used = 0;
    }

    public void makeEmpty() {
        this.elements = new HashTableElement[this.size];
        this.used = 0;
    }

    public AnyType search(AnyType element) {
        int index = searchPosition(element);
        if (this.elements[index] == null || !this.elements[index].exists)
            return null;
        return element;
    }

    public void remove(AnyType element) {
        int index = searchPosition(element);
        if (this.elements[index].element.equals(element)) {
            this.elements[index].remove();
        }
    }

    public void insert(AnyType element) {
        double load = this.loadFactor();
        if (load > 0.5)
            this.reHash();
        int index = searchPosition(element);
        this.elements[index] = new HashTableElement<>(element);
        this.used++;
    }

    public void reHash() {
        int newSize = newSize(2 * this.size);
        HashTableElement<AnyType>[] save = this.elements;
        this.elements = new HashTableElement[newSize];
        this.size = newSize;
        this.used = 0;
        for (HashTableElement<AnyType> element : save) {
            if (element != null)
                this.insert(element.element);
        }
    }

    private int newSize(int size) {
        size++;
        for (int i = 2; i < size; i++) {
            if (size % i == 0) {
                size++;
                i = 2;
            }
        }
        return size;
    }

    public void print() {
        for (int i = 0; i < this.size; i++) {
            if (this.elements[i] != null) {
                if (this.elements[i].exists)
                    System.out.println(this.getSpaces(i) + i + " » " + this.elements[i]);
                else
                    System.out.println(this.getSpaces(i) + i + " » <removed>");
            } else
                System.out.println(this.getSpaces(i) + i + " » ");
        }
    }

    private int unityLength(int number) {
        if (number == 0) return 1;
        return (int) (Math.log10(number) + 1);
    }

    private String getSpaces(int number) {
        StringBuilder str = new StringBuilder();
        int length = this.unityLength(this.size) - this.unityLength(number);
        for (int i = 0; i < length; i++)
            str.append(" ");
        return str.toString();
    }
}
