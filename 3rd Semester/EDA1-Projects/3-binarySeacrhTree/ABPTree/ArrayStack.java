package ABPTree;

public class ArrayStack<Tipo> {
    private Tipo[] pilha;
    private int indice = 0;
    private int tamanho;

    public ArrayStack() {
        this.pilha = (Tipo[]) new Object[25];
        this.tamanho = 25;
    }

    public ArrayStack(int tamanho) {
        this.pilha = (Tipo[]) new Object[tamanho];
        this.tamanho = tamanho;
    }

    public void push(Tipo elemento) {
        if(this.indice == this.tamanho)
            throw new RuntimeException("A pilha está cheia");
        this.pilha[this.indice] = elemento;
        this.indice++;
    }

    public Tipo top() {
        if(this.empty())
            throw new RuntimeException("A pilha está vazia");
        else
            return this.pilha[this.indice - 1];
    }

    public Tipo pop() {
        if(this.empty())
            throw new RuntimeException("A pilha está vazia");
        this.indice--;
        return this.pilha[this.indice];
    }

    public int size() {
        return this.indice;
    }

    public boolean empty() {
        return this.indice == 0;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < this.indice; i++)
            string.append(this.pilha[i].toString()).append(" ");
        return string.toString();
    }
}
