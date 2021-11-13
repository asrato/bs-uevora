public class LinearHashTable<AnyType> extends HashTable<AnyType> {
    public int searchPosition(AnyType element) {
        int number = element.hashCode() % this.size;
        if(number < 0) {
            number *=-1;
        }
        while(true) {
            if (this.elements[number] == null)
                return number;
            else if (this.elements[number].element.equals(element) && this.elements[number].exists)
                return number;
            if (number + 1 == size)
                number = 0;
            else
                number++;
        }
    }
}
