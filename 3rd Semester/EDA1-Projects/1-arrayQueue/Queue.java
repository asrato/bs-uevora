import CostumExceptions.EmptyException;
import CostumExceptions.FullException;

public interface Queue<AnyType> {
    void enqueue(AnyType o) throws FullException;

    AnyType front() throws EmptyException;

    AnyType dequeue() throws EmptyException;

    int size();

    boolean empty();
}