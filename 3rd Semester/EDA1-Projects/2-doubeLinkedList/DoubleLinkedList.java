import java.util.Iterator;

public class DoubleLinkedList<AnyType> implements Lista<AnyType>, Iterable<AnyType> {

    Node<AnyType> firstElement = new Node<>();
    Node<AnyType> lastElement = new Node<>();

    private int size = 0;

    public DoubleLinkedList() {

        this.firstElement.setNext(this.lastElement);
        this.lastElement.setPrevious(this.firstElement);

    }

    public Iterator<AnyType> iterator() {
        return new DoubleLinkedListIterator<AnyType>(this.firstElement);
    }

    public void add(AnyType newNode) {

        Node<AnyType> x = new Node(newNode);
        Node<AnyType> last = this.lastElement.getPrevious();
        last.setNext(x);
        x.setPrevious(last);
        x.setNext(this.lastElement);
        this.lastElement.setPrevious(x);
        this.size++;
        
    }

    public void add(int i, AnyType x) {

        Node<AnyType> newNode = new Node<>();

        if (this.size == 0) {

            newNode.setPrevious(this.firstElement);
            newNode.setNext(this.lastElement);
            this.firstElement.setNext(newNode);
            this.lastElement.setPrevious(newNode);

        } else if (i == 0) {

            newNode.setNext(this.firstElement.getNext());
            this.firstElement.getNext().setPrevious(newNode);
            newNode.setPrevious(this.firstElement);
            this.firstElement.setNext(newNode);

        } else if (i == this.size) {

            newNode.setNext(this.lastElement);
            newNode.setPrevious(this.lastElement.getPrevious());
            this.lastElement.getPrevious().setNext(newNode);
            this.lastElement.setPrevious(newNode);

        } else if (i < this.size) {

            Node<AnyType> myNode = getNode(i);
            Node<AnyType> y = new Node<>(myNode.getPrevious(), x, myNode);
            myNode.getPrevious().setNext(y);
            myNode.setPrevious(y);

        }

        this.size++;

    }

    public void set(int i, AnyType x) {

        Node<AnyType> y = getNode(i);
        y.setElement(x);

    }

    public AnyType remove(int i) {

        this.size--;
        Node<AnyType> x = getNode(i);
        AnyType y = x.getElement();
        x.getPrevious().setNext(x.getNext());
        x.getNext().setPrevious(x.getPrevious());

        return y;

    }

    public void remove(AnyType x) {

        Node<AnyType> y = this.firstElement;

        while (y.getElement() != x) {
            y = y.getNext();
        }

        y.getNext().setPrevious(y.getPrevious());
        y.getPrevious().setNext(y.getNext());

        this.size--;

    }

    public void clear() {
        this.size = 0;
        this.firstElement.setNext(this.lastElement);
        this.lastElement.setPrevious(this.firstElement);
    }

    public AnyType get(int i) {
        int number = 0;
        Node<AnyType> x = this.firstElement;
        while (number != i) {
            x = x.getNext();
            number++;
        }
        return x.getElement();
    }

    private Node<AnyType> getNode(int i) {
        int number = 0;
        Node<AnyType> x = this.firstElement;
        while (number != 1) {
            x = x.getNext();
            number++;
        }
        return x;
    }

    public int size() {
        return this.size;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        for (AnyType x : this) {
            if (x != null)
                string.append(x).append("\n");
        }

        return string.toString();
    }

    // Classe do iterador
    private class DoubleLinkedListIterator<AnyType> implements Iterator<AnyType> {

        Node<AnyType> element;

        private DoubleLinkedListIterator(Node<AnyType> x) {
            element = x;
        }

        public boolean hasNext() {
            return element.getNext() != null;
        }

        public boolean hasPrevious() {
            return element.getPrevious() != null;
        }

        public AnyType next() {
            if (hasNext()) {
                AnyType x = element.getElement();
                element = element.getNext();
                return x;
            }
            return null;
        }

        public AnyType previous() {
            if (hasPrevious()) {
                AnyType x = element.getElement();
                element = element.getPrevious();
                return x;
            }
            return null;
        }

    }

    // Classe do nó
    public static class Node<AnyType> {

        private Node<AnyType> previous;
        private AnyType element;
        private Node<AnyType> next;

        public Node(Node previous, AnyType element, Node next) {
            this.previous = previous;
            this.element = element;
            this.next = next;
        }

        public Node() {
            this.previous = null;
            this.element = null;
            this.next = null;
        }

        public Node(AnyType element) {
            this.previous = null;
            this.element = element;
            this.next = null;
        }

        public Node(AnyType element, Node next) {
            this.previous = null;
            this.element = element;
            this.next = next;
        }

        public Node(Node previous, AnyType element) {
            this.previous = previous;
            this.element = element;
            this.next = null;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public boolean hasPrevious() {
            return this.previous != null;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }

        public void setElement(AnyType element) {
            this.element = element;
        }

        public Node getNext() {
            return this.next;
        }

        public AnyType getElement() {
            return this.element;
        }

        public Node getPrevious() {
            return this.previous;
        }

    }

}
