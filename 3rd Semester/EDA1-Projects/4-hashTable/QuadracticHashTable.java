public class QuadracticHashTable<AnyType> extends HashTable<AnyType> {
    public int searchPosition(AnyType element) {
        int number = element.hashCode() % this.size;

        if (number < 0) {
            number *= -1;
        }

        int save = number, i = 1;
        while (true) {
            if (this.elements[number] == null)
                return number;
            else if (this.elements[number].element.equals(element) && this.elements[number].exists)
                return number;
            if (save + (i * i) >= this.size)
                number = (save + (i * i)) % this.size;
            else
                i++;
        }
    }
}
