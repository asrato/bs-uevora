package CostumExceptions;

public class EmptyException extends Exception {
    public EmptyException() {
        super("EmptyException - The queue is empty.");
    }
}
